package io.github.kloping.mirai0.Main;

import io.github.kloping.MySpringTool.annotations.AutoStand;
import io.github.kloping.MySpringTool.annotations.CommentScan;
import io.github.kloping.MySpringTool.annotations.Controller;
import io.github.kloping.MySpringTool.interfaces.component.ContextManager;
import io.github.kloping.initialize.FileInitializeValue;
import io.github.kloping.mirai0.Main.temp.C0;
import io.github.kloping.mirai0.Main.temp.C1;
import kotlin.coroutines.CoroutineContext;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.BotFactory;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.event.events.FriendMessageEvent;
import net.mamoe.mirai.message.data.PlainText;
import net.mamoe.mirai.utils.BotConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.Future;

import static io.github.kloping.mirai0.Main.Resource.THREADS;
import static io.github.kloping.mirai0.Main.Resource.setterStarterApplication;
import static io.github.kloping.mirai0.commons.resouce_and_tool.ResourceSet.FinalString.NEWLINE;

/**
 * @author github.kloping
 */
@CommentScan(path = "io.github.kloping.mirai0.Main.temp")
@Controller
public class Temp extends SimpleListenerHost {
    @Override
    public void handleException(@NotNull CoroutineContext context, @NotNull Throwable exception) {
        super.handleException(context, exception);
    }

    private static Resource.BotConf abot;
    private static Bot bot;
    @AutoStand
    static ContextManager contextManager;

    public static void main(String[] args) {
        abot = Resource.get(4);
        setterStarterApplication(Temp.class);
        contextManager.append("ANDROID_PAD", "bot.protocol");
        BotConfiguration botConfiguration = new BotConfiguration();
        botConfiguration.setProtocol(BotConfiguration.MiraiProtocol.valueOf(contextManager.getContextEntity(String.class, "bot.protocol")));
        botConfiguration.setHeartbeatStrategy(BotConfiguration.HeartbeatStrategy.STAT_HB);
        botConfiguration.setCacheDir(new File("./cache"));
        botConfiguration.fileBasedDeviceInfo("./device.json");
        bot = BotFactory.INSTANCE.newBot(abot.getQq(), abot.getPassWord(), botConfiguration);
        bot.login();
        bot.getEventChannel().registerListenerHost(contextManager.getContextEntity(Temp.class));
        contextManager.getContextEntity(Temp.class).start();
    }

    @EventHandler
    public void sync(FriendMessageEvent event) {
        if (event.getBot().getId() == event.getSender().getId()) {
            try {
                PlainText text = (PlainText) event.getMessage().get(1);
                String out = comm(text.contentToString().trim());
                if (out != null)
                    event.getSender().sendMessage(out);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public C1 c1 = new C1();

    private String comm(String trim) {
        if (trim.startsWith("/")) {
            String[] ss = trim.substring(1).split("\\s");
            return parse(ss);
        } else {
            return null;
        }
    }

    private String path = "./conf/c1.json";

    private String load() {
        c1 = FileInitializeValue.getValue(path, c1);
        return list();
    }

    private String list() {
        StringBuilder sb = new StringBuilder();
        for (C0 c0 : c1.getList()) {
            sb.append(c0.getHour()).append(":").append(c0.getMinutes()).
                    append("给").append(c0.getTargetId()).append("发送\"").append(c0.getContent()).append("\"").append(NEWLINE);
        }
        if (sb.length() == 0) return "无";
        return sb.toString();
    }

    private String save() {
        FileInitializeValue.putValues(path, c1);
        return "ok";
    }

    private String delete(String s) {
        try {
            c1.getList().remove(Integer.parseInt(s));
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return e.getMessage();
        }
        start();
        return list();
    }

    private String add(String t, String qid, String content) {
        String[] ss = t.split(":");
        Integer t0 = Integer.valueOf(ss[0]);
        Integer t1 = Integer.valueOf(ss[1]);
        String type = qid.substring(0, 1);
        C0 c0 = new C0().setContent(content).setTargetId(Long.parseLong(qid.substring(1)))
                .setType(type).setHour(t0).setMinutes(t1);
        c1.getList().add(c0);
        start();
        return save();
    }

    private String parse(String... ss) {
        for (Method declaredMethod : this.getClass().getDeclaredMethods()) {
            if (declaredMethod.getName().equals(ss[0])) {
                declaredMethod.setAccessible(true);
                Object[] os = new Object[ss.length - 1];
                System.arraycopy(ss, 1, os, 0, os.length);
                try {
                    return declaredMethod.invoke(this, os).toString();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    private Future future;

    public String start() {
        if (c1.getList().isEmpty()) return "empty";
        if (future != null) {
            future.cancel(true);
        }
        future = THREADS.submit(() -> {
            try {
                C0 c0 = c1.nearestC0();
                long t = c0.st();
                Thread.sleep(t);
                switch (c0.getType()) {
                    case "g":
                        bot.getGroup(c0.getTargetId()).sendMessage(c0.getContent());
                        break;
                    case "u":
                        bot.getFriend(c0.getTargetId()).sendMessage(c0.getContent());
                        break;
                    default:
                        break;
                }
                THREADS.execute(this::start);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        return "running";
    }
}
