package Project.services.detailServices.shopItems;

import Project.controllers.recr.HasTimeActionController;
import Project.services.player.UseRestrictions;
import io.github.kloping.mirai0.commons.PersonInfo;
import io.github.kloping.mirai0.commons.broadcast.enums.ObjType;
import io.github.kloping.mirai0.commons.gameEntitys.SkillInfo;
import io.github.kloping.mirai0.unitls.Tools.GameTool;
import io.github.kloping.mirai0.unitls.Tools.Tool;

import java.util.Map;

import static Project.dataBases.GameDataBase.*;
import static Project.dataBases.skill.SkillDataBase.getSkillInfo;
import static Project.dataBases.skill.SkillDataBase.updateSkillInfo;
import static io.github.kloping.mirai0.Main.Resource.THREADS;
import static io.github.kloping.mirai0.commons.resouce_and_tool.CommonSource.percentTo;
import static io.github.kloping.mirai0.commons.resouce_and_tool.ResourceSet.FinalNormalString.USE_UPPER_LIMIT_TIPS;
import static io.github.kloping.mirai0.commons.resouce_and_tool.ResourceSet.FinalString.NOT_SUPPORTED_NUM_USE;
import static io.github.kloping.mirai0.commons.resouce_and_tool.ResourceSet.FinalValue.OBJ116_VALUE;
import static io.github.kloping.mirai0.unitls.Tools.GameTool.getRandXl;

public class UseTool {

    public PersonInfo personInfo;

    public void remove(int id, long who) {
        removeFromBgs(who, id, ObjType.use);
    }

    public void before(long who) {
        personInfo = getInfo(who);
    }

    public String useObjNum(Long who, Integer id, Integer num) {
        PersonInfo personInfo = getInfo(who);
        long l = 0;
        if (id != 116) {
            for (Integer integer = 0; integer < num; integer++) {
                UseRestrictions.record(who, id);
            }
        } else {
            UseRestrictions.record(who, id);
        }
        if (UseRestrictions.cant(who.longValue(), id)) return USE_UPPER_LIMIT_TIPS;
        switch (id) {
            case 102:
                String s0 = "";
                long m = personInfo.getHpL();
                long t = personInfo.getHp();
                l = 0;
                int i1 = personInfo.getLevel() / 10;
                i1 = i1 < 4 ? 4 : i1;
                i1 = i1 > 7 ? 7 : i1;
                l = m / i1;
                l *= num;
                if (m - t < l) {
                    l = m - t;
                }
                if (GameTool.isATrue(who)) {
                    personInfo.addHp(l / 2);
                    s0 = "处于选择状态增加减半 加血=>" + (l / 2);
                } else {
                    personInfo.addHp(l);
                    s0 = "加血=>" + l;
                }
                putPerson(personInfo);
                removeFromBgs(Long.valueOf(who), id, num, ObjType.use);
                return s0;
            case 103:
                int c = (getRandXl(personInfo.getLevel()));
                long xr = personInfo.getXpL() / c;
                long mx = (long) (xr * 0.92f);
                mx *= num;
                putPerson(personInfo.addXp(mx));
                removeFromBgs(Long.valueOf(who), id, num, ObjType.use);
                return "增加了:" + mx + "点经验";
            case 104:
                long att = personInfo.getLevel() * 25;
                att *= num;
                putPerson(personInfo.addAtt(att));
                removeFromBgs(Long.valueOf(who), id, num, ObjType.use);
                return "增加了" + att + "点攻击";
            case 105:
                l = personInfo.getLevel() * 35;
                l *= num;
                putPerson(personInfo.addHp(l).addHpl(l));
                removeFromBgs(Long.valueOf(who), id, num, ObjType.use);
                return "增加了" + l + "点最大生命";
            case 112:
                long v = percentTo((int) Tool.tool.randA(10, 15), getInfo(who).getHjL());
                v = v < 0 ? 1 : v;
                v *= num;
                putPerson(getInfo(who).addHj(v));
                removeFromBgs(Long.valueOf(who), id, num, ObjType.use);
                return "恢复了" + v + "点精神力";
            case 116:
                Map<Integer, SkillInfo> infos = getSkillInfo(who.longValue());
                if (infos.containsKey(num)) {
                    SkillInfo skillInfo = infos.get(num);
                    skillInfo.setTime(skillInfo.getTime() - OBJ116_VALUE);
                    updateSkillInfo(skillInfo);
                    removeFromBgs(Long.valueOf(who), id, 1, ObjType.use);
                    UseRestrictions.record(who.longValue(), id);
                    return "使用成功" + num;
                } else {
                    return ("其魂技未解锁");
                }
            default:
                return NOT_SUPPORTED_NUM_USE;
        }
    }

    public String use101(long who) {
        remove(101, who);
        putPerson(getInfo(who).setK1(1L).setK2(1L));
        return "清空修炼和进入冷却";
    }

    public String use102(long who) {
        long m = personInfo.getHpL();
        long t = personInfo.getHp();
        long l = 0;
        if (t >= m) {
            return "满状态无需使用";
        } else {
            int i1 = personInfo.getLevel() / 10;
            i1 = i1 < 4 ? 4 : i1;
            i1 = i1 > 7 ? 7 : i1;
            l = m / i1;
            if (m - t < l) {
                l = m - t;
            }
            remove(102, who);
            UseRestrictions.record(who, 102);
            if (GameTool.isATrue(who)) {
                putPerson(getInfo(who).addHp(l / 2));
                return "处于选择状态增加减半 加血=>" + (l / 2);
            } else {
                putPerson(getInfo(who).addHp(l));
            }
        }
        return "加血=>" + l;
    }

    public String use103(long who) {
        int c = (getRandXl(personInfo.getLevel()));
        long xr = personInfo.getXpL() / c;
        long mx = (long) (xr * 1.1f);
        putPerson(getInfo(who).addXp(mx));
        remove(103, who);
        return "增加了" + mx + "点经验";
    }

    public String use104(long who) {
        long att = personInfo.getLevel() * 25;
        putPerson(personInfo.addAtt(att));
        remove(104, who);
        return "增加了" + att + "点攻击";
    }

    public String use105(long who) {
        long l = getInfo(who).getLevel() * 35;
        putPerson(getInfo(who).addHp(l).addHpl(l));
        remove(105, who);
        return "增加了" + l + "点最大生命";
    }

    public String use106(long who) {
        remove(106, who);
        UseRestrictions.record(who, 106);
        long m = personInfo.getHll();
        long t = personInfo.getHl();
        long l = 0;
        if (t >= m) {
            return "满状态无需使用";
        } else {
            l = (long) (m / 3.5f);
            if (m - t < l) {
                l = m - t;
            }
        }
        if (GameTool.isATrue(who)) {
            putPerson(getInfo(who).addHl(l / 2));
            return "处于选择状态增加减半 增加了" + (l / 2) + "点魂力";
        } else {
            putPerson(personInfo.addHl(l));
        }
        return "增加了" + l + "点魂力";
    }

    public String use107(long who) {
        int r = personInfo.getNextR1();
        if (r != -2) {
            remove(107, who);
            putPerson(personInfo.setNextR1(-2));
            return "使用成功!!";
        } else {
            return "使用失败,另一个正在使用..";
        }
    }

    public String use108(long who) {
        Integer nr = personInfo.getNextR2();
        if (nr != -2) {
            remove(108, who);
            putPerson(personInfo.setNextR2(-2));
            return "使用成功!!";
        } else {
            return "使用失败,另一个正在使用..";
        }
    }

    public String use109(long who) {
        remove(109, who);
        putPerson(getInfo(who).setHelpC(personInfo.getHelpC() - 1));
        UseRestrictions.record(who, 109);
        return "使用成功!!\r\n获得一次请求支援机会";
    }

    public String use110(long who) {
        remove(110, who);
        UseRestrictions.record(who, 110);
        putPerson(getInfo(who).setHelpToc(personInfo.getHelpToc() - 1));
        return "使用成功!!\r\n获得一次支援机会";
    }

    public String use111(long who) {
        return "该物品自动使用.";
    }

    public String use112(long who) {
        long v = percentTo((int) Tool.tool.randA(11, 17), getInfo(who).getHjL());
        v = v < 0 ? 1 : v;
        putPerson(getInfo(who).addHj(v));
        remove(112, who);
        UseRestrictions.record(who, 112);
        return "恢复了" + v + "点精神力";
    }

    public String use115(long who) {
        Integer nr = personInfo.getNextR3();
        if (nr != -2) {
            remove(115, who);
            putPerson(personInfo.setNextR3(-2));
            return "使用成功!!";
        } else {
            return "使用失败,另一个正在使用..";
        }
    }

    public String use117(long who) {
        UseRestrictions.record(who, 117);
        Map<Integer, SkillInfo> infos = getSkillInfo(who);
        for (SkillInfo value : infos.values()) {
            value.setTime(1L);
            updateSkillInfo(value);
        }
        removeFromBgs(Long.valueOf(who), 117, 1, ObjType.use);
        return "使用成功";
    }

    public String use118(long who) {
        UseRestrictions.record(who, 118);
        THREADS.submit(new Item118(who));
        removeFromBgs(Long.valueOf(who), 118, 1, ObjType.use);
        return "使用成功";
    }

    public String use1000(long who) {
        return "参见=>暗器菜单";
    }

    private String use160x(long who) {
        return "升级券,自动使用,升级第<几>魂环";
    }

    public String use1601(long who) {
        return use160x(who);
    }

    public String use1602(long who) {
        return use160x(who);
    }

    public String use1603(long who) {
        return use160x(who);
    }

    public String use1604(long who) {
        return use160x(who);
    }

    public String use1605(long who) {
        return use160x(who);
    }

    public String use7001(long who) {
        removeFromBgs(Long.valueOf(who), 7001, 1, ObjType.use);
        return HasTimeActionController.use(who);
    }
}