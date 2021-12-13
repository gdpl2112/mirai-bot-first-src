package Project.Controllers.GameControllers;

import Entitys.Group;
import Entitys.User;
import Entitys.gameEntitys.Warp;
import Entitys.gameEntitys.task.Task;
import Entitys.gameEntitys.task.TaskPoint;
import Project.DataBases.GameTaskDatabase;
import Project.Services.DetailServices.TaskDetailService;
import Project.Tools.Tool;
import io.github.kloping.Mirai.Main.ITools.MessageTools;
import io.github.kloping.MySpringTool.annotations.Action;
import io.github.kloping.MySpringTool.annotations.AllMess;
import io.github.kloping.MySpringTool.annotations.Before;
import io.github.kloping.MySpringTool.annotations.Controller;
import io.github.kloping.MySpringTool.exceptions.NoRunException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static Project.Controllers.ControllerTool.CanGroup;
import static Project.DataBases.GameDataBase.getInfo;
import static io.github.kloping.Mirai.Main.Resource.Switch.AllK;
import static io.github.kloping.Mirai.Main.Resource.println;

@Controller
public class GameTaskController {
    public GameTaskController() {
        println(this.getClass().getSimpleName() + "构建");
    }

    private static List<String> listFx = new ArrayList<>();

    static {
    }

    @Before
    public void before(User qq, Group group, @AllMess String mess) throws NoRunException {
        if (!AllK) {
            throw new NoRunException("总开关——关闭");
        }
        if (!CanGroup(group.getId())) {
            throw new NoRunException("未开启");
        }
        if (getInfo(qq.getId()).getHp() <= 0) {
            MessageTools.sendMessageInGroupWithAt("无状态", group.getId(), qq.getId());
            throw new NoRunException("无状态");
        }
    }
//
//    @Action("接徒弟任务")
//    public Object m1(long q, Group group) {
//        Warp warp = Warp.getInstance(q);
//        if (warp.getMaster().longValue() == -1) return "您没有师傅";
//        if (GameTaskDatabase.tasks.containsKey(q)) return "请先完成\"当前任务\"";
//        TaskPoint taskPoint = TaskPoint.getInstance(q);
//        if (taskPoint.getNextCan() > System.currentTimeMillis())
//            return "接任务冷却中=>" + Tool.getTimeDDHHMM(taskPoint.getNextCan());
//        if (taskPoint.getPrenticeIndex() >= GameTaskDatabase.maxPrenticeIndex) return "暂无更多任务..";
//        Task task = new Task();
//        task.setTaskId(taskPoint.getPrenticeIndex());
//        task.setDeadline(System.currentTimeMillis() + 1000 * 60 * 60 * 24);
//        task.setType(Task.Type.normal);
//        task.setHost(q);
//        task.getTasker().add(warp.getMaster().longValue());
//        task.setUuid(UUID.randomUUID() + "-task.json");
//        task.setFromG(group.getId());
//        task.setState(0);
//        taskPoint.addPrenticeIndex().apply();
//        StringBuilder sb = new StringBuilder();
//        GameTaskDatabase.saveTask(task, true);
//        sb.append(TaskDetailService.getIntro(task.getTaskId()));
//        sb.append("\r\n时限:").append(Tool.getTimeDDHHMM(task.getDeadline()));
//        sb.append("\r\n若时间内未完成,将在短时间内无法再接受任务");
//        return sb.toString();
//    }
//
//    @Action("当前任务")
//    public Object m2(long q) {
//        StringBuilder sb = new StringBuilder();
//        int i = 1;
//        try {
//            for (Task task : GameTaskDatabase.tasks.get(q)) {
//                sb.append(i).append(".").append(TaskDetailService.getIntro(task.getTaskId()));
//                sb.append("\r\n\t  主:").append(task.getHost()).append("\r\n");
//            }
//            return sb.toString();
//        } catch (Exception e) {
//            return "没有任务!";
//        }
//    }
}
