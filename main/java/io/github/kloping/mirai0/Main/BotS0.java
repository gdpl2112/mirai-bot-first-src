package io.github.kloping.mirai0.Main;

import io.github.kloping.MySpringTool.StarterApplication;
import io.github.kloping.MySpringTool.h1.impl.LoggerImpl;
import io.github.kloping.mirai0.Main.Handlers.LittleHandler;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.BotFactory;
import net.mamoe.mirai.utils.BotConfiguration;

import java.io.File;

import static io.github.kloping.mirai0.Main.Resource.bot;

/**
 * qq
 * password
 * protocol
 * device
 * HeartbeatStrategy
 *
 * @author github-kloping
 * @version 1.0
 */
public class BotS0 {
    public static void main(String[] args) {
        BotConfiguration botConfiguration = new BotConfiguration();
        BotConfiguration.MiraiProtocol protocol = BotConfiguration.MiraiProtocol.valueOf(args[2]);
        botConfiguration.setProtocol(protocol == null ? BotConfiguration.MiraiProtocol.ANDROID_PAD : protocol);
        BotConfiguration.HeartbeatStrategy hs = args.length >= 5 ? BotConfiguration.HeartbeatStrategy.valueOf(args[4]) : null;
        botConfiguration.setHeartbeatStrategy(hs == null ? BotConfiguration.HeartbeatStrategy.STAT_HB : hs);
        botConfiguration.setCacheDir(new File("./cache"));
        botConfiguration.fileBasedDeviceInfo(args.length >= 4 ? args[3] : "./device.json");
        Bot bot = BotFactory.INSTANCE.newBot(Long.parseLong(args[0]), args[1].trim(), botConfiguration);
        bot.login();
        StarterApplication.logger = new LoggerImpl();
        bot.getEventChannel().registerListenerHost(LittleHandler.contextManager.getContextEntity(LittleHandler.class));
    }

}
