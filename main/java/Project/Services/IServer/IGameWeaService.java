package Project.Services.IServer;


import Project.Bases.BaseIService;

public interface IGameWeaService extends BaseIService {
    String UseAq(String what, Long who);

    String AqBgs(Long who);

    String makeAq(Long who, int id);

    String AqList();

    String AqMeun();
}
