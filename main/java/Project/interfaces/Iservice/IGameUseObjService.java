package Project.interfaces.Iservice;


import java.lang.reflect.InvocationTargetException;

public interface IGameUseObjService {

    String getIntro(int id);

    String useObj(Long who, int id) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException;

    String useObj(Long who, int id, int num) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException;

    String buyObj(Long who, int id);

    String buyObj(Long who, int id, Integer num);

    String sleObj(Long who, int id);

    String sleObj(Long who, int id, Integer num);

    String objTo(Long who, int id, Long whos);

    String objTo(Long who, int id, Long whos, Integer num);
}
