package Project.controllers.gameControllers;


import Project.aSpring.SpringBootResource;
import Project.commons.SpGroup;
import Project.commons.SpUser;
import Project.dataBases.GameDataBase;
import Project.interfaces.Iservice.IGameJoinAcService;
import io.github.kloping.MySpringTool.annotations.*;
import io.github.kloping.MySpringTool.exceptions.NoRunException;
import io.github.kloping.mirai0.Main.iutils.MemberUtils;
import io.github.kloping.mirai0.Main.iutils.MessageUtils;
import Project.utils.Tools.GameTool;
import Project.utils.Tools.Tool;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static Project.commons.rt.ResourceSet.FinalNormalString.BG_TIPS;
import static Project.commons.rt.ResourceSet.FinalString.*;
import static Project.commons.rt.ResourceSet.FinalValue.NOT_OPEN_NO_RUN_EXCEPTION;
import static Project.controllers.auto.ControllerTool.opened;
import static Project.controllers.normalController.ScoreController.longs;
import static Project.dataBases.GameDataBase.ID_2_NAME_MAPS;
import static Project.dataBases.GameDataBase.getInfo;
import static io.github.kloping.mirai0.Main.BootstarpResource.println;
import static Project.utils.drawers.Drawer.getImageFromStrings;

/**
 * @author github-kloping
 */
@Controller
public class GameJoinAcController {
    private static List<String> listFx = new ArrayList<>();

    static {
        listFx.add("探查");
        listFx.add("魂兽击杀排行");
    }

    @AutoStand
    IGameJoinAcService gameJoinAcService;

    public GameJoinAcController() {
        println(this.getClass().getSimpleName() + "构建");
    }

    @Before
    public void before(SpUser qq, SpGroup group, @AllMess String message) throws NoRunException {
        if (!opened(group.getId(), this.getClass())) {
            throw NOT_OPEN_NO_RUN_EXCEPTION;
        }
        if (GameDataBase.getInfo(qq.getId()).getHp() <= 0) {
            if (Tool.INSTANCE.EveListStartWith(listFx, message) == -1) {
                MessageUtils.INSTANCE.sendMessageInGroupWithAt("无状态", group.getId(), qq.getId());
                throw new NoRunException();
            }
        }
        if (getInfo(qq.getId()).isBg()) {
            MessageUtils.INSTANCE.sendMessageInGroupWithAt(BG_TIPS, group.getId(), qq.getId());
            throw new NoRunException(BG_TIPS);
        }
    }

    @Action(GHOST_LIST)
    public String com0(SpGroup group) {
        return getImageFromStrings(gameJoinAcService.list());
    }

    @Action("进入<.{1,}=>name>")
    public String com1(SpGroup group, @Param("name") String name, SpUser qq) {
        return gameJoinAcService.join(qq.getId(), name, group);
    }

    @Action("选择<.{1,}=>name>")
    public Object com2(SpGroup group, @Param("name") String name, SpUser qq) {
        return gameJoinAcService.startSelect(qq.getId(), name);
    }

    @Action("请求支援")
    public String com3(SpGroup group, SpUser qq) {
        return gameJoinAcService.getHelp(qq.getId());
    }

    @Action("支援<.{1,}=>name>")
    public String com4(SpGroup group, @Param("name") String name, SpUser qq) {
        if (longs.contains(qq.getId())) {
            return "您不能支援";
        }
        long whos = Project.utils.Utils.getAtFromString(name);
        if (whos == -1) {
            return NOT_FOUND_AT;
        } else {
            if (!GameDataBase.exist(whos)) {
                return PLAYER_NOT_REGISTERED;
            }
            return gameJoinAcService.helpTo(qq.getId(), whos);
        }
    }

    @Action("探查")
    public Object com5(SpGroup group, SpUser qq) {
        return gameJoinAcService.getIntro(qq.getId());
    }

    @Action("魂兽击杀排行")
    public String com6(SpGroup group) {
        return com7("5", group);
    }

    @Action("魂兽击杀排行<.+=>n>")
    public String com7(@Param("n") String n, SpGroup group) {
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
            sb.append("第").append(na).append(": ").append(MemberUtils.getNameFromGroup(qid, group))
                    .append("=>").append("击杀").append(num).append("只\n");
        }
        return sb.toString().isEmpty() ? "暂无记录" : sb.toString().trim();
    }

    @Action("魂兽列表")
    public Object listAll() {
        StringBuilder sb = new StringBuilder();
        for (int id = 500; id < 1000; id++) {
            if (GameDataBase.ID_2_NAME_MAPS.containsKey(id)) {
                String v = ID_2_NAME_MAPS.get(id);
                sb.append(v).append("=>").append(GameTool.getLevelByGhostId(id)).append(NEWLINE);
            }
        }
        return sb.toString().trim();
    }
}
