package Project.Controllers.GameControllers;

import Entitys.Group;
import Entitys.SkillInfo;
import Entitys.User;
import Project.Services.IServer.ISkillService;
import Project.Tools.Tool;
import io.github.kloping.Mirai.Main.ITools.MessageTools;
import io.github.kloping.MySpringTool.annotations.*;
import io.github.kloping.MySpringTool.exceptions.NoRunException;
import io.github.kloping.number.NumberUtils;

import java.util.*;

import static Project.Controllers.ControllerTool.CanGroup;
import static Project.DataBases.GameDataBase.getInfo;
import static Project.DataBases.SkillDataBase.getSkillInfo;
import static io.github.kloping.Mirai.Main.Resource.Switch.AllK;
import static io.github.kloping.Mirai.Main.Resource.println;

@Controller
public class GameSkillController {
    public GameSkillController() {
        println(this.getClass().getSimpleName() + "构建");
    }

    private static final boolean open = true;
    private static List<String> listFx = new ArrayList<>();

    @AutoStand
    ISkillService skillService;

    static {
        listFx.add("我的第");
        listFx.add("魂技");
        listFx.add("魂技菜单");
        listFx.add("魂技更新");
        listFx.add("我的魂技");
    }

    @Before
    public void before(User qq, Group group, @AllMess String str) throws NoRunException {
        if (!AllK)
            throw new NoRunException();
        if (!CanGroup(group.getId())) {
            throw new NoRunException();
        }
        if (!open) {
            throw new NoRunException("未开启此类");
        }
        if (getInfo(qq.getId()).getHp() <= 0) {
            if (Tool.EveListStartWith(listFx, str) == -1) {
                MessageTools.sendMessageInGroupWithAt("无状态", group.getId(), qq.getId());
                throw new NoRunException("无状态");
            }
        }
    }

    @Action("激活魂技<.+=>st>")
    public String InitSkill(User member, Group group, @Param("st") String st) {
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
        return skillService.InitSkill(member.getId(), group, i);
    }

    @Action(value = "第<.+=>str>", otherName = {"释放第<.+=>str>"})
    public String use(@Param("str") String str, User qq, Group group) {
        if (str.contains("魂技")) {
            str = str.replace("魂技", "");
            String s1 = Tool.findNumberZh(str);
            Integer st = Integer.valueOf(Tool.chineseNumber2Int(s1));
            str = str.replace(Tool.trans(st) + "", "");
            return String.valueOf(skillService.UseSkill(qq.getId(), st, getAllAt(str), str, group));
        } else {
            throw new NoRunException();
        }
    }

    @Action(value = "魂技取名<.+=>str>", otherName = {"魂技起名<.+=>str>"})
    public String setName(@Param("str") String str, User qq, Group group) {
        if (str.contains("魂技")) {
            str = str.replace("魂技", "").replace("第", "");
            String s1 = Tool.findNumberZh(str);
            Integer st = Integer.valueOf(Tool.chineseNumber2Int(s1));
            str = str.replace(Tool.trans(st) + "", "");
            return String.valueOf(skillService.setName(qq.getId(), st, str));
        } else {
            return "格式错误";
        }
    }

    @Action("我的第<.+=>str>")
    public String getIntro(@Param("str") String str, User qq, Group group) {
        if (str.contains("魂技")) {
            str = str.replace("魂技", "");
            String s1 = Tool.findNumberZh(str);
            Integer st = Integer.valueOf(Tool.chineseNumber2Int(s1));
            str = str.replace(Tool.trans(st) + "", "");
            return String.valueOf(skillService.getIntro(qq.getId(), st, str));
        } else {
            throw new NoRunException();
        }
    }

    @Action("忘掉第<.+=>name>")
    public String forget(@Param("name") String str, User user) {
        if (str.contains("魂技")) {
            try {
                str = str.replace("魂技", "");
                String s1 = Tool.findNumberZh(str);
                Integer st = Integer.valueOf(Tool.chineseNumber2Int(s1));
                str = str.replace(Tool.trans(st) + "", "");
                return skillService.forget(user.getId(), st);
            } catch (Exception e) {
                return "未知异常.";
            }
        } else throw new NoRunException("=><>^");
    }

    private static String menu;

    static {
        menu = "魂技释放时,若需要选择 且 没选择 则魂技释放且无效果,请知悉\n选择器为 @xx 或 # 代表当前魂兽\n=======\n存在bug请及时反馈\n========\n" +
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

    @Action("魂技菜单")
    public String menu() {
        return menu;
    }

    private static Number[] getAllAt(String allMess) {
        Set<Number> numbers = new HashSet<>();
        while (true) {
            if (allMess.contains("#")) {
                allMess = allMess.replaceAll("\\#", "");
                numbers.add(-2);
            }
            Long l1 = MessageTools.getAtFromString(allMess);
            allMess = allMess.replaceFirst("\\[@" + l1 + "\\]", "");
            if (l1 == -1) break;
            else numbers.add(l1);
        }
        return numbers.toArray(new Number[0]);
    }

    private static String m1 = "新.\n" +
            "\t1._N秒内,躲避下次攻击\n" +
            "\t2.攻击指定敌人,对血量越少的敌人造成的伤害越高 已损失50%时加成为 攻击xN%\n" +
            "\t3.蓄力型技能,指定敌人,蓄力5秒后对其造成 攻击的N%+- 10% 的 伤害\n" +
            "==============\n" +
            "调整.\n" +
            "\t1.在接下来的来N秒内,免疫一次死亡";

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