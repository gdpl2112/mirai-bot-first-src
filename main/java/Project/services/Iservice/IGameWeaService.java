package Project.services.Iservice;


public interface IGameWeaService  {
    String UseAq(String what, Long who);

    String AqBgs(Long who);

    String makeAq(Long who, int id);

    String AqList();

    String AqMeun();
}
