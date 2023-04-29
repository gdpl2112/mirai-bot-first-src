package Project.services.player;

import Project.commons.rt.ResourceSet;
import io.github.kloping.MySpringTool.annotations.Entity;
import io.github.kloping.map.MapUtils;

import java.util.*;

/**
 * 前/后 摇控制
 *
 * @author github.kloping
 */
@Entity
public class PlayerBehavioralManager {
    public static final String ATTACK_PRE = "attack_pre";
    public static final String ATTACK_AFTER = "attack_post";
    public static final long PRE_LEAST = 500;
    public static final long POST_LEAST = 1000;
    public Map<Long, List<Growth>> growths = new HashMap<>();

    public long getAttPre(long q) {
        filter(q);
        long default0 = ResourceSet.FinalValue.ATT_PRE_CD;
        if (growths.containsKey(q)) {
            for (Growth growth : growths.get(q)) {
                if (ATTACK_PRE.equals(growth.getType())) {
                    default0 += growth.getValue().longValue();
                }
            }
        }
        return default0 < PRE_LEAST ? PRE_LEAST : default0;
    }

    public long getAttPost(long q) {
        filter(q);
        long default0 = ResourceSet.FinalValue.ATT_CD;
        if (growths.containsKey(q)) {
            for (Growth growth : growths.get(q)) {
                if (ATTACK_AFTER.equals(growth.getType())) {
                    default0 += growth.getValue().longValue();
                }
            }
        }
        return default0 < POST_LEAST ? POST_LEAST : default0;
    }

    public List<Growth> add(Growth growth) {
        MapUtils.append(growths, growth.getQid(), growth, ArrayList.class);
        return growths.get(growth.getQid());
    }

    private synchronized void filter(long q) {
        if (growths.containsKey(q) && growths.get(q) != null) {
            Iterator<Growth> iterator = growths.get(q).iterator();
            while (iterator.hasNext()) {
                Growth growth = iterator.next();
                if (growth.getTime() <= System.currentTimeMillis()) iterator.remove();
            }
            if (growths.get(q).isEmpty()) {
                growths.remove(q);
            }
        }
    }
}
