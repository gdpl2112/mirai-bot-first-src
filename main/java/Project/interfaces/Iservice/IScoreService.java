package Project.interfaces.Iservice;


import io.github.kloping.mirai0.commons.SpGroup;

/**
 * @author github-kloping
 */
public interface IScoreService {

    String selectInfo(Long who);

    String getScore(Long who, long num);

    String putScore(Long who, long num);

    String getScoreTo(Long who, Long whos, long num);

    String robbery(Long who, Long whos);

    String sign(Long who);

    String workLong(Long who);

    String todayList(SpGroup group);

    String earnings(long id);
}
