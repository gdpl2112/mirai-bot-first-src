package Project.controllers.gameControllers;

import Project.aSpring.SpringBootResource;
import Project.dataBases.GameDataBase;
import Project.dataBases.GameTaskDatabase;
import Project.interfaces.Iservice.IGameTaskService;
import Project.services.detailServices.TaskDetailService;
import io.github.kloping.MySpringTool.annotations.*;
import io.github.kloping.MySpringTool.exceptions.NoRunException;
import io.github.kloping.mirai0.Main.ITools.MessageTools;
import io.github.kloping.mirai0.commons.Group;
import io.github.kloping.mirai0.commons.User;
import io.github.kloping.mirai0.commons.task.Task;
import io.github.kloping.mirai0.unitls.Tools.Tool;

import java.util.Date;

import static Project.controllers.auto.ControllerTool.opened;
import static Project.controllers.auto.TimerController.MORNING_RUNNABLE;
import static Project.dataBases.GameDataBase.getInfo;
import static io.github.kloping.mirai0.Main.Resource.println;
import static io.github.kloping.mirai0.commons.resouce_and_tool.ResourceSet.FinalNormalString.BG_TIPS;
import static io.github.kloping.mirai0.commons.resouce_and_tool.ResourceSet.FinalValue.NOT_OPEN_NO_RUN_EXCEPTION;
import static io.github.kloping.mirai0.unitls.Tools.Tool.WEEK_DAYS;

/**
 * @author github-kloping
 */
@Controller
public class GameTaskController {
    static {
        MORNING_RUNNABLE.add(() -> {
            if (Tool.getWeekOfDate(new Date()).equals(WEEK_DAYS[WEEK_DAYS.length - 1])) {
                SpringBootResource.getTaskPointMapper().updateAll();
                SpringBootResource.getScoreMapper().updateEarnings();
            }
        });
    }

    @AutoStand
    IGameTaskService gameTaskService;

    public GameTaskController() {
        println(this.getClass().getSimpleName() + "构建");
    }

    @Before
    public void before(User qq, Group group, @AllMess String mess) throws NoRunException {
        if (!opened(group.getId(), this.getClass())) {
            throw NOT_OPEN_NO_RUN_EXCEPTION;
        }
        if (GameDataBase.getInfo(qq.getId()).getHp() <= 0) {
            MessageTools.sendMessageInGroupWithAt("无状态", group.getId(), qq.getId());
            throw new NoRunException("无状态");
        }
        if (getInfo(qq.getId()).isBg()) {
            MessageTools.sendMessageInGroupWithAt(BG_TIPS, group.getId(), qq.getId());
            throw new NoRunException(BG_TIPS);
        }
    }

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
            for (Task task : GameTaskDatabase.TASKS.get(q)) {
                sb.append(i++).append(".").append(TaskDetailService.getIntro(task));
                sb.append("\r\n\t  主:").append(task.getHost()).append("\r\n");
            }
            return sb.toString().isEmpty() ? "暂无任务!" : sb.toString();
        } catch (Exception e) {
            return "没有任务!";
        }
    }
}
