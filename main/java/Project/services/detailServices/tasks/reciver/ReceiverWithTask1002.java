package Project.services.detailServices.tasks.reciver;

import Project.broadcast.game.GhostLostBroadcast;
import Project.dataBases.SourceDataBase;
import Project.services.detailServices.tasks.Task1002;
import io.github.kloping.mirai0.Main.iutils.MessageUtils;
import io.github.kloping.mirai0.commons.GInfo;
import io.github.kloping.mirai0.commons.GhostObj;
import io.github.kloping.mirai0.commons.broadcast.enums.ObjType;
import io.github.kloping.mirai0.unitls.Tools.Tool;

import java.util.Set;

import static Project.dataBases.GameDataBase.addToBgs;
import static Project.dataBases.GameTaskDatabase.deleteTask;

/**
 * 周任务1
 *
 * @author github-kloping
 */
public class ReceiverWithTask1002 extends GhostLostBroadcast.AbstractGhostLostReceiverWith<Task1002> {
    public static final Integer[] T_1002_OBJS = new Integer[]{
            115,
            116,
            117,
            118,
            1512,
            1601,
            1521,
    };

    public ReceiverWithTask1002(Task1002 task1002) {
        super(task1002);
    }

    @Override
    public void onReceive(long who, Set<Long> withs, GhostObj ghostObj, GhostLostBroadcast.KillType killType) {
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
            int id = getRandObj1002();
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

    private int getRandObj1002() {
        return Tool.INSTANCE.getRandT(T_1002_OBJS);
    }
}
