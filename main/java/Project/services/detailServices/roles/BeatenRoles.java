package Project.services.detailServices.roles;

import Project.broadcast.game.GhostLostBroadcast;
import Project.dataBases.skill.SkillDataBase;
import Project.services.detailServices.GameJoinDetailService;
import Project.services.detailServices.GameSkillDetailService;
import io.github.kloping.mirai0.Entitys.gameEntitys.GhostObj;
import io.github.kloping.mirai0.Entitys.gameEntitys.SoulAttribute;
import io.github.kloping.mirai0.Entitys.gameEntitys.base.BaseInfo;

import static Project.ResourceSet.FinalString.NEWLINE;
import static Project.ResourceSet.FinalString.SPLIT_LINE_0;
import static Project.dataBases.GameDataBase.getInfo;
import static Project.dataBases.GameDataBase.putPerson;
import static Project.dataBases.skill.SkillDataBase.*;
import static Project.services.detailServices.GameDetailService.gameBoneService;
import static Project.services.detailServices.GameDetailService.proZ;
import static Project.services.detailServices.GameDetailServiceUtils.getBaseInfoFromAny;
import static Project.services.detailServices.roles.RoleState.STOP;

/**
 * @author github-kloping
 * @version 1.0
 */
public class BeatenRoles {
    private static final String CANT_HIDE_ARG_KEY = "cant hide";
    private static final String TRUE_HIT_ARG_KEY = "true att";
    public static final String THIS_DANGER_OVER_FLAG = "$";

    public static final Role TAG_FJ = (sb, q1, q2, ov, nv, p1, args) -> {
        if (p1.containsTag(SkillDataBase.TAG_FJ)) {
            sb.append(NEWLINE).append(SPLIT_LINE_0);
            Integer p = p1.getTagValue(SkillDataBase.TAG_FJ).intValue();
            long v1 = percentTo(p, ov);
            v1 = v1 < 1 ? 1 : v1;
            if (q2.longValue() > 0) {
                getInfo(q2).addHp(-v1).apply();
                sb.append("\n被攻击者,由于带有反甲,攻击者受到 ").append(v1).append("点伤害");
            } else {
                GhostObj ghostObj = GameJoinDetailService.getGhostObjFrom(q1.longValue());
                sb.append("\n您带有反甲,").append(ghostObj.getName()).append("受到").append(v1).append("点伤害");
                sb.append(GameJoinDetailService.attGho(q1.longValue(), v1, false, false, GhostLostBroadcast.KillType.SKILL_ATT));
            }
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
            sb.append(NEWLINE).append(SPLIT_LINE_0);
            sb.append("\n得益于魂骨你恢复了").append(fn).append("生命");
        }
        return null;
    };

    public static final Role TAG_SHIELD = (sb, q1, q2, ov, nv, p1, args) -> {
        if (p1.containsTag(SkillDataBase.TAG_SHIELD)) {
            if (!Boolean.parseBoolean(args.get(TRUE_HIT_ARG_KEY).toString()) == true) {
                RoleResponse response = new RoleResponse(ov, nv, q1, q2);
                sb.append(NEWLINE).append(SPLIT_LINE_0);
                long v = GameSkillDetailService.getTagValue(q1, SkillDataBase.TAG_SHIELD).longValue();
                if (v >= nv) {
                    p1.eddTag(SkillDataBase.TAG_SHIELD, v);
                    p1.addTag(SkillDataBase.TAG_SHIELD, v - nv);
                    sb.append("\n此次伤害全部护盾抵挡");
                    response.setNowV(0);
                } else {
                    p1.eddTag(SkillDataBase.TAG_SHIELD, v);
                    response.setNowV(nv - v);
                    sb.append("\n部分伤害护盾抵挡,伤害剩余:").append(response.getNowV()).append(SPLIT_LINE_0);
                }
                sb.append("\n护盾剩余:").append(v);
                return response;
            }
        }
        return null;
    };
    public static final Role TAG_TURE = (sb, q1, q2, ov, nv, p1, args) -> {
        RoleResponse response = new RoleResponse(ov, nv, q1, q2);
        if (p1.containsTag(TAG_TRUE)) {
            sb.append(NEWLINE).append(SPLIT_LINE_0).append("\n此次真实伤害");
            response.addArg(TRUE_HIT_ARG_KEY, true);
        } else {
            response.addArg(TRUE_HIT_ARG_KEY, false);
        }
        return response;
    };

    public static final Role HG_HIDE = (sb, q1, q2, ov, nv, p1, args) -> {
        if (args.containsKey(CANT_HIDE_ARG_KEY) && Boolean.parseBoolean(args.get(CANT_HIDE_ARG_KEY).toString()) == true) {
            if (proZ(gameBoneService.getSoulAttribute(q1.longValue()).getHideChance())) {
                sb.append(NEWLINE).append(SPLIT_LINE_0).append(THIS_DANGER_OVER_FLAG).append("\n得益于魂骨你闪避了此次伤害");
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

    public static final Role TAG_XYS = (sb, q1, q2, ov, nv, p1, args) -> {
        if (p1.containsTag(TAG_XUAN_YU_S)) {
            putPerson(p1.eddTag(TAG_XUAN_YU_S, 1));
            sb.append(NEWLINE).append(SPLIT_LINE_0).append(THIS_DANGER_OVER_FLAG).append("\n免疫此次伤害");
            return new RoleResponse(STOP, ov, 0, q1, q2);
        }
        return null;
    };

    public static final Role TAG_MS = (sb, q1, q2, ov, nv, p1, args) -> {
        if (p1.containsTag(SkillDataBase.TAG_MS)) {
            if (p1.getHp() - ov <= 0) {
                putPerson(p1.eddTag(SkillDataBase.TAG_MS, 0));
                sb.append(NEWLINE).append(SPLIT_LINE_0).append(THIS_DANGER_OVER_FLAG).append("\n被攻击者,由于使用了免死类魂技,免疫此次 死亡");
                return new RoleResponse(STOP, ov, 0, q1, q2);
            }
        }
        return null;
    };

    public static final Role TAG_WD = (sb, q1, q2, ov, nv, p1, args) -> {
        if (p1.containsTag(SkillDataBase.TAG_WD)) {
            sb.append(NEWLINE).append(SPLIT_LINE_0).append(THIS_DANGER_OVER_FLAG).append("\n无敌效果,攻击无效");
            return new RoleResponse(STOP, ov, 0, q1, q2);
        } else {
            return null;
        }
    };

    public static final Role XG_VERTIGO = (sb, q1, q2, ov, nv, p1, args) -> {
        BaseInfo p2 = getBaseInfoFromAny(q1, q2);
        if (p2.isVertigo()) {
            sb.append(NEWLINE).append(SPLIT_LINE_0).append("\n攻击者处于眩晕状态,攻击无效");
            return new RoleResponse(STOP, ov, 0, q1, q2);
        }
        return null;
    };

    public static final Role[] RS = new Role[]{
            XG_VERTIGO, TAG_WD, TAG_MS, TAG_XYS, TAG_CANT_HIDE,
            HG_HIDE, TAG_TURE, TAG_SHIELD, HG_HF, TAG_FJ
    };
}
