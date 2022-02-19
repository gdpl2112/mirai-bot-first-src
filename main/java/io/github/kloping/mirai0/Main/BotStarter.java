package io.github.kloping.mirai0.Main;

import Project.aSpring.SpringStarter;
import io.github.kloping.MySpringTool.StarterApplication;
import io.github.kloping.mirai0.Main.Handlers.EmojiCompositeHandler;
import io.github.kloping.mirai0.Main.Handlers.LittleHandler;
import io.github.kloping.mirai0.Main.Handlers.MyHandler;
import io.github.kloping.MySpringTool.annotations.CommentScan;
import Project.listeners.NbListener;
import io.github.kloping.mirai0.Main.Handlers.SaveHandler;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.BotFactory;
import net.mamoe.mirai.utils.BotConfiguration;

import java.io.File;

import static Project.dataBases.GameDataBase.getInfo;
import static io.github.kloping.mirai0.unitls.Tools.Tool.*;
import static io.github.kloping.mirai0.Main.Resource.*;

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
        }
    }

    private static BotConf abot = null;

    public static void main(String[] args) {
        setOnErrInFIle(getLogTimeFormat() + "b1_err.log");
        setOnOutInFIle(getLogTimeFormat() + "b1_console.log");
        deleteDir(new File("./cache"));
        deleteDir(new File("./cache1"));
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
        SpringStarter.main(args);
        bot.login();
        starterOk();
        setterStarterApplication(BotStarter.class);
        startRegisterListenerHost(args);
        System.out.println("==============================" + qq.getQq() + ":启动完成=======================================");
        Resource.println("运行的线程=》" + Thread.activeCount());
    }

    private static void initBot() {
        abot = Resource.get(test ? 3 : 1);
    }

    private static void startRegisterListenerHost(String[] args) {
        bot.getEventChannel().registerListenerHost(new MyHandler());
        bot.getEventChannel().registerListenerHost(LittleHandler.contextManager.getContextEntity(LittleHandler.class));
        bot.getEventChannel().registerListenerHost(
                StarterApplication.Setting.INSTANCE.getContextManager().getContextEntity(NbListener.class)
        );
        bot.getEventChannel().registerListenerHost(new EmojiCompositeHandler());
        bot.getEventChannel().registerListenerHost(new SaveHandler(args));
    }
}