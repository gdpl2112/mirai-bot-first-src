package Project.services.detailServices;

import Project.commons.gameEntitys.challange.Challenges;
import io.github.kloping.MySpringTool.annotations.Entity;
import Project.aSpring.dao.PersonInfo;

import java.util.HashMap;
import java.util.Map;

/**
 * @author github.kloping
 */
@Entity
public class ChallengeDetailService {
    public static final Map<Long, PersonInfo> TEMP_PERSON_INFOS = new HashMap<>();
    public Challenges challenges = new Challenges();

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
