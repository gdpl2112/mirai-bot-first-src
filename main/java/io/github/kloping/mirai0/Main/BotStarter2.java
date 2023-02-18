package io.github.kloping.mirai0.Main;

import Project.aSpring.SpringStarter;
import Project.listeners.DefaultHandler;
import io.github.kloping.MySpringTool.annotations.CommentScan;
import io.github.kloping.common.Public;
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
public class BotStarter2 {

    public static void main(String[] args) throws Exception {
        long t = System.currentTimeMillis();
        Public.EXECUTOR_SERVICE.submit(() -> {
            MiraiConsoleImplementationTerminal terminal = new MiraiConsoleImplementationTerminal(Paths.get("./works", "/console2"));
            MiraiConsoleTerminalLoader.INSTANCE.startAsDaemon(terminal);
            Tool.INSTANCE.setOnErrInFIle(Tool.INSTANCE.getLogTimeFormat() + "b2_err.log");
            Tool.INSTANCE.setOnOutInFIle(Tool.INSTANCE.getLogTimeFormat() + "b2_console.log");
        });
        GlobalEventChannel.INSTANCE.registerListenerHost(new DefaultHandler());
        setterStarterApplication(BotStarter2.class);
        startRegisterListenerHost(args);
        datePath = "./Libs";
        init();
        SpringStarter.main(args);
        startedAfter();
        System.out.println("耗时: " + (System.currentTimeMillis() - t) + "豪秒");
    }
}
