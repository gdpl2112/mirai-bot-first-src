package Project.services.Iservice;


import io.github.kloping.mirai0.Entitys.Group;

public interface IShoperService {
    String allInfo(Group group);

    String upItem(long id, Integer id1, long aLong, Long aLong1);

    String downItem(long id, int ids);

    String buy(long id, Integer valueOf);

    String intro(long id, Integer valueOf, Group group);
}
