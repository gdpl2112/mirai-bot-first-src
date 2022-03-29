package Project.services.detailServices;

import io.github.kloping.MySpringTool.annotations.Entity;
import io.github.kloping.mirai0.commons.PersonInfo;
import io.github.kloping.mirai0.commons.broadcast.Receiver;

import java.util.HashMap;
import java.util.Map;

/**
 * @author github.kloping
 */
@Entity
public class ChallengeDetailService {
    public static final Map<Long, Long> A2R = new HashMap<>();
    public static final Map<Long, PersonInfo> TEMP_PERSON_INFOS = new HashMap<>();
    public static final Map<Long, Boolean> USED = new HashMap<>();
    public static final Map<Long, Receiver> RECEIVER_MAP = new HashMap<>();
    public static final Map<Long, Long> WILL_GO = new HashMap<>();

    public boolean isTemping(long q) {
        return TEMP_PERSON_INFOS.containsKey(q);
    }

    public PersonInfo getTempInfo(long q) {
        return TEMP_PERSON_INFOS.get(q);
    }

    public PersonInfo setTempInfo(long q, PersonInfo personInfo) {
        return TEMP_PERSON_INFOS.put(q, personInfo);
    }
}
