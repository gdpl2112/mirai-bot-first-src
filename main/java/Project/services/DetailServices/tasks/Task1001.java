package Project.services.DetailServices.tasks;

import io.github.kloping.mirai0.Entitys.gameEntitys.task.Task;
import io.github.kloping.mirai0.Entitys.gameEntitys.task.TaskPoint;
import io.github.kloping.mirai0.Main.ITools.MessageTools;

import static Project.DataBases.GameTaskDatabase.cd_0;
import static io.github.kloping.mirai0.unitls.Tools.Tool.getRandT;

/**
 * 周任务2
 *
 * @author github-kloping
 */
public class Task1001 extends Task {
    public int needId;
    public static Integer[] ids = {501, 502, 503, 504, 505, 506, 507, 508, 509, 510, 511, 512,
            513, 514, 515, 516, 517, 518, 519, 520, 601, 602, 603};

    public int getNeedId() {
        return needId;
    }

    public Task1001() {
        this.needId = getRandT(ids);
    }

    @Override
    public void over() {
        TaskPoint.getInstance(getHost().longValue())
                .setNextCan(System.currentTimeMillis() + (cd_0))
                .addNormalIndex(-1).apply();

        MessageTools.sendMessageInGroupWithAt("任务过期,未完成", getFromG().longValue(), getHost());
        destroy();
    }
}
