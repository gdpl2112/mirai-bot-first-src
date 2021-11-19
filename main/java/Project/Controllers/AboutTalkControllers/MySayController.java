package Project.Controllers.AboutTalkControllers;

import Entitys.Group;
import Entitys.User;
import Project.DataBases.DataBase;
import Project.Tools.Tool;
import io.github.kloping.Mirai.Main.Resource;
import io.github.kloping.MySpringTool.annotations.Action;
import io.github.kloping.MySpringTool.annotations.Before;
import io.github.kloping.MySpringTool.annotations.Controller;
import io.github.kloping.MySpringTool.annotations.Param;
import io.github.kloping.MySpringTool.exceptions.NoRunException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static Project.Tools.Tool.getRandString;
import static io.github.kloping.Mirai.Main.Resource.Switch.AllK;
import static io.github.kloping.Mirai.Main.Resource.println;
import static io.github.kloping.Mirai.Main.Resource.superQ;

@Controller
public class MySayController {
    public MySayController() {
        println(this.getClass().getSimpleName() + "构建");
    }

    @Before
    public void before(Group group, User qq) throws NoRunException {
        if (!AllK)
            throw new NoRunException();
        if (qq.getId() == Long.parseLong(superQ)) {
            println("超级权限执行...");
            return;
        }
        if (!DataBase.isFather(qq.getId())) {
            throw new NoRunException("无权限");
        }
    }

    private static final Map<Long, Long> upAks = new ConcurrentHashMap<>();

    public static final String[] isAdministratorTips = new String[]{"应该是", "是的", "是吧", "是", "你猜", "yeah", "is", "1"};
    public static final String[] istAdministratorTips = new String[]{"你觉得呢", "不是", "?你猜", "问我呢?", "no", "222", "你确定?", "-1"};

    @Action(value = "ta是管理<.+=>par>", otherName = {"他是管理<.+=>par>", "她是管理<.+=>par>"})
    public Object isIAdministrator(@Param("par") String par, User qq) {
        Number q2 = null;
        try {
            q2 = Long.valueOf(Tool.findNumberFromString(par));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        if (q2 == null || q2.longValue() == -1) return "谁?";
        upAks.put(qq.getId(), q2.longValue());
        if (DataBase.isFather(q2.longValue())) {
            return isAdministratorTips[Tool.rand.nextInt(isAdministratorTips.length)];
        } else {
            return istAdministratorTips[Tool.rand.nextInt(istAdministratorTips.length)];
        }
    }

    public static final String[] addFatherTips = new String[]{"好的", "听你的", "OK", "嗯嗯", "真好"};
    public static final String[] nddFatherTips = new String[]{"不要", "为啥", "凭什么", "阿巴巴", "就不"};

    @Action("现在是了")
    public Object isAdministratorNow(User qq) {
        Long q2 = upAks.get(qq.getId());
        if (q2 == null || q2 == 0) return "是什么?";
        if (DataBase.isFather(q2)) return "ta本来就是好吧...";
        if (qq.getId() == Resource.superQL) {
            DataBase.addFather(q2);
            return getRandString(addFatherTips);
        } else {
            return getRandString(nddFatherTips);
        }
    }

    public static final String[] eddFatherTips = new String[]{"好的", "收到", "OK", "完成", "弄好了"};

    @Action("现在不是了")
    public Object istAdministratorNow(User qq) {
        Long q2 = upAks.get(qq.getId());
        if (q2 == null || q2 == 0) return "不是什么?";
        if (!DataBase.isFather(q2)) return "ta本来就不是好吧...";
        if (qq.getId() == Resource.superQL) {
            DataBase.removeFather(q2);
            return getRandString(eddFatherTips);
        } else {
            return getRandString(nddFatherTips);
        }
    }


}