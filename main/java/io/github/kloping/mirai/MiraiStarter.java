package io.github.kloping.mirai;

import Project.listeners.*;
import io.github.kloping.MySpringTool.StarterApplication;
import io.github.kloping.common.Public;
import io.github.kloping.mirai0.unitls.Tools.Tool;
import net.mamoe.mirai.console.terminal.MiraiConsoleImplementationTerminal;
import net.mamoe.mirai.console.terminal.MiraiConsoleTerminalLoader;
import net.mamoe.mirai.event.GlobalEventChannel;

import java.nio.file.Paths;

/**
 * @author github.kloping
 */
public class MiraiStarter {
    public static void main(String[] args) {
        Public.EXECUTOR_SERVICE.submit(() -> {
            MiraiConsoleImplementationTerminal terminal = new MiraiConsoleImplementationTerminal(Paths.get(args[0], args[1]));
            MiraiConsoleTerminalLoader.INSTANCE.startAsDaemon(terminal);
            Tool.INSTANCE.setOnErrInFIle(Tool.INSTANCE.getLogTimeFormat() + "b1_err.log");
            Tool.INSTANCE.setOnOutInFIle(Tool.INSTANCE.getLogTimeFormat() + "b1_console.log");
        });
        GlobalEventChannel.INSTANCE.registerListenerHost(new DefaultHandler());
        if (args.length >= 3) {
            GlobalEventChannel.INSTANCE.registerListenerHost(new LewisHandler());
        }
    }

    private static void startRegisterListenerHost(String[] args) {
        GlobalEventChannel.INSTANCE.registerListenerHost(LittleHandler.contextManager.getContextEntity(LittleHandler.class));
        GlobalEventChannel.INSTANCE.registerListenerHost(new SaveHandler(args));
        GlobalEventChannel.INSTANCE.registerListenerHost(new NoGroupHandler());
        StarterApplication.STARTED_RUNNABLE.add(() -> {
            GlobalEventChannel.INSTANCE.
                    registerListenerHost(StarterApplication.Setting.INSTANCE.getContextManager().getContextEntity(NbListener.class));
        });
    }
}
