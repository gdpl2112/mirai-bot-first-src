package Project.controllers.auto;

import Project.dataBases.DataBase;
import Project.dataBases.GameDataBase;
import com.google.gson.Gson;
import io.github.kloping.MySpringTool.annotations.AutoStand;
import io.github.kloping.MySpringTool.annotations.Controller;
import io.github.kloping.MySpringTool.annotations.Schedule;
import io.github.kloping.mirai0.Main.Resource;
import io.github.kloping.mirai0.commons.apiEntitys.iciba.Dsapi;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.message.data.MessageChainBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import static io.github.kloping.mirai0.Main.Resource.BOT;
import static io.github.kloping.mirai0.Main.Resource.THREADS;
import static io.github.kloping.mirai0.unitls.Tools.Tool.updateToday;

/**
 * @author github-kloping
 */
@Controller
public class TimerController {
    public static final List<Runnable> ZERO_RUNS = new ArrayList<>();

    @AutoStand(id = "gson0")
    private static Gson gson;

    public static final Set<Runnable> MORNING_RUNNABLE = new CopyOnWriteArraySet<>();
    private static int ts = 10;

    public static void appendOneDay(MessageChainBuilder builder, Group group) {
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
        updateToday();
        GameDataBase.HIST_INFOS.clear();
        DataBase.HIST_U_SCORE.clear();
        Resource.Switch.AllK = false;
        startOnZeroTime();
        Resource.Switch.AllK = true;
        try {
            Resource.verify();
        } catch (RuntimeException e) {
            e.printStackTrace();
            System.exit(0);
        }
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
        updateToday();
        THREADS.submit(() -> {
            for (Group group : BOT.getGroups()) {
                if (!ControllerTool.canGroup(group.getId())) {
                    continue;
                }
                MessageChainBuilder builder = new MessageChainBuilder();
                appendOneDay(builder, group);
                group.sendMessage(builder.build());
            }
            THREADS.submit(() -> {
                for (Runnable runnable : MORNING_RUNNABLE) {
                    runnable.run();
                }
            });
        });
    }


    @Schedule("12:00:00")
    public static void onMidTwe() {
        updateToday();
    }


    @Schedule("17:50:00")
    public static void onNightSix() {
        updateToday();
    }
}
