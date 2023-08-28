package io.github.kloping.mirai;

import Project.gs.client.GsClient;
import Project.gs.client.MiraiListenerHost;
import Project.listeners.*;
import Project.utils.Tools.Tool;
import io.github.kloping.MySpringTool.StarterApplication;
import io.github.kloping.common.Public;
import net.mamoe.mirai.console.terminal.MiraiConsoleImplementationTerminal;
import net.mamoe.mirai.console.terminal.MiraiConsoleTerminalLoader;
import net.mamoe.mirai.event.GlobalEventChannel;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.nio.file.Paths;

/**
 * @author github.kloping
 */
public class MiraiStarter {
    public static final File FILE_PID = new File("./pid");

    public static void main(String[] args) throws IOException {
        new File(args[0], args[1]).mkdirs();
        Public.EXECUTOR_SERVICE.submit(() -> {
            MiraiConsoleImplementationTerminal terminal =
                    new MiraiConsoleImplementationTerminal(Paths.get(args[0], args[1]));
            MiraiConsoleTerminalLoader.INSTANCE.startAsDaemon(terminal);
        });
        GlobalEventChannel.INSTANCE.registerListenerHost(new DefaultHandler());
        Tool.INSTANCE.setOnErrInFIle(Tool.INSTANCE.getLogTimeFormat() + "b1_err.log");
        Tool.INSTANCE.setOnOutInFIle(Tool.INSTANCE.getLogTimeFormat() + "b1_console.log");
        if (args.length >= 3) {
            GlobalEventChannel.INSTANCE.registerListenerHost(new LewisHandler());
            try {
                GsClient client = new GsClient();
                Public.EXECUTOR_SERVICE.submit(client);
                GlobalEventChannel.INSTANCE.registerListenerHost(new MiraiListenerHost(client));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //write pid
        String name = ManagementFactory.getRuntimeMXBean().getName();
        String pid = name.split("@")[0];
        if (FILE_PID.exists()) FILE_PID.delete();
        write(FILE_PID.getAbsolutePath(), String.valueOf(pid));
    }

    public static void write(String fillname, String line) throws IOException {
        File file = new File(fillname);
        file.getParentFile().mkdirs();
        FileWriter fw = new FileWriter(fillname, true);
        fw.write(line);
        fw.close();
    }
}
