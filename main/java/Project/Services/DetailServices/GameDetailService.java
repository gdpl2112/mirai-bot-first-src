package Project.Services.DetailServices;

import Entitys.gameEntitys.AttributeBone;
import Entitys.gameEntitys.GhostObj;
import Entitys.gameEntitys.PersonInfo;
import Project.Controllers.GameControllers.GameController;
import Project.DataBases.GameDataBase;
import Project.DataBases.skill.SkillDataBase;
import Project.Services.Iservice.IGameBoneService;
import Project.Services.impl.GameBoneServiceImpl;
import Project.Tools.Tool;
import Project.broadcast.game.HpChangeBroadcast;
import Project.broadcast.game.PlayerLostBroadcast;
import io.github.kloping.Mirai.Main.Handlers.MyTimer;
import io.github.kloping.Mirai.Main.Resource;
import io.github.kloping.MySpringTool.annotations.AutoStand;
import io.github.kloping.MySpringTool.annotations.Entity;

import java.io.File;

import static Project.DataBases.GameDataBase.*;
import static Project.DataBases.skill.SkillDataBase.*;

@Entity
public class GameDetailService {

    @AutoStand
    static IGameBoneService gameBoneService;

    static {
        Resource.StartOkRuns.add(new Runnable() {
            @Override
            public void run() {
                MyTimer.ZeroRuns.add(() -> {
                    GameController.deleteC.clear();
                    File file = new File(GameDataBase.path + "/dates/users/");
                    for (File f1 : file.listFiles()) {
                        try {
                            String endN = f1.getName();
                            PersonInfo personInfo = getInfo(endN);
                            if (isNeedUpdate(personInfo))
                                putPerson(personInfo.setHelpC(0).setHelpToc(0).setBuyHelpC(0).setBuyHelpToC(0).setDied(false).setDowned(false));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    private static boolean isNeedUpdate(PersonInfo info) {
        return info.getBuyHelpC() > 0 || info.getHelpToc() > 0 || info.getHelpC() > 0 || info.getHelpToc() > 0 || info.isDied() || info.isDowned();
    }

    /**
     * 当被打了
     *
     * @param qq  我   被
     * @param qq2 ta 打了
     * @param o   这么多  血量
     * @return
     */
    public static synchronized String Beaten(Number qq, Number qq2, final long o) {
        boolean canHide = true;
        boolean isTrue_Hit = false;
        long oNow = o;
        StringBuilder sb = new StringBuilder();
        PersonInfo personInfo = getInfo(qq);
        //=====无敌
        if (personInfo.containsTag(SkillDataBase.tag_Wd)) {
            sb.append("\n$无敌效果,攻击无效\n============");
            return sb.toString();
        }
        //=====名刀
        if (personInfo.containsTag(SkillDataBase.tag_Ms)) {
            if (personInfo.getHp() - oNow <= 0) {
                putPerson(personInfo.eddTag(tag_Ms, 0));
                sb.append("\n$被攻击者,由于使用了免死类魂技,免疫此次 死亡\n============");
                return sb.toString();
            }
        }
        if (personInfo.containsTag(tag_XuanYuS)) {
            putPerson(personInfo.eddTag(tag_Ms, 1));
            sb.append("\n$被攻击者,由于使用了免疫此次伤害\n============");
            return sb.toString();
        }
        //=====无法闪避
        if (personInfo.containsTag(SkillDataBase.tag_CantHide)) {
            canHide = false;
            sb.append("\n此次伤害无法躲避\n============");
        }
        AttributeBone attributeBone = gameBoneService.getAttribute(qq.longValue());
        //=====闪避了
        if (ProZ(attributeBone.getHide_pro()) && canHide) {
            Integer[] ids = GameBoneServiceImpl.getIdsFromAttributeMap(gameBoneService.getAttributeMap(qq.longValue(), true), "hide");
            for (Integer i : ids)
                sb.append(GameDataBase.getNameById(i)).append(",");
            return "\n$得益于 " + sb + "你闪避了此次伤害" + (ids.length > 0 ? getImgById(ids[0]) : "") + "\n============";
        }
        //=====真实伤害
        if (personInfo.containsTag(tag_True_)) {
            isTrue_Hit = true;
            sb.append("\n此次真实伤害\n=============");
        }
        //=====护盾抵消
        if (personInfo.containsTag(tag_Shield) && !isTrue_Hit) {
            long v = GameSkillDetailService.getTagValue(qq, tag_Shield).longValue();
            oNow = o - v;
            if (v >= o) {
                personInfo.eddTag(tag_Shield, v);
                v -= o;
                personInfo.addTag(tag_Shield, v);
                sb.append("\n此次伤害全部护盾抵挡\n============");
            } else {
                personInfo.eddTag(tag_Shield, v);
                v = 0;
                sb.append("\n部分伤害护盾抵挡,伤害剩余:").append(oNow).append("\n============");
            }
            sb.append("\n护盾剩余" + v + "\n============");
        }
        //=====恢复了
        if (ProZ(attributeBone.getHp_pro())) {
            float fn;
            if (o > 100)
                fn = percentTo(attributeBone.getHp_Rec_Eff(), o);
            else
                fn = attributeBone.getHp_Rec_Eff();
            personInfo.addHp((long) fn);
            sb.append("\n得益于魂骨你恢复了").append(fn).append("生命\n").append("============");
        }
        //=====反甲刺伤
        if (personInfo.containsTag(SkillDataBase.tag_Fj)) {
            Integer p = personInfo.getTagValue(tag_Fj).intValue();
            long v1 = percentTo(p, o);
            if (v1 < 1) v1 = 1;
            Long q1 = qq2.longValue();
            if (q1 != -2) {
                putPerson(getInfo(q1).addHp(-v1));
                sb.append("\n被攻击者,由于带有反甲,攻击者受到 ").append(v1).append("点伤害\n============");
            } else {
                GhostObj ghostObj = GameJoinDetailService.getGhostObjFrom(qq.longValue());
                ghostObj.updateHp(-v1);
                GameJoinDetailService.saveGhostObjIn(qq.longValue(), ghostObj);
                sb.append("\n您带有反甲,").append(ghostObj.getName()).append("受到").append(v1).append("点伤害\n============");
            }
        }
        //=====精神力 判断
        if (oNow > 0) {
            long v1 = personInfo.getHj();
            long v2 = personInfo.getHjL();
            int b = toPercent(v1, v2);
            long ev = 0;
            if (b > 80) {
                ev = percentTo(10, oNow);
                sb.append("\n精神力高于80%,额外抵挡10%的伤害\n============");
            } else if (b < 40) {
                ev = -percentTo(10, oNow);
                sb.append("\n精神力低于40%,额外受到10%的伤害\n============");
            } else if (b <= 1) {
                ev = 0;
            }
            if (ev != 0) {
                oNow -= ev;
            }
            //===== 消耗精神力 判断
            int sf = toPercent(oNow, personInfo.getHpl());
            sf = sf > 80 ? 80 : sf < 1 ? 1 : sf;
            long sv = percentTo(sf, personInfo.getHjL());
            personInfo.addHj(-sv);
            sb.append(String.format("\n消耗了%s精神力\n============", sv));
        }
        //=====广播
        HpChangeBroadcast.INSTANCE.broadcast(qq.longValue(), personInfo.getHp(), personInfo.getHp() - oNow,
                oNow, qq2.longValue()
                , qq2.longValue() > 0 ?
                        HpChangeBroadcast.HpChangeReceiver.type.fromQ :
                        HpChangeBroadcast.HpChangeReceiver.type.fromG
        );
        personInfo.addHp(-oNow);

        if (personInfo.hp <= 0)
            PlayerLostBroadcast.INSTANCE.broadcast(qq.longValue(),
                    qq2.longValue(), PlayerLostBroadcast.PlayerLostReceiver.type.att);
        putPerson(personInfo);
        return sb.toString();
    }

    public static synchronized String ConsumedHl(long who, final long o) {
        PersonInfo personInfo = getInfo(who);
        StringBuilder sb = new StringBuilder();
        AttributeBone attributeBone = gameBoneService.getAttribute(who);
        long oNow = o;
        //=====恢复了
        if (ProZ(attributeBone.getHl_pro())) {
            float fn;
            if (o > 100) {
                fn = percentTo(attributeBone.getHl_Rec_Eff(), o);
            } else {
                fn = attributeBone.getHl_Rec_Eff();
            }
            oNow -= fn;
            sb.append("\n得益于 魂骨 你恢复了" + fn + "魂力\n============");
        }
        personInfo.addHl(-oNow);
        putPerson(personInfo);
        return sb.toString();
    }

    public static boolean ProZ(Integer n) {
        int i = Tool.rand.nextInt(100) + 1;
        return n >= i;
    }

    public static synchronized String onAtt(Number qq, Number qq2, Long v) {
        PersonInfo info = getInfo(qq);
        StringBuilder sb = new StringBuilder();
        //=====
        if (info.containsTag(SkillDataBase.tag_Xx)) {
            String tag = info.getTag(SkillDataBase.tag_Xx);
            Integer p = Integer.valueOf(tag.replace(SkillDataBase.tag_Xx, ""));
            long v1 = percentTo(p, v);
            if (v1 < 1) v1 = 1;
            info.addHp(v1);
            sb.append("\n攻击者,由于吸血技能恢复了 " + v1 + "的生命值\n============");
        }
        if (qq2.longValue() > 0) {
            PersonInfo info1 = getInfo(qq2);
            if (info.containsTag(tag_She) && info.containsTag(tag_Shield)) {
                int b = info.getTagValue(tag_She).intValue();
                long v2 = percentTo(b, v);
                putPerson(getInfo(qq2.longValue()).addHp(v2));
                sb.append("\n对有护盾的敌人额外造成").append(v2).append("伤害\n=========");
            }
        }
        putPerson(info);
        return "";
    }
}