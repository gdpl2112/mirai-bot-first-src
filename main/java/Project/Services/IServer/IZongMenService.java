package Project.Services.IServer;

import Entitys.Group;
import Entitys.gameEntitys.Zong;

public interface IZongMenService {
    String create(String name, Long who, Group group);

    String ZongInfo(Long qq, Group group);

    String ZongInfo(Zong zong, Group group);

    String ZongInfo(Integer id, Group group);

    String List(Group group);

    String setIcon(String imgUrl, Long who, Group group);

    String Invite(long who, long qq, Group group);

    String ListPer(Long who, Group group);

    String setName(String name, long id, Group group);

    String Cob(Long qq);

    String help(long id, long who);

    String setElder(long id, long who);

    String cancelElder(long id, long who);

    String UpUp(long id, Group group);

    String quite(long id);

    String QuiteOne(long id, long who);
}
