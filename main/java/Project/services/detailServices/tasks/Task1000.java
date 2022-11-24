package Project.services.detailServices.tasks;

import Project.broadcast.game.GhostLostBroadcast;
import Project.services.detailServices.tasks.reciver.ReceiverWithTask1000;
import io.github.kloping.mirai0.commons.broadcast.Receiver;
import io.github.kloping.mirai0.commons.task.Task;
import io.github.kloping.mirai0.unitls.Tools.Tool;


/**
 * @author github-kloping
 * @version 1.0
 */
public class Task1000 extends Task {
    private GhostLostBroadcast.KillType needType;

    public Task1000() {
        this.needType = Tool.tool.getRandT(GhostLostBroadcast.KillType.values());
    }

    public GhostLostBroadcast.KillType getNeedType() {
        return needType;
    }

    public void setNeedType(GhostLostBroadcast.KillType needType) {
        this.needType = needType;
    }

    @Override
    public String getIntro() {
        return "每周任务:以" + getNeedType().getName() + "方式击杀一只魂兽";
    }

    @Override
    public String getFinish() {
        return "每周任务:\n以指定方式击杀魂兽 完成\n奖励随机物品";
    }

    @Override
    public Receiver registrationReceiver() {
        Receiver receiver = null;
        GhostLostBroadcast.INSTANCE.add(receiver = new ReceiverWithTask1000(this));
        return receiver;
    }
}
