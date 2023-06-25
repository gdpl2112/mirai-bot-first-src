package io.github.kloping.gb.starter;

import io.github.kloping.common.Public;
import io.github.kloping.file.FileUtils;
import net.mamoe.mirai.console.terminal.MiraiConsoleImplementationTerminal;
import net.mamoe.mirai.console.terminal.MiraiConsoleTerminalLoader;
import net.mamoe.mirai.event.GlobalEventChannel;
import net.mamoe.mirai.event.SimpleListenerHost;

import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.nio.file.Paths;

/**
 * @author github.kloping
 */
public class MiraiStarter {
    public static final File FILE_PID = new File("./pid");
    public static final String F_PATH = "work";
    public static final String S_PATH = "console";

    public static void main(String[] args) throws IOException {
        new File(F_PATH, S_PATH).mkdirs();
        Public.EXECUTOR_SERVICE.submit(() -> {
            MiraiConsoleImplementationTerminal terminal =
                    new MiraiConsoleImplementationTerminal(Paths.get(F_PATH, S_PATH));
            MiraiConsoleTerminalLoader.INSTANCE.startAsDaemon(terminal);
        });
        GlobalEventChannel.INSTANCE.registerListenerHost(new DefaultHandler());
        String name = ManagementFactory.getRuntimeMXBean().getName();
        String pid = name.split("@")[0];
        if (FILE_PID.exists()) FILE_PID.delete();
        FileUtils.putStringInFile(pid, FILE_PID);
    }

    public static class DefaultHandler extends SimpleListenerHost {

    }
}
