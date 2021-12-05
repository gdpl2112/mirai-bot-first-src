package io.github.kloping.Mirai.Main;

import io.github.kloping.Mirai.Main.Handlers.LittleHandler;
import io.github.kloping.MySpringTool.StarterApplication;
import io.github.kloping.MySpringTool.h1.impl.LoggerImpl;
import io.github.kloping.MySpringTool.interfaces.entitys.MatherResult;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.BotFactory;
import net.mamoe.mirai.utils.BotConfiguration;

import java.io.File;
import java.lang.reflect.Method;
import java.util.Scanner;

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

//        Scanner sc = new Scanner(System.in);
//        String line = null;
//        while ((line = sc.nextLine()) != null) {
//            try {
//                // sendAllGroup ?
//                MatherResult result = LittleHandler.actionManager.mather(line);
//                if (result != null) {
//                    for (Method method : result.getMethods()) {
//                        try {
//                            method.invoke(handler, line);
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
    }
}
