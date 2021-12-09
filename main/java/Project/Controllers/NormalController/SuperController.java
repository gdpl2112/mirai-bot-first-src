package Project.Controllers.NormalController;

import Entitys.Group;
import Entitys.User;
import Project.DataBases.DataBase;
import Project.Tools.Tool;
import Project.drawers.GameDrawer;
import Project.drawers.entity.GameMap;
import io.github.kloping.Mirai.Main.ITools.MemberTools;
import io.github.kloping.Mirai.Main.ITools.MessageTools;
import io.github.kloping.MySpringTool.annotations.*;
import io.github.kloping.MySpringTool.exceptions.NoRunException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static Project.ASpring.SpringBootResource.move0;
import static Project.ASpring.SpringBootResource.move1;
import static io.github.kloping.Mirai.Main.Resource.println;
import static io.github.kloping.Mirai.Main.Resource.superQL;

@Controller
public class SuperController {
    public SuperController() {
        println(this.getClass().getSimpleName() + "构建");
    }

    @Before
    public void before(@AllMess String mess, Group group, User qq) throws NoRunException {
        if (qq.getId() != superQL)
            throw new NoRunException("can`t do this");
    }

    @Action("/move0")
    public void m0() {
        try {
            move0();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Action("/move1")
    public void m1() {
        try {
            move1();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Action("addScore.{1,}")
    public String addScore(@AllMess String messages, User qq, Group gr) throws NoRunException {
        if (qq.getId() == superQL) {
            long who = MessageTools.getAtFromString(messages);
            messages = messages.replace(Long.toString(who), "");
            if (who == -1) return ("Are You True??");
            long num = Long.parseLong(Tool.findNumberFromString(messages));
            DataBase.addScore(num, who);
            return new StringBuilder().append("给 =》 ").append(MemberTools.getNameFromGroup(who, gr)).append("增加了\r\n=>").append(num + "").append("积分").toString();
        } else throw new NoRunException();
    }

    @Action("全体加积分.{1,}")
    public String addAllScore(@AllMess String messages, User qq) throws NoRunException {
        if (qq.getId() == superQL) {
            long num = Long.parseLong(Tool.findNumberFromString(messages));
            DataBase.AddAllScore(num);
            return new StringBuilder().append("加积分=>异步执行中... On 积分").toString();
        } else throw new NoRunException();
    }
}
