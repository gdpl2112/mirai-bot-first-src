package Project.Controllers.AboutTalkControllers;

import Entitys.Group;
import Entitys.User;
import Entitys.gameEntitys.Warp;
import Entitys.gameEntitys.task.Task;
import Project.DataBases.GameDataBase;
import Project.DataBases.GameTaskDatabase;
import Project.Services.DetailServices.TaskDetailService;
import io.github.kloping.Mirai.Main.BotStarter;
import io.github.kloping.MySpringTool.annotations.Action;
import io.github.kloping.MySpringTool.annotations.AllMess;
import io.github.kloping.MySpringTool.annotations.Before;
import io.github.kloping.MySpringTool.annotations.Controller;
import io.github.kloping.MySpringTool.exceptions.NoRunException;

import java.util.UUID;

import static Project.Controllers.ControllerTool.CanGroup;
import static io.github.kloping.Mirai.Main.Resource.Switch.AllK;
import static io.github.kloping.Mirai.Main.Resource.println;

@Controller
public class TestController {
    public TestController() {
        println(this.getClass().getSimpleName() + "构建");
    }


    @Before
    public void before(Group group, long qq, @AllMess String s) throws NoRunException {
        if (!AllK)
            throw new NoRunException();
        if (!CanGroup(group.getId())) {
            throw new NoRunException();
        }
        if (!BotStarter.test)
            throw new NoRunException();
    }
//
//    @Action("任务测试0")
//    public String m1(User user, Group group) {
//        Warp warp = GameDataBase.getWarp(user.getId());
//        if (warp.getMaster().longValue() == -1) return "没有师傅 淦 ";
//        Task task = new Task();
//        task.setTaskId(0);
//        task.setDeadline(System.currentTimeMillis() + 1000 * 60 * 60 * 24);
//        task.setType(Task.Type.normal);
//        task.setHost(user.getId());
//        task.getTasker().add(warp.getMaster().longValue());
//        task.setUuid(UUID.randomUUID() + "-task.json");
//        task.setFromG(group.getId());
//        task.setState(0);
//        GameTaskDatabase.saveTask(task, true);
//        return TaskDetailService.getIntro(task.getTaskId());
//    }
//
//    @Action("/stopAll")
//    public String exit() {
//        System.exit(0);
//        return "";
//    }
}
