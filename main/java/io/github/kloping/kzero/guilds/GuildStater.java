package io.github.kloping.kzero.guilds;

import io.github.kloping.date.DateUtils;
import io.github.kloping.kzero.gsuid.GsuidClient;
import io.github.kloping.kzero.main.KZeroMainThreads;
import io.github.kloping.kzero.main.api.*;
import io.github.kloping.qqbot.Starter;
import io.github.kloping.qqbot.api.SendAble;
import io.github.kloping.qqbot.api.event.ConnectedEvent;
import io.github.kloping.qqbot.api.message.MessageChannelReceiveEvent;
import io.github.kloping.qqbot.api.message.MessageDirectReceiveEvent;
import io.github.kloping.qqbot.api.v2.GroupMessageEvent;
import io.github.kloping.qqbot.entities.Bot;
import io.github.kloping.qqbot.entities.ex.msg.MessageChain;
import io.github.kloping.qqbot.impl.EventReceiver;
import io.github.kloping.qqbot.impl.ListenerHost;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.ByteArrayInputStream;
import java.io.File;
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
        this.appid = appid;
        this.token = token;
        this.secret = secret;
        this.code = code;
    }

    private final String host = "localhost";

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
        starter.getConfig().setInterceptor0(bytes -> {
            try {
                String url = Jsoup.connect(String.format("http://%s/upload-img", host))
                        .ignoreContentType(true)
                        .ignoreContentType(true)
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/114.0.0.0 Safari/537.36 Edg/114.0.1823.67")
                        .data("file", "temp.jpg", new ByteArrayInputStream(bytes)).method(Connection.Method.POST).execute().body();
                url = url.replace(host, "kloping.top");
                return url;
            } catch (IOException e) {
                return e.getMessage();
            }
        });
        starter.run();
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
        GsuidClient.INSTANCE.addListener(bid, g2g);
        if (KZeroMainThreads.BOT_MAP.containsKey(bid)) return;
        onConnectedEventFirst(event);
    }

    private void onConnectedEventFirst(ConnectedEvent event) {
        Bot bot = event.getBot();
        GuildSerializer guildSerializer = new GuildSerializer(bot);
        KZeroBot<SendAble, Bot> kZeroBot = create(bot.getId(), bot, new GuildBotAdapter(bot, guildSerializer), guildSerializer);
        listener.created(this, kZeroBot);
    }

    private Guild2Gsuid g2g = new Guild2Gsuid();

    @EventReceiver
    public void onEvent(MessageChannelReceiveEvent event) {
        MessageChain chain = event.getMessage();
        g2g.offer(event);
        if (handler != null) {
            KZeroBot<SendAble, Bot> kZeroBot = KZeroMainThreads.BOT_MAP.get(String.valueOf(event.getBot().getId()));
            String outMsg = kZeroBot.getSerializer().serialize(chain);
            if (outMsg.startsWith("/") && outMsg.length() > 1) outMsg = outMsg.substring(1);
            MessagePack pack = new MessagePack(MessageType.GROUP, event.getSender().getUser().getId(),
                    event.getChannelId(), outMsg);
            pack.setRaw(event);
            handler.onMessage(pack);
            //plugin to gsuid
            g2g.sendToGsuid(event);
        }
    }

    @EventReceiver
    public void onEvent(GroupMessageEvent event) {
        MessageChain chain = event.getMessage();
        g2g.offer(event);
        if (handler != null) {
            KZeroBot<SendAble, Bot> kZeroBot = KZeroMainThreads.BOT_MAP.get(String.valueOf(event.getBot().getId()));
            String outMsg = kZeroBot.getSerializer().serialize(chain);
            if (outMsg.startsWith("/") && outMsg.length() > 1) outMsg = outMsg.substring(1);
            MessagePack pack = new MessagePack(MessageType.GROUP, event.getSender().getId(),
                    event.getSubject().getId(), outMsg);
            pack.setRaw(event);
            handler.onMessage(pack);
            //plugin to gsuid
            g2g.sendToGsuid(event);
        }
    }

    @EventReceiver
    public void onEvent(MessageDirectReceiveEvent event) {
        MessageChain chain = event.getMessage();
        g2g.offer(event);
        if (handler != null) {
            KZeroBot<SendAble, Bot> kZeroBot = KZeroMainThreads.BOT_MAP.get(String.valueOf(event.getBot().getId()));
            MessagePack pack = new MessagePack(MessageType.GROUP, event.getSender().getUser().getId(),
                    event.getSrcGuildId(), kZeroBot.getSerializer().serialize(chain));
            pack.setRaw(event);
            handler.onMessage(pack);
            //plugin to gsuid
            g2g.sendToGsuid(event);
        }
    }
}
