package Project.interfaces.Iservice;

import io.github.kloping.mirai0.commons.SpGroup;
import io.github.kloping.mirai0.commons.Zong;

public interface IZongMenService {
    String create(String name, Long who, SpGroup group);

    String zongInfo(Long qq, SpGroup group);

    String zongInfo(Zong zong, SpGroup group);

    String zongInfo(Integer id, SpGroup group);

    String list(SpGroup group);

    String setIcon(String imgUrl, Long who, SpGroup group);

    String invite(long who, long qq, SpGroup group);

    String listPer(Long who, SpGroup group);

    String setName(String name, long id, SpGroup group);

    String cob(Long qq);

    String help(long id, long who);

    String setElder(long id, long who);

    String cancelElder(long id, long who);

    String upUp(long id, SpGroup group);

    String quite(long id);

    String quiteOne(long id, long who);

    String addMax(long id);
}
