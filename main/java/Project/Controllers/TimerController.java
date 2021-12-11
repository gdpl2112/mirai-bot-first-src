package Project.Controllers;

import Project.DataBases.DataBase;
import Project.DataBases.GameDataBase;
import Project.Tools.Tool;
import io.github.kloping.Mirai.Main.Handlers.MyTimer;
import io.github.kloping.Mirai.Main.ITools.MessageTools;
import io.github.kloping.Mirai.Main.Resource;
import io.github.kloping.MySpringTool.annotations.Controller;
import io.github.kloping.MySpringTool.annotations.Schedule;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.MessageChainBuilder;

import java.text.SimpleDateFormat;

import static Project.Tools.Tool.update_Today;
import static io.github.kloping.Mirai.Main.Handlers.MyTimer.ZeroRuns;
import static io.github.kloping.Mirai.Main.Handlers.MyTimer.gs;
import static io.github.kloping.Mirai.Main.Resource.threads;

@Controller
public class TimerController {

    @Schedule("00:00:00")
    public static void onZero() {
        update_Today();
        threads.execute(() -> {
            GameDataBase.histInfos.clear();
            DataBase.histUScore.clear();
            Resource.Switch.AllK = false;
            for (long g : gs) {
                if (!ControllerTool.CanGroup(g)) continue;
                Group group = Resource.bot.getGroup(g);
                group.sendMessage("自动关闭5分钟");
            }
            startOnZeroTime();
            try {
                Thread.sleep(1000 * 60 * 5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Resource.Switch.AllK = true;
            for (long g : gs) {
                if (!ControllerTool.CanGroup(g)) continue;
                Group group = Resource.bot.getGroup(g);
                group.sendMessage("自动开启");
            }
        });
        threads.execute(() -> {
            m1();
        });
    }

    private static void startOnZeroTime() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (Runnable runnable : ZeroRuns) {
                    try {
                        runnable.run();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Resource.bot.getFriend(3474006766L).sendMessage(e.getMessage());
                    }
                }
            }
        }).start();
    }

    @Schedule("07:20:00")
    public static void onSix() {
        update_Today();
        threads.execute(() -> {
            for (long g : gs) {
                if (!ControllerTool.CanGroup(g)) continue;
                Group group = Resource.bot.getGroup(g);
                MessageChainBuilder builder = new MessageChainBuilder();
                builder.append("早啊,早啊");
                MyTimer.appendOneDay(builder, group);
                group.sendMessage(builder.build());
            }
        });
        threads.execute(() -> {
            m1();
        });
    }

    @Schedule("12:00:00")
    public static void onMidTwe() {
        update_Today();
        threads.execute(() -> {
            for (long g : gs) {
                if (!ControllerTool.CanGroup(g)) continue;
                Group group = Resource.bot.getGroup(g);
                MessageChainBuilder builder = new MessageChainBuilder();
                builder.append("午好,午好");
                group.sendMessage(builder.build());
            }
        });
        threads.execute(() -> {
            m1();
        });
    }

    @Schedule("17:50:00")
    public static void onNightSix() {
        update_Today();
        threads.execute(() -> {
            for (long g : gs) {
                if (!ControllerTool.CanGroup(g)) continue;
                Group group = Resource.bot.getGroup(g);
                MessageChainBuilder builder = new MessageChainBuilder();
                builder.append("晚好");
                group.sendMessage(builder.build());
            }
        });
        threads.execute(() -> {
            m1();
        });
    }

    public static final String baseUrlCloud = "http://img.nsmc.org.cn/CLOUDIMAGE/FY4A/MTCC/FY4A_CHINA.JPG";
    public static String baseC3 = null;

    //    @TimeEve(1000 * 60 * 60 * 10)
    public static void m1() {
        threads.execute(() -> {
//            File file = null;
//            try {
//                if (baseC3 == null) {
//                    byte[] bytes = Tool.getBytesFromHttpUrl(NetMain.rootPath + "/getMCloud3");
//                    baseC3 = new String(bytes, "utf-8").trim();
//                }
//
//                file = File.createTempFile("temp", ".mp4");
//
//                byte[] bytes = Tool.getBytesFromHttpUrl(baseC3);
//                FileOutputStream fos = new FileOutputStream(file);
//                fos.write(bytes);
//                fos.close();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }

            for (long g : gs) {
                if (!ControllerTool.CanGroup(g)) continue;
                Group group = Resource.bot.getGroup(g);
                Image image = MessageTools.createImageInGroup(group, baseUrlCloud);
                MessageChainBuilder builder = new MessageChainBuilder();
                builder.append("当前时间:" + Tool.getTimeYMdhm(System.currentTimeMillis()));
                builder.append("\n");
                builder.append(image);
                group.sendMessage(builder.build());
//                File finalFile = file;
//                threads.execute(() -> {
//                    try {
//                        String dataStr = format.format(new Date());
//                        RemoteFile rf = group.getFilesRoot().resolve("/cloudVs");
//                        if (!rf.exists())
//                            rf.mkdir();
//                        Message message = ExternalResource.uploadAsFile(ExternalResource.create(finalFile), group, "/cloudVs/" + dataStr + ".mp4");
//                        group.sendMessage(message);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                });
            }
        });
    }

    public static final SimpleDateFormat format = new SimpleDateFormat("MM_dd_HH");
}
