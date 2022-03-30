package Project.services.detailServices.roles;

import Project.broadcast.game.GhostLostBroadcast;
import Project.dataBases.GameDataBase;
import Project.dataBases.skill.SkillDataBase;
import Project.services.detailServices.GameJoinDetailService;
import Project.services.detailServices.GameSkillDetailService;
import Project.services.detailServices.ac.entity.Ghost702;
import io.github.kloping.mirai0.commons.GhostObj;
import io.github.kloping.mirai0.commons.PersonInfo;
import io.github.kloping.mirai0.commons.gameEntitys.SoulAttribute;
import io.github.kloping.mirai0.commons.gameEntitys.base.BaseInfo;

import java.util.Map;

import static Project.dataBases.GameDataBase.getInfo;
import static Project.dataBases.GameDataBase.putPerson;
import static Project.dataBases.skill.SkillDataBase.*;
import static Project.services.detailServices.GameDetailService.gameBoneService;
import static Project.services.detailServices.GameDetailService.proZ;
import static Project.services.detailServices.GameDetailServiceUtils.getBaseInfoFromAny;
import static Project.services.detailServices.roles.RoleState.STOP;
import static io.github.kloping.mirai0.commons.resouce_and_tool.ResourceSet.FinalString.NEWLINE;

/**
 * @author github-kloping
 * @version 1.0
 */
public class BeatenRoles {
    public static final String THIS_DANGER_OVER_FLAG = "$";
    public static final Role TAG_DAMAGE_REDUCTION = (sb, q1, q2, ov, nv, p1, args) -> {
        if (p1.containsTag(SkillDataBase.TAG_DAMAGE_REDUCTION)) {
            int r = p1.getTagValue(SkillDataBase.TAG_DAMAGE_REDUCTION).intValue();
            int lose = 100 - r;
            nv = percentTo(lose, nv);
            RoleResponse response = new RoleResponse(ov, nv, q1, q2);
            response.setNowV(nv);
            sb.append(NEWLINE);
            sb.append("伤害免疫").append(r).append("%");
            return response;
        }
        return null;
    };
    public static final Role HG_HF = (sb, q1, q2, ov, nv, p1, args) -> {
        SoulAttribute soulAttribute = gameBoneService.getSoulAttribute(q1.longValue());
        if (proZ(soulAttribute.getHpChance())) {
            float fn;
            if (ov > 100) {
                fn = percentTo(soulAttribute.getHpEffect(), ov);
            } else {
                fn = soulAttribute.getHpEffect();
            }
            p1.addHp((long) fn);
            sb.append(NEWLINE);
            sb.append("得益于魂骨你恢复了").append(fn).append("生命");
        }
        return null;
    };
    /**
     * 以下免疫到此
     */
    public static final Role TAG_XYS = (sb, q1, q2, ov, nv, p1, args) -> {
        if (p1.containsTag(TAG_XUAN_YU_S)) {
            putPerson(p1.eddTag(TAG_XUAN_YU_S, 1));
            sb.append(NEWLINE).append(THIS_DANGER_OVER_FLAG).append("免疫此次伤害");
            return new RoleResponse(STOP, ov, 0, q1, q2);
        }
        return null;
    };

    public static final Role TAG_MS = (sb, q1, q2, ov, nv, p1, args) -> {
        if (p1.containsTag(SkillDataBase.TAG_MS)) {
            if (p1.getHp() - ov <= 0) {
                putPerson(p1.eddTag(SkillDataBase.TAG_MS));
                sb.append(NEWLINE).append(THIS_DANGER_OVER_FLAG).append("被攻击者,由于使用了免死类魂技,免疫此次 死亡");
                return new RoleResponse(STOP, ov, 0, q1, q2);
            }
        }
        return null;
    };
    public static final Role TAG_WD = (sb, q1, q2, ov, nv, p1, args) -> {
        if (p1.containsTag(SkillDataBase.TAG_WD)) {
            sb.append(NEWLINE).append(THIS_DANGER_OVER_FLAG).append("无敌效果,攻击无效");
            return new RoleResponse(STOP, ov, 0, q1, q2);
        } else {
            return null;
        }
    };
    public static final Role XG_VERTIGO = (sb, q1, q2, ov, nv, p1, args) -> {
        BaseInfo p2 = getBaseInfoFromAny(q1, q2);
        if (p2.isVertigo()) {
            sb.append(NEWLINE).append("攻击者处于眩晕状态,攻击无效");
            return new RoleResponse(STOP, ov, 0, q1, q2);
        }
        return null;
    };
    public static final Role TAG_FJ = (sb, q1, q2, ov, nv, p1, args) -> {
        if (p1.containsTag(SkillDataBase.TAG_FJ)) {
            sb.append(NEWLINE);
            Integer p = p1.getTagValue(SkillDataBase.TAG_FJ).intValue();
            long v1 = percentTo(p, ov);
            v1 = v1 < 1 ? 1 : v1;
            if (q2.longValue() > 0) {
                getInfo(q2).addHp(-v1).apply();
                sb.append("被攻击者,由于带有反甲,攻击者受到 ").append(v1).append("点伤害");
            } else {
                GhostObj ghostObj = GameJoinDetailService.getGhostObjFrom(q1.longValue());
                sb.append("您带有反甲,").append(ghostObj.getName()).append("受到").append(v1).append("点伤害");
                sb.append(GameJoinDetailService.attGho(q1.longValue(), v1, false, false, GhostLostBroadcast.KillType.SKILL_ATT));
            }
        }
        return null;
    };
    private static final String CANT_HIDE_ARG_KEY = "cant hide";
    /**
     * 特殊闪避
     */
    public static final Role HG_HIDE = (sb, q1, q2, ov, nv, p1, args) -> {
        if (args.containsKey(CANT_HIDE_ARG_KEY) && Boolean.parseBoolean(args.get(CANT_HIDE_ARG_KEY).toString()) == true) {
            if (proZ(gameBoneService.getSoulAttribute(q1.longValue()).getHideChance())) {
                sb.append(NEWLINE).append(THIS_DANGER_OVER_FLAG).append("得益于魂骨你闪避了此次伤害");
                return new RoleResponse(STOP, ov, 0, q1, q2);
            }
        }
        return null;
    };
    public static final Role TAG_CANT_HIDE = (sb, q1, q2, ov, nv, p1, args) -> {
        RoleResponse response = new RoleResponse(ov, nv, q1, q2);
        if (p1.containsTag(SkillDataBase.TAG_CANT_HIDE)) {
            response.addArg(CANT_HIDE_ARG_KEY, true);
        } else {
            response.addArg(CANT_HIDE_ARG_KEY, false);
        }
        return response;
    };
    private static final String TRUE_HIT_ARG_KEY = "true att";
    public static final Role TAG_SHIELD_ROLE = (sb, q1, q2, ov, nv, p1, args) -> {
        if (p1.containsTag(SkillDataBase.TAG_SHIELD)) {
            if (!Boolean.parseBoolean(args.get(TRUE_HIT_ARG_KEY).toString()) == true) {
                RoleResponse response = new RoleResponse(ov, nv, q1, q2);
                sb.append(NEWLINE);
                long v = GameSkillDetailService.getTagValue(q1, SkillDataBase.TAG_SHIELD).longValue();
                if (v >= nv) {
                    Long i = v - nv;
                    p1.eddTag(SkillDataBase.TAG_SHIELD, v);
                    p1.addTag(SkillDataBase.TAG_SHIELD, i);
                    sb.append("此次伤害全部护盾抵挡");
                    sb.append("护盾剩余:").append(i);
                    response.setNowV(0);
                } else {
                    p1.eddTag(SkillDataBase.TAG_SHIELD, v);
                    response.setNowV(nv - v);
                    sb.append("部分伤害护盾抵挡,伤害剩余:").append(response.getNowV());
                }
                return response;
            }
        }
        return null;
    };
    public static final Role TAG_TURE = (sb, q1, q2, ov, nv, p1, args) -> {
        RoleResponse response = new RoleResponse(ov, nv, q1, q2);
        if (getInfo(q2).containsTag(TAG_TRUE)) {
            sb.append(NEWLINE).append("此次真实伤害");
            response.addArg(TRUE_HIT_ARG_KEY, true);
        } else {
            response.addArg(TRUE_HIT_ARG_KEY, false);
        }
        return response;
    };


    public static final Role[] RS = new Role[]{
            XG_VERTIGO, TAG_WD, TAG_MS, TAG_XYS, TAG_CANT_HIDE,
            HG_HIDE, TAG_TURE, TAG_SHIELD_ROLE, HG_HF, TAG_FJ, TAG_DAMAGE_REDUCTION
    };


    public static final Role TAG_XX = new Role() {
        @Override
        public RoleResponse call(StringBuilder sb, Number q1, Number q2, long ov, long nv, PersonInfo p1, Map<String, Object> args) {
            if (p1.containsTag(SkillDataBase.TAG_XX)) {
                int p = p1.getTagValue(SkillDataBase.TAG_XX).intValue();
                long v1 = percentTo(p, ov);
                if (v1 < 1) {
                    v1 = 1;
                }
                p1.addHp(v1);
                sb.append("\n攻击者,由于吸血技能恢复了").append(v1).append("的生命值");
            }
            return null;
        }
    };

    public static final Role TAG_SHE_ROLE = new Role() {
        @Override
        public RoleResponse call(StringBuilder sb, Number q1, Number q2, long ov, long nv, PersonInfo p1, Map<String, Object> args) {
            if (q2.longValue() > 0) {
                PersonInfo info1 = GameDataBase.getInfo(q2);
                if (p1.containsTag(TAG_SHE) && p1.containsTag(TAG_SHIELD)) {
                    int b = p1.getTagValue(TAG_SHE).intValue();
                    long v2 = percentTo(b, ov);
                    putPerson(GameDataBase.getInfo(q2.longValue()).addHp(-v2));
                    sb.append(NEWLINE);
                    sb.append("\n对有护盾的敌人额外造成").append(v2).append("伤害");
                }
            } else {
                GhostObj ghostObj = GameJoinDetailService.getGhostObjFrom(q1.longValue());
                if (ghostObj instanceof Ghost702) {
                    Ghost702 ghost702 = (Ghost702) ghostObj;
                    if (ghost702.getShield() >= 0) {
                        int b = p1.getTagValue(TAG_SHE).intValue();
                        long v2 = percentTo(b, ov);
                        GameJoinDetailService.attGho(q1.longValue(), v2, false, false, GhostLostBroadcast.KillType.SKILL_ATT);
                        sb.append(NEWLINE);
                        sb.append("\n对有护盾的敌人额外造成").append(v2).append("伤害");
                    }
                }
            }
            return null;
        }
    };

    public static final Role[] ATT_RS = new Role[]{
            TAG_XX, TAG_SHE_ROLE
    };
}
