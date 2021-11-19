package Project.Services.IServer;


import Entitys.Group;

public interface IShoperService {
    String AllInfo(Group group);

    String UpItem(long id, Integer id1, long aLong, Long aLong1);

    String DownItem(long id, int ids);

    String Buy(long id, Integer valueOf);

    String Intro(long id, Integer valueOf, Group group);
}
