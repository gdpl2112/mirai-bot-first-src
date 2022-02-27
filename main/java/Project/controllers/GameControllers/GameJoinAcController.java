package Project.controllers.GameControllers;


import Project.aSpring.SpringBootResource;
import Project.dataBases.GameDataBase;
import Project.interfaces.Iservice.IGameJoinAcService;
import io.github.kloping.MySpringTool.annotations.*;
import io.github.kloping.MySpringTool.exceptions.NoRunException;
import io.github.kloping.mirai0.Entitys.Group;
import io.github.kloping.mirai0.Entitys.User;
import io.github.kloping.mirai0.Main.ITools.MemberTools;
import io.github.kloping.mirai0.Main.ITools.MessageTools;
import io.github.kloping.mirai0.unitls.Tools.Tool;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static Project.ResourceSet.FinalString.LIST_STR;
import static Project.controllers.NormalController.ScoreController.longs;
import static Project.controllers.auto.ControllerTool.opened;
import static io.github.kloping.mirai0.Main.ITools.MessageTools.getAtFromString;
import static io.github.kloping.mirai0.Main.Resource.println;
import static io.github.kloping.mirai0.unitls.drawers.Drawer.getImageFromStrings;

/**
 * @author github-kloping
 */
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
        if (!opened(group.getId(), this.getClass())) {
            throw new NoRunException("未开启");
        }
        if (GameDataBase.getInfo(qq.getId()).getHp() <= 0) {
            if (Tool.EveListStartWith(listFx, message) == -1) {
                MessageTools.sendMessageInGroupWithAt("无状态", group.getId(), qq.getId());
                throw new NoRunException();
            }
        }
    }

    @Action(LIST_STR)
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
        if (whos == -1) {
            return "支援谁？";
        } else {
            if (!GameDataBase.exist(whos)) {
                return "该玩家尚未注册";
            }
            return gameJoinAcService.helpTo(qq.getId(), whos);
        }
    }

    @Action("探查")
    public Object com5(Group group, User qq) {
        return gameJoinAcService.getIntro(qq.getId());
    }

    @Action("魂兽击杀排行")
    public String com6(Group group) {
        return com7("5", group);
    }

    @Action("魂兽击杀排行<.+=>n>")
    public String com7(@Param("n") String n, Group group) {
        int mc = 5;
        if (n != null) {
            mc = Integer.valueOf(n);
        }
        StringBuilder sb = new StringBuilder();
        List<Map<String, Number>> list = SpringBootResource.getKillGhostMapper().select(mc);
        int na = 0;
        for (Map<String, Number> map : list) {
            ++na;
            Long qid = map.get("id").longValue();
            Integer num = map.get("num").intValue();
            sb.append("第").append(na).append(": ").append(MemberTools.getNameFromGroup(qid, group))
                    .append("=>").append("击杀").append(num).append("只\n");
        }
        return sb.toString().isEmpty() ? "暂无记录" : sb.toString().trim();
    }
}
