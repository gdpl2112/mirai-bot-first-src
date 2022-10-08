package Project.services.detailServices;

import io.github.kloping.MySpringTool.annotations.Entity;
import io.github.kloping.mirai0.commons.PersonInfo;
import io.github.kloping.mirai0.commons.gameEntitys.challange.Challenges;

import java.util.HashMap;
import java.util.Map;

/**
 * @author github.kloping
 */
@Entity
public class ChallengeDetailService {
    public Challenges challenges = new Challenges();
    public static final Map<Long, PersonInfo> TEMP_PERSON_INFOS = new HashMap<>();

    public synchronized boolean isTemping(long q) {
        return TEMP_PERSON_INFOS.containsKey(q);
    }

    public synchronized PersonInfo getTempInfo(long q) {
        return TEMP_PERSON_INFOS.get(q);
    }

    public synchronized PersonInfo setTempInfo(long q, PersonInfo personInfo) {
        return TEMP_PERSON_INFOS.put(q, personInfo);
    }
}
