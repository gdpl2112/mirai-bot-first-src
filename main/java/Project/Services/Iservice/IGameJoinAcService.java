package Project.Services.Iservice;


import Entitys.Group;

public interface IGameJoinAcService  {

    String[] list();

    String join(long who, String name, Group group);

    Object startAtt(long who,String select);

    String getHelp(long who);

    String helpTo(long who, long whos);

    String getIntro(long qq);
}
