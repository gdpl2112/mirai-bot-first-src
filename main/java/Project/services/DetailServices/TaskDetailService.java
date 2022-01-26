package Project.services.DetailServices;

import Entitys.gameEntitys.task.Task;
import Project.services.DetailServices.tasks.Task1001;
import Project.services.DetailServices.tasks.Task1002;

import java.lang.reflect.Method;

import static Project.DataBases.GameDataBase.getNameById;

/**
 * @author github-kloping
 */
public class TaskDetailService {

    private static String s0(Task task) {
        return "师傅支援徒弟击杀一直十万年或以上的魂兽";
    }

    private static String f0(Task task) {
        StringBuilder sb = new StringBuilder();
        sb.append("师傅支援徒弟击杀一直十万年或以上的魂兽").append("\n");
        sb.append("任务完成,奖励师徒白升级券各一张");
        return sb.toString();
    }

    private static String s1000(Task task) {
        return "每周任务:进入列表中所有活动,并击败每个活动中的一只魂兽";
    }

    private static String f1000(Task task) {
        return "每周任务:\n进入列表中所有活动,并击败每个活动中的一只魂兽 完成\n奖励随机物品";
    }

    private static String s1001(Task task) {
        Task1001 task1001 = (Task1001) task;
        return "每周任务:击杀一只:" + getNameById(task1001.getNeedId());
    }

    private static String f1001(Task task) {
        return "每周任务:\n击杀指定一只魂兽 完成\n奖励随机物品";
    }

    private static String s1002(Task task) {
        Task1002 task1002 = (Task1002) task;
        return "每周任务:以" + task1002.getNeedType().getName() + "方式击杀一只魂兽";
    }

    private static String f1002(Task task) {
        return "每周任务:\n以指定方式击杀魂兽 完成\n奖励随机物品";
    }

    public static String getFinish(Task task) {
        try {
            Method m = TaskDetailService.class.getDeclaredMethod("f" + task.getTaskId(), Task.class);
            m.setAccessible(true);
            return m.invoke(null, task).toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getIntro(Task task) {
        try {
            Method m = TaskDetailService.class.getDeclaredMethod("s" + task.getTaskId(), Task.class);
            m.setAccessible(true);
            return m.invoke(null, task).toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
