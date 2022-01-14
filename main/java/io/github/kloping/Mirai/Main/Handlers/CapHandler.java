package io.github.kloping.Mirai.Main.Handlers;

import Project.Controllers.FirstController;
import Project.DataBases.DataBase;
import io.github.kloping.Mirai.Main.ITools.MessageTools;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.Face;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.MessageChainBuilder;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author github-kloping
 */
public class CapHandler {
    public static final Map<Long, String> caping = new ConcurrentHashMap<>();
    public static final Map<Long, Group> cap2 = new ConcurrentHashMap<>();
    public static final Map<Long, Integer> capT = new ConcurrentHashMap<>();

    public static void join(long qid, Group group) {
        if (DataBase.needCap(group.getId())) {
            Object[] o = FirstController.createCapImage();
            String path = o[0].toString();
            String capCode = o[1].toString();
            Image image = MessageTools.createImageInGroup(group, path);
            MessageChainBuilder builder = new MessageChainBuilder();
            builder.append(new At(qid)).append("\n请在180秒内完成验证(\n否则将被视为人机踢出群聊\n如果看不清 请说 看不清/换一个 \n ");
            builder.append(image);
            group.sendMessage(builder.build());
            caping.put(qid, capCode);
            cap2.put(qid, group);
            if (!capT.containsKey(qid))
                startTimer(qid);
            capT.put(qid, 180);
        }
    }

    private static final ExecutorService threads = Executors.newFixedThreadPool(10);

    private static void startTimer(long qid) {
        threads.execute(new Runnable() {

            @Override
            public void run() {
                try {
                    if (!caping.containsKey(qid)) return;
                    Thread.sleep(1000);
                    int t = capT.get(qid);
                    t--;
                    capT.put(qid, t);
                    if (t <= 0) {
                        err(qid);
                    } else {
                        run();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private static void err(long qid) {
        caping.remove(qid);
        Group group = cap2.get(qid);
        cap2.remove(qid);
        capT.remove(qid);
        MessageChainBuilder builder = new MessageChainBuilder();
        builder.append(new At(qid)).append("\n您没有通过验证");
        builder.append("\n请重新申请加入群聊");
        builder.append(new Face(Face.SAO_RAO));
        group.sendMessage(builder.build());
        group.get(qid).kick("验证失败");
    }

    public static void cap(long qid, String text) {
        String t1 = caping.get(qid);
        switch (text.trim()) {
            case "看不清":
            case "换一个":
                join(qid, cap2.get(qid));
                break;
            default:
                if (text.trim().toLowerCase().equals(t1.trim().toLowerCase())) {
                    ok(qid);
                } else {
                    cap2.get(qid).sendMessage("好像不对哦~");
                }
        }
    }

    public static void ok(long qid) {
        caping.remove(qid);
        Group group = cap2.get(qid);
        cap2.remove(qid);
        capT.remove(qid);
        MessageChainBuilder builder = new MessageChainBuilder();
        builder.append(new At(qid)).append("\n恭喜你通过了验证");
        builder.append("\n群内成员要好好的与新人相处哦");
        builder.append("\n说\"菜单\"即可查看我的功能了");
        builder.append(new Face(Face.HAN_XIAO));
        group.sendMessage(builder.build());
    }
}
