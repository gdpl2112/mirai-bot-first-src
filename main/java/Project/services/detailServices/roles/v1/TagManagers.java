package Project.services.detailServices.roles.v1;

import java.util.HashMap;
import java.util.Map;

/**
 * @author github.kloping
 */
public class TagManagers {
    public static Map<Long, TagManager> managerMap = new HashMap<>();

    public static TagManager getTagManager(long qid) {
        if (managerMap.containsKey(qid)) {
            return managerMap.getOrDefault(qid, new TagManager(qid));
        } else {
            managerMap.put(qid, new TagManager(qid));
            return getTagManager(qid);
        }
    }
}
