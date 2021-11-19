package Project.Services.IServer;


import Entitys.Group;
import Project.Bases.BaseIService;

public interface IGameJoinAcService extends BaseIService {

    String[] list();

    String join(long who, String name, Group group);

    Object startAtt(long who,String select);

    String getHelp(long who);

    String HelpTo(long who,long whos);

    String getIntro(long qq);
}
