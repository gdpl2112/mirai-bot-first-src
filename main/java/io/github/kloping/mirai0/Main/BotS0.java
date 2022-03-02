package io.github.kloping.mirai0.Main;

import io.github.kloping.mirai0.Main.Handlers.SaveHandler;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.BotFactory;
import net.mamoe.mirai.utils.BotConfiguration;

import java.io.File;

import static io.github.kloping.mirai0.unitls.Tools.Tool.*;

/**
 * @author github-kloping
 * @version 1.0
 */
public class BotS0 {
    public static void main(String[] args) {
        setOnErrInFIle(getLogTimeFormat() + "err.log");
        setOnOutInFIle(getLogTimeFormat() + "console.log");
        deleteDir(new File("./cache"));
        BotConfiguration botConfiguration = new BotConfiguration();
        botConfiguration.setProtocol(BotConfiguration.MiraiProtocol.ANDROID_PAD);
        botConfiguration.setHeartbeatStrategy(BotConfiguration.HeartbeatStrategy.STAT_HB);
        botConfiguration.setCacheDir(new File("./cache"));
        botConfiguration.fileBasedDeviceInfo("./devices/device.json");
        Resource.BotConf abot = Resource.get(1);
        Bot bot = BotFactory.INSTANCE.newBot(abot.getQq(), abot.getPassWord(), botConfiguration);
        bot.login();
        bot.getEventChannel().registerListenerHost(new SaveHandler(args));
    }
}
