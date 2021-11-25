package Project.Services.Iservice;


import Entitys.Group;

public interface ISkillService {

    String InitSkill(long qq, Group group, Integer st);

    String UseSkill(long qq, Integer st, Number[] allAt,String name,Group group);

    String setName(long qq, Integer st, String str);

    String getIntro(long id, Integer st, String str);

    String forget(long qq,Integer st);
}
