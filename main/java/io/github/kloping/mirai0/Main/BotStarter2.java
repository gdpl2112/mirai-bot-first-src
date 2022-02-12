package io.github.kloping.mirai0.Main;

import Project.aSpring.SpringStarter2;
import Project.listeners.NbListener;
import io.github.kloping.MySpringTool.StarterApplication;
import io.github.kloping.MySpringTool.annotations.CommentScan;
import io.github.kloping.mirai0.Main.Handlers.LittleHandler;
import io.github.kloping.mirai0.Main.Handlers.MyHandler;
import io.github.kloping.mirai0.Main.Handlers.SaveHandler;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.BotFactory;
import net.mamoe.mirai.utils.BotConfiguration;

import java.io.File;

import static io.github.kloping.mirai0.Main.Resource.*;
import static io.github.kloping.mirai0.unitls.Tools.Tool.*;

/**
 * @author github-kloping
 */
@CommentScan(path = "Project")
public class BotStarter2 {

    private static BotConf abot;

    public static void main(String[] args) {
        setOnErrInFIle(getLogTimeFormat() + "b2_err.log");
        setOnOutInFIle(getLogTimeFormat() + "b2_console.log");
        deleteDir(new File("./cache2"));
        abot = get(6);
        BotConfiguration botConfiguration = new BotConfiguration();
        botConfiguration.setProtocol(BotConfiguration.MiraiProtocol.ANDROID_PHONE);
        botConfiguration.setHeartbeatStrategy(BotConfiguration.HeartbeatStrategy.STAT_HB);
        botConfiguration.setCacheDir(new File("./cache2"));
        botConfiguration.fileBasedDeviceInfo("./devices/device3.json");
        Bot bot = BotFactory.INSTANCE.newBot(abot.getQq(), abot.getPassWord(), botConfiguration);
        Resource.bot = bot;
        Resource.datePath = "./Libs2";
        Resource.init();
        Resource.setterStarterApplication(BotStarter2.class);
        SpringStarter2.main(args);
        bot.login();
        pluginLoad();
        startRegisterListenerHost(args);
        starterOk();
        System.out.println("==============================" + qq.getQq() + ":启动完成=======================================");
        println("运行的线程=》" + Thread.activeCount());
    }

    private static void startRegisterListenerHost(String[] args) {
        bot.getEventChannel().registerListenerHost(new MyHandler());
        bot.getEventChannel().registerListenerHost(LittleHandler.contextManager.getContextEntity(LittleHandler.class));
        bot.getEventChannel().registerListenerHost(
                StarterApplication.Setting.INSTANCE.getContextManager().getContextEntity(NbListener.class)
        );
        bot.getEventChannel().registerListenerHost(new SaveHandler(args));
    }

}
