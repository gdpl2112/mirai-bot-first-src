package Project.services.Iservice;



import java.lang.reflect.InvocationTargetException;

public interface IGameUseObjService   {

    String getIntro(int id);

    String useObj(Long who, int id) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException;

    String useObj(Long who, int id, int num) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException;

    String BuyObj(Long who, int id);

    String BuyObj(Long who, int id, Integer num);

    String SleObj(Long who, int id);

    String SleObj(Long who, int id, Integer num);

    String ObjTo(Long who, int id, Long whos);

    String ObjTo(Long who, int id, Long whos, Integer num);
}
