package Project.interfaces.Iservice;


import io.github.kloping.mirai0.commons.SpGroup;

/**
 * @author github-kloping
 */
public interface IGameJoinAcService {

    String[] list();

    String join(long who, String name, SpGroup group);

    Object startSelect(long who, String select);

    String getHelp(long who);

    String helpTo(long who, long whos);

    String getIntro(long qq);
}
