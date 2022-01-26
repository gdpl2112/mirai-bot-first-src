package Project.services.DetailServices.tasks.reciver;

import Entitys.gameEntitys.GhostObj;
import Project.broadcast.enums.ObjType;
import Project.broadcast.game.GhostLostBroadcast;
import Project.services.DetailServices.TaskDetailService;
import Project.services.DetailServices.tasks.Task1001;
import Project.services.DetailServices.tasks.Task1002;
import io.github.kloping.Mirai.Main.ITools.MessageTools;

import static Project.DataBases.GameDataBase.addToBgs;
import static Project.DataBases.GameDataBase.getImgById;
import static Project.DataBases.GameTaskDatabase.deleteTask;
import static Project.DataBases.task.TaskCreator.getRandObj1000;

/**
 * @author github-kloping
 * @version 1.0
 */
public class GhostLostReceiverWithTask1002
        extends GhostLostBroadcast.AbstractGhostLostReceiverWith<Task1002> {
    public GhostLostReceiverWithTask1002(Task1002 task1002) {
        super(task1002);
    }

    @Override
    public void onReceive(long who, Long with, GhostObj ghostObj, GhostLostBroadcast.KillType killType) {
        Task1002 task = getT();
        if (who != task.getHost().longValue()) {
            return;
        } else {
            if (killType == task.getNeedType()) {
                deleteTask(task);
                int id = getRandObj1000();
                MessageTools.sendMessageInGroupWithAt(TaskDetailService.getFinish(task) + getImgById(id)
                        , task.getFromG().longValue(), task.getHost());
                addToBgs(who, id, ObjType.got);
                GhostLostBroadcast.INSTANCE.AfterRunnable.add(() -> {
                    task.destroy();
                });
            }
        }
    }
}
