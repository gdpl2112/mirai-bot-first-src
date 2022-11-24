package Project.services.detailServices.tasks;

import Project.broadcast.game.GhostLostBroadcast;
import Project.services.detailServices.tasks.reciver.ReceiverWithTask1001;
import io.github.kloping.mirai0.Main.ITools.MessageTools;
import io.github.kloping.mirai0.commons.broadcast.Receiver;
import io.github.kloping.mirai0.commons.task.Task;
import io.github.kloping.mirai0.commons.task.TaskPoint;
import io.github.kloping.mirai0.unitls.Tools.Tool;

import static Project.dataBases.GameDataBase.getNameById;
import static Project.dataBases.GameTaskDatabase.CD1;

/**
 * 周任务2
 *
 * @author github-kloping
 */
public class Task1001 extends Task {
    public static Integer[] ids = {501, 502, 503, 504, 505, 506, 507, 508, 509, 510, 511, 512,
            513, 514, 515, 516, 517, 518, 519, 520, 601, 602, 603};
    public int needId;

    public Task1001() {
        this.needId = Tool.tool.getRandT(ids);
    }

    public int getNeedId() {
        return needId;
    }

    @Override
    public void over() {
        TaskPoint.getInstance(getHost().longValue())
                .setNextCan(System.currentTimeMillis() + (CD1))
                .addNormalIndex(-1).apply();

        MessageTools.instance.sendMessageInGroupWithAt("任务过期,未完成", getFromG().longValue(), getHost());
        destroy();
    }

    @Override
    public String getIntro() {
        return "每周任务:击杀一只:" + getNameById(getNeedId());
    }

    @Override
    public String getFinish() {
        return "每周任务:\n以指定方式击杀魂兽 完成\n奖励随机物品";
    }

    @Override
    public Receiver registrationReceiver() {
        Receiver receiver = null;
        GhostLostBroadcast.INSTANCE.add(receiver
                = new ReceiverWithTask1001(this));
        return receiver;
    }
}
