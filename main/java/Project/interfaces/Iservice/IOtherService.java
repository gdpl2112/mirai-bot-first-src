package Project.interfaces.Iservice;


import Project.commons.SpGroup;

public interface IOtherService {

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
    String talk(String str);

    /**
     * 2传话
     */
    String trans2(String str, SpGroup group, Long qq);

    /**
     * 传话
     */
    String trans(String str, SpGroup group, Long qq);

}
