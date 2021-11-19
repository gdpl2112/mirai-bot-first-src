package Project.Services.IServer;


import Entitys.Group;
import Project.Bases.BaseIService;

public interface IGameService extends BaseIService {
    String xl(Long who);

    String info(Long who);

    String upUp(Long who);

    String upTrue(Long who);

    String openEyeWh(Long who);

    String[] getBags(Long who);

    String[] getBags(String who);

    String BuyGold(Long who, long num);

    String showHh(Long who);

    String parseHh(Long who, int id);

    String AttWhos(Long who, Long whos, Group group);

    String getScoreFromGold(Long who, long num);

    String makeSname(Long who,String name,Group group);

    String returnA(long id);

    String xl2(long qq);

    String Fusion(Long q1,Long q2,Group group);
}
