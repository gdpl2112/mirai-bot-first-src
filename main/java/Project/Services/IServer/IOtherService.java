package Project.Services.IServer;


import Entitys.Group;
import Project.Bases.BaseIService;

public interface IOtherService extends BaseIService {

    /**
     * 猜拳
     *
     * @param who
     * @param what
     * @return
     */
    String mora(Long who, String what);

    /**
     * 聊天引擎
     *
     * @param str
     * @return
     */
    String Talk(String str);

    /**
     * 2传话
     */
    String trans2(String str, Group group, Long qq);
    /**
     * 传话
     */
    String trans(String str, Group group, Long qq);

}
