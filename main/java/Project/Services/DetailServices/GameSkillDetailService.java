package Project.Services.DetailServices;

import Entitys.gameEntitys.GhostObj;
import Entitys.gameEntitys.PersonInfo;
import Entitys.gameEntitys.SkillIntro;
import Project.DataBases.GameDataBase;
import Project.DataBases.skill.SkillDataBase;
import Project.Tools.GameTool;
import Project.broadcast.HpChangeBroadcast;
import io.github.kloping.MySpringTool.annotations.Entity;

import java.lang.reflect.Field;
import java.util.LinkedHashSet;
import java.util.Set;

import static Project.DataBases.GameDataBase.getInfo;
import static Project.DataBases.GameDataBase.putPerson;
import static Project.DataBases.skill.SkillDataBase.*;
import static Project.Services.DetailServices.GameDetailService.Beaten;
import static Project.Services.DetailServices.GameDetailService.onAtt;
import static Project.Services.DetailServices.GameJoinDetailService.AttGho;

@Entity
public class GameSkillDetailService {

    /**
     * 获取魂技基础加成
     *
     * @param id
     * @return
     */
    public static synchronized Integer getBasePercent(Integer id) {
        switch (id) {
            case 0:
                return 10;
            case 1:
                return 4;
            case 2:
                return 10;
            case 3:
                return 4;
            case 4:
                return 10;
            case 5:
                return 4;
            case 6:
                return 5;
            case 7:
                return 5;
            case 8:
                return 35;
            case 9:
                return 4;
            case 10:
                return 5;
            case 11:
                return 25;
            case 12:
                return 8;
            case 13:
                return 25;
            case 14:
                return -1;
            case 15:
                return 18;
            case 16:
                return 50;
            case 17:
                return 5;
            case 18:
                return 40;
            case 19:
                return 40;
            //=================
            case 71:
                return 42;
            case 72:
                return 58;
            case 73:
                return 5;
            case 74:
                return 7;
            case 75:
                return 5;
            case 76:
                return 3;
            case 77:
                return 52;
            case 78:
                return 42;
            case 711:
                return 50;
            case 712:
                return 51;
            case 713:
                return 43;
            case 714:
                return 43;
            case 715:
                return 34;
            case 716:
                return 42;
            case 717:
                return 39;
            case 718:
                return 38;
            case 719:
                return 5;
            case 720:
                return 41;
            case 721:
                return 12;
            case 722:
                return 32;
            case 723:
                return 32;
            case 724:
                return 50;
            case 725:
                return 42;
            case 726:
                return 50;
            case 727:
                return 40;
            case 728:
                return 40;
            case 729:
                return 54;
            case 730:
                return 40;
            case 731:
                return 40;
        }
        return -1;
    }

    /**
     * 获取魂技介绍
     *
     * @param id
     * @param jid
     * @return
     */
    public static synchronized String getIntroContent(int id, int jid) {
        switch (jid) {
            case 0:
                return String.format("对指定一个人恢复%s%%的血量", getAddP(jid, id));
            case 1:
                return String.format("对指定几个人恢复%s%%的血量", getAddP(jid, id));
            case 2:
                return String.format("对指定一个人恢复%s%%的魂力", getAddP(jid, id));
            case 3:
                return String.format("对指定几个人恢复%s%%的魂力", getAddP(jid, id));
            case 4:
                return String.format("对指定一个人增加%s%%的攻击", getAddP(jid, id));
            case 5:
                return String.format("对指定几个人增加%s%%的攻击", getAddP(jid, id));
            case 6:
                return String.format("在接下来的一段时间内,攻击任何,将额外恢复攻击的%s%%的生命(不可叠加)", getAddP(jid, id));
            case 7:
                return String.format("给予指定一个人反甲,在接下来的一段时间内,任何攻击者,将额外受到攻击的%s%%的伤害(不可叠加))", getAddP(jid, id));
            case 8:
                return String.format("对指定敌人造成 攻击%s%%的伤害", getAddP(jid, id));
            case 9:
                return String.format("给予指定一个人,在接下来的来两分钟内,每24秒恢复%s%%的生命值", getAddP(jid, id));
            case 10:
                return String.format("在接下来的来%s秒内,免疫一次死亡", getAddP(jid, id));
            case 11:
                return String.format("狂热,让指定一人伤害变为真实伤害持续%s%%秒", getAddP(jid, id));
            case 12:
                return String.format("令自身 增加%s点闪避", getAddP(jid, id));
            case 13:
                return String.format("令指定一个人魂力减少%s%%", getAddP(jid, id));
            case 14:
                return String.format("令指定一个人无法躲避下次的攻击");
            case 15:
                return String.format("为自己增加一个最大生命值的%s%%的永久护盾(直到被打掉为止)", getAddP(jid, id));
            case 16:
                return String.format("为自己增加一个最大生命值的%s%%的临时护盾持续时间%s秒,##永久护盾和临时护盾不能叠加,后者将不生效", getAddP(jid, id), getAddP(jid, id) / 5);
            case 17:
                return String.format("%s秒内,躲避下次攻击", getAddP(jid, id));
            case 18:
                return String.format("攻击指定敌人,对血量越少的敌人造成的伤害越高 已损失50%%时加成为攻击x%s%%", getAddP(jid, id));
            case 19:
                return String.format("蓄力型技能,指定敌人,蓄力5秒后对其造成 攻击的%s+- 10% 的 伤害");
            //==========================================================
            case 71:
                return String.format("释放雷霆之力,对指定2个敌人造成%s%%攻击的伤害,10秒后在造成30%的伤害,10秒后造成10%的伤害", getAddP(jid, id));
            case 72:
                return String.format("释放昊天真身,增加%s%%的攻击力", getAddP(jid, id));
            case 73:
                return String.format("释放天使真身,每10秒恢复5%的魂力,增加%s%%的攻击力", getAddP(jid, id));
            case 74:
                return String.format("释放噬魂真身,吸取敌人%s%%的攻击力,恢复 吸取值得一半 的生命值", getAddP(jid, id));
            case 75:
                return String.format("强大的蓝银皇,增加%s的攻击力,拥有强大的生命力,每%s秒恢复%s%%的生命值", getAddP(jid, id) * 4, (t75 / 1000), getAddP(jid, id));
            case 76:
                return String.format("柔骨兔无敌真身,持续%s秒", getAddP(jid, id));
            case 77:
                return String.format("释放白虎真身,增加%s%%的攻击力", getAddP(jid, id));
            case 78:
                return String.format("释放邪火凤凰真身,增加%%s% + 魂力剩余百分比的一半% 的攻击力", getAddP(jid, id));
            case 79:
                return String.format("释放七杀剑真身,增加%s%%的攻击力", getAddP(jid, id));
            case 710:
                return String.format("释放碧灵蛇皇毒,对指定2个敌人造成30%%攻击的伤害,10秒后在造成30%%的伤害,10秒后造成30%%的伤害");
            case 711:
                return String.format("释放破魂枪真身,增加%s%%的攻击力", getAddP(jid, id));
            case 712:
                return String.format("释放大力金刚熊真身,增加%s%%的攻击力", getAddP(jid, id));
            case 713:
                return String.format("释放奇茸通天菊真身,增加%s%%的攻击力,并令一个人,无法躲避下次攻击", getAddP(jid, id));
            case 714:
                return String.format("释放鬼魅真身,增加%s%%的攻击力,并令一个人,无法躲避下次攻击", getAddP(jid, id));
            case 715:
                return String.format("释放刺豚真身,为自己增加一个最大生命值的%s%%的护盾,并每10秒恢复2%的生命值,持续%s秒", getAddP(jid, id), getAddP(jid, id) / 2);
            case 716:
                return String.format("释放蛇矛真身,为自己增加%s%%的攻击,同时增加%s的吸血", getAddP(jid, id), getAddP(jid, id) / 8);
            case 717:
                return String.format("骨龙真身,为自己增加%s%%的攻击,增加一个最大生命值的%s%%的护盾", getAddP(jid, id), getAddP(jid, id));
            case 718:
                return String.format("释放蛇杖=真身,为自己增加%s%%的攻击,同时增加%s的吸血", getAddP(jid, id), getAddP(jid, id) / 8);
            case 719:
                return String.format("蓝银草,释放蓝银草,每%s秒恢复%s%%的生命值", (t719 / 1000), getAddP(jid, id));
            case 720:
                return String.format("释放玄龟真身,为自己增加一个最大生命值的%s%%的护盾,永久护盾", getAddP(jid, id));
            case 721:
                return String.format("释放幽冥真身,令自身 增加%s点闪避,并增加%s的攻击", getAddP(jid, id), getAddP(jid, id));
            case 722:
                return String.format("光明圣龙,增加%s%%的攻击,和恢复%s%%的 血量,魂力,精神力", getAddP(jid, id), getAddP(jid, id) / 2);
            case 723:
                return String.format("黑暗圣龙,增加%s%%的攻击,和恢复%s%%的 精神力,并增加 %s%%的吸血", getAddP(jid, id), getAddP(jid, id) / 2, getAddP(jid, id) / 5);
            case 724:
                return String.format("修罗神剑,令自身变真实伤害一分钟,增加%s%%的攻击,并恢复%s%%的魂力", getAddP(jid, id), getAddP(jid, id) / 3);
            case 725:
                return String.format("青龙真身,增加%s%%的攻击,并为自己增加%s%%的反甲效果", getAddP(jid, id), getAddP(jid, id), getAddP(jid, id) / 3);
            case 726:
                return String.format("海神,千载空悠,武魂真身时间内,增加%s%%的攻击,对有护盾的敌人造成额外的%s%%伤害,", getAddP(jid, id), getAddP(jid, id) / 8);
            case 727:
            case 728:
                return String.format("渺小的农具,武魂真身,增加%s点攻击", getAddP(jid, id));
            case 729:
                return String.format("杀神昊天锤,奥义炸环第一魂环,第一魂技进入冷却,增加%s%%^^的攻击(随第一魂环品质提升而提升)", getAddP(jid, id));
            case 730:
                return String.format("魔神剑,令自身变真实伤害一分钟,增加%s%%的攻击,且神魔一体,窃取某的精神力,为自己恢复状态", getAddP(jid, id));
            case 731:
                return String.format("暗金恐爪熊,发挥恐怖的威力增加%s%%的攻击和%s%%的临时护盾", getAddP(jid, id), getAddP(jid, id));
        }
        return "无介绍=";
    }

    /**
     * 获取武魂类型
     *
     * @param jid
     * @return
     */
    public static synchronized SkillIntro.Type[] getTypesFromJid(int jid) {
        switch (jid) {
            case 0:
                return WhTypes._0;
            case 1:
                return WhTypes._1;
            case 2:
                return WhTypes._0;
            case 3:
                return WhTypes._1;
            case 4:
                return WhTypes._4;
            case 5:
                return WhTypes._5;
            case 6:
                return WhTypes._6;
            case 7:
                return WhTypes._6;
            case 8:
                return WhTypes._8;
            case 9:
                return new SkillIntro.Type[]{SkillIntro.Type.Special, SkillIntro.Type.Add, SkillIntro.Type.ToOne, SkillIntro.Type.HasTime};
            case 10:
                return WhTypes._6;
            case 11:
                return new SkillIntro.Type[]{SkillIntro.Type.Special, SkillIntro.Type.Add, SkillIntro.Type.Edd, SkillIntro.Type.HasTime};
            case 12:
                return new SkillIntro.Type[]{SkillIntro.Type.Add, SkillIntro.Type.HasTime, SkillIntro.Type.ToOne};
            case 13:
                return new SkillIntro.Type[]{SkillIntro.Type.Edd, SkillIntro.Type.OneTime, SkillIntro.Type.ToOne};
            case 14:
                return new SkillIntro.Type[]{SkillIntro.Type.Special, SkillIntro.Type.Mark};
            case 15:
                return new SkillIntro.Type[]{SkillIntro.Type.Mark, SkillIntro.Type.Shd};
            case 16:
                return new SkillIntro.Type[]{SkillIntro.Type.Mark, SkillIntro.Type.Shd};
            case 17:
                return new SkillIntro.Type[]{SkillIntro.Type.Mark, SkillIntro.Type.Special, SkillIntro.Type.OneTime};
            case 18:
                return new SkillIntro.Type[]{SkillIntro.Type.Att, SkillIntro.Type.Special};
            case 19:
                return new SkillIntro.Type[]{SkillIntro.Type.Att, SkillIntro.Type.NLonTime};
            //=======================================================================
            case 71:
                return new SkillIntro.Type[]{SkillIntro.Type.WHZs, SkillIntro.Type.Att, SkillIntro.Type.HasTime, SkillIntro.Type.ToNum};
            case 72:
                return WhTypes._72;
            case 73:
                return WhTypes._72;
            case 74:
                return new SkillIntro.Type[]{SkillIntro.Type.WHZs, SkillIntro.Type.Add, SkillIntro.Type.Att, SkillIntro.Type.OneTime};
            case 75:
                return WhTypes._72;
            case 76:
                return new SkillIntro.Type[]{SkillIntro.Type.WHZs, SkillIntro.Type.Special, SkillIntro.Type.Mark};
            case 77:
                return WhTypes._72;
            case 78:
                return new SkillIntro.Type[]{SkillIntro.Type.WHZs, SkillIntro.Type.Special, SkillIntro.Type.Add, SkillIntro.Type.HasTime, SkillIntro.Type.Edd};
            case 79:
                return WhTypes._72;
            case 710:
                return new SkillIntro.Type[]{SkillIntro.Type.WHZs, SkillIntro.Type.HasTime, SkillIntro.Type.Att};
            case 711:
                return WhTypes._72;
            case 712:
                return WhTypes._72;
            case 713:
                return new SkillIntro.Type[]{SkillIntro.Type.WHZs, SkillIntro.Type.OneTime, SkillIntro.Type.Add, SkillIntro.Type.Control};
            case 714:
                return new SkillIntro.Type[]{SkillIntro.Type.WHZs, SkillIntro.Type.OneTime, SkillIntro.Type.Add, SkillIntro.Type.Control};
            case 715:
                return new SkillIntro.Type[]{SkillIntro.Type.WHZs, SkillIntro.Type.OneTime, SkillIntro.Type.Add, SkillIntro.Type.Shd, SkillIntro.Type.Mark};
            case 716:
                return new SkillIntro.Type[]{SkillIntro.Type.WHZs, SkillIntro.Type.HasTime, SkillIntro.Type.Add, SkillIntro.Type.Mark};
            case 717:
                return new SkillIntro.Type[]{SkillIntro.Type.WHZs, SkillIntro.Type.HasTime, SkillIntro.Type.Add, SkillIntro.Type.Shd, SkillIntro.Type.Mark};
            case 718:
                return new SkillIntro.Type[]{SkillIntro.Type.WHZs, SkillIntro.Type.HasTime, SkillIntro.Type.Add, SkillIntro.Type.Mark};
            case 719:
                return WhTypes._72;
            case 720:
                return new SkillIntro.Type[]{SkillIntro.Type.WHZs, SkillIntro.Type.OneTime, SkillIntro.Type.Add, SkillIntro.Type.Shd, SkillIntro.Type.Mark};
            case 721:
                return new SkillIntro.Type[]{SkillIntro.Type.WHZs, SkillIntro.Type.HasTime, SkillIntro.Type.Special, SkillIntro.Type.Add};
            case 722:
                return new SkillIntro.Type[]{SkillIntro.Type.WHZs, SkillIntro.Type.HasTime, SkillIntro.Type.Special, SkillIntro.Type.Add};
            case 723:
                return new SkillIntro.Type[]{SkillIntro.Type.WHZs, SkillIntro.Type.HasTime, SkillIntro.Type.Special, SkillIntro.Type.Add};
            case 724:
                return new SkillIntro.Type[]{SkillIntro.Type.WHZs, SkillIntro.Type.HasTime, SkillIntro.Type.Special, SkillIntro.Type.Add};
            case 725:
                return new SkillIntro.Type[]{SkillIntro.Type.WHZs, SkillIntro.Type.HasTime, SkillIntro.Type.Special, SkillIntro.Type.Add, SkillIntro.Type.Mark};
            case 726:
                return new SkillIntro.Type[]{SkillIntro.Type.WHZs, SkillIntro.Type.HasTime, SkillIntro.Type.Special, SkillIntro.Type.Add, SkillIntro.Type.Mark};
            case 727:
                return WhTypes._72;
            case 728:
                return WhTypes._72;
            case 729:
                return new SkillIntro.Type[]{SkillIntro.Type.WHZs, SkillIntro.Type.HasTime, SkillIntro.Type.Special, SkillIntro.Type.Add, SkillIntro.Type.Mark, SkillIntro.Type.Edd};
            case 730:
                return new SkillIntro.Type[]{SkillIntro.Type.WHZs, SkillIntro.Type.HasTime, SkillIntro.Type.Special, SkillIntro.Type.Add, SkillIntro.Type.Mark};
            case 731:
                return new SkillIntro.Type[]{SkillIntro.Type.WHZs, SkillIntro.Type.HasTime, SkillIntro.Type.Special, SkillIntro.Type.Add, SkillIntro.Type.Mark, SkillIntro.Type.Shd};
        }
        return new SkillIntro.Type[]{SkillIntro.Type.Err};
    }


    /**
     * @param jid 魂技ID
     * @param id  魂环ID
     * @return
     */
    private static synchronized Long getAddP(Integer jid, Integer id) {
        return (long) (getBasePercent(jid) * GameTool.getAHBl_(id));
    }

    public static Number getDuration(int jid) {
        try {
            Field field = SkillDataBase.class.getDeclaredField("t" + jid);
            return (Number) field.get(null);
        } catch (Exception e) {
        }
        return -1;
    }

    private static final class WhTypes {
        public static final SkillIntro.Type[] _0 = new SkillIntro.Type[]{SkillIntro.Type.Add, SkillIntro.Type.ToOne, SkillIntro.Type.OneTime};
        public static final SkillIntro.Type[] _1 = new SkillIntro.Type[]{SkillIntro.Type.Add, SkillIntro.Type.ToNum, SkillIntro.Type.OneTime};
        public static final SkillIntro.Type[] _4 = new SkillIntro.Type[]{SkillIntro.Type.Add, SkillIntro.Type.ToOne, SkillIntro.Type.HasTime};
        public static final SkillIntro.Type[] _5 = new SkillIntro.Type[]{SkillIntro.Type.Add, SkillIntro.Type.ToNum, SkillIntro.Type.HasTime};
        public static final SkillIntro.Type[] _6 = new SkillIntro.Type[]{SkillIntro.Type.Special, SkillIntro.Type.ToOne, SkillIntro.Type.Mark};
        public static final SkillIntro.Type[] _8 = new SkillIntro.Type[]{SkillIntro.Type.Att, SkillIntro.Type.ToOne, SkillIntro.Type.OneTime};
        public static final SkillIntro.Type[] _72 = new SkillIntro.Type[]{SkillIntro.Type.WHZs, SkillIntro.Type.Add, SkillIntro.Type.HasTime};

    }


    /**
     * 获取魂技冷却时间
     *
     * @param id  魂环id
     * @param jid 魂技id
     * @param wh  武魂id
     * @param st  第几魂技
     * @return
     */
    public static Number getCoolTime(int id, int jid, int wh, int st) {
        if (st == 7) {
            if (wh == 6)
                return 30;
        }
        if (jid == 14) return 18;
        int n = 205 - id;
        return 12 - n;
    }

    public static Number getUserPercent(int st, int jid) {
        return (7 + (st - 1));
    }

    /**
     * 获取tag值
     *
     * @param qq
     * @param tag
     * @return
     */
    public static Number getTagValue(Number qq, String tag) {
        PersonInfo info = getInfo(qq);
        String sb = info.getMyTag();
        int i = sb.indexOf(tag);
        if (i < 0) return -1;
        sb = sb.substring(i);
        int i2 = sb.indexOf(",");
        String vs = sb.substring(1, i2);
        return Integer.parseInt(vs);
    }


    /**
     * 从任何东西上获取攻击值
     *
     * @param who 谁获取 谁
     * @param num
     * @return
     */
    public static long getAttFromAny(Number who, Number num) {
        if (num.longValue() != -2) {
            if (!GameDataBase.exist(num.longValue())) {
                return 1;
            }
            return getInfo(num).getAtt();
        } else {
            GhostObj ghostObj = GameJoinDetailService.getGhostObjFrom(who.longValue());
            if (ghostObj == null) return 0;
            return ghostObj.getAtt();
        }
    }

    /**
     * 从任何东西上获取精神值
     *
     * @param who
     * @param num
     * @return
     */
    public static long getHjFromAny(Number who, Number num) {
        if (num.longValue() != -2) {
            if (!GameDataBase.exist(num.longValue())) {
                return 0;
            }
            return getInfo(num).getHj();
        } else {
            GhostObj ghostObj = GameJoinDetailService.getGhostObjFrom(who.longValue());
            if (ghostObj == null) return 0;
            return ghostObj.getHj();
        }
    }

    /**
     * 对某个造成伤害
     *
     * @param sb
     * @param who
     * @param who2
     * @param v
     */
    public static void attGhostOrMan(StringBuilder sb, Number who, Number who2, Long v) {
        if (who2.longValue() == -2) {
            sb.append(AttGho(who.longValue(), v, true, false));
        } else {
            if (!GameDataBase.exist(who2.longValue())) {
                sb.append("该玩家尚未注册");
                return;
            }
            sb.append(Beaten(who2, who, v));
            sb.append("\n你对ta造成 " + v + "点伤害");
            if (!sb.toString().contains("$"))
                sb.append(onAtt(who2, who, v));
        }
    }

    /**
     * 减少任何攻击
     *
     * @param who
     * @param num
     * @param v
     * @return
     */
    public static boolean eddAttAny(Number who, Number num, long v) {
        if (num.longValue() != -2) {
            if (!GameDataBase.exist(num.longValue())) {
                return false;
            }
            putPerson(getInfo(num).addAtt(-v));
        } else {
            GhostObj ghostObj = GameJoinDetailService.getGhostObjFrom(who.longValue());
            ghostObj.setAtt(ghostObj.getAtt() - v);
            GameJoinDetailService.saveGhostObjIn(who.longValue(), ghostObj);
        }
        return true;
    }

    /**
     * 一个 player 对 另一个 player 的加血
     *
     * @param who  p1 加血者
     * @param who2 p2 被加血者
     * @param bf   比例
     */
    public static void addHp(Number who, long who2, float bf) {
        PersonInfo p1 = getInfo(who);
        long v1 = percentTo((int) bf, p1.getHpl());
        PersonInfo p2 = getInfo(who2);
        v1 = v1 > p2.getHpl() / 2 ? p2.getHpl() / 2 : v1;
        HpChangeBroadcast.INSTANCE.broadcast(who.longValue(), p2.getHp(),
                p2.getHp() + v1, v1, who.longValue(), HpChangeBroadcast.HpChangeReceiver.type.fromQ);
        p2.addHp(v1);
        putPerson(p2);
    }

    /**
     * 一个 player 对 另一个 player 的加魂力
     *
     * @param who  p1 加者
     * @param who2 p2 被加者
     * @param bf   比例
     */
    public static void addHl(Number who, long who2, float bf) {
        PersonInfo p1 = getInfo(who);
        long v1 = percentTo((int) bf, p1.getHll());
        PersonInfo p2 = getInfo(who2);
        v1 = v1 > p2.getHll() / 2 ? p2.getHll() / 2 : v1;
        HpChangeBroadcast.INSTANCE.broadcast(who.longValue(), p2.getHl(),
                p2.getHl() + v1, v1, who.longValue(), HpChangeBroadcast.HpChangeReceiver.type.fromQ);
        p2.addHl(v1);
        putPerson(p2);
    }

    public static void addAtt(Number who, long who2, float bf) {
        PersonInfo p1 = getInfo(who);
        long v1 = percentTo((int) bf, p1.getAtt());
        PersonInfo p2 = getInfo(who2);
        v1 = v1 > p2.getHll() / 2 ? p2.getHll() / 2 : v1;
        HpChangeBroadcast.INSTANCE.broadcast(who.longValue(), p2.getHl(),
                p2.getHl() + v1, v1, who.longValue(), HpChangeBroadcast.HpChangeReceiver.type.fromQ);
        p2.addHl(v1);
        putPerson(p2);
    }

    public static long oneNearest(Number who, Number[] nums) {
        return nums.length >= 1 ? nums[0].longValue() : who.longValue();
    }

    public static Long[] nearest(int n, long who, Number[] nums) {
        Set<Long> ls = new LinkedHashSet<>();
        for (Number num : nums) {
            if (ls.size() != n)
                ls.add(num.longValue());
            else break;
        }
        if (ls.size() < n) ls.add(who);
        return ls.toArray(new Long[0]);
    }

    public static Long[] nearest(int n, Number[] nums) {
        Set<Long> ls = new LinkedHashSet<>();
        for (Number num : nums) {
            if (ls.size() != n)
                ls.add(num.longValue());
            else break;
        }
        return ls.toArray(new Long[0]);
    }
}
