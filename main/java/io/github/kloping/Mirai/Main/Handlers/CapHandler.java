package io.github.kloping.Mirai.Main.Handlers;

import Project.Controllers.ControllerSource;
import Project.Controllers.ControllerTool;
import Project.DataBases.DataBase;
import io.github.kloping.Mirai.Main.ITools.MessageTools;
import io.github.kloping.MySpringTool.annotations.AutoStand;
import io.github.kloping.MySpringTool.annotations.Entity;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.message.data.Face;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.MessageChainBuilder;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static io.github.kloping.Mirai.Main.ITools.MessageTools.getAt;

/**
 * @author github-kloping
 */
@Entity
public class CapHandler {
    public static final Map<Long, String> CAPING = new ConcurrentHashMap<>();
    public static final Map<Long, Group> CAP_2 = new ConcurrentHashMap<>();
    public static final Map<Long, Integer> CAP_T = new ConcurrentHashMap<>();
    private static final Number MAX_WAIT = 300;
    @AutoStand
    static ControllerSource controllerSource;

    public static void join(long qid, Group group) {
        if (DataBase.needCap(group.getId())) {
            Object[] o = controllerSource.createCapImage();
            String path = o[0].toString();
            String capCode = o[1].toString();
            Image image = MessageTools.createImage(group, path);
            MessageChainBuilder builder = new MessageChainBuilder();
            builder.append(getAt(qid)).append("\n请在").append(MAX_WAIT.toString()).append("秒内完成验证(\n否则将被视为人机踢出群聊\n如果看不清 请说 看不清/换一个 \n ");
            builder.append(image);
            group.sendMessage(builder.build());
            CAPING.put(qid, capCode);
            CAP_2.put(qid, group);
            if (!CAP_T.containsKey(qid))
                startTimer(qid);
            CAP_T.put(qid, MAX_WAIT.intValue());
        }
    }

    private static final ExecutorService THREADS = Executors.newFixedThreadPool(10);

    private static void startTimer(long qid) {
        THREADS.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    if (!CAPING.containsKey(qid)) return;
                    Thread.sleep(1000);
                    int t = CAP_T.get(qid);
                    t--;
                    CAP_T.put(qid, t);
                    if (t <= 0) {
                        err(qid);
                    } else {
                        run();
                    }
                } catch (NullPointerException nullPointerException) {

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private static void err(long qid) {
        CAPING.remove(qid);
        Group group = CAP_2.get(qid);
        CAP_2.remove(qid);
        CAP_T.remove(qid);
        MessageChainBuilder builder = new MessageChainBuilder();
        builder.append(getAt(qid)).append("\n您没有通过验证");
        builder.append("\n请重新申请加入群聊");
        builder.append(new Face(Face.SAO_RAO));
        group.sendMessage(builder.build());
        group.get(qid).kick("验证失败");
    }

    public static void cap(long qid, String text) {
        String t1 = CAPING.get(qid);
        switch (text.trim()) {
            case "看不清":
            case "换一个":
                join(qid, CAP_2.get(qid));
                break;
            default:
                if (text.trim().toLowerCase().equals(t1.trim().toLowerCase())) {
                    ok(qid);
                } else {
                    CAP_2.get(qid).sendMessage("好像不对哦~");
                }
        }
    }

    public static void ok(long qid) {
        CAPING.remove(qid);
        Group group = CAP_2.get(qid);
        CAP_2.remove(qid);
        CAP_T.remove(qid);
        MessageChainBuilder builder = new MessageChainBuilder();
        builder.append(getAt(qid)).append("\n恭喜你通过了验证");
        builder.append("\n群内成员要好好的与新人相处哦");
        builder.append("\n说\"菜单\"即可查看我的功能了");
        builder.append(new Face(Face.HAN_XIAO));
        group.sendMessage(builder.build());
    }
}
