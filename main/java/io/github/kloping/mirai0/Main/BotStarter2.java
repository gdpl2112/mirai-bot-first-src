package io.github.kloping.mirai0.Main;

import Project.aSpring.SpringStarter;
import Project.listeners.NbListener;
import io.github.kloping.MySpringTool.StarterApplication;
import io.github.kloping.MySpringTool.annotations.CommentScan;
import io.github.kloping.mirai0.Main.Handlers.LittleHandler;
import io.github.kloping.mirai0.Main.Handlers.MyHandler;
import io.github.kloping.mirai0.Main.Handlers.SaveHandler;
import io.github.kloping.mirai0.unitls.Tools.Tool;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.BotFactory;
import net.mamoe.mirai.utils.BotConfiguration;

import java.io.File;

import static io.github.kloping.mirai0.Main.Resource.*;

/**
 * @author github-kloping
 */
@CommentScan(path = "Project")
public class BotStarter2 {

    private static BotConf abot;

    public static void main(String[] args) throws Exception {
        long t = System.currentTimeMillis();
        Tool.tool.setOnErrInFIle(Tool.tool.getLogTimeFormat() + "b2_err.log");
        Tool.tool.setOnOutInFIle(Tool.tool.getLogTimeFormat() + "b2_console.log");
        Tool.tool.deleteDir(new File("./cache2"));
        abot = get(2);
        BotConfiguration botConfiguration = new BotConfiguration();
        botConfiguration.setProtocol(BotConfiguration.MiraiProtocol.ANDROID_PAD);
        botConfiguration.setHeartbeatStrategy(BotConfiguration.HeartbeatStrategy.REGISTER);
        botConfiguration.setCacheDir(new File("./cache2"));
        botConfiguration.fileBasedDeviceInfo("./devices/device3.json");
        Bot bot = BotFactory.INSTANCE.newBot(abot.getQq(), abot.getPassWord(), botConfiguration);
        BOT = bot;
        datePath = "./Libs2";
        init();
        setterStarterApplication(BotStarter2.class);
        SpringStarter.main(args);
        bot.login();
//        pluginLoad();
        startRegisterListenerHost(args);
        startedAfter();
        System.out.println("==============================" + qq.getQq() + ":启动完成=======================================");
        println("运行的线程=》" + Thread.activeCount());
        System.out.println("耗时: " + (System.currentTimeMillis() - t) + "豪秒");
    }

    private static void startRegisterListenerHost(String[] args) {
        BOT.getEventChannel().registerListenerHost(new MyHandler());
        BOT.getEventChannel().registerListenerHost(LittleHandler.contextManager.getContextEntity(LittleHandler.class));
        BOT.getEventChannel().registerListenerHost(
                StarterApplication.Setting.INSTANCE.getContextManager().getContextEntity(NbListener.class)
        );
        BOT.getEventChannel().registerListenerHost(new SaveHandler(args));
    }

}
