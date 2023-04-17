package Project.services.detailServices.tasks.reciver;

import Project.broadcast.game.GhostLostBroadcast;
import Project.broadcast.game.challenge.TrialChallengeEndBroadcast;
import io.github.kloping.mirai0.Main.iutils.MessageUtils;
import io.github.kloping.mirai0.commons.GInfo;
import Project.commons.broadcast.enums.ObjType;
import io.github.kloping.mirai0.commons.task.Task;

import static Project.dataBases.GameDataBase.addToBgs;
import static Project.dataBases.GameTaskDatabase.deleteTask;

/**
 * 师徒任务
 *
 * @author github-kloping
 */
public class ReceiverWithTask1
        extends TrialChallengeEndBroadcast.AbstractTrialChallengeReceiverWith<Task> {
    public ReceiverWithTask1(Task task) {
        super(task);
    }

    /**
     *
     * @param q1 玩家1 胜者
     * @param q2 玩家2
     * @param w  0.玩家1胜,1.玩家2胜
     */
    @Override
    public void onReceive(long q1, long q2, int w) {
        Task task = getT();
        if (q1 == task.getHost().longValue()) {
            if (task.getTasker().contains(q2)) {
                deleteTask(task);
                MessageUtils.INSTANCE.sendMessageInGroupWithAt(task.getFinish()
                        , task.getFromG().longValue(), task.getHost());
                addToBgs(q1, 1601, ObjType.got);
                addToBgs(q2, 1601, ObjType.got);
                GInfo.getInstance(q1).addFtc().apply();
                GhostLostBroadcast.INSTANCE.afterRunnable.add(() -> task.destroy());
            }
        }
    }
}