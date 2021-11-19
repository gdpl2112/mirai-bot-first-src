package io.github.kloping.Mirai.Main;

import io.github.kloping.Mirai.Main.Handlers.MyHandler;
import io.github.kloping.MySpringTool.annotations.CommentScan;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.BotFactory;
import net.mamoe.mirai.utils.BotConfiguration;

import java.io.File;

import static Project.Tools.Tool.deleteDir;
import static Project.Tools.Tool.getLogTimeFormat;
import static io.github.kloping.Mirai.Main.Resource.*;

@CommentScan(path = "Project")
public class BotStarter2 {

    private static ABot abot;

    public static void main(String[] args) {
        SetOnErrInFIle(getLogTimeFormat() + "b2_err.log");
        SetOnOutInFIle(getLogTimeFormat() + "b2_console.log");
        deleteDir(new File("./cache2"));
        abot = get(5);
        BotConfiguration botConfiguration = new BotConfiguration();
        botConfiguration.setProtocol(BotConfiguration.MiraiProtocol.ANDROID_PHONE);
        botConfiguration.setHeartbeatStrategy(BotConfiguration.HeartbeatStrategy.STAT_HB);
        botConfiguration.setCacheDir(new File("./cache2"));
        botConfiguration.fileBasedDeviceInfo("./devices/device3.json");
        Bot bot = BotFactory.INSTANCE.newBot(abot.getQq(), abot.getPassWord(), botConfiguration);
        Resource.bot = bot;
        Resource.datePath = "./Libs2";
        Resource.Init();
        Resource.SetterStarter(BotStarter2.class);
        bot.login();
        BotStarter2.afterLogin();
    }

    public static void afterLogin() {
        startRegisterListenerHost();
        startTimer();
        System.out.println("==============================" + qq.getQq() + ":启动完成=======================================");
        println("运行的线程=》" + Thread.activeCount());
        StarterOk();
    }

    private static void startRegisterListenerHost() {
        bot.getEventChannel().registerListenerHost(new MyHandler());
    }
}
