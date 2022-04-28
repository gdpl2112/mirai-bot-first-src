package Project.controllers.gameControllers;

import Project.dataBases.GameDataBase;
import Project.interfaces.Iservice.ISkillService;
import io.github.kloping.MySpringTool.annotations.*;
import io.github.kloping.MySpringTool.exceptions.NoRunException;
import io.github.kloping.mirai0.Main.ITools.MessageTools;
import io.github.kloping.mirai0.commons.Group;
import io.github.kloping.mirai0.commons.User;
import io.github.kloping.mirai0.commons.gameEntitys.SkillInfo;
import io.github.kloping.mirai0.unitls.Tools.Tool;
import io.github.kloping.number.NumberUtils;

import java.util.*;

import static Project.controllers.auto.ControllerTool.opened;
import static Project.dataBases.GameDataBase.getInfo;
import static Project.dataBases.skill.SkillDataBase.getSkillInfo;
import static io.github.kloping.mirai0.Main.Resource.bot;
import static io.github.kloping.mirai0.Main.Resource.println;
import static io.github.kloping.mirai0.commons.resouce_and_tool.ResourceSet.FinalNormalString.BG_TIPS;
import static io.github.kloping.mirai0.commons.resouce_and_tool.ResourceSet.FinalNormalString.EMPTY_STR;
import static io.github.kloping.mirai0.commons.resouce_and_tool.ResourceSet.FinalString.NEWLINE;
import static io.github.kloping.mirai0.commons.resouce_and_tool.ResourceSet.FinalValue.NOT_OPEN_NO_RUN_EXCEPTION;

/**
 * @author github-kloping
 */
@Controller
public class GameSkillController {
    private static List<String> listFx = new ArrayList<>();
    private static String menu;
    private static String m1 = null;

    static {
        StringBuilder sb = new StringBuilder();
        sb.append("调整:").append(NEWLINE)
                .append("\t1.蓄力型技能,指定敌人,蓄力1.5倍攻击前摇秒后对其造成 攻击的%s +- 10%% 的 伤害").append(NEWLINE);
        sb.append("新.").append(NEWLINE)
                .append("\t1.缩减指定人自身%s秒的攻击前摇,前摇最小0.5s\n")
                .append("\t2.减少指定人对应该魂技的位置的魂技%s秒的冷却");
        m1 = sb.toString();

    }

    static {
        listFx.add("我的第");
        listFx.add("魂技");
        listFx.add("魂技菜单");
        listFx.add("魂技更新");
        listFx.add("我的魂技");
    }

    static {
        menu = "魂技释放时,若需要选择 且 没选择 则魂技释放且无效果,请知悉\n选择器为 @xx 或 # 代表当前魂兽" +
                "1.激活魂技\r\n\t" +
                "  示例:激活魂技1\n" +
                "2.第(几)魂技(名字) ##释放魂技#没名字则忽略\r\n\t" +
                "  示例:第一魂技\n" +
                "3.魂技取名第(几)魂技(名字)\r\n\t" +
                "  示例:魂技起名第一魂技缠绕\r\n" +
                "4.我的第(几)魂技\r\n\t" +
                "  示例:我的第一魂技#获取介绍\n" +
                "5.忘掉第(几)魂技\r\n\t" +
                "  示例:忘掉第一魂技#需要遗忘药水"
                + "\n6.我的魂技"
                + "\n7.魂技更新列表"
        ;
    }

    @AutoStand
    ISkillService skillService;

    public GameSkillController() {
        println(this.getClass().getSimpleName() + "构建");
    }

    @Before
    public void before(User qq, Group group, @AllMess String str) throws NoRunException {
        if (!opened(group.getId(), this.getClass())) {
            throw NOT_OPEN_NO_RUN_EXCEPTION;
        }
        if (GameDataBase.getInfo(qq.getId()).getHp() <= 0) {
            if (Tool.EveListStartWith(listFx, str) == -1) {
                MessageTools.sendMessageInGroupWithAt("无状态", group.getId(), qq.getId());
                throw new NoRunException("无状态");
            }
        }
        if (getInfo(qq.getId()).isBg()) {
            MessageTools.sendMessageInGroupWithAt(BG_TIPS, group.getId(), qq.getId());
            throw new NoRunException(BG_TIPS);
        }
    }

    @Action("激活魂技<.+=>st>")
    public String initSkill(User member, Group group, @Param("st") String st) {
        Integer i;
        try {
            i = Integer.parseInt(st);
        } catch (Exception e) {
            try {
                i = Tool.chineseNumber2Int(Tool.findNumberZh(st));
            } catch (Exception ex) {
                return ("错误!\r\n示例:激活魂技1");
            }
        }
        return skillService.initSkill(member.getId(), group, i);
    }

    @Action(value = "第<.+=>str>", otherName = {"释放第<.+=>str>"})
    public String use(@Param("str") String str, User qq, Group group) {
        if (str.contains("魂技")) {
            str = str.replace("魂技", EMPTY_STR);
            String s1 = Tool.findNumberZh(str);
            Integer st = Integer.valueOf(Tool.chineseNumber2Int(s1));
            str = str.replaceFirst(Tool.trans(st), EMPTY_STR);
            Set<Number> numbers = new HashSet<>();
            while (true) {
                if (str.contains("#")) {
                    str = str.replaceAll("#", EMPTY_STR);
                    numbers.add(-2);
                }
                Long l1 = MessageTools.getAtFromString(str);
                str = str.replaceFirst("\\[@" + (l1 == bot.getId() ? "me" : l1) + "]", EMPTY_STR);
                if (l1 == -1) break;
                else numbers.add(l1);
            }
            Number[] ats = numbers.toArray(new Number[0]);
            return String.valueOf(skillService.useSkill(qq.getId(), st, ats, str, group));
        } else {
            throw new NoRunException();
        }
    }

    @Action(value = "魂技取名<.+=>str>", otherName = {"魂技起名<.+=>str>"})
    public String setName(@Param("str") String str, User qq, Group group) {
        if (str.contains("魂技")) {
            str = str.replaceFirst("魂技", EMPTY_STR).replaceFirst("第", EMPTY_STR);
            String s1 = Tool.findNumberZh(str);
            s1 = s1.substring(0, 1);
            Integer st = Integer.valueOf(Tool.chineseNumber2Int(s1));
            str = str.replaceFirst(Tool.trans(st) + EMPTY_STR, EMPTY_STR);
            return String.valueOf(skillService.setName(qq.getId(), st, str));
        } else {
            return "格式错误";
        }
    }

    @Action("我的第<.+=>str>")
    public String getIntro(@Param("str") String str, User qq, Group group) {
        if (str.contains("魂技")) {
            str = str.replace("魂技", EMPTY_STR);
            String s1 = Tool.findNumberZh(str);
            Integer st = Integer.valueOf(Tool.chineseNumber2Int(s1));
            str = str.replace(Tool.trans(st) + EMPTY_STR, EMPTY_STR);
            return String.valueOf(skillService.getIntro(qq.getId(), st, str));
        } else {
            throw new NoRunException();
        }
    }

    @Action("忘掉第<.+=>name>")
    public String forget(@Param("name") String str, User user) {
        if (str.contains("魂技")) {
            try {
                str = str.replaceFirst("魂技", EMPTY_STR);
                String s1 = Tool.findNumberZh(str);
                Integer st = Integer.valueOf(Tool.chineseNumber2Int(s1));
                str = str.replaceFirst(Tool.trans(st)  , EMPTY_STR);
                return skillService.forget(user.getId(), st);
            } catch (Exception e) {
                return "未知异常.";
            }
        } else {
            throw new NoRunException();
        }
    }

    @Action("魂技菜单")
    public String menu() {
        return menu;
    }

    @Action("魂技更新列表")
    public String m1() {
        return m1;
    }

    @Action("我的魂技")
    public String m2(User user) {
        StringBuilder sb = new StringBuilder();
        sb.append("只会发送有名字的魂技\n");
        Map<Integer, SkillInfo> infos = getSkillInfo(user.getId());
        for (SkillInfo info : infos.values()) {
            if (info.getName() == null || info.getName().isEmpty()) continue;
            sb.append("第").append(NumberUtils.intNumber2ChineseNumber(info.getSt()))
                    .append("魂技:").append(info.getName())
                    .append("\n");
        }
        return sb.toString();
    }
}