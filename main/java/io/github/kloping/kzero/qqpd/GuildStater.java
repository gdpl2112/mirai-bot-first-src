package io.github.kloping.kzero.qqpd;

import com.alibaba.fastjson.JSONObject;
import io.github.kloping.MySpringTool.h1.impl.component.PackageScannerImpl;
import io.github.kloping.MySpringTool.interfaces.component.PackageScanner;
import io.github.kloping.date.DateUtils;
import io.github.kloping.kzero.gsuid.GsuidClient;
import io.github.kloping.kzero.main.KlopZeroMainThreads;
import io.github.kloping.kzero.main.api.*;
import io.github.kloping.kzero.qqpd.exclusive.Guild2Gsuid;
import io.github.kloping.kzero.qqpd.exclusive.LiveMsgSender;
import io.github.kloping.kzero.qqpd.exclusive.MihdpConnect2;
import io.github.kloping.qqbot.Starter;
import io.github.kloping.qqbot.api.SendAble;
import io.github.kloping.qqbot.api.event.ConnectedEvent;
import io.github.kloping.qqbot.api.message.MessageChannelReceiveEvent;
import io.github.kloping.qqbot.api.message.MessageDirectReceiveEvent;
import io.github.kloping.qqbot.api.message.MessageEvent;
import io.github.kloping.qqbot.entities.Bot;
import io.github.kloping.qqbot.entities.ex.msg.MessageChain;
import io.github.kloping.qqbot.impl.ListenerHost;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * @author github.kloping
 */
public class GuildStater extends ListenerHost implements KZeroStater {
    private BotCreated listener;
    private BotMessageHandler handler;

    @Override
    public void setHandler(KZeroBot bot, BotMessageHandler handler) {
        this.handler = handler;
    }

    @Override
    public void setCreated(BotCreated listener) {
        this.listener = listener;
    }

    private String appid;
    private String token;
    private String secret;
    private Integer code;

    public GuildStater(String appid, String token, Integer code) {
        this.appid = appid;
        this.token = token;
        this.code = code;
    }

    public GuildStater(String appid, String token, String secret, Integer code) {
        this(appid, token, code);
        this.secret = secret;
    }

    @Override
    public void run() {
        Starter starter;
        if (secret == null) starter = new Starter(appid, token);
        else starter = new Starter(appid, token, secret);
        try {
            File file = new File(String.format("./logs/%s/%s-%s-%s.log", appid, DateUtils.getYear(), DateUtils.getMonth(), DateUtils.getDay()));
            file.getParentFile().mkdirs();
            file.createNewFile();
            starter.APPLICATION.logger.setOutFile(file.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        starter.getConfig().setCode(code);
        starter.registerListenerHost(this);
        starter.registerListenerHost(MihdpConnect2.INSTANCE);
        starter.registerListenerHost(LiveMsgSender.INSTANCE);
//        starter.getConfig().setInterceptor0(bytes -> upload(bytes));
        starter.run();
    }

    public static String upload(byte[] bytes) {
        try {
            String url = Jsoup.connect(String.format("https://p.xiaofankj.com.cn/upimg.php"))
                    .ignoreContentType(true)
                    .ignoreContentType(true)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/114.0.0.0 Safari/537.36 Edg/114.0.1823.67")
                    .data("file", "temp.jpg", new ByteArrayInputStream(bytes)).method(Connection.Method.POST).execute().body();
            url = JSONObject.parseObject(url).getString("msg");
            return url;
        } catch (IOException e) {
            return e.getMessage();
        }
    }

    public KZeroBot<SendAble, Bot> create(String bid, Bot o, KZeroBotAdapter adapter, MessageSerializer<SendAble> serializer) {
        KZeroBot<SendAble, Bot> bot = new KZeroBot<SendAble, Bot>() {
            @Override
            public String getId() {
                return bid;
            }

            @Override
            public KZeroBotAdapter getAdapter() {
                return adapter;
            }

            @Override
            public MessageSerializer<SendAble> getSerializer() {
                return serializer;
            }

            @Override
            public Bot getSelf() {
                return o;
            }
        };
        return bot;
    }

    @Override
    public void handleException(Throwable e) {
        e.printStackTrace();
    }

    @EventReceiver
    public void onConnectedEvent(ConnectedEvent event) {
        String bid = event.getBot().getId();
        if (GsuidClient.INSTANCE != null) GsuidClient.INSTANCE.addListener(bid, Guild2Gsuid.INSTANCE);
        if (KlopZeroMainThreads.BOT_MAP.containsKey(bid)) return;
        onConnectedEventFirst(event);
    }

    private void onConnectedEventFirst(ConnectedEvent event) {
        Bot bot = event.getBot();
        GuildSerializer guildSerializer = new GuildSerializer(bot);
        KZeroBot<SendAble, Bot> kZeroBot = create(bot.getId(), bot, new GuildBotAdapter(bot, guildSerializer), guildSerializer);
        listener.created(this, kZeroBot);
    }

    @EventReceiver
    public void onEvent(MessageChannelReceiveEvent event) {
        MessageChain chain = event.getMessage();
        Guild2Gsuid.INSTANCE.offer(event);
        if (handler != null) {
            KZeroBot<SendAble, Bot> kZeroBot = KlopZeroMainThreads.BOT_MAP.get(String.valueOf(event.getBot().getId()));
            String outMsg = kZeroBot.getSerializer().serialize(chain);
            if (outMsg.startsWith("/") && outMsg.length() > 1) outMsg = outMsg.substring(1);
            MessagePack pack = new MessagePack(MessageType.GROUP, event.getSender().getUser().getId(),
                    event.getChannelId(), outMsg);
            pack.setRaw(event);
            handler.onMessage(pack);
            //plugin to gsuid
            Guild2Gsuid.INSTANCE.sendToGsuid(event);
            MihdpConnect2.INSTANCE.sendToMihdp(null, event, kZeroBot);
        }
        temp(event);
    }

    @EventReceiver
    public void onEvent(MessageDirectReceiveEvent event) {
        MessageChain chain = event.getMessage();
        Guild2Gsuid.INSTANCE.offer(event);
        if (handler != null) {
            KZeroBot<SendAble, Bot> kZeroBot = KlopZeroMainThreads.BOT_MAP.get(String.valueOf(event.getBot().getId()));
            MessagePack pack = new MessagePack(MessageType.GROUP, event.getSender().getUser().getId(),
                    event.getSrcGuildId(), kZeroBot.getSerializer().serialize(chain));
            pack.setRaw(event);
            handler.onMessage(pack);
            //plugin to gsuid
            Guild2Gsuid.INSTANCE.sendToGsuid(event);
            MihdpConnect2.INSTANCE.sendToMihdp(null, event,kZeroBot);
        }
    }

    private static final ScriptEngineManager SCRIPT_ENGINE_MANAGER = new ScriptEngineManager();
    public static final ScriptEngine engine = SCRIPT_ENGINE_MANAGER.getEngineByName("JavaScript");

    public interface ContextTemp {
        default void imports(String... packages) {
            PackageScanner scanner = new PackageScannerImpl(true);
            for (String aPackage : packages) {
                try {
                    try {
                        Class cla = Class.forName(aPackage);
                        engine.put(cla.getSimpleName(), engine.eval("Java.type('" + cla.getName() + "')"));
                    } catch (ClassNotFoundException e) {
                        for (Class<?> aClass : scanner.scan(this.getClass(), this.getClass().getClassLoader(), aPackage)) {
                            engine.put(aClass.getSimpleName(), engine.eval("Java.type('" + aClass.getName() + "')"));
                        }
                    }
                } catch (Exception ex) {
                    System.err.println(ex.getMessage());
                    System.err.println(aPackage);
                }
            }
        }

        void send(String msg);

        void send(SendAble msg);
    }

    static {
        try {
            if (engine != null) engine.eval("function importJ(c){ context.imports(c)}");
        } catch (ScriptException e) {
            e.printStackTrace();
        }
    }

    private void temp(MessageEvent event) {
        try {
            if (engine == null) return;
            engine.put("context", new ContextTemp() {
                @Override
                public void send(String msg) {
                    event.send(msg);
                }

                @Override
                public void send(SendAble msg) {
                    event.send(msg);
                }
            });
            engine.eval(new FileReader(new File("./scripts/dev.nashorn")));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
