package Project.interfaces.Iservice;


import io.github.kloping.mirai0.commons.Group;

import java.lang.reflect.InvocationTargetException;

public interface IGameUseObjService {

    String getIntro(int id);

    String useObj(Long who, int id) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException;

    String useObj(Long who, int id, int num) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException;

    String buyObj(Long who, int id);

    String buyObj(Long who, int id, Integer num);

    String sleObj(Long who, int id);

    String sleObj(Long who, int id, Integer num, Group group);

    String objTo(Long who, int id, Long whos);

    String objTo(Long who, int id, Long whos, Integer num,Group group);
}
