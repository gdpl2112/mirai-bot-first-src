package Project.interfaces.Iservice;


import io.github.kloping.mirai0.Entitys.Group;

public interface ISkillService {

    String initSkill(long qq, Group group, Integer st);

    String useSkill(long qq, Integer st, Number[] allAt, String name, Group group);

    String setName(long qq, Integer st, String str);

    String getIntro(long id, Integer st, String str);

    String forget(long qq,Integer st);
}
