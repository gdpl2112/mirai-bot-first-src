package Project.services.DetailServices;

import Entitys.gameEntitys.GhostObj;
import Entitys.gameEntitys.base.BaseInfo;
import Project.DataBases.GameDataBase;

import static Project.DataBases.GameDataBase.getInfo;
import static Project.services.DetailServices.GameDetailService.beaten;
import static Project.services.DetailServices.GameDetailService.onAtt;
import static Project.services.DetailServices.GameJoinDetailService.attGho;

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
            return getInfo(num).getAtt();
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
            return getInfo(num).getHj();
        } else {
            GhostObj ghostObj = GameJoinDetailService.getGhostObjFrom(who.longValue());
            if (ghostObj == null) {
                return 0;
            }
            return ghostObj.getHj();
        }
    }

    /**
     * 获取基本信息
     *
     * @param who
     * @param num
     * @return
     */
    public static BaseInfo getBaseInfoFromAny(Number who, Number o) {
        BaseInfo baseInfo = null;
        if (o.longValue() > 0) {
            if (!GameDataBase.exist(o)) {
                return null;
            }
            baseInfo = getInfo(o.longValue());
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
            return getInfo(q2).addHj(v.longValue()).apply().getHj();
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
     * @param who
     * @param who2
     * @param v
     */
    public static void attGhostOrMan(StringBuilder sb, Number who, Number who2, Long v) {
        if (who2.longValue() == -2) {
            sb.append(attGho(who.longValue(), v, true, false));
        } else {
            if (!GameDataBase.exist(who2.longValue())) {
                sb.append("该玩家尚未注册");
                return;
            }
            sb.append(beaten(who2, who, v));
            sb.append("\n你对ta造成 " + v + "点伤害");
            if (!sb.toString().contains("$")) {
                sb.append(onAtt(who2, who, v));
            }
        }
    }
}
