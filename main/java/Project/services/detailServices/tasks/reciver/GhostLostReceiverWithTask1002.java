package Project.services.detailServices.tasks.reciver;

import Project.broadcast.game.GhostLostBroadcast;
import Project.dataBases.SourceDataBase;
import Project.services.detailServices.TaskDetailService;
import Project.services.detailServices.tasks.Task1002;
import io.github.kloping.mirai0.Main.ITools.MessageTools;
import io.github.kloping.mirai0.commons.GhostObj;
import io.github.kloping.mirai0.commons.broadcast.enums.ObjType;
import io.github.kloping.mirai0.unitls.Tools.Tool;

import static Project.dataBases.GameDataBase.addToBgs;
import static Project.dataBases.GameTaskDatabase.deleteTask;

/**
 * 周任务1
 *
 * @author github-kloping
 */
public class GhostLostReceiverWithTask1002 extends GhostLostBroadcast.AbstractGhostLostReceiverWith<Task1002> {
    public static final Integer[] T_1002_OBJS = new Integer[]{
            115,
            116,
            117,
            118,
            1512,
            1601,
            1521,
    };

    public GhostLostReceiverWithTask1002(Task1002 task1002) {
        super(task1002);
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
            int id = getRandObj1002();
            MessageTools.sendMessageInGroupWithAt(TaskDetailService.getFinish(task) + SourceDataBase.getImgPathById(id)
                    , task.getFromG().longValue(), task.getHost());
            addToBgs(who, id, ObjType.got);
            GhostLostBroadcast.INSTANCE.AfterRunnable.add(() -> {
                task.destroy();
            });
        }
    }

    private int getRandObj1002() {
        return Tool.tool.getRandT(T_1002_OBJS);
    }
}
