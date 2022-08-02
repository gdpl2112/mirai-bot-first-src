package Project.interfaces.Iservice;


/**
 * @author github-kloping
 */
public interface IGameWeaService {
    String useAq(String what, Long who);

    String aqBgs(Long who);

    String makeAq(Long who, int id);

    String aqList();

    String aqMeun();

    String decomposition(long id, Integer id1);

    String objTo(Long q1, int id, Long q2);
}
