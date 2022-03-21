package Project.services.detailServices.roles;

import io.github.kloping.mirai0.commons.PersonInfo;

import java.util.Map;

/**
 * @author github-kloping
 * @version 1.0
 */
public interface Role {
    /**
     * 作用 or de buff
     *
     * @param sb
     * @param q1
     * @param q2
     * @param ov
     * @param nv
     * @param p1
     * @param args
     * @return
     */
    RoleResponse call(StringBuilder sb, Number q1, Number q2, final long ov, long nv, PersonInfo p1, Map<String, Object> args);
}
