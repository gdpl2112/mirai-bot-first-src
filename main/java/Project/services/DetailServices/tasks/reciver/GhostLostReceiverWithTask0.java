
package Project.services.DetailServices.tasks.reciver;

import Entitys.gameEntitys.GhostObj;
import Entitys.gameEntitys.task.Task;
import Project.broadcast.enums.ObjType;
import Project.broadcast.game.GhostLostBroadcast;
import Project.services.DetailServices.TaskDetailService;
import io.github.kloping.Mirai.Main.ITools.MessageTools;

import static Project.DataBases.GameDataBase.addToBgs;
import static Project.DataBases.GameTaskDatabase.deleteTask;

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