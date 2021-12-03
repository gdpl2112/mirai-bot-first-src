package io.github.kloping.Mirai.Main;

import io.github.kloping.Mirai.Main.Handlers.LittleHandler;
import io.github.kloping.MySpringTool.StarterApplication;
import io.github.kloping.MySpringTool.h1.impl.LoggerImpl;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.BotFactory;
import net.mamoe.mirai.utils.BotConfiguration;

import java.io.File;

public class OwnerSetStarter {

    public static void main(String[] args) {
        BotConfiguration botConfiguration = new BotConfiguration();
        botConfiguration.setProtocol(BotConfiguration.MiraiProtocol.ANDROID_PAD);
        botConfiguration.setHeartbeatStrategy(BotConfiguration.HeartbeatStrategy.NONE);
        botConfiguration.setCacheDir(new File("./cache"));
        botConfiguration.fileBasedDeviceInfo("./devices/device2.json");
        Resource.ABot abot = Resource.get(4);
        Bot bot = BotFactory.INSTANCE.newBot(abot.getQq(), abot.getPassWord(), botConfiguration);
        bot.login();

        LittleHandler.contextManager.append(Bot.class, bot, "0");
        StarterApplication.logger = new LoggerImpl();
        LittleHandler.init();
        LittleHandler handler = LittleHandler.contextManager.getContextEntity(LittleHandler.class);
        bot.getEventChannel().registerListenerHost(handler);
    }
}
