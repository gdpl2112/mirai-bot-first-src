package Project.Services.impl;

import Entitys.Group;
import Entitys.gameEntitys.Warp;
import Entitys.gameEntitys.task.Task;
import Entitys.gameEntitys.task.TaskPoint;
import Project.DataBases.GameTaskDatabase;
import Project.Services.DetailServices.TaskDetailService;
import Project.Services.Iservice.IGameTaskService;
import Project.Tools.Tool;
import io.github.kloping.MySpringTool.annotations.Entity;

import java.util.UUID;

import static Project.DataBases.task.TaskCreator.*;

@Entity
public class GameTaskServiceImpl implements IGameTaskService {
    @Override
    public String m1(long q, Group group) {
        Warp warp = Warp.getInstance(q);
        if (warp.getMaster().longValue() == -1) return "您没有师傅";
        if (GameTaskDatabase.tasks.containsKey(q)) return "请先完成\"当前任务\"";
        TaskPoint taskPoint = TaskPoint.getInstance(q);
        if (taskPoint.getNextCan() > System.currentTimeMillis())
            return "接任务冷却中=>" + Tool.getTimeDDHHMM(taskPoint.getNextCan());
        if (taskPoint.getPrenticeIndex() >= maxPrenticeIndex) return "暂无更多任务..";
        int id = taskPoint.getPrenticeIndex();
        Task task = getTask(id);
        task.setTaskId(id);
        task.setDeadline(System.currentTimeMillis() + 1000 * 60 * 60 * 24);
        task.setType(Task.Type.prentice);
        task.setHost(q);
        task.getTasker().add(warp.getMaster().longValue());
        task.setUuid(UUID.randomUUID() + "-task.json");
        task.setFromG(group.getId());
        task.setState(0);
        taskPoint.addPrenticeIndex().apply();
        StringBuilder sb = new StringBuilder();
        task.save();
        sb.append(TaskDetailService.getIntro(task.getTaskId()));
        sb.append("\r\n时限:").append(Tool.getTimeDDHHMM(task.getDeadline()));
        sb.append("\r\n若时间内未完成,将在短时间内无法再接受任务");
        return sb.toString();
    }

    @Override
    public Object m2(long q, Group group) {
        if (GameTaskDatabase.tasks.containsKey(q)
                && !GameTaskDatabase.tasks.get(q).isEmpty()) return "请先完成\"当前任务\"";
        TaskPoint taskPoint = TaskPoint.getInstance(q);
        if (taskPoint.getNextCan() > System.currentTimeMillis())
            return "接任务冷却中=>" + Tool.getTimeDDHHMM(taskPoint.getNextCan());
        if (taskPoint.getNormalIndex() >= maxIndex) return "暂无更多任务..";
        int id = taskPoint.getNormalIndex();
        Task task = getTask(id);
        task.setTaskId(id);
        task.setDeadline((System.currentTimeMillis() + (1000 * 60 * 60 * 24)));
        task.setType(Task.Type.week);
        task.setHost(q);
        task.setUuid(UUID.randomUUID() + "-task.json");
        task.setFromG(group.getId());
        task.setState(0);
        task.save();
        taskPoint.addNormalIndex().apply();
        StringBuilder sb = new StringBuilder();
        sb.append(TaskDetailService.getIntro(task.getTaskId()));
        sb.append("\r\n时限:").append(Tool.getTimeDDHHMM(task.getDeadline()));
        sb.append("\r\n若时间内未完成,将在短时间内无法再接受任务");
        return sb.toString();
    }
}

