package Project.controllers.gameControllers;

import Project.aSpring.SpringBootResource;
import Project.dataBases.GameDataBase;
import Project.dataBases.GameTaskDatabase;
import Project.interfaces.Iservice.IGameTaskService;
import io.github.kloping.MySpringTool.annotations.*;
import io.github.kloping.MySpringTool.exceptions.NoRunException;
import io.github.kloping.mirai0.Main.iutils.MessageUtils;
import io.github.kloping.mirai0.commons.SpGroup;
import io.github.kloping.mirai0.commons.SpUser;
import io.github.kloping.mirai0.commons.task.Task;
import io.github.kloping.mirai0.commons.task.TaskPoint;
import io.github.kloping.mirai0.unitls.Tools.Tool;

import java.util.Date;

import static Project.controllers.auto.ControllerTool.opened;
import static Project.controllers.auto.TimerController.MORNING_RUNNABLE;
import static Project.dataBases.GameDataBase.getInfo;
import static Project.dataBases.GameTaskDatabase.CD1;
import static io.github.kloping.mirai0.Main.BootstarpResource.println;
import static io.github.kloping.mirai0.commons.resouce_and_tool.ResourceSet.FinalNormalString.BG_TIPS;
import static io.github.kloping.mirai0.commons.resouce_and_tool.ResourceSet.FinalString.OK_TIPS;
import static io.github.kloping.mirai0.commons.resouce_and_tool.ResourceSet.FinalString.TASK_OVER_TIME;
import static io.github.kloping.mirai0.commons.resouce_and_tool.ResourceSet.FinalValue.NOT_OPEN_NO_RUN_EXCEPTION;

/**
 * @author github-kloping
 */
@Controller
public class GameTaskController {
    static {
        MORNING_RUNNABLE.add(() -> {
            //周六早刷新CD
            if (Tool.INSTANCE.getWeekOfDate(new Date()).equals(Tool.INSTANCE.WEEK_DAYS[Tool.INSTANCE.WEEK_DAYS.length - 1])) {
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
    public void before(SpUser qq, SpGroup group, @AllMess String mess) throws NoRunException {
        if (!opened(group.getId(), this.getClass())) {
            throw NOT_OPEN_NO_RUN_EXCEPTION;
        }
        if (GameDataBase.getInfo(qq.getId()).getHp() <= 0) {
            MessageUtils.INSTANCE.sendMessageInGroupWithAt("无状态", group.getId(), qq.getId());
            throw new NoRunException("无状态");
        }
        if (getInfo(qq.getId()).isBg()) {
            MessageUtils.INSTANCE.sendMessageInGroupWithAt(BG_TIPS, group.getId(), qq.getId());
            throw new NoRunException(BG_TIPS);
        }
    }

    @Action("接徒弟任务")
    public Object m1(long q, SpGroup group) {
        return gameTaskService.m1(q, group);
    }

    @Action(value = "接每周任务", otherName = {"接周任务"})
    public Object m2(long q, SpGroup group) {
        Object o = gameTaskService.m2(q, group);
        return o;
    }

    @Action(value = "当前任务", otherName = {"我的任务"})
    public Object m2(long q) {
        StringBuilder sb = new StringBuilder();
        int i = 1;
        try {
            for (Task task : GameTaskDatabase.TASKS.get(q)) {
                sb.append(i++).append(".").append(task.getIntro());
                sb.append("\r\n\t  主:").append(task.getHost()).append("\r\n");
            }
            return sb.toString().isEmpty() ? "暂无任务!" : sb.toString();
        } catch (Exception e) {
            return "没有任务!";
        }
    }

    @Action(value = "放弃任务")
    public Object m3(long q) {
        if (!GameTaskDatabase.TASKS.containsKey(q) || GameTaskDatabase.TASKS.get(q).isEmpty()) return "当前无任务";
        Task task = GameTaskDatabase.TASKS.get(q).get(0);
        for (Long aLong : task.getTasker()) {
            TaskPoint.getInstance(aLong.longValue()).setNextCan(System.currentTimeMillis() + (CD1 * 2)).apply();
        }

        TaskPoint.getInstance(task.getHost().longValue()).setNextCan(System.currentTimeMillis() + (CD1)).addPrenticeIndex(-1).apply();

        MessageUtils.INSTANCE.sendMessageInGroupWithAt(TASK_OVER_TIME, task.getFromG().longValue(), task.getHost());
        task.destroy();
        return OK_TIPS;
    }
}
