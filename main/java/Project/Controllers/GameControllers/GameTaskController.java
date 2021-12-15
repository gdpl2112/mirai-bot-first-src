package Project.Controllers.GameControllers;

import Entitys.Group;
import Entitys.User;
import Entitys.gameEntitys.task.Task;
import Entitys.gameEntitys.task.TaskPoint;
import Project.DataBases.GameTaskDatabase;
import Project.Services.DetailServices.TaskDetailService;
import Project.Services.Iservice.IGameTaskService;
import io.github.kloping.Mirai.Main.ITools.MessageTools;
import io.github.kloping.MySpringTool.annotations.*;
import io.github.kloping.MySpringTool.exceptions.NoRunException;

import static Project.Controllers.ControllerTool.CanGroup;
import static Project.Controllers.TimerController.morningRunnable;
import static Project.DataBases.GameDataBase.getInfo;
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
        morningRunnable.add(() -> {
//            TaskPoint.getInstance();
        });
    }

    @AutoStand
    IGameTaskService gameTaskService;

    @Action("接徒弟任务")
    public Object m1(long q, Group group) {
        return gameTaskService.m1(q, group);
//        return "等待完善...";
    }

    @Action("当前任务")
    public Object m2(long q) {
        StringBuilder sb = new StringBuilder();
        int i = 1;
        try {
            for (Task task : GameTaskDatabase.tasks.get(q)) {
                sb.append(i).append(".").append(TaskDetailService.getIntro(task.getTaskId()));
                sb.append("\r\n\t  主:").append(task.getHost()).append("\r\n");
            }
            return sb.toString();
        } catch (Exception e) {
            return "没有任务!";
        }
    }
}
