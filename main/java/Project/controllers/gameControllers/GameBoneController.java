package Project.controllers.gameControllers;


import Project.interfaces.Iservice.IGameBoneService;
import Project.interfaces.Iservice.ISkillService;
import io.github.kloping.MySpringTool.annotations.*;
import io.github.kloping.MySpringTool.exceptions.NoRunException;
import io.github.kloping.mirai0.Main.ITools.MessageTools;
import io.github.kloping.mirai0.commons.Group;
import io.github.kloping.mirai0.commons.User;
import io.github.kloping.mirai0.commons.gameEntitys.SoulBone;
import io.github.kloping.mirai0.unitls.Tools.Tool;
import io.github.kloping.mirai0.unitls.drawers.Drawer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static Project.controllers.auto.ControllerSource.challengeDetailService;
import static Project.controllers.auto.ControllerTool.opened;
import static Project.dataBases.GameDataBase.NAME_2_ID_MAPS;
import static Project.dataBases.GameDataBase.getInfo;
import static io.github.kloping.mirai0.Main.Resource.BOT;
import static io.github.kloping.mirai0.Main.Resource.println;
import static io.github.kloping.mirai0.commons.resouce_and_tool.ResourceSet.FinalNormalString.BG_TIPS;
import static io.github.kloping.mirai0.commons.resouce_and_tool.ResourceSet.FinalNormalString.EMPTY_STR;
import static io.github.kloping.mirai0.commons.resouce_and_tool.ResourceSet.FinalString.CHALLENGE_ING;
import static io.github.kloping.mirai0.commons.resouce_and_tool.ResourceSet.FinalValue.NOT_OPEN_NO_RUN_EXCEPTION;
import static io.github.kloping.mirai0.unitls.drawers.Drawer.getImageFromStrings;

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

    public GameBoneController() {
        println(this.getClass().getSimpleName() + "构建");
    }

    @Before
    public void before(User qq, Group group, @AllMess String str) throws NoRunException {
        if (!opened(group.getId(), this.getClass())) {
            throw NOT_OPEN_NO_RUN_EXCEPTION;
        }
        if (getInfo(qq.getId()).getHp() <= 0) {
            if (Tool.tool.EveListStartWith(listFx, str) == -1) {
                throw new NoRunException();
            }
        }
        if (getInfo(qq.getId()).isBg()) {
            MessageTools.instance.sendMessageInGroupWithAt(BG_TIPS, group.getId(), qq.getId());
            throw new NoRunException(BG_TIPS);
        }
    }

    @Action("魂骨菜单")
    public String boneMenu(long qq, Group g) {
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
    public String myAttribute(long qq, Group g) {
        return gameBoneService.getInfoAttributes(qq);
    }

    @Action("我的魂骨")
    public String myBones(long qq, Group g) {
        List<SoulBone> list = gameBoneService.getSoulBones(qq);
        return list.isEmpty() ? "没有魂骨!" : Tool.tool.pathToImg(Drawer.drawBoneMap(list));
    }

    @Action("吸收魂骨<.{1,}=>name>")
    public String parseBone(@Param("name") String name, long qq, Group g) {
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
    public String unParseBone(@Param("name") String name, long qq, Group g) {
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

    @AutoStand
    ISkillService skillService;

    @Action("头部魂骨技能<.{1,}=>str>")
    private Object t0(@Param("str") String str, long qq, Group g) {
        String idp = "151";
        for (SoulBone soulBone : gameBoneService.getSoulBones(qq)) {
            if (soulBone.getOid().toString().startsWith(idp)) {
                if (soulBone.hasSkill()) {
                    Set<Number> numbers = new HashSet<>();
                    while (true) {
                        if (str.contains("#")) {
                            str = str.replaceAll("#", EMPTY_STR);
                            numbers.add(-2);
                        }
                        Long l1 = MessageTools.instance.getAtFromString(str);
                        str = str.replaceFirst("\\[@" + (l1 == BOT.getId() ? "me" : l1) + "]", EMPTY_STR);
                        if (l1 <= 0) {
                            break;
                        } else {
                            //过滤挑战
                            if (challengeDetailService.isTemping(l1)) {
                                if (challengeDetailService.isTemping(qq)) {
                                    if (challengeDetailService.challenges.Q2Q.get(qq) == l1.longValue()) {
                                        numbers.add(l1);
                                    }
                                }
                            } else if (challengeDetailService.isTemping(qq)) {
                                if (challengeDetailService.isTemping(l1)) {
                                    if (challengeDetailService.challenges.Q2Q.get(qq) == l1.longValue()) {
                                        numbers.add(l1);
                                    }
                                }
                            } else {
                                numbers.add(l1);
                            }
                        }
                    }
                    Number[] ats = numbers.toArray(new Number[0]);
                    return String.valueOf(skillService.useSkill(qq, -1, ats, str, g));
                }
            }
        }
        return "您的头部魂骨尚不附带技能!";
    }
}
