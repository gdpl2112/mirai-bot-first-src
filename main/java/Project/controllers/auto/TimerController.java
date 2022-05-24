package Project.controllers.auto;

import Project.dataBases.DataBase;
import Project.dataBases.GameDataBase;
import com.alibaba.fastjson.JSONObject;
import io.github.kloping.MySpringTool.annotations.Controller;
import io.github.kloping.MySpringTool.annotations.Schedule;
import io.github.kloping.mirai0.Main.Resource;
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

    static {
    }

    public static final Set<Runnable> MORNING_RUNNABLE = new CopyOnWriteArraySet<>();
    private static int ts = 10;

    public static void appendOneDay(MessageChainBuilder builder, Group group) {
        try {
            URL url = new URL("http://open.iciba.com/dsapi");
            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
            String str = br.readLine();
            JSONObject object = (JSONObject) JSONObject.parse(str);
            builder.append(object.getString("note"));
            builder.append("\r\n" + object.getString("content"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Schedule("00:00:00")
    public static void onZero() {
        updateToday();
        THREADS.execute(() -> {
            GameDataBase.HIST_INFOS.clear();
            DataBase.HIST_U_SCORE.clear();
            Resource.Switch.AllK = false;
            startOnZeroTime();
            Resource.Switch.AllK = true;
        });
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
        THREADS.execute(() -> {
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

//    public static String[] At_NOON_TIPS = {"中午好呀~", "午好<Face:336>", "午好<Face:287>", "午好", "又到中午了", "干饭了吗<Face:171>"};

    @Schedule("12:00:00")
    public static void onMidTwe() {
        updateToday();
//        THREADS.execute(() -> {
//            for (Group group : bot.getGroups()) {
//                if (!ControllerTool.canGroup(group.getId())) {
//                    continue;
//                }
//                MessageTools.sendMessageInGroup(getRandString(At_NOON_TIPS), group.getId());
//            }
//        });
    }

//    public static String[] AT_NIGHT_TIPS = {"晚好..", "晚上好<Face:41>", "晚好<Face:63>", "晚好晚好\n<Pic:{74FC0290-CD86-C3E2-754A-9A3FB4196522}.gif>", "晚好\n<Pic:{4687B190-05D6-922E-8B7F-B769D555648C}.gif>"};

    @Schedule("17:50:00")
    public static void onNightSix() {
        updateToday();
//        THREADS.execute(() -> {
//            for (Group group : bot.getGroups()) {
//                if (!ControllerTool.canGroup(group.getId())) {
//                    continue;
//                }
//                MessageTools.sendMessageInGroup(getRandString(AT_NIGHT_TIPS), group.getId());
//            }
//        });
    }
}
