package Project.services.detailServices.roles;

import io.github.kloping.mirai0.commons.PersonInfo;

import java.util.HashMap;
import java.util.Map;

/**
 * @author github-kloping
 */
public class RoleResponse {
    private final long oV;
    private RoleState state = RoleState.CONTINUE;
    private long nowV;
    private Number q1;
    private Number q2;
    private PersonInfo p1;
    private PersonInfo p2;
    private Map<String, Object> args = new HashMap<>();

    public RoleResponse(long oV, long nowV, Number q1, Number q2) {
        this.oV = oV;
        this.nowV = nowV;
        this.q1 = q1;
        this.q2 = q2;
    }

    public RoleResponse(RoleState state, long oV, long nowV, Number q1, Number q2) {
        this.state = state;
        this.oV = oV;
        this.nowV = nowV;
        this.q1 = q1;
        this.q2 = q2;
    }

    public Map<String, ? extends Object> addArg(String key, Object o) {
        args.put(key, o);
        return args;
    }

    public Map<String, Object> getArgs() {
        return args;
    }

    public PersonInfo getP1(PersonInfo p1) {
        return this.p1 == null ? p1 : this.p1;
    }

    public void setP1(PersonInfo p1) {
        this.p1 = p1;
    }

    public PersonInfo getP2(PersonInfo p2) {
        return this.p2 == null ? p2 : this.p2;
    }

    public void setP2(PersonInfo p2) {
        this.p2 = p2;
    }

    public RoleState getState() {
        return state;
    }

    public long getoV() {
        return oV;
    }

    public long getNowV() {
        return nowV;
    }

    public void setNowV(long nowV) {
        this.nowV = nowV;
    }

    public Number getQ1() {
        return q1;
    }

    public Number getQ2() {
        return q2;
    }
}
