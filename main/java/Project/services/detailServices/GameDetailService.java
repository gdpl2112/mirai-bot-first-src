package Project.services.detailServices;

import Project.aSpring.SpringBootResource;
import Project.broadcast.game.GhostLostBroadcast;
import Project.broadcast.game.HpChangeBroadcast;
import Project.broadcast.game.PlayerLostBroadcast;
import Project.controllers.gameControllers.GameController;
import Project.dataBases.GameDataBase;
import Project.dataBases.skill.SkillDataBase;
import Project.interfaces.Iservice.IGameBoneService;
import Project.services.detailServices.ac.entity.Ghost702;
import Project.services.detailServices.roles.BeatenRoles;
import Project.services.detailServices.roles.Role;
import Project.services.detailServices.roles.RoleResponse;
import Project.services.detailServices.roles.RoleState;
import io.github.kloping.MySpringTool.annotations.AutoStand;
import io.github.kloping.MySpringTool.annotations.Entity;
import io.github.kloping.mirai0.Entitys.gameEntitys.GhostObj;
import io.github.kloping.mirai0.Entitys.gameEntitys.PersonInfo;
import io.github.kloping.mirai0.Entitys.gameEntitys.SoulAttribute;
import io.github.kloping.mirai0.Entitys.gameEntitys.base.BaseInfo;
import io.github.kloping.mirai0.Main.Resource;
import io.github.kloping.mirai0.unitls.Tools.Tool;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static Project.ResourceSet.FinalFormat.*;
import static Project.ResourceSet.FinalString.*;
import static Project.ResourceSet.FinalValue.*;
import static Project.controllers.auto.TimerController.ZERO_RUNS;
import static Project.dataBases.GameDataBase.putPerson;
import static Project.dataBases.skill.SkillDataBase.*;
import static Project.services.detailServices.GameDetailServiceUtils.getBaseInfoFromAny;

/**
 * @author github-kloping
 */
@Entity
public class GameDetailService {

    @AutoStand
    public static IGameBoneService gameBoneService;

    static {
        Resource.START_AFTER.add(new Runnable() {
            @Override
            public void run() {
                ZERO_RUNS.add(() -> {
                    GameController.deleteC.clear();
                    SpringBootResource.getPersonInfoMapper().updateAll();
                });
            }
        });
    }

    private static boolean isNeedUpdate(PersonInfo info) {
        return info.isVertigo() || info.getBuyHelpC() > 0 || info.getHelpToc() > 0 || info.getHelpC() > 0 || info.getHelpToc() > 0 || info.isDied() || info.isDowned();
    }

    /**
     * 当被打了
     *
     * @param qq  我   被
     * @param qq2 ta 打了
     * @param o   这么多  血量
     * @return
     */
    public static String beaten(Number qq, Number qq2, final long o) {
        synchronized (qq2) {
            long oNow = o;
            StringBuilder sb = new StringBuilder();
            PersonInfo p1 = GameDataBase.getInfo(qq);
            Map<String, Object> maps = new ConcurrentHashMap<>();
            for (Role r : BeatenRoles.RS) {
                RoleResponse response = r.call(sb, qq, qq2, o, oNow, p1, maps);
                if (response != null) {
                    oNow = response.getNowV();
                    if (!response.getArgs().isEmpty()) {
                        maps.putAll(response.getArgs());
                    }
                    if (response.getState() == RoleState.STOP) {
                        break;
                    }
                }
            }
            if (oNow > 0) {
                long v1 = p1.getHj();
                long v2 = p1.getHjL();
                int b = toPercent(v1, v2);
                long ev = 0;
                if (b > 80) {
                    ev = percentTo(10, oNow);
                    sb.append(NEWLINE).append(HJ_OVER_80_TIPS);
                } else if (b <= 1) {
                    ev = 0;
                }
                if (ev != 0) {
                    oNow -= ev;
                }
                //===== 消耗精神力 判断
                int sf = toPercent(oNow, p1.getHpL());
                sf = sf > 80 ? 80 : sf < 1 ? 1 : sf;
                long sv = percentTo(sf, p1.getHjL());
                sb.append(onHjLose(qq.longValue(), qq2, sv));
            }
            //=====广播
            HpChangeBroadcast.INSTANCE.broadcast(qq.longValue(), p1.getHp(), p1.getHp() - oNow,
                    oNow, qq2.longValue()
                    , qq2.longValue() > 0 ?
                            HpChangeBroadcast.HpChangeReceiver.type.fromQ :
                            HpChangeBroadcast.HpChangeReceiver.type.fromG
            );
            if (oNow > 0) {
                p1.addHp(-oNow);
                p1.apply();
                if (p1.hp <= 0) {
                    PlayerLostBroadcast.INSTANCE.broadcast(qq.longValue(),
                            qq2.longValue(), PlayerLostBroadcast.PlayerLostReceiver.type.att);
                }
            }
            return sb.toString();
        }
    }

    public static String consumedHl(long who, final long o) {
        PersonInfo personInfo = GameDataBase.getInfo(who);
        StringBuilder sb = new StringBuilder();
        SoulAttribute attributeBone = gameBoneService.getSoulAttribute(who);
        long oNow = o;
        //=====恢复了
        if (proZ(attributeBone.getHlChance())) {
            float fn;
            if (o > 100) {
                fn = percentTo(attributeBone.getHlEffect(), o);
            } else {
                fn = attributeBone.getHlEffect();
            }
            oNow -= fn;
            sb.append("\n得益于 魂骨 你恢复了").append(fn).append("魂力");
        }
        personInfo.addHl(-oNow);
        putPerson(personInfo);
        return sb.toString();
    }

    public static boolean proZ(Integer n) {
        int i = Tool.RANDOM.nextInt(100) + 1;
        return n >= i;
    }

    public static String onAtt(Number qq, Number qq2, Long v) {
        PersonInfo info = GameDataBase.getInfo(qq);
        StringBuilder sb = new StringBuilder();
        //=====
        sb.append(NEWLINE).append(SPLIT_LINE_0);
        if (info.containsTag(SkillDataBase.TAG_XX)) {
            String tag = info.getTag(SkillDataBase.TAG_XX);
            Integer p = Integer.valueOf(tag.replace(SkillDataBase.TAG_XX, ""));
            long v1 = percentTo(p, v);
            if (v1 < 1) {
                v1 = 1;
            }
            info.addHp(v1);
            sb.append("\n攻击者,由于吸血技能恢复了").append(v1).append("的生命值");
        }
        if (qq2.longValue() > 0) {
            PersonInfo info1 = GameDataBase.getInfo(qq2);
            if (info.containsTag(TAG_SHE) && info.containsTag(TAG_SHIELD)) {
                int b = info.getTagValue(TAG_SHE).intValue();
                long v2 = percentTo(b, v);
                putPerson(GameDataBase.getInfo(qq2.longValue()).addHp(-v2));
                sb.append(NEWLINE).append(SPLIT_LINE_0);
                sb.append("\n对有护盾的敌人额外造成").append(v2).append("伤害");
            }
        } else {
            GhostObj ghostObj = GameJoinDetailService.getGhostObjFrom(qq.longValue());
            if (ghostObj instanceof Ghost702) {
                Ghost702 ghost702 = (Ghost702) ghostObj;
                if (ghost702.getShield() >= 0) {
                    int b = info.getTagValue(TAG_SHE).intValue();
                    long v2 = percentTo(b, v);
                    GameJoinDetailService.attGho(qq.longValue(), v2, false, false, GhostLostBroadcast.KillType.SKILL_ATT);
                    sb.append(NEWLINE).append(SPLIT_LINE_0);
                    sb.append("\n对有护盾的敌人额外造成").append(v2).append("伤害");
                }
            }
        }
        putPerson(info);
        return "";
    }

    /**
     * 精神攻击
     *
     * @param q  攻击者
     * @param q2 被攻击者
     * @param by 攻击比率
     * @return
     */
    public static String onSpiritAttack(Number q, Number q2, Integer by) {
        synchronized (q) {
            PersonInfo p1 = GameDataBase.getInfo(q);
            if (p1.isVertigo()) {
                return ATTACKER_IN_VERTIGO;
            }
            long v1L = p1.getHjL();
            long v1 = p1.getHj();
            long v2 = percentTo(by, v1L);
            if (v2 > v1) {
                return String.format(HJ_NOT_ENOUGH_TIPS0, by);
            } else {
                p1.addHj(-v2);
            }
            StringBuilder sb = new StringBuilder();
            BaseInfo baseInfo = getBaseInfoFromAny(q, q2);
            int b1 = toPercent(v2, baseInfo.getHjL());
            b1 = b1 > MAX_SA_LOSE_HJ_B ? MAX_SA_LOSE_HJ_B : b1;
            long ov2 = percentTo(b1, baseInfo.getHj());
            ov2 = ov2 == 0 ? v2 : ov2;
            if (baseInfo.getHj() < ov2) {
                ov2 = baseInfo.getHj();
            }
            baseInfo.addHj(-ov2);
            baseInfo.apply();
            sb.append(NEWLINE).append(SPLIT_LINE_0);
            sb.append("\n对其造成了ta的").append(ov2).append("(").append(b1).append("%)精神力的损失");
            long nv2 = v2 - ov2;
            if (nv2 > 0) {
                nv2 *= HJ_LOSE_1_X;
                if (baseInfo instanceof GhostObj) {
                    sb.append(GameJoinDetailService.attGho(q.longValue(), nv2, true, false, GhostLostBroadcast.KillType.SPIRIT_ATT));
                } else {
                    int v = toPercent(nv2, baseInfo.getHpL());
                    v = v > MAX_SA_LOSE_HP_B ? MAX_SA_LOSE_HP_B : v;
                    long nv0 = percentTo(v, baseInfo.getHpL());
                    sb.append(NEWLINE).append(SPLIT_LINE_0);
                    sb.append("\n对其造成了").append(nv0).append("(").append(v).append("%)额外伤害");
                    sb.append(beaten(q2, q, nv0)).append(NEWLINE);
                }
            }
            p1.apply();
            return sb.toString().trim();
        }
    }

    /**
     * @param qq  消耗方
     * @param qq2 主动方
     * @param v   值
     * @return
     */
    public static String onHjLose(Number qq, Number qq2, Long v) {
        SoulAttribute soulAttribute = gameBoneService.getSoulAttribute(qq.longValue());
        String s = NEWLINE + "\n消耗了" + v + "点精神力";
        if (proZ(soulAttribute.getHjChance())) {
            Long v0 = percentTo(soulAttribute.getHjEffect(), v);
            v = v -= v0;
            s += ("\n恢复了" + v0 + "点精神力");
        }
        BaseInfo baseInfo = getBaseInfoFromAny(qq, qq);
        baseInfo.addHj(-v);
        return s;
    }

    public static String addHp(long q, int b) {
        Long l = GameDataBase.getInfo(q).getHpL();
        Long v = percentTo(b, l);
        GameDataBase.getInfo(q).addHp(v).apply();
        return String.format(ADDHP_TIPS, v);
    }

    public static String addHl(long q, int b) {
        Long l = GameDataBase.getInfo(q).getHll();
        Long v = percentTo(b, l);
        GameDataBase.getInfo(q).addHl(v).apply();
        return String.format(ADDHL_TIPS, v);
    }

    public static String addHj(long q, int b) {
        Long l = GameDataBase.getInfo(q).getHjL();
        Long v = percentTo(b, l);
        GameDataBase.getInfo(q).addHj(v).apply();
        return String.format(ADDHJ_TIPS, v);
    }
}


