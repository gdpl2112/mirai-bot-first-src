package io.github.kloping.mirai0.Main;

import Project.aSpring.SpringStarter;
import Project.listeners.NbListener;
import io.github.kloping.MySpringTool.StarterApplication;
import io.github.kloping.MySpringTool.annotations.CommentScan;
import io.github.kloping.mirai0.Main.Handlers.LittleHandler;
import io.github.kloping.mirai0.Main.Handlers.MyHandler;
import io.github.kloping.mirai0.Main.Handlers.SaveHandler;
import io.github.kloping.mirai0.unitls.Tools.Tool;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.console.MiraiConsole;
import net.mamoe.mirai.console.terminal.MiraiConsoleImplementationTerminal;
import net.mamoe.mirai.console.terminal.MiraiConsoleTerminalLoader;
import net.mamoe.mirai.utils.BotConfiguration;

import java.nio.file.Paths;

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
        abot = get(2);
        MiraiConsoleImplementationTerminal terminal = new MiraiConsoleImplementationTerminal(Paths.get("./works", "/console2"));
        MiraiConsoleTerminalLoader.INSTANCE.startAsDaemon(terminal);
//        BotConfiguration botConfiguration = new BotConfiguration();
//        botConfiguration.setProtocol(BotConfiguration.MiraiProtocol.ANDROID_PHONE);
//        botConfiguration.setHeartbeatStrategy(BotConfiguration.HeartbeatStrategy.STAT_HB);
//        botConfiguration.setCacheDir(new File("./cache"));
//        botConfiguration.fileBasedDeviceInfo("./devices/device2.json");
//        bot = BotFactory.INSTANCE.newBot(abot.getQq(), abot.getPassWord(), botConfiguration);
        Bot bot;
        bot = MiraiConsole.INSTANCE.addBot(abot.getQq(), abot.getPassWord(), new Function1<BotConfiguration, Unit>() {
            @Override
            public Unit invoke(BotConfiguration botConfiguration) {
                botConfiguration.setProtocol(BotConfiguration.MiraiProtocol.ANDROID_PHONE);
                botConfiguration.setHeartbeatStrategy(BotConfiguration.HeartbeatStrategy.STAT_HB);
                return null;
            }
        });
        BOT = bot;
        datePath = "./Libs2";
        init();
        setterStarterApplication(BotStarter2.class);
        SpringStarter.main(args);
        bot.login();
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
