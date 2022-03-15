package Project.interfaces.Iservice;

import io.github.kloping.mirai0.Entitys.Group;
import io.github.kloping.mirai0.Entitys.gameEntitys.Zong;

public interface IZongMenService {
    String create(String name, Long who, Group group);

    String zongInfo(Long qq, Group group);

    String zongInfo(Zong zong, Group group);

    String zongInfo(Integer id, Group group);

    String List(Group group);

    String setIcon(String imgUrl, Long who, Group group);

    String Invite(long who, long qq, Group group);

    String listPer(Long who, Group group);

    String setName(String name, long id, Group group);

    String Cob(Long qq);

    String help(long id, long who);

    String setElder(long id, long who);

    String cancelElder(long id, long who);

    String UpUp(long id, Group group);

    String quite(long id);

    String QuiteOne(long id, long who);
}
