package io.github.kloping.mirai0.Main;

import Project.aSpring.SpringBootResource;
import Project.aSpring.SpringStarter;
import Project.listeners.DefaultHandler;
import io.github.kloping.MySpringTool.StarterApplication;
import io.github.kloping.MySpringTool.annotations.CommentScan;
import io.github.kloping.common.Public;
import io.github.kloping.mirai0.Main.iutils.MinecraftServerClient;
import io.github.kloping.mirai0.unitls.Tools.Tool;
import net.mamoe.mirai.console.terminal.MiraiConsoleImplementationTerminal;
import net.mamoe.mirai.console.terminal.MiraiConsoleTerminalLoader;
import net.mamoe.mirai.event.GlobalEventChannel;

import java.nio.file.Paths;

import static io.github.kloping.mirai0.Main.BootstarpResource.*;

/**
 * @author github-kloping
 */
@CommentScan(path = "Project")
public class BotStarter {

    public static boolean test = false;

    public static void main(String[] args) {
        long t = System.currentTimeMillis();
        Public.EXECUTOR_SERVICE.submit(() -> {
            MiraiConsoleImplementationTerminal terminal = new MiraiConsoleImplementationTerminal(Paths.get("./works", "/console1"));
            MiraiConsoleTerminalLoader.INSTANCE.startAsDaemon(terminal);
            Tool.INSTANCE.setOnErrInFIle(Tool.INSTANCE.getLogTimeFormat() + "b1_err.log");
            Tool.INSTANCE.setOnOutInFIle(Tool.INSTANCE.getLogTimeFormat() + "b1_console.log");
        });
        GlobalEventChannel.INSTANCE.registerListenerHost(new DefaultHandler());
        setterStarterApplication(BotStarter.class);
        Boolean t0 = StarterApplication.Setting.INSTANCE.getContextManager().getContextEntity(Boolean.class, "env.test");
        test = t0 == null ? false : t0;
        System.out.println(test ? "=============测试=============" : "长运行....................");
        startRegisterListenerHost(args);
        datePath = "./Libs";
        init();
        SpringStarter.main(args);
        startedAfter();
        System.out.println("耗时: " + (System.currentTimeMillis() - t) + "豪秒");
    }

    private static void loadMc() {
        String ip = SpringBootResource.getEnvironment().getProperty("mc.ip");
        if (ip != null)
            new MinecraftServerClient(ip, SpringBootResource.getEnvironment().getProperty("mc.port"), SpringBootResource.getEnvironment().getProperty("mc.gid"));
    }

}