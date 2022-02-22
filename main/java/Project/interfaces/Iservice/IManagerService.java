package Project.interfaces.Iservice;


import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.contact.Member;

/**
 * @author github-kloping
 */
public interface IManagerService   {
    /**
     * 添加管理
     *
     * @param father
     * @return
     */
    String addFather(long father,long who);

    /**
     * 移除管理
     *
     * @param father
     * @return
     */
    String removeFather(long father,long who);

    String notSpeak(Member who, String what, Group group);

    String backMess(Group group, long whos, long g, int... ns);
}

