
package Project.services.detailServices.tasks.reciver;

import io.github.kloping.mirai0.commons.GhostObj;
import io.github.kloping.mirai0.commons.task.Task;
import io.github.kloping.mirai0.commons.broadcast.enums.ObjType;
import Project.broadcast.game.GhostLostBroadcast;
import Project.services.detailServices.TaskDetailService;
import io.github.kloping.mirai0.Main.ITools.MessageTools;

import static Project.dataBases.GameDataBase.addToBgs;
import static Project.dataBases.GameTaskDatabase.deleteTask;

/**
 * 师徒任务
 *
 * @author github-kloping
 */
public class GhostLostReceiverWithTask0
        extends GhostLostBroadcast.AbstractGhostLostReceiverWith<Task> {
    public GhostLostReceiverWithTask0(Task task) {
        super(task);
    }

    @Override
    public void onReceive(long who, Long with, GhostObj ghostObj, GhostLostBroadcast.KillType killType) {
        Task task = getT();
        if (task.getHost().longValue() != who) {
            return;
        }
        if (who == task.getHost().longValue()) {
            if (ghostObj.getLevel() >= 10 * 10000) {
                if (task.getTasker().contains(with.longValue())) {
                    deleteTask(task);
                    MessageTools.sendMessageInGroupWithAt(TaskDetailService.getFinish(task)
                            , task.getFromG().longValue(), task.getHost());
                    addToBgs(who, 1601, ObjType.got);
                    addToBgs(with.longValue(), 1601, ObjType.got);
                    GhostLostBroadcast.INSTANCE.AfterRunnable.add(() -> task.destroy());
                }
            }
        }
    }
}