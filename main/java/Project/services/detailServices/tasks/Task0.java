package Project.services.detailServices.tasks;

import Project.broadcast.game.GhostLostBroadcast;
import Project.services.detailServices.tasks.reciver.ReceiverWithTask0;
import io.github.kloping.mirai0.commons.broadcast.Receiver;
import io.github.kloping.mirai0.commons.task.Task;

/**
 * @author github.kloping
 */
public class Task0 extends Task {
    @Override
    public String getIntro() {
        return "师傅支援徒弟击杀一直十万年或以上的魂兽";
    }

    @Override
    public String getFinish() {
        StringBuilder sb = new StringBuilder();
        sb.append("徒弟与师傅完成试炼挑战且徒弟战胜师傅").append("\n");
        sb.append("任务完成,奖励师徒白升级券各两张");
        return sb.toString();
    }


    @Override
    public Receiver registrationReceiver() {
        Receiver receiver = new ReceiverWithTask0(this);
        GhostLostBroadcast.INSTANCE.add(receiver);
        return receiver;
    }
}
