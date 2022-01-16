package io.github.kloping.Mirai.Main;

import Project.ASpring.SpringStarter;
import io.github.kloping.Mirai.Main.Handlers.LittleHandler;
import io.github.kloping.Mirai.Main.Handlers.MyHandler;
import io.github.kloping.Mirai.Main.Handlers.OwnerHandler;
import io.github.kloping.MySpringTool.annotations.CommentScan;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.BotFactory;
import net.mamoe.mirai.utils.BotConfiguration;

import java.io.File;

import static Project.Tools.Tool.*;
import static io.github.kloping.Mirai.Main.Resource.*;

/**
 * @author github-kloping
 */
@CommentScan(path = "Project")
public class BotStarter {

    public static boolean test = false;

    static {
        try {
            test = System.getenv().containsKey("USERDOMAIN_ROAMINGPROFILE");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static BotConf abot = null;

    public static void main(String[] args) {
        SetOnErrInFIle(getLogTimeFormat() + "b1_err.log");
        SetOnOutInFIle(getLogTimeFormat() + "b1_console.log");
        deleteDir(new File("./cache"));
        deleteDir(new File("./cache1"));
        parseArgs(args);
        initBot();
        System.out.println(test ? "=============测试=============" : "长运行....................");
        BotConfiguration botConfiguration = new BotConfiguration();
        botConfiguration.setProtocol(
//                test ? BotConfiguration.MiraiProtocol.ANDROID_PAD :
                BotConfiguration.MiraiProtocol.ANDROID_PHONE);
        botConfiguration.setHeartbeatStrategy(BotConfiguration.HeartbeatStrategy.STAT_HB);
        botConfiguration.setCacheDir(new File("./cache1"));
        botConfiguration.fileBasedDeviceInfo("./devices/device1.json");
        Bot bot = BotFactory.INSTANCE.newBot(abot.getQq(), abot.getPassWord(), botConfiguration);
        Resource.bot = bot;
        datePath = "./Libs";
        init();
        SetterStarterApplication(BotStarter.class);
        SpringStarter.main(args);
        bot.login();
        BotStarter.afterLogin();
        pluginLoad();
    }

    private static void initBot() {
        abot = Resource.get(test ? 3 : 1);
    }

    private static void parseArgs(String[] args) {
        try {
            if ("test".equals(args[0].trim().toLowerCase()))
                test = true;
        } catch (Exception e) {
        }
    }

    public static void afterLogin() {
        startRegisterListenerHost();
        startTimer();
        System.out.println("==============================" + qq.getQq() + ":启动完成=======================================");
        Resource.println("运行的线程=》" + Thread.activeCount());
        starterOk(true);
    }

    private static void startRegisterListenerHost() {
        bot.getEventChannel().registerListenerHost(new MyHandler());
        bot.getEventChannel().registerListenerHost(new OwnerHandler());
        bot.getEventChannel().registerListenerHost(LittleHandler.contextManager.getContextEntity(LittleHandler.class));
    }
}