package io.github.kloping.mirai0.Main;

import Project.aSpring.SpringBootResource;
import Project.aSpring.SpringStarter;
import Project.listeners.NbListener;
import io.github.kloping.MySpringTool.StarterApplication;
import io.github.kloping.MySpringTool.annotations.CommentScan;
import io.github.kloping.common.Public;
import io.github.kloping.mirai0.Main.Handlers.LittleHandler;
import io.github.kloping.mirai0.Main.Handlers.MyHandler;
import io.github.kloping.mirai0.Main.Handlers.SaveHandler;
import io.github.kloping.mirai0.Main.ITools.Client;
import io.github.kloping.mirai0.unitls.Tools.Tool;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.console.terminal.MiraiConsoleImplementationTerminal;
import net.mamoe.mirai.console.terminal.MiraiConsoleTerminalLoader;
import net.mamoe.mirai.event.GlobalEventChannel;

import java.nio.file.Paths;
import java.util.concurrent.CountDownLatch;

import static io.github.kloping.mirai0.Main.Resource.*;

/**
 * @author github-kloping
 */
@CommentScan(path = "Project")
public class BotStarter {

    public static boolean test = false;
    private static BotConf abot = null;

    public static void main(String[] args) {
        long t = System.currentTimeMillis();
        Public.EXECUTOR_SERVICE.submit(() -> {
            MiraiConsoleImplementationTerminal terminal = new MiraiConsoleImplementationTerminal(Paths.get("./works", "/console1"));
            MiraiConsoleTerminalLoader.INSTANCE.startAsDaemon(terminal);
            Tool.tool.setOnErrInFIle(Tool.tool.getLogTimeFormat() + "b1_err.log");
            Tool.tool.setOnOutInFIle(Tool.tool.getLogTimeFormat() + "b1_console.log");
        });
        GlobalEventChannel.INSTANCE.registerListenerHost(new MyHandler());
        setterStarterApplication(BotStarter.class);
        Boolean t0 = StarterApplication.Setting.INSTANCE.getContextManager().getContextEntity(Boolean.class, "env.test");
        test = t0 == null ? false : t0;
        initBot();
        System.out.println(test ? "=============测试=============" : "长运行....................");
        startRegisterListenerHost(args);
        datePath = "./Libs";
        init();
        SpringStarter.main(args);
        startedAfter();
        System.out.println("==============================" + qq.getQq() + ":启动完成=======================================");
        Resource.println("运行的线程=》" + Thread.activeCount());
        System.out.println("耗时: " + (System.currentTimeMillis() - t) + "豪秒");
    }

    private static void loadMc() {
        String ip = SpringBootResource.getEnvironment().getProperty("mc.ip");
        if (ip != null)
            new Client(ip, SpringBootResource.getEnvironment().getProperty("mc.port"), SpringBootResource.getEnvironment().getProperty("mc.gid"));
    }

    private static void initBot() {
        abot = Resource.get(test ? 3 : 1);
    }

    public static void startRegisterListenerHost(String[] args) {
        GlobalEventChannel.INSTANCE.registerListenerHost(LittleHandler.contextManager.getContextEntity(LittleHandler.class));
        GlobalEventChannel.INSTANCE.registerListenerHost(StarterApplication.Setting.INSTANCE.getContextManager().getContextEntity(NbListener.class));
        GlobalEventChannel.INSTANCE.registerListenerHost(new SaveHandler(args));
    }
}