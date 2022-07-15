package Project.services.detailServices.tasks.reciver;

import Project.broadcast.game.GhostLostBroadcast;
import Project.broadcast.game.challenge.TrialChallengeEndBroadcast;
import Project.services.detailServices.TaskDetailService;
import io.github.kloping.mirai0.Main.ITools.MessageTools;
import io.github.kloping.mirai0.commons.broadcast.enums.ObjType;
import io.github.kloping.mirai0.commons.task.Task;

import static Project.dataBases.GameDataBase.addToBgs;
import static Project.dataBases.GameTaskDatabase.deleteTask;

/**
 * 师徒任务
 *
 * @author github-kloping
 */
public class GhostLostReceiverWithTask1
        extends TrialChallengeEndBroadcast.AbstractTrialChallengeReceiverWith<Task> {
    public GhostLostReceiverWithTask1(Task task) {
        super(task);
    }

    @Override
    public void onReceive(long q1, long q2, int w) {
        Task task = getT();
        if (task.getHost().longValue() != q2) {
            return;
        }
        if (q2 == task.getHost().longValue()) {
            if (task.getTasker().contains(q1)) {
                deleteTask(task);
                MessageTools.instance.sendMessageInGroupWithAt(TaskDetailService.getFinish(task)
                        , task.getFromG().longValue(), task.getHost());
                addToBgs(q1, 1601, ObjType.got);
                addToBgs(q2, 1601, ObjType.got);
                GhostLostBroadcast.INSTANCE.AfterRunnable.add(() -> task.destroy());
            }
        }
    }
}