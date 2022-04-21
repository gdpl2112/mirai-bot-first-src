package Project.services.detailServices;

import io.github.kloping.extension.ThreeMap;
import io.github.kloping.extension.ThreeMapImpl;

import static io.github.kloping.mirai0.commons.resouce_and_tool.ResourceSet.FinalValue.ATT_PRE_CD;

/**
 * @author github.kloping
 */
public class AttackPrePostManager {

    public static final ThreeMap<Long, Long, Long> ATTACK_PRE_MAP = new ThreeMapImpl<>();

    /**
     * 给 q 加 攻击前摇 cd  t毫秒
     *
     * @param q
     * @param cd
     * @param t
     */
    public static final void append(long q, long cd, long t) {

    }

    public static final long getPreCd(long qid) {
//        return ATTACK_PRE_MAP.containsKey(qid) ? ATTACK_PRE_MAP.get(qid) + ATT_PRE_CD : ATT_PRE_CD;
        return ATT_PRE_CD;
    }
}
