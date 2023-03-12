package Project.services.detailServices.tasks;

import Project.broadcast.game.challenge.TrialChallengeEndBroadcast;
import Project.services.detailServices.tasks.reciver.ReceiverWithTask1;
import io.github.kloping.mirai0.commons.broadcast.Receiver;
import io.github.kloping.mirai0.commons.task.Task;

/**
 * @author github.kloping
 */
public class Task1 extends Task {
    @Override
    public String getIntro() {
        return "徒弟与师傅完成试炼挑战且徒弟战胜师傅";
    }

    @Override
    public String getFinish() {
        StringBuilder sb = new StringBuilder();
        sb.append("徒弟与师傅完成试炼挑战且徒弟战胜师傅").append("\n");
        sb.append("任务完成,奖励师徒白升级券各一张");
        return sb.toString();
    }

    @Override
    public Receiver registrationReceiver() {
        Receiver receiver = null;
        receiver = new ReceiverWithTask1(this);
        TrialChallengeEndBroadcast.INSTANCE.add(receiver);
        return receiver;
    }
}
