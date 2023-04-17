package Project.services.detailServices.tasks.reciver;

import Project.broadcast.game.GhostLostBroadcast;
import io.github.kloping.mirai0.Main.iutils.MessageUtils;
import io.github.kloping.mirai0.commons.GInfo;
import io.github.kloping.mirai0.commons.GhostObj;
import Project.commons.broadcast.enums.ObjType;
import io.github.kloping.mirai0.commons.task.Task;

import java.util.Set;

import static Project.dataBases.GameDataBase.addToBgs;
import static Project.dataBases.GameTaskDatabase.deleteTask;

/**
 * 师徒任务
 *
 * @author github-kloping
 */
public class ReceiverWithTask0
        extends GhostLostBroadcast.AbstractGhostLostReceiverWith<Task> {
    public ReceiverWithTask0(Task task) {
        super(task);
    }

    @Override
    public void onReceive(long who, Set<Long> withs, GhostObj ghostObj, GhostLostBroadcast.KillType killType) {
        Task task = getT();
        if (task.getHost().longValue() != who) {
            return;
        }
        if (who == task.getHost().longValue()) {
            if (ghostObj.getLevel() >= 10 * 10000) {
                for (Long with : withs) {
                    if (task.getTasker().contains(with.longValue())) {
                        deleteTask(task);
                        MessageUtils.INSTANCE.sendMessageInGroupWithAt(task.getFinish()
                                , task.getFromG().longValue(), task.getHost());
                        addToBgs(who, 1601, ObjType.got);
                        addToBgs(with.longValue(), 1601, ObjType.got);
                        GInfo.getInstance(who).addFtc().apply();
                        GhostLostBroadcast.INSTANCE.afterRunnable.add(() -> task.destroy());
                    }
                }
            }
        }
    }
}