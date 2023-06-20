package Project.services.impl;

import Project.commons.SpGroup;
import Project.dataBases.GameTaskDatabase;
import Project.interfaces.Iservice.IGameTaskService;
import io.github.kloping.MySpringTool.annotations.Entity;
import io.github.kloping.mirai0.commons.Warp;
import io.github.kloping.mirai0.commons.task.Task;
import io.github.kloping.mirai0.commons.task.TaskPoint;
import Project.utils.Tools.Tool;

import java.util.UUID;

import static Project.dataBases.task.TaskCreator.*;

/**
 * @author github-kloping
 */
@Entity
public class GameTaskServiceImpl implements IGameTaskService {
    @Override
    public String m1(long q, SpGroup group) {
        Warp warp = Warp.getInstance(q);
        if (warp.getMaster().longValue() == -1) return "您没有师傅";
        if (GameTaskDatabase.TASKS.containsKey(q)
                && !GameTaskDatabase.TASKS.get(q).isEmpty()) return "请先完成\"当前任务\"";
        TaskPoint taskPoint = TaskPoint.getInstance(q);
        if (taskPoint.getNextCan() > System.currentTimeMillis())
            return "接任务冷却中=>" + Tool.INSTANCE.getTimeDDHHMM(taskPoint.getNextCan());
        if (taskPoint.getPrenticeIndex() >= MAX_PRENTICE_INDEX) return "暂无更多任务..";
        int id = taskPoint.getPrenticeIndex();
        Task task = getTask(id);
        task.setTaskId(id);
        task.setDeadline(System.currentTimeMillis() + 1000 * 60 * 60 * 24);
        task.setType(Task.Type.PRENTICE);
        task.setHost(q);
        task.getTasker().add(warp.getMaster().longValue());
        task.setUuid(UUID.randomUUID() + "-task.json");
        task.setFromG(group.getId());
        task.setState(0);
        taskPoint.addPrenticeIndex().apply();
        StringBuilder sb = new StringBuilder();
        task.save();
        sb.append(task.getIntro());
        sb.append("\r\n时限:").append(Tool.INSTANCE.getTimeDDHHMM(task.getDeadline()));
        sb.append("\r\n若时间内未完成,将在短时间内无法再接受任务");
        return sb.toString();
    }

    @Override
    public Object m2(long q, SpGroup group) {
        if (GameTaskDatabase.TASKS.containsKey(q)
                && !GameTaskDatabase.TASKS.get(q).isEmpty()) return "请先完成\"当前任务\"";
        TaskPoint taskPoint = TaskPoint.getInstance(q);
        if (taskPoint.getNextCan() > System.currentTimeMillis())
            return "接任务冷却中=>" + Tool.INSTANCE.getTimeDDHHMM(taskPoint.getNextCan());
        if (taskPoint.getNormalIndex() >= MAX_INDEX) return "暂无更多任务..";
        int id = taskPoint.getNormalIndex();
        Task task = getTask(id);
        task.setTaskId(id);
        task.setDeadline((System.currentTimeMillis() + (1000 * 60 * 60 * 24)));
        task.setType(Task.Type.WEEK);
        task.setHost(q);
        task.setUuid(UUID.randomUUID() + "-task.json");
        task.setFromG(group.getId());
        task.setState(0);
        task.save();
        taskPoint.addNormalIndex().apply();
        StringBuilder sb = new StringBuilder();
        sb.append(task.getIntro());
        sb.append("\r\n时限:").append(Tool.INSTANCE.getTimeDDHHMM(task.getDeadline()));
        sb.append("\r\n若时间内未完成,将在短时间内无法再接受任务");
        return sb.toString();
    }
}

