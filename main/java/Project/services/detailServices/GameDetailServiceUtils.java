package Project.services.detailServices;

import Project.broadcast.game.GhostLostBroadcast;
import Project.broadcast.game.PlayerLostBroadcast;
import Project.dataBases.GameDataBase;
import Project.services.detailServices.ac.GameJoinDetailService;
import Project.services.detailServices.roles.DamageType;
import io.github.kloping.mirai0.commons.GhostObj;
import Project.commons.gameEntitys.base.BaseInfo;

import static Project.controllers.auto.ControllerSource.challengeDetailService;
import static Project.services.detailServices.GameDetailService.beaten;
import static Project.services.detailServices.GameDetailService.onAtt;
import static Project.services.detailServices.ac.GameJoinDetailService.attGho;
import static Project.commons.resouce_and_tool.ResourceSet.FinalFormat.ATTACK_TIPS0;
import static Project.commons.resouce_and_tool.ResourceSet.FinalString.*;

/**
 * @author github-kloping
 * @version 1.0
 */
public class GameDetailServiceUtils {

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
            return GameDataBase.getInfo(num).att();
        } else {
            GhostObj ghostObj = GameJoinDetailService.getGhostObjFrom(who.longValue());
            if (ghostObj == null) {
                return 0;
            }
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
        if (num.longValue() > 0) {
            if (!GameDataBase.exist(num.longValue())) {
                return 0;
            }
            return GameDataBase.getInfo(num).getHj();
        } else {
            GhostObj ghostObj = GameJoinDetailService.getGhostObjFrom(who.longValue());
            if (ghostObj == null) {
                return 0;
            }
            return ghostObj.getHj();
        }
    }

    /**
     * who 获取 o  获取基本信息
     *
     * @param who
     * @param o   playerId
     * @return
     */
    public static BaseInfo getBaseInfoFromAny(Number who, Number o) {
        BaseInfo baseInfo = null;
        if (o.longValue() > 0) {
            if (!GameDataBase.exist(o.longValue())) {
                return null;
            }
            baseInfo = GameDataBase.getInfo(o.longValue());
        } else {
            GhostObj ghostObj = GameJoinDetailService.getGhostObjFrom(who.longValue());
            if (ghostObj == null) {
                return null;
            }
            baseInfo = ghostObj;
        }
        return baseInfo;
    }

    /**
     * 从 play or ghost 改变精神力
     *
     * @param who
     * @param q2
     * @param v
     * @return
     */
    public static long addHjToAny(Number who, Number q2, Number v) {
        if (q2.longValue() > 0) {
            if (!GameDataBase.exist(q2.longValue())) {
                return 0;
            }
            return GameDataBase.getInfo(q2).addHj(v.longValue()).apply().getHj();
        } else {
            GhostObj ghostObj = GameJoinDetailService.getGhostObjFrom(who.longValue());
            if (ghostObj == null) {
                return 0;
            }
            ghostObj.setHj(ghostObj.getHj() + v.longValue());
            return ghostObj.apply().getHj();
        }
    }

    /**
     * 对某个造成伤害
     *
     * @param sb
     * @param who  攻击者
     * @param who2 被攻击者
     * @param v
     */
    public static void attGhostOrMan(StringBuilder sb, Number who, Number who2, Long v) {
        attGhostOrMan(sb, who, who2, v, true);
    }

    /**
     * @param sb
     * @param who
     * @param who2
     * @param v
     * @param k    show
     */
    public static void attGhostOrMan(StringBuilder sb, Number who, Number who2, Long v, Boolean k) {
        attGhostOrMan(sb, who, who2, v, k, DamageType.AD);
    }

    /**
     * att
     *
     * @param sb
     * @param who
     * @param who2
     * @param v
     * @param k
     * @param type
     */
    public static void attGhostOrMan(StringBuilder sb, Number who, Number who2, Long v, Boolean k, DamageType type) {
        if (who2.longValue() == -2) {
            sb.append(attGho(who.longValue(), v,type, k,  GhostLostBroadcast.KillType.SKILL_ATT));
        } else {
            if (!GameDataBase.exist(who2.longValue())) {
                sb.append(PLAYER_NOT_REGISTERED);
                return;
            }
            sb.append(beaten(who2, who, v, type).trim());
            sb.append(NEWLINE).append(String.format(ATTACK_TIPS0, v));
            if (!sb.toString().contains(JUMP_STR_0)) {
                sb.append(onAtt(who2, who, v, type));
            }
        }
    }

    private static PlayerLostBroadcast.PlayerLostReceiver.LostType getLostType(long q1) {
        return challengeDetailService.isTemping(q1) ?
                PlayerLostBroadcast.PlayerLostReceiver.LostType.fake :
                PlayerLostBroadcast.PlayerLostReceiver.LostType.att;
    }
}
