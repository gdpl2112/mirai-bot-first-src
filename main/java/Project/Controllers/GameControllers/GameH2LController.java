package Project.Controllers.GameControllers;

import Entitys.Group;
import Entitys.User;
import Entitys.gameEntitys.PersonInfo;
import io.github.kloping.Mirai.Main.ITools.MessageTools;
import io.github.kloping.MySpringTool.annotations.Action;
import io.github.kloping.MySpringTool.annotations.AllMess;
import io.github.kloping.MySpringTool.annotations.Before;
import io.github.kloping.MySpringTool.annotations.Controller;
import io.github.kloping.MySpringTool.exceptions.NoRunException;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static Project.Controllers.ControllerTool.CanGroup;
import static Project.DataBases.GameDataBase.getInfo;
import static io.github.kloping.Mirai.Main.Resource.Switch.AllK;
import static io.github.kloping.Mirai.Main.Resource.println;

@Controller
public class GameH2LController {
    public GameH2LController() {
        println(this.getClass().getSimpleName() + "构建");
    }

    public static void check(Object[] objects) throws NoRunException {
        try {
            long q = Long.parseLong(objects[0].toString());
            Group group = (Group) objects[4];
            PersonInfo info = getInfo(q);
            if (info.getTemp()) {
                if (!inCanDonging(objects[1].toString())) {
                    MessageTools.sendMessageInGroupWithAt("您正在斗魂,请不要做其他不相干的事情", group.getId(), q);
                    throw new NoRunException("out Around");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static boolean inCanDonging(String str) {
        if (str.equals("信息")) return true;
        if (str.startsWith("结束")) return true;
        return false;
    }

    public static final List<Long> douingList = new CopyOnWriteArrayList<>();

    @Before
    public void before(User qq, Group group, @AllMess String mess) throws NoRunException {
        if (!AllK) throw new NoRunException("总开关——关闭");
        if (!CanGroup(group.getId())) throw new NoRunException("未开启");
        if (getInfo(qq.getId()).getHp() <= 0) {
        }
    }

    @Action("单人斗魂.+")
    public String m1() {
        return "等待实现";
    }

    @Action("双人斗魂.+")
    public String m2() {
        return "等待实现";
    }
}
