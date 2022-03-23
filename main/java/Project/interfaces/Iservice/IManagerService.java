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
     *
     * @param father
     * @param who
     * @param perm
     * @return
     */
    String addFather(long father,long who,String perm);

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

