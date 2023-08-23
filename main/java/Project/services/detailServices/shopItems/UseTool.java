package Project.services.detailServices.shopItems;

import Project.commons.broadcast.enums.ObjType;
import Project.aSpring.dao.SkillInfo;
import Project.commons.gameEntitys.base.BaseInfoTemp;
import Project.controllers.recr.HasTimeActionController;
import Project.services.player.UseRestrictions;
import Project.aSpring.dao.PersonInfo;
import io.github.kloping.mirai0.commons.game.AsynchronousThing;
import Project.utils.Tools.GameTool;
import Project.utils.Tools.Tool;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import static Project.commons.rt.CommonSource.percentTo;
import static Project.commons.rt.ResourceSet.FinalNormalString.USE_UPPER_LIMIT_TIPS;
import static Project.commons.rt.ResourceSet.FinalString.NOT_SUPPORTED_NUM_USE;
import static Project.commons.rt.ResourceSet.FinalValue.OBJ116_VALUE;
import static Project.controllers.auto.ControllerSource.gameService;
import static Project.controllers.auto.ControllerSource.playerBehavioralManager;
import static Project.dataBases.GameDataBase.getInfo;
import static Project.dataBases.GameDataBase.removeFromBgs;
import static Project.dataBases.skill.SkillDataBase.*;
import static io.github.kloping.mirai0.Main.BootstarpResource.THREADS;
import static Project.utils.Tools.GameTool.getRandXl;
import static Project.utils.Tools.GameTool.isJTop0;

/**
 * @author HRS-Computer
 */
public class UseTool {

    public void remove(int id, long who) {
        removeFromBgs(who, id, ObjType.use);
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
                (personInfo).apply();
                removeFromBgs(Long.valueOf(who), id, num, ObjType.use);
                return s0;
            case 103:
                int c = (getRandXl(personInfo.getLevel()));
                long xr = personInfo.getXpL() / c;
                long mx = (long) (xr * 0.92f);
                mx *= num;
                (personInfo.addXp(mx)).apply();
                removeFromBgs(Long.valueOf(who), id, num, ObjType.use);
                return "增加了:" + mx + "点经验";
            case 104:
                long att = personInfo.getLevel() * 25;
                att *= num;
                (personInfo.addAtt(att)).apply();
                removeFromBgs(Long.valueOf(who), id, num, ObjType.use);
                return "增加了" + att + "点攻击";
            case 105:
                l = personInfo.getLevel() * 35;
                l *= num;
                (personInfo.addHp(l).addHpl(l)).apply();
                removeFromBgs(Long.valueOf(who), id, num, ObjType.use);
                return "增加了" + l + "点最大生命";
            case 112:
                long v = percentTo((int) Tool.INSTANCE.randA(10, 15), getInfo(who).getHjL());
                v = v < 0 ? 1 : v;
                v *= num;
                (getInfo(who).addHj(v)).apply();
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
        (getInfo(who).setK1(1L).setK2(1L)).apply();
        return "清空修炼和进入冷却";
    }

    public String use102(long who) {
        PersonInfo personInfo = getInfo(who);
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
                (getInfo(who).addHp(l / 2)).apply();
                return "处于选择状态增加减半 加血=>" + (l / 2);
            } else {
                (getInfo(who).addHp(l)).apply();
            }
        }
        return "加血=>" + l;
    }

    public String use103(long who) {
        PersonInfo personInfo = getInfo(who);
        int c = (getRandXl(personInfo.getLevel()));
        long xr = personInfo.getXpL() / c;
        long mx = (long) (xr * 1.1f);
        (getInfo(who).addXp(mx)).apply();
        remove(103, who);
        return "增加了" + mx + "点经验";
    }

    public String use104(long who) {
        PersonInfo personInfo = getInfo(who);
        long att = personInfo.getLevel() * 25;
        (personInfo.addAtt(att)).apply();
        remove(104, who);
        return "增加了" + att + "点攻击";
    }

    public String use105(long who) {
        long l = getInfo(who).getLevel() * 35;
        (getInfo(who).addHp(l).addHpl(l)).apply();
        remove(105, who);
        return "增加了" + l + "点最大生命";
    }

    public String use106(long who) {
        PersonInfo personInfo = getInfo(who);
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
            (getInfo(who).addHl(l / 2)).apply();
            return "处于选择状态增加减半 增加了" + (l / 2) + "点魂力";
        } else {
            (personInfo.addHl(l)).apply();
        }
        return "增加了" + l + "点魂力";
    }

    public String use107(long who) {
        PersonInfo personInfo = getInfo(who);
        int r = personInfo.getNextR1();
        if (r != -2) {
            remove(107, who);
            (personInfo.setNextR1(-2)).apply();
            return "使用成功!!";
        } else {
            return "使用失败,另一个正在使用..";
        }
    }

    public String use108(long who) {
        PersonInfo personInfo = getInfo(who);
        Integer nr = personInfo.getNextR2();
        if (nr != -2) {
            remove(108, who);
            (personInfo.setNextR2(-2)).apply();
            return "使用成功!!";
        } else {
            return "使用失败,另一个正在使用..";
        }
    }

    public String use109(long who) {
        PersonInfo personInfo = getInfo(who);
        remove(109, who);
        (getInfo(who).setHelpC(personInfo.getHelpC() - 1)).apply();
        UseRestrictions.record(who, 109);
        return "使用成功!!\r\n获得一次请求支援机会";
    }

    public String use110(long who) {
        PersonInfo personInfo = getInfo(who);
        remove(110, who);
        UseRestrictions.record(who, 110);
        (getInfo(who).setHelpToc(personInfo.getHelpToc() - 1)).apply();
        return "使用成功!!\r\n获得一次支援机会";
    }

    public String use111(long who) {
        return "该物品自动使用.";
    }

    public String use112(long who) {
        long v = percentTo((int) Tool.INSTANCE.randA(11, 17), getInfo(who).getHjL());
        v = v < 0 ? 1 : v;
        (getInfo(who).addHj(v)).apply();
        remove(112, who);
        UseRestrictions.record(who, 112);
        return "恢复了" + v + "点精神力";
    }

    public String use115(long who) {
        PersonInfo personInfo = getInfo(who);
        Integer nr = personInfo.getNextR3();
        if (nr != -2) {
            remove(115, who);
            (personInfo.setNextR3(-2)).apply();
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

    public String use119(long who) {
        UseRestrictions.record(who, 119);
        PersonInfo personInfo = getInfo(who);
        for (String negativeTag : NEGATIVE_TAGS) {
            if (personInfo.containsTag(negativeTag)) {
                personInfo.eddTag(negativeTag);
            }
        }
        personInfo.cancelVertigo(5000);
        List<Future> futures = AsynchronousThing.L_2_FS.get(who);
        if (futures != null) {
            for (Future future : futures) {
                future.cancel(true);
            }
        }
        Long q2 = BaseInfoTemp.VERTIGO_T2.get(who);
        if (q2 != null) {
            BaseInfoTemp.VERTIGO_T1.get(q2).cancel(true);
        }
        playerBehavioralManager.growths.remove(who);
        (getInfo(who).addTag(TAG_WD, 1, 800)).apply();
        removeFromBgs(Long.valueOf(who), 119, 1, ObjType.use);
        return "使用成功";
    }

    public String use128(long who) {
        PersonInfo pInfo = getInfo(who);
        if (isJTop0(who)) {
            return "无法升级,因为到达等级瓶颈,吸收魂环后继续升级";
        } else if (pInfo.getLevel() == 151) {
            return "等级最大限制";
        } else if (pInfo.getLevel() == 150) {
            pInfo.addLevel(1);
            pInfo.apply();
            removeFromBgs(Long.valueOf(who), 128, 1, ObjType.use);
            return "升级成功";
        } else {
            pInfo.addLevel(1);
            String s0 = gameService.upTrue(who);
            removeFromBgs(Long.valueOf(who), 128, 1, ObjType.use);
            return "升级成功\n" + s0;
        }
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
        return HasTimeActionController.use7001(who);
    }

    public String use7003(long who) {
        removeFromBgs(Long.valueOf(who), 7003, 1, ObjType.use);
        return HasTimeActionController.use7003(who);
    }
}