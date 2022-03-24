package Project.interfaces.Iservice;

/**
 * @author github.kloping
 */
public interface IChallengeService {
    /**
     * 人机挑战
     * @param qid
     * @param gid
     * @return
     */
    Object startWithBot(long qid, long gid);

    /**
     * 移动
     * @param id
     * @param str
     * @return
     */
    Object moveOnChallenge(long id, String str);
}
