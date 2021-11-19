package Project.Services.IServer;


import Entitys.Group;
import Project.Bases.BaseIService;

public interface IScoreService extends BaseIService {

    String selectInfo(Long who);

    String getScore(Long who, long num);

    String putScore(Long who, long num);

    String getScoreTo(Long who, Long whos, long num);

    String Robbery(Long who, Long whos);

    String Sign(Long who);

    String WorkLong(Long who);

    String todayList(Group group);
}
