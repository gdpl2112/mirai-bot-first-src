package Project.services.DetailServices.tasks.reciver;

import Entitys.gameEntitys.GhostObj;
import Project.broadcast.enums.ObjType;
import Project.broadcast.game.GhostLostBroadcast;
import Project.services.DetailServices.TaskDetailService;
import Project.services.DetailServices.tasks.Task1000;
import io.github.kloping.Mirai.Main.ITools.MessageTools;

import static Project.DataBases.GameDataBase.addToBgs;
import static Project.DataBases.GameDataBase.getImgById;
import static Project.DataBases.GameTaskDatabase.deleteTask;
import static Project.DataBases.task.TaskCreator.getRandObj1000;

/**
 * 周任务1
 *
 * @author github-kloping
 */
public class GhostLostReceiverWithTask1000 extends GhostLostBroadcast.AbstractGhostLostReceiverWith<Task1000> {
    public GhostLostReceiverWithTask1000(Task1000 task1000) {
        super(task1000);
    }

    @Override
    public void onReceive(long who, Long with, GhostObj ghostObj, GhostLostBroadcast.KillType killType) {
        Task1000 task = getT();
        if (task.getHost().longValue() != who) {
            return;
        }
        if (ghostObj.getId() < 600) {
            task.m1.put(1, true);
        } else if (ghostObj.getId() < 700) {
            task.m1.put(2, true);
        } else if (ghostObj.getId() < 800) {
            task.m1.put(3, true);
        }

        task.update();

        if (task.isFinish()) {
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
