package io.github.kloping.gb.starter;

import io.github.kloping.common.Public;
import io.github.kloping.file.FileUtils;
import io.github.kloping.gb.*;
import net.mamoe.mirai.console.terminal.MiraiConsoleImplementationTerminal;
import net.mamoe.mirai.console.terminal.MiraiConsoleTerminalLoader;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.GlobalEventChannel;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.event.events.BotOnlineEvent;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.PlainText;
import net.mamoe.mirai.message.data.SingleMessage;

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
        BootstrapResource.INSTANCE.info("bot from mirai started!");
    }

    public static class DefaultHandler extends SimpleListenerHost {
        public BotInterface botInterface;

        @EventHandler
        public void onEvent(GroupMessageEvent event) {
            MessageContext context = new MessageContext(event.getSender().getId(), event.getGroup().getId(), event.getBot().getId());
            context.setData(event.getMessage());
            for (SingleMessage singleMessage : event.getMessage()) {
                if (singleMessage instanceof PlainText) {
                    PlainText text = (PlainText) singleMessage;
                    context.getMsgs().add(new DataText(text.contentToString()));
                } else if (singleMessage instanceof Image) {
                    Image image = (Image) singleMessage;
                    String url = Image.queryUrl(image);
                    DataImage dataImage = new DataImage(image.getImageId(), url);
                    context.getMsgs().add(dataImage);
                } else if (singleMessage instanceof At) {
                    At at = (At) singleMessage;
                    context.getMsgs().add(new DataAt(String.valueOf(at.getTarget())));
                }
            }
            BootstrapResource.INSTANCE.starter.handler(botInterface, context);
        }

        @EventHandler
        public void onEvent(BotOnlineEvent event) {
            botInterface = new MiraiBotInterface(event.getBot());
        }
    }
}
