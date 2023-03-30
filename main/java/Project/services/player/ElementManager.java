package Project.services.player;

import io.github.kloping.MySpringTool.annotations.Entity;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author github.kloping
 */
@Entity
public class ElementManager {
    public static final Map<Long, Map<String, Integer>> ES_MAP = new LinkedHashMap<>();

    public static boolean containsEs(Long qid, String es) {
        if (ES_MAP.containsKey(qid.longValue())) {
            Map<String, Integer> esMap = ES_MAP.get(qid);
            if (esMap.containsKey(es)) {
                return esMap.get(es) > 0;
            } else return false;
        } else return false;
    }

    public static Integer getEs(Long qid, String es) {
        if (containsEs(qid, es)) return 0;
        else return ES_MAP.get(qid).get(es);
    }

    public static Integer appendEs(Long qid, String es, Integer e) {
        Integer e0 = getEs(qid, es);
        e0 = e0 + e;
        Map<String, Integer> esMap = ES_MAP.get(qid);
        if (esMap == null) {
            esMap = new LinkedHashMap<>();
        }
        esMap.put(es, e0);
        ES_MAP.put(qid, esMap);
        return e0;
    }

    public static Integer removeEs(Long qid, String es, Integer e) {
        Integer e0 = getEs(qid, es);
        e0 = e0 - e;
        Map<String, Integer> esMap = ES_MAP.get(qid);
        if (esMap == null) {
            esMap = new LinkedHashMap<>();
        }
        if (e0 > 0) {
            esMap.put(es, e0);
        } else {
            esMap.remove(es);
        }
        ES_MAP.put(qid, esMap);
        return e0;
    }

    public static boolean removeEs(Long qid, String es) {
        Map<String, Integer> esMap = ES_MAP.get(qid);
        if (esMap == null) {
            return false;
        } else {
            return esMap.remove(es) != null;
        }
    }
}
