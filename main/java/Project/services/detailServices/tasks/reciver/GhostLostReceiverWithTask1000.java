package Project.services.detailServices.tasks.reciver;

import Project.broadcast.game.GhostLostBroadcast;
import Project.dataBases.SourceDataBase;
import Project.services.detailServices.TaskDetailService;
import Project.services.detailServices.tasks.Task1000;
import io.github.kloping.mirai0.Main.ITools.MessageTools;
import io.github.kloping.mirai0.commons.GhostObj;
import io.github.kloping.mirai0.commons.broadcast.enums.ObjType;

import static Project.dataBases.GameDataBase.addToBgs;
import static Project.dataBases.GameTaskDatabase.deleteTask;
import static Project.dataBases.task.TaskCreator.getRandObj1000;

/**
 * @author github-kloping
 * @version 1.0
 */
public class GhostLostReceiverWithTask1000
        extends GhostLostBroadcast.AbstractGhostLostReceiverWith<Task1000> {
    public GhostLostReceiverWithTask1000(Task1000 task1000) {
        super(task1000);
    }

    @Override
    public void onReceive(long who, Long with, GhostObj ghostObj, GhostLostBroadcast.KillType killType) {
        Task1000 task = getT();
        if (who != task.getHost().longValue()) {
            return;
        } else {
            if (killType == task.getNeedType()) {
                deleteTask(task);
                int id = getRandObj1000();
                MessageTools.sendMessageInGroupWithAt(TaskDetailService.getFinish(task) + SourceDataBase.getImgPathById(id)
                        , task.getFromG().longValue(), task.getHost());
                addToBgs(who, id, ObjType.got);
                GhostLostBroadcast.INSTANCE.AfterRunnable.add(() -> {
                    task.destroy();
                });
            }
        }
    }
}
