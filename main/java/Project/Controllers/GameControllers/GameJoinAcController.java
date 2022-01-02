package Project.Controllers.GameControllers;


import Entitys.Group;
import Entitys.User;
import Project.DataBases.GameDataBase;
import Project.Services.Iservice.IGameJoinAcService;
import Project.Tools.Tool;
import io.github.kloping.Mirai.Main.ITools.MemberTools;
import io.github.kloping.Mirai.Main.ITools.MessageTools;
import io.github.kloping.MySpringTool.annotations.*;
import io.github.kloping.MySpringTool.exceptions.NoRunException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static Project.Controllers.ControllerTool.CanGroup;
import static Project.Controllers.NormalController.ScoreController.longs;
import static Project.DataBases.GameDataBase.getInfo;
import static Project.DataBases.GameDataBase.killedC;
import static Project.drawers.Drawer.getImageFromStrings;
import static io.github.kloping.Mirai.Main.ITools.MessageTools.getAtFromString;
import static io.github.kloping.Mirai.Main.Resource.Switch.AllK;
import static io.github.kloping.Mirai.Main.Resource.println;

@Controller
public class GameJoinAcController {
    public GameJoinAcController() {
        println(this.getClass().getSimpleName() + "构建");
    }

    private static List<String> listFx = new ArrayList<>();

    @AutoStand
    IGameJoinAcService gameJoinAcService;

    static {
        listFx.add("探查");
        listFx.add("魂兽击杀排行");
    }

    @Before
    public void before(User qq, Group group, @AllMess String message) throws NoRunException {
        if (!AllK)
            throw new NoRunException();
        if (!CanGroup(group.getId())) {
            throw new NoRunException();
        }
        if (getInfo(qq.getId()).getHp() <= 0) {
            if (Tool.EveListStartWith(listFx, message) == -1) {
                MessageTools.sendMessageInGroupWithAt("无状态", group.getId(), qq.getId());
                throw new NoRunException();
            }
        }
    }

    @Action("列表")
    public String com0(Group group) {
        return getImageFromStrings(gameJoinAcService.list());
    }

    @Action("进入<.{1,}=>name>")
    public String com1(Group group, @Param("name") String name, User qq) {
        return gameJoinAcService.join(qq.getId(), name, group);
    }

    @Action("选择<.{1,}=>name>")
    public Object com2(Group group, @Param("name") String name, User qq) {
        return gameJoinAcService.startAtt(qq.getId(), name);
    }

    @Action("请求支援")
    public String com3(Group group, User qq) {
        return gameJoinAcService.getHelp(qq.getId());
    }

    @Action("支援<.{1,}=>name>")
    public String com4(Group group, @Param("name") String name, User qq) {
        if (longs.contains(qq.getId())) {
            return "您不能支援";
        }
        long whos = getAtFromString(name);
        if (whos == -1)
            return "支援谁？";
        if (!GameDataBase.exist(whos)) return "该玩家尚未注册";
        return gameJoinAcService.HelpTo(qq.getId(), whos);
    }

    @Action("探查")
    public Object com5(Group group, User qq) {
        return gameJoinAcService.getIntro(qq.getId());
    }

    @Action("魂兽击杀排行")
    public String com6(Group group) {
        int Mc = 5;
        Map<Number, Integer> map = Tool.sortMapByValue(killedC);
        StringBuilder sb = new StringBuilder();
        int na = 0;
        for (Map.Entry<Number, Integer> entry : map.entrySet()) {
            Number number = entry.getKey();
            if (na++ == Mc) break;
            sb.append("第").append(na).append(": ").append(MemberTools.getNameFromGroup(number.longValue(), group))
                    .append("=>").append("击杀").append(killedC.get(number.longValue())).append("只\n");
        }
        return sb.toString().isEmpty() ? "暂无记录" : sb.toString().trim();
    }

    @Action("魂兽击杀排行<.+=>n>")
    public String com7(@Param("n") String n, Group group) {
        int Mc = 5;
        if (n != null) Mc = Integer.valueOf(n);
        Map<Number, Integer> map = Tool.sortMapByValue(killedC);
        StringBuilder sb = new StringBuilder();
        int na = 0;
        for (Map.Entry<Number, Integer> entry : map.entrySet()) {
            Number number = entry.getKey();
            if (na++ == Mc) break;
            sb.append("第").append(na).append(": ").append(MemberTools.getNameFromGroup(number.longValue(), group))
                    .append("=>").append("击杀").append(killedC.get(number.longValue())).append("只\n");
        }
        return sb.toString().isEmpty() ? "暂无记录" : sb.toString().trim();
    }
}
