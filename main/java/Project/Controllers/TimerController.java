package Project.Controllers;

import Project.DataBases.DataBase;
import Project.DataBases.GameDataBase;
import io.github.kloping.Mirai.Main.Handlers.MyTimer;
import io.github.kloping.Mirai.Main.Resource;
import io.github.kloping.MySpringTool.annotations.Controller;
import io.github.kloping.MySpringTool.annotations.Schedule;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.message.data.MessageChainBuilder;

import java.text.SimpleDateFormat;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import static Project.Tools.Tool.update_Today;
import static io.github.kloping.Mirai.Main.Handlers.MyTimer.ZERO_RUNS;
import static io.github.kloping.Mirai.Main.Resource.THREADS;
import static io.github.kloping.Mirai.Main.Resource.bot;

/**
 * @author github-kloping
 */
@Controller
public class TimerController {
    private static int ts = 10;

    @Schedule("00:00:00")
    public static void onZero() {
        update_Today();
        THREADS.execute(() -> {
            GameDataBase.histInfos.clear();
            DataBase.HIST_U_SCORE.clear();
            Resource.Switch.AllK = false;
            for (Group group : bot.getGroups()) {
                if (!ControllerTool.canGroup(group.getId())) {
                    continue;
                }
                group.sendMessage("自动关闭" + ts + "分钟");
            }
            startOnZeroTime();
            try {
                Thread.sleep(1000 * 60 * ts);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Resource.Switch.AllK = true;
            for (Group group : bot.getGroups()) {
                if (!ControllerTool.canGroup(group.getId())) {
                    continue;
                }
                group.sendMessage("自动开启");
            }
        });
    }

    private static void startOnZeroTime() {
        THREADS.execute(() -> {
            for (Runnable runnable : ZERO_RUNS) {
                try {
                    runnable.run();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static final Set<Runnable> MORNING_RUNNABLE = new CopyOnWriteArraySet<>();

    @Schedule("07:10:00")
    public static void onSix() {
        update_Today();
        THREADS.execute(() -> {
            for (Group group : bot.getGroups()) {
                if (!ControllerTool.canGroup(group.getId())) {
                    continue;
                }
                MessageChainBuilder builder = new MessageChainBuilder();
                builder.append("早啊,早啊");
                MyTimer.appendOneDay(builder, group);
                group.sendMessage(builder.build());
            }
        });
    }

    @Schedule("12:00:00")
    public static void onMidTwe() {
        update_Today();
        THREADS.execute(() -> {
            for (Group group : bot.getGroups()) {
                if (!ControllerTool.canGroup(group.getId())) {
                    continue;
                }
                MessageChainBuilder builder = new MessageChainBuilder();
                builder.append("午好,午好");
                group.sendMessage(builder.build());
            }
        });
    }

    @Schedule("17:50:00")
    public static void onNightSix() {
        update_Today();
        THREADS.execute(() -> {
            for (Group group : bot.getGroups()) {
                if (!ControllerTool.canGroup(group.getId())) {
                    continue;
                }
                MessageChainBuilder builder = new MessageChainBuilder();
                builder.append("晚好");
                group.sendMessage(builder.build());
            }
        });
    }

    public static final String BASE_URL_CLOUD = "http://img.nsmc.org.cn/CLOUDIMAGE/FY4A/MTCC/FY4A_CHINA.JPG";
    public static String baseC3 = null;
    public static final SimpleDateFormat FORMAT = new SimpleDateFormat("MM_dd_HH");

}
