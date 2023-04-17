package Project.interfaces.Iservice;

import Project.commons.SpGroup;

/**
 * @author github.kloping
 */
public interface IChallengeService {
    /**
     * 人机挑战
     *
     * @param qid
     * @param gid
     * @return
     */
    Object createTrialChallenge(long qid, long gid);

    /**
     * 人机挑战
     *
     * @param qid
     * @param gid
     * @return
     */
    Object createTrial2Challenge(long qid, long gid);

    /**
     * 移动
     *
     * @param id
     * @param str
     * @return
     */
    Object joinChallenge(long id, long str);

    /**
     * 魂兽
     *
     * @param id
     * @param str
     * @param group
     * @return
     */
    Object joinChallenge(long id, String str, SpGroup group);

    /**
     * destroy
     *
     * @param qid
     * @return
     */
    Object destroy(long qid);
}
