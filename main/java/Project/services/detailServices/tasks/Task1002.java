package Project.services.detailServices.tasks;

import Project.broadcast.game.GhostLostBroadcast;
import Project.commons.broadcast.Receiver;
import Project.services.detailServices.tasks.reciver.ReceiverWithTask1002;
import io.github.kloping.mirai0.Main.iutils.MessageUtils;
import io.github.kloping.mirai0.commons.task.Task;
import io.github.kloping.mirai0.commons.task.TaskPoint;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static Project.dataBases.GameTaskDatabase.CD1;


/**
 * 周任务0
 *
 * @author github-kloping
 */
public class Task1002 extends Task {
    public Map<Integer, Boolean> m1 = new ConcurrentHashMap<>();

    public boolean isFinish() {
        if (m1.containsKey(1))
            if (m1.get(1))
                if (m1.containsKey(2))
                    if (m1.get(2))
                        if (m1.containsKey(3))
                            if (m1.get(3))
                                return true;
        return false;
    }

    public Map<Integer, Boolean> getM1() {
        return m1;
    }

    public void setM1(Map<Integer, Boolean> m1) {
        this.m1 = m1;
    }

    @Override
    public void over() {
        TaskPoint.getInstance(getHost().longValue())
                .setNextCan(System.currentTimeMillis() + (CD1))
                .addNormalIndex(-1).apply();

        MessageUtils.INSTANCE.sendMessageInGroupWithAt("任务过期,未完成", getFromG().longValue(), getHost());
        destroy();
    }

    @Override
    public String getIntro() {
        return "每周任务:进入副本列表中所有活动,并击败每个活动中的一只魂兽";
    }

    @Override
    public String getFinish() {
        return "每周任务:\n进入副本列表中所有活动,并击败每个活动中的一只魂兽 完成\n奖励随机物品";
    }

    @Override
    public Receiver registrationReceiver() {
        Receiver receiver = null;
        GhostLostBroadcast.INSTANCE.add(receiver
                = new ReceiverWithTask1002(this));
        return receiver;
    }
}
