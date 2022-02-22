package Project.interfaces.Iservice;


import io.github.kloping.mirai0.Entitys.Group;

/**
 * @author github-kloping
 */
public interface IGameJoinAcService  {

    String[] list();

    String join(long who, String name, Group group);

    Object startAtt(long who,String select);

    String getHelp(long who);

    String helpTo(long who, long whos);

    String getIntro(long qq);
}
