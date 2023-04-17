package Project.controllers.auto;

import Project.dataBases.DataBase;
import Project.dataBases.GameDataBase;
import com.google.gson.Gson;
import io.github.kloping.MySpringTool.annotations.AutoStand;
import io.github.kloping.MySpringTool.annotations.Controller;
import io.github.kloping.MySpringTool.annotations.CronSchedule;
import io.github.kloping.MySpringTool.annotations.Schedule;
import io.github.kloping.mirai0.Main.BotStarter;
import io.github.kloping.mirai0.Main.BootstarpResource;
import Project.commons.apiEntitys.iciba.Dsapi;
import io.github.kloping.mirai0.unitls.Tools.Tool;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.message.data.Message;
import net.mamoe.mirai.message.data.MessageChainBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import static io.github.kloping.mirai0.Main.BootstarpResource.BOT;
import static io.github.kloping.mirai0.Main.BootstarpResource.THREADS;

/**
 * @author github-kloping
 */
@Controller
public class TimerController {
    public static final List<Runnable> ZERO_RUNS = new ArrayList<>();
    public static final Set<Runnable> MORNING_RUNNABLE = new CopyOnWriteArraySet<>();
    @AutoStand(id = "gson0")
    private static Gson gson;
    private static int ts = 10;

    public static void appendOneDay(MessageChainBuilder builder) {
        try {
            URL url = new URL("http://open.iciba.com/dsapi");
            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
            String str = br.readLine();
            Dsapi dsapi = gson.fromJson(str, Dsapi.class);
            builder.append(dsapi.getNote());
            builder.append("\r\n" + dsapi.getContent());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Schedule("00:00:00")
    public static void onZero() {
        Tool.INSTANCE.updateToday();
        GameDataBase.HIST_INFOS.clear();
        DataBase.HIST_U_SCORE.clear();
        BootstarpResource.Switch.AllK = false;
        startOnZeroTime();
        BootstarpResource.Switch.AllK = true;
        THREADS.submit(() -> {
            try {
                int r = Tool.INSTANCE.RANDOM.nextInt(15);
                Thread.sleep(r * 1000);
                BootstarpResource.verify();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    private static void startOnZeroTime() {
        for (Runnable runnable : ZERO_RUNS) {
            try {
                runnable.run();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Schedule("06:10:00")
    public static void onSix() {
        Tool.INSTANCE.updateToday();
        THREADS.submit(() -> {
            MessageChainBuilder builder = new MessageChainBuilder();
            appendOneDay(builder);
            Message message = builder.build();
            for (Group group : BOT.getGroups()) {
                if (!ControllerTool.canGroup(group.getId())) {
                    continue;
                }
                group.sendMessage(message);
            }
            THREADS.submit(() -> {
                for (Runnable runnable : MORNING_RUNNABLE) {
                    runnable.run();
                }
            });
        });
    }

    private static final String TIPS0 = "星期一到星期五的上午10:10分到晚上22:20开启,星期六,天全天开放";
    private static long t = 759590727L;

    @CronSchedule("0 10 10 ? * 2-6")
    public static void testOn() {
        if (BotStarter.test) {
            BootstarpResource.Switch.AllK = true;
            BOT.getGroups().get(t).sendMessage(TIPS0);
            BOT.getGroups().get(t).sendMessage("现已开机");
        }
    }

    @CronSchedule("0 20 22 ? * 2-5")
    public static void testOff() {
        if (BotStarter.test) {
            BootstarpResource.Switch.AllK = false;
            BOT.getGroups().get(t).sendMessage(TIPS0);
            BOT.getGroups().get(t).sendMessage("现已关机");
        }
    }

    @Schedule("12:00:00")
    public static void onMidTwe() {
        Tool.INSTANCE.updateToday();
    }


    @Schedule("17:50:00")
    public static void onNightSix() {
        Tool.INSTANCE.updateToday();
    }
}
