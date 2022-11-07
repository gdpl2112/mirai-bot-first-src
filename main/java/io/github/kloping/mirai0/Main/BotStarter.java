package io.github.kloping.mirai0.Main;

import Project.aSpring.SpringBootResource;
import Project.aSpring.SpringStarter;
import Project.listeners.NbListener;
import io.github.kloping.MySpringTool.StarterApplication;
import io.github.kloping.MySpringTool.annotations.CommentScan;
import io.github.kloping.mirai0.Main.Handlers.LittleHandler;
import io.github.kloping.mirai0.Main.Handlers.MyHandler;
import io.github.kloping.mirai0.Main.Handlers.SaveHandler;
import io.github.kloping.mirai0.Main.ITools.Client;
import io.github.kloping.mirai0.unitls.Tools.Tool;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.console.MiraiConsole;
import net.mamoe.mirai.console.terminal.MiraiConsoleImplementationTerminal;
import net.mamoe.mirai.console.terminal.MiraiConsoleTerminalLoader;
import net.mamoe.mirai.utils.BotConfiguration;

import java.nio.file.Paths;

import static io.github.kloping.mirai0.Main.Resource.*;

/**
 * @author github-kloping
 */
@CommentScan(path = "Project")
public class BotStarter {

    public static boolean test = false;
    private static BotConf abot = null;

    public static void main(String[] args) throws Exception {
        long t = System.currentTimeMillis();
        Tool.tool.setOnErrInFIle(Tool.tool.getLogTimeFormat() + "b1_err.log");
        Tool.tool.setOnOutInFIle(Tool.tool.getLogTimeFormat() + "b1_console.log");
        setterStarterApplication(BotStarter.class);
        Boolean t0 = StarterApplication.Setting.INSTANCE.getContextManager().getContextEntity(Boolean.class, "env.test");
        test = t0 == null ? false : t0;
        initBot();
        System.out.println(test ? "=============测试=============" : "长运行....................");
//        BotConfiguration botConfiguration = new BotConfiguration();
//        botConfiguration.setProtocol(BotConfiguration.MiraiProtocol.valueOf(
//                StarterApplication.Setting.INSTANCE.getContextManager().getContextEntity(String.class, "bot.protocol")));
//        botConfiguration.setHeartbeatStrategy(BotConfiguration.HeartbeatStrategy.STAT_HB);
//        botConfiguration.setCacheDir(new File("./cache1"));
//        botConfiguration.fileBasedDeviceInfo("./devices/device1.json");
        MiraiConsoleImplementationTerminal terminal = new MiraiConsoleImplementationTerminal(Paths.get("./works", "/console1"));
        MiraiConsoleTerminalLoader.INSTANCE.startAsDaemon(terminal);
        Bot bot;
        bot = MiraiConsole.INSTANCE.addBot(abot.getQq(), abot.getPassWord(), new Function1<BotConfiguration, Unit>() {
            @Override
            public Unit invoke(BotConfiguration botConfiguration) {
                botConfiguration.setProtocol(BotConfiguration.MiraiProtocol.valueOf(
                        StarterApplication.Setting.INSTANCE.getContextManager().getContextEntity(String.class, "bot.protocol")));
                botConfiguration.setHeartbeatStrategy(BotConfiguration.HeartbeatStrategy.STAT_HB);
                return null;
            }
        });
        Resource.BOT = bot;
        datePath = "./Libs";
        init();
        SpringStarter.main(args);
        bot.login();
        startRegisterListenerHost(args);
        startedAfter();
        System.out.println("==============================" + qq.getQq() + ":启动完成=======================================");
        Resource.println("运行的线程=》" + Thread.activeCount());
        System.out.println("耗时: " + (System.currentTimeMillis() - t) + "豪秒");
//        pluginLoad();
    }

    private static void loadMc() {
        String ip = SpringBootResource.getEnvironment().getProperty("mc.ip");
        if (ip != null)
            new Client(
                    ip,
                    SpringBootResource.getEnvironment().getProperty("mc.port"),
                    SpringBootResource.getEnvironment().getProperty("mc.gid")
            );
    }

    private static void initBot() {
        abot = Resource.get(test ? 3 : 1);
    }

    private static void startRegisterListenerHost(String[] args) {
        BOT.getEventChannel().registerListenerHost(new MyHandler());
        BOT.getEventChannel().registerListenerHost(LittleHandler.contextManager.getContextEntity(LittleHandler.class));
        BOT.getEventChannel().registerListenerHost(
                StarterApplication.Setting.INSTANCE.getContextManager().getContextEntity(NbListener.class)
        );
        BOT.getEventChannel().registerListenerHost(new SaveHandler(args));
    }
}