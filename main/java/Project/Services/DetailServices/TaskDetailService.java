package Project.Services.DetailServices;

import Entitys.gameEntitys.task.Task;

import java.lang.reflect.Method;

public class TaskDetailService {
    public static String getIntro(Integer id) {
        try {
            Method m = TaskDetailService.class.getDeclaredMethod("s" + id);
            m.setAccessible(true);
            return m.invoke(null).toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String s0() {
        return "师傅支援徒弟击杀一直十万年或以上的魂兽";
    }

    private static String f0(Task task) {
        StringBuilder sb = new StringBuilder();
        sb.append("师傅支援徒弟击杀一直十万年或以上的魂兽").append("\n");
        sb.append("任务完成,奖励师徒白升级券各一张");
        return sb.toString();
    }

    private static String s1000() {
        return "进入列表中所有活动,并击败每个活动中的一只魂兽";
    }

    private static String f1000(Task task) {
        return "每周任务:\n进入列表中所有活动,并击败每个活动中的一只魂兽\n完成\n奖励随机物品";
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
}
