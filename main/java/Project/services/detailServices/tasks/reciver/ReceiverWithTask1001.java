package Project.services.detailServices.tasks.reciver;

import Project.broadcast.game.GhostLostBroadcast;
import Project.commons.broadcast.enums.ObjType;
import Project.dataBases.SourceDataBase;
import Project.services.detailServices.tasks.Task1001;
import io.github.kloping.mirai0.Main.iutils.MessageUtils;
import Project.aSpring.dao.GInfo;
import io.github.kloping.mirai0.commons.GhostObj;

import java.util.Set;

import static Project.dataBases.GameDataBase.addToBgs;
import static Project.dataBases.GameTaskDatabase.deleteTask;
import static Project.dataBases.task.TaskCreator.getRandObj1000;

/**
 * 周任务2
 *
 * @author github-kloping
 */
public class ReceiverWithTask1001
        extends GhostLostBroadcast.AbstractGhostLostReceiverWith<Task1001> {
    public ReceiverWithTask1001(Task1001 task) {
        super(task);
    }

    @Override
    public void onReceive(long who, Set<Long> withs, GhostObj ghostObj, GhostLostBroadcast.KillType killType) {
        Task1001 task = getT();
        if (task.getHost().longValue() != who) {
            return;
        }
        if (ghostObj.getId().intValue() == task.needId) {
            deleteTask(task);
            int id = getRandObj1000();
            MessageUtils.INSTANCE.sendMessageInGroupWithAt(
                    task.getFinish() + SourceDataBase.getImgPathById(id)
                    , task.getFromG().longValue(), task.getHost());
            addToBgs(who, id, ObjType.got);
            GInfo.getInstance(who).addFtc().apply();
            GhostLostBroadcast.INSTANCE.afterRunnable.add(() -> {
                task.destroy();
            });
        }
    }
}
