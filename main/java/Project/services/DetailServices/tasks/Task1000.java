package Project.services.DetailServices.tasks;

import Entitys.gameEntitys.task.Task;
import Entitys.gameEntitys.task.TaskPoint;
import io.github.kloping.Mirai.Main.ITools.MessageTools;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static Project.DataBases.GameTaskDatabase.cd_0;

/**
 * 周任务0
 * @author github-kloping
 */
public class Task1000 extends Task {
    public Map<Integer, Boolean> m1 = new ConcurrentHashMap<>();

    public boolean isFinish() {
        if (m1.containsKey(1))
            if (m1.get(1))
                if (m1.containsKey(2))
                    if (m1.get(2))
//                            if (m1.containsKey(3))
//                                if (m1.get(3))
                        return true;
        return false;
    }

    public Map<Integer, Boolean> getM1() {
        return m1;
    }

    public void setM1(Map<Integer, Boolean> m1) {
        this.m1 = m1;
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
