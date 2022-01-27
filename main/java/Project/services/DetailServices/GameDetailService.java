package Project.services.DetailServices;

import io.github.kloping.mirai0.Entitys.gameEntitys.AttributeBone;
import io.github.kloping.mirai0.Entitys.gameEntitys.GhostObj;
import io.github.kloping.mirai0.Entitys.gameEntitys.PersonInfo;
import io.github.kloping.mirai0.Entitys.gameEntitys.base.BaseInfo;
import Project.Controllers.GameControllers.GameController;
import Project.DataBases.GameDataBase;
import Project.DataBases.skill.SkillDataBase;
import io.github.kloping.mirai0.unitls.Tools.Tool;
import Project.broadcast.game.GhostLostBroadcast;
import Project.broadcast.game.HpChangeBroadcast;
import Project.broadcast.game.PlayerLostBroadcast;
import Project.services.DetailServices.roles.BeatenRoles;
import Project.services.DetailServices.roles.Role;
import Project.services.DetailServices.roles.RoleResponse;
import Project.services.DetailServices.roles.RoleState;
import Project.services.Iservice.IGameBoneService;
import io.github.kloping.mirai0.Main.Handlers.MyTimer;
import io.github.kloping.mirai0.Main.Resource;
import io.github.kloping.MySpringTool.annotations.AutoStand;
import io.github.kloping.MySpringTool.annotations.Entity;

import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static Project.DataBases.GameDataBase.getInfo;
import static Project.DataBases.GameDataBase.putPerson;
import static Project.DataBases.skill.SkillDataBase.*;
import static Project.ResourceSet.FinalFormat.*;
import static Project.ResourceSet.FinalString.*;
import static Project.ResourceSet.FinalValue.*;
import static Project.services.DetailServices.GameDetailServiceUtils.getBaseInfoFromAny;

/**
 * @author github-kloping
 */
@Entity
public class GameDetailService {

    @AutoStand
    public static IGameBoneService gameBoneService;

    static {
        Resource.StartOkRuns.add(new Runnable() {
            @Override
            public void run() {
                MyTimer.ZERO_RUNS.add(() -> {
                    GameController.deleteC.clear();
                    File file = new File(GameDataBase.path + "/dates/users/");
                    for (File f1 : file.listFiles()) {
                        try {
                            String endN = f1.getName();
                            PersonInfo personInfo = getInfo(endN);
                            if (isNeedUpdate(personInfo)) {
                                putPerson(personInfo.cancelVertigo().setHelpC(0).setHelpToc(0)
                                        .setBuyHelpC(0).setBuyHelpToC(0).setDied(false).setDowned(false));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
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
            PersonInfo p1 = getInfo(qq);
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
                    sb.append(NEWLINE).append(HJ_OVER_80_TIPS).append(NEWLINE).append(SPLIT_LINE_0);
                } else if (b < 40) {
                    ev = -percentTo(10, oNow);
                    sb.append(NEWLINE).append(HJ_LOW_40_TIPS).append(NEWLINE).append(SPLIT_LINE_0);
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
                p1.addHj(-sv);
                sb.append(String.format("\n消耗了%s精神力\n============", sv));
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
        PersonInfo personInfo = getInfo(who);
        StringBuilder sb = new StringBuilder();
        AttributeBone attributeBone = gameBoneService.getAttribute(who);
        long oNow = o;
        //=====恢复了
        if (proZ(attributeBone.getHlPro())) {
            float fn;
            if (o > 100) {
                fn = percentTo(attributeBone.getHlRecEff(), o);
            } else {
                fn = attributeBone.getHlRecEff();
            }
            oNow -= fn;
            sb.append("\n得益于 魂骨 你恢复了" + fn + "魂力\n============");
        }
        personInfo.addHl(-oNow);
        putPerson(personInfo);
        return sb.toString();
    }

    public static boolean proZ(Integer n) {
        int i = Tool.rand.nextInt(100) + 1;
        return n >= i;
    }

    public static String onAtt(Number qq, Number qq2, Long v) {
        PersonInfo info = getInfo(qq);
        StringBuilder sb = new StringBuilder();
        //=====
        if (info.containsTag(SkillDataBase.TAG_XX)) {
            String tag = info.getTag(SkillDataBase.TAG_XX);
            Integer p = Integer.valueOf(tag.replace(SkillDataBase.TAG_XX, ""));
            long v1 = percentTo(p, v);
            if (v1 < 1) {
                v1 = 1;
            }
            info.addHp(v1);
            sb.append("\n攻击者,由于吸血技能恢复了 " + v1 + "的生命值\n============");
        }
        if (qq2.longValue() > 0) {
            PersonInfo info1 = getInfo(qq2);
            if (info.containsTag(TAG_SHE) && info.containsTag(TAG_SHIELD)) {
                int b = info.getTagValue(TAG_SHE).intValue();
                long v2 = percentTo(b, v);
                putPerson(getInfo(qq2.longValue()).addHp(v2));
                sb.append("\n对有护盾的敌人额外造成").append(v2).append("伤害\n=========");
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
            PersonInfo p1 = getInfo(q);
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
            sb.append("对其造成了ta的").append(ov2).append("(").append(b1).append("%)精神力的损失\n");

            long nv2 = v2 - ov2;
            if (nv2 > 0) {
                nv2 *= HJ_LOSE_1_X;
                if (baseInfo instanceof GhostObj) {
                    sb.append(GameJoinDetailService.attGho(q.longValue(), nv2, true, false, GhostLostBroadcast.KillType.SPIRIT_ATT));
                } else {
                    int v = toPercent(nv2, baseInfo.getHpL());
                    v = v > MAX_SA_LOSE_HP_B ? MAX_SA_LOSE_HP_B : v;
                    long nv0 = percentTo(v, baseInfo.getHpL());
                    sb.append("对其造成了").append(nv0).append("(").append(v).append("%)额外伤害\n");
                    sb.append(beaten(q2, q, nv0)).append("\n");
                }
            }
            p1.apply();
            return sb.toString().trim();
        }
    }

    public static String addHp(long q, int b) {
        Long l = getInfo(q).getHpL();
        Long v = percentTo(b, l);
        getInfo(q).addHp(v).apply();
        return String.format(ADDHP_TIPS, v);
    }

    public static String addHl(long q, int b) {
        Long l = getInfo(q).getHll();
        Long v = percentTo(b, l);
        getInfo(q).addHl(v).apply();
        return String.format(ADDHL_TIPS, v);
    }

    public static String addHj(long q, int b) {
        Long l = getInfo(q).getHjL();
        Long v = percentTo(b, l);
        getInfo(q).addHj(v).apply();
        return String.format(ADDHJ_TIPS, v);
    }
}


