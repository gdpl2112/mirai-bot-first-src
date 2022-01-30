package Project.Controllers.NormalController;

import Project.interfaces.JuiLi;
import io.github.kloping.MySpringTool.annotations.*;
import io.github.kloping.MySpringTool.exceptions.NoRunException;
import io.github.kloping.mirai0.Entitys.Group;
import io.github.kloping.mirai0.Main.ITools.MessageTools;

import static Project.Controllers.ControllerTool.opened;
import static io.github.kloping.mirai0.Main.Resource.println;

/**
 * @author github-kloping
 */
@Controller
public class EntertainmentController3 {
    public EntertainmentController3() {
        println(this.getClass().getSimpleName() + "构建");
    }

    @Before
    public void before(Group group) throws NoRunException {
        if (!opened(group.getId(), this.getClass())) {
            throw new NoRunException("未开启");
        }
    }

    @AutoStand
    JuiLi jiuLi;

    @Action("/爬.+")
    public Object o1(@AllMess String m, Group group, long q1) {
        long q = MessageTools.getAtFromString(m);
        String urlStr = null;
        if (q == -1) {
            return "目前只支@的形式";
        }
        byte[] bytes = jiuLi.paImg(q, null);
        MessageTools.sendImageByBytesOnGroupWithAt(bytes, group.getId(), q1);
        return null;
    }

    @Action("/举牌子.+")
    public Object o2(@AllMess String m, Group group, long q1) {
        String msg = m.replace("/举牌子", "");
        if (msg == null || msg.trim().isEmpty()) {
            msg = "请指定内容哦~";
        }
        byte[] bytes = jiuLi.jupaizi(msg);
        MessageTools.sendImageByBytesOnGroupWithAt(bytes, group.getId(), q1);
        return null;
    }
}
