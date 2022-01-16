package Project.Controllers.GameControllers;

import Entitys.Group;
import Entitys.User;
import Entitys.gameEntitys.task.Task;
import Entitys.gameEntitys.task.TaskPoint;
import Project.DataBases.GameTaskDatabase;
import Project.Services.DetailServices.TaskDetailService;
import Project.Services.Iservice.IGameTaskService;
import Project.Tools.Tool;
import io.github.kloping.Mirai.Main.ITools.MessageTools;
import io.github.kloping.MySpringTool.annotations.*;
import io.github.kloping.MySpringTool.exceptions.NoRunException;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import static Project.Controllers.ControllerTool.CanGroup;
import static Project.Controllers.TimerController.MORNING_RUNNABLE;
import static Project.DataBases.GameDataBase.getInfo;
import static Project.Tools.Tool.weekDays;
import static io.github.kloping.Mirai.Main.Resource.Switch.AllK;
import static io.github.kloping.Mirai.Main.Resource.println;

@Controller
public class GameTaskController {
    public GameTaskController() {
        println(this.getClass().getSimpleName() + "构建");
    }

    @Before
    public void before(User qq, Group group, @AllMess String mess) throws NoRunException {
        if (!AllK) throw new NoRunException("总开关——关闭");
        if (!CanGroup(group.getId())) throw new NoRunException("未开启");
        if (getInfo(qq.getId()).getHp() <= 0) {
            MessageTools.sendMessageInGroupWithAt("无状态", group.getId(), qq.getId());
            throw new NoRunException("无状态");
        }
    }

    static {
        MORNING_RUNNABLE.add(() -> {
            if (Tool.getWeekOfDate(new Date()).equals(weekDays[weekDays.length - 1])) {
                List<Long> longs = new LinkedList<>();
                for (long activity : GameTaskDatabase.getActivities(true)) {
                    if (longs.contains(activity)) continue;
                    else {
                        TaskPoint.getInstance(activity).setNormalIndex(1000).apply();
                        longs.add(activity);
                    }
                }
            }
        });
    }

    @AutoStand
    IGameTaskService gameTaskService;

    @Action("接徒弟任务")
    public Object m1(long q, Group group) {
        return gameTaskService.m1(q, group);
    }

    @Action(value = "接每周任务", otherName = {"接周任务"})
    public Object m2(long q, Group group) {
        Object o = gameTaskService.m2(q, group);
        return o;
    }

    @Action(value = "当前任务", otherName = {"我的任务"})
    public Object m2(long q) {
        StringBuilder sb = new StringBuilder();
        int i = 1;
        try {
            for (Task task : GameTaskDatabase.tasks.get(q)) {
                sb.append(i).append(".").append(TaskDetailService.getIntro(task));
                sb.append("\r\n\t  主:").append(task.getHost()).append("\r\n");
            }
            return sb.toString().isEmpty() ? "暂无任务!" : sb.toString();
        } catch (Exception e) {
            return "没有任务!";
        }
    }
}
