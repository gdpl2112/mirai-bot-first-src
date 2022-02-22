package Project.interfaces.Iservice;


public interface IGameWeaService  {
    String useAq(String what, Long who);

    String aqBgs(Long who);

    String makeAq(Long who, int id);

    String aqList();

    String aqMeun();
}
