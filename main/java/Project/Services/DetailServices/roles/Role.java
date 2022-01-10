package Project.Services.DetailServices.roles;

import Entitys.gameEntitys.PersonInfo;

import java.util.HashMap;
import java.util.Map;

/**
 * @author github-kloping
 * @version 1.0
 */
public interface Role {
    public static enum State {
        /**
         * continue next call
         */
        CONTINUE,
        /**
         * stop
         */
        STOP,
    }

    public static class Response {
        private State state = State.CONTINUE;
        private final long oV;
        private long nowV;
        private Number q1;
        private Number q2;
        private PersonInfo p1;
        private PersonInfo p2;
        private Map<String, Object> args = new HashMap<>();

        public Response(long oV, long nowV, Number q1, Number q2) {
            this.oV = oV;
            this.nowV = nowV;
            this.q1 = q1;
            this.q2 = q2;
        }

        public Response(State state, long oV, long nowV, Number q1, Number q2) {
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

        public State getState() {
            return state;
        }

        public long getoV() {
            return oV;
        }

        public void setNowV(long nowV) {
            this.nowV = nowV;
        }

        public long getNowV() {
            return nowV;
        }

        public Number getQ1() {
            return q1;
        }

        public Number getQ2() {
            return q2;
        }
    }

    /**
     * 作用 or  debuff
     *
     * @param sb
     * @param q1
     * @param q2
     * @param v
     * @param ov
     * @param p1
     * @param args
     * @return
     */
    Response call(StringBuilder sb, Number q1, Number q2, final long ov, long nv, PersonInfo p1, Map<String, Object> args);
}
