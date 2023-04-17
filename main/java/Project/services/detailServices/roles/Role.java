package Project.services.detailServices.roles;

import Project.commons.gameEntitys.base.BaseInfo;

import java.util.Map;

/**
 * @author github-kloping
 * @version 1.0
 */
public interface Role {
    /**
     * 作用 or de buff
     *
     * @param sb   tips
     * @param q1   主动
     * @param q2   被动
     * @param ov   原有伤害
     * @param nv   现有伤害
     * @param type 伤害类型
     * @param p1   p1
     * @param args args
     * @return
     */
    RoleResponse call(StringBuilder sb, Number q1, Number q2, final long ov, long nv,
                      DamageType type,
                      BaseInfo p1, Map<String, Object> args);
}
