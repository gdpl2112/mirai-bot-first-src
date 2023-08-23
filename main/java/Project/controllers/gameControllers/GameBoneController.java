package Project.controllers.gameControllers;


import Project.commons.SpGroup;
import Project.commons.SpUser;
import Project.aSpring.dao.SoulBone;
import Project.interfaces.Iservice.IGameBoneService;
import Project.interfaces.Iservice.ISkillService;
import Project.utils.GameUtils;
import Project.utils.bao.SelectResult;
import io.github.kloping.MySpringTool.annotations.*;
import io.github.kloping.MySpringTool.exceptions.NoRunException;
import io.github.kloping.mirai0.Main.iutils.MessageUtils;
import Project.utils.Tools.Tool;
import Project.utils.drawers.Drawer;

import java.util.ArrayList;
import java.util.List;

import static Project.commons.rt.ResourceSet.FinalNormalString.BG_TIPS;
import static Project.commons.rt.ResourceSet.FinalString.CHALLENGE_ING;
import static Project.commons.rt.ResourceSet.FinalValue.NOT_OPEN_NO_RUN_EXCEPTION;
import static Project.controllers.auto.ControllerSource.challengeDetailService;
import static Project.controllers.auto.ControllerTool.opened;
import static Project.dataBases.GameDataBase.NAME_2_ID_MAPS;
import static Project.dataBases.GameDataBase.getInfo;
import static io.github.kloping.mirai0.Main.BootstarpResource.println;
import static Project.utils.drawers.Drawer.getImageFromStrings;

/**
 * @author github-kloping
 */
@Controller
public class GameBoneController {
    private static List<String> listFx = new ArrayList<>();

    static {
        listFx.add("我的属性");
        listFx.add("我的魂骨");
    }

    @AutoStand
    IGameBoneService gameBoneService;
    @AutoStand
    ISkillService skillService;

    public GameBoneController() {
        println(this.getClass().getSimpleName() + "构建");
    }

    @Before
    public void before(SpUser qq, SpGroup group, @AllMess String str) throws NoRunException {
        if (!opened(group.getId(), this.getClass())) {
            throw NOT_OPEN_NO_RUN_EXCEPTION;
        }
        if (getInfo(qq.getId()).getHp() <= 0) {
            if (Tool.INSTANCE.EveListStartWith(listFx, str) == -1) {
                throw new NoRunException();
            }
        }
        if (getInfo(qq.getId()).isBg()) {
            MessageUtils.INSTANCE.sendMessageInGroupWithAt(BG_TIPS, group.getId(), qq.getId());
            throw new NoRunException(BG_TIPS);
        }
    }

    @Action("魂骨菜单")
    public String boneMenu(long qq, SpGroup g) {
        return getImageFromStrings(
                "吸收魂骨 (魂骨名)",
                "我的属性 #查看属性",
                "我的魂骨 #展示魂骨",
                "卸掉魂骨 (卸掉之后无状态",
                "=====>经验清零)",
                "更多功能开发中..."
        );
    }

    @Action(value = "我的属性", otherName = "属性信息")
    public String myAttribute(long qq, SpGroup g) {
        return gameBoneService.getInfoAttributes(qq);
    }

    @Action("我的魂骨")
    public String myBones(long qq, SpGroup g) {
        List<SoulBone> list = gameBoneService.getSoulBones(qq);
        return list.isEmpty() ? "没有魂骨!" : Tool.INSTANCE.pathToImg(Drawer.drawBoneMap(list));
    }

    @Action("吸收魂骨<.{1,}=>name>")
    public String parseBone(@Param("name") String name, long qq, SpGroup g) {
        int id = 0;
        try {
            id = NAME_2_ID_MAPS.get(name);
            if (!(id > 1500 && id < 1600))
                return new StringBuilder().append("系统没有找到==》").append(name).toString();
        } catch (Exception e) {
            return new StringBuilder().append("错误的==》").append(name).toString();
        }
        String str = gameBoneService.parseBone(id, qq);
        return str;
    }

    @Action("卸掉魂骨<.{1,}=>name>")
    public String unParseBone(@Param("name") String name, long qq, SpGroup g) {
        if (challengeDetailService.isTemping(qq)) return CHALLENGE_ING;
        int id = 0;
        try {
            id = NAME_2_ID_MAPS.get(name);
            if (!(id > 1500 && id < 1600))
                return new StringBuilder().append("系统没有找到==》").append(name).toString();
        } catch (Exception e) {
            return new StringBuilder().append("错误的==》").append(name).toString();
        }
        String str = gameBoneService.unInstallBone(id, qq);
        return str;
    }

    @Action("头部魂骨技能<.{1,}=>str>")
    private Object t0(@Param("str") String str, long qq, SpGroup g) {
        String idp = "151";
        for (SoulBone soulBone : gameBoneService.getSoulBones(qq)) {
            if (soulBone.getOid().toString().startsWith(idp)) {
                if (soulBone.hasSkill()) {
                    SelectResult result = GameUtils.getAllSelect(qq, str);
                    return String.valueOf(skillService.useSkill(qq, -1, result.getAts(), result.getStr(), g));
                }
            }
        }
        return "您的头部魂骨尚不附带技能!";
    }
}
