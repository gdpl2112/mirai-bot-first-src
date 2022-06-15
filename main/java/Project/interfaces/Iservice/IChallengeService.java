package Project.interfaces.Iservice;

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
     * 移动
     *
     * @param id
     * @param str
     * @return
     */
    Object joinChallenge(long id, long str);

    /**
     * destroy
     *
     * @param qid
     * @return
     */
    Object destroy(long qid);
}
