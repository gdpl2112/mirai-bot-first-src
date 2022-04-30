package Project.services.detailServices.tasks.reciver;

import Project.broadcast.game.GhostLostBroadcast;
import Project.dataBases.SourceDataBase;
import Project.services.detailServices.TaskDetailService;
import Project.services.detailServices.tasks.Task1002;
import io.github.kloping.mirai0.Main.ITools.MessageTools;
import io.github.kloping.mirai0.commons.GhostObj;
import io.github.kloping.mirai0.commons.broadcast.enums.ObjType;

import static Project.dataBases.GameDataBase.addToBgs;
import static Project.dataBases.GameTaskDatabase.deleteTask;
import static Project.dataBases.task.TaskCreator.getRandObj1000;

/**
 * 周任务1
 *
 * @author github-kloping
 */
public class GhostLostReceiverWithTask1000 extends GhostLostBroadcast.AbstractGhostLostReceiverWith<Task1002> {
    public GhostLostReceiverWithTask1000(Task1002 task1000) {
        super(task1000);
    }

    @Override
    public void onReceive(long who, Long with, GhostObj ghostObj, GhostLostBroadcast.KillType killType) {
        Task1002 task = getT();
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
            MessageTools.sendMessageInGroupWithAt(TaskDetailService.getFinish(task) + SourceDataBase.getImgPathById(id)
                    , task.getFromG().longValue(), task.getHost());
            addToBgs(who, id, ObjType.got);
            GhostLostBroadcast.INSTANCE.AfterRunnable.add(() -> {
                task.destroy();
            });
        }
    }
}
