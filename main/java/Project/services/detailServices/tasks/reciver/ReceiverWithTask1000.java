package Project.services.detailServices.tasks.reciver;

import Project.broadcast.game.GhostLostBroadcast;
import Project.dataBases.SourceDataBase;
import Project.services.detailServices.tasks.Task1000;
import io.github.kloping.mirai0.Main.iutils.MessageUtils;
import io.github.kloping.mirai0.commons.GInfo;
import io.github.kloping.mirai0.commons.GhostObj;
import Project.commons.broadcast.enums.ObjType;

import java.util.Set;

import static Project.dataBases.GameDataBase.addToBgs;
import static Project.dataBases.GameTaskDatabase.deleteTask;
import static Project.dataBases.task.TaskCreator.getRandObj1000;

/**
 * @author github-kloping
 * @version 1.0
 */
public class ReceiverWithTask1000
        extends GhostLostBroadcast.AbstractGhostLostReceiverWith<Task1000> {
    public ReceiverWithTask1000(Task1000 task1000) {
        super(task1000);
    }

    @Override
    public void onReceive(long who, Set<Long> withs, GhostObj ghostObj, GhostLostBroadcast.KillType killType) {
        Task1000 task = getT();
        if (who != task.getHost().longValue()) {
            return;
        } else {
            if (killType == task.getNeedType()) {
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
}
