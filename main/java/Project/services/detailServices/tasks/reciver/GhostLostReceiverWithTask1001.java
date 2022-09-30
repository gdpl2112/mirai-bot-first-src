package Project.services.detailServices.tasks.reciver;

import Project.broadcast.game.GhostLostBroadcast;
import Project.dataBases.SourceDataBase;
import Project.services.detailServices.TaskDetailService;
import Project.services.detailServices.tasks.Task1001;
import io.github.kloping.mirai0.Main.ITools.MessageTools;
import io.github.kloping.mirai0.commons.GInfo;
import io.github.kloping.mirai0.commons.GhostObj;
import io.github.kloping.mirai0.commons.broadcast.enums.ObjType;

import java.util.ArrayList;

import static Project.dataBases.GameDataBase.addToBgs;
import static Project.dataBases.GameTaskDatabase.deleteTask;
import static Project.dataBases.task.TaskCreator.getRandObj1000;

/**
 * 周任务2
 *
 * @author github-kloping
 */
public class GhostLostReceiverWithTask1001
        extends GhostLostBroadcast.AbstractGhostLostReceiverWith<Task1001> {
    public GhostLostReceiverWithTask1001(Task1001 task) {
        super(task);
    }

    @Override
    public void onReceive(long who, ArrayList<Long> withs, GhostObj ghostObj, GhostLostBroadcast.KillType killType) {
        Task1001 task = getT();
        if (task.getHost().longValue() != who) {
            return;
        }
        if (ghostObj.getId().intValue() == task.needId) {
            deleteTask(task);
            int id = getRandObj1000();
            MessageTools.instance.sendMessageInGroupWithAt(TaskDetailService.getFinish(task) + SourceDataBase.getImgPathById(id)
                    , task.getFromG().longValue(), task.getHost());
            addToBgs(who, id, ObjType.got);
            GInfo.getInstance(who).addFtc().apply();
            GhostLostBroadcast.INSTANCE.AfterRunnable.add(() -> {
                task.destroy();
            });
        }
    }
}
