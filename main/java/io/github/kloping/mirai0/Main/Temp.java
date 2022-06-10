package io.github.kloping.mirai0.Main;

import io.github.kloping.MySpringTool.annotations.AutoStand;
import io.github.kloping.MySpringTool.annotations.CommentScan;
import io.github.kloping.MySpringTool.annotations.Controller;
import io.github.kloping.MySpringTool.interfaces.component.ContextManager;
import kotlin.coroutines.CoroutineContext;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.BotFactory;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.utils.BotConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;

import static io.github.kloping.mirai0.Main.Resource.pluginLoad;
import static io.github.kloping.mirai0.Main.Resource.setterStarterApplication;

/**
 * @author github.kloping
 */
@CommentScan(path = "io.github.kloping.mirai0.Main.temp")
@Controller
public class Temp extends SimpleListenerHost {
    @AutoStand
    static ContextManager contextManager;
    private static Resource.BotConf abot;
    private static Bot bot;

    public static void main(String[] args) {
        abot = Resource.get(4);
        setterStarterApplication(Temp.class);
        contextManager.append("ANDROID_PAD", "bot.protocol");
        BotConfiguration botConfiguration = new BotConfiguration();
        botConfiguration.setProtocol(BotConfiguration.MiraiProtocol.valueOf(contextManager.getContextEntity(String.class, "bot.protocol")));
        botConfiguration.setHeartbeatStrategy(BotConfiguration.HeartbeatStrategy.STAT_HB);
        botConfiguration.setCacheDir(new File("./cache"));
        botConfiguration.fileBasedDeviceInfo("./device.json");
        bot = BotFactory.INSTANCE.newBot(abot.getQq(), abot.getPassWord(), botConfiguration);
        bot.login();
        pluginLoad();
    }

    @Override
    public void handleException(@NotNull CoroutineContext context, @NotNull Throwable exception) {
        super.handleException(context, exception);
    }
}
