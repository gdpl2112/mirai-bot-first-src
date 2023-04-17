package Project.interfaces.Iservice;


import Project.commons.SpGroup;

/**
 * @author github-kloping
 */
public interface ISkillService {
    /**
     * init skill
     *
     * @param qq
     * @param group
     * @param st
     * @return
     */
    String initSkill(long qq, SpGroup group, Integer st);

    /**
     * use skill
     *
     * @param qq
     * @param st
     * @param allAt
     * @param arg
     * @param group
     * @return
     */
    String useSkill(long qq, Integer st, Number[] allAt, String arg, SpGroup group);

    /**
     * set Skill Name
     *
     * @param qq
     * @param st
     * @param str
     * @return
     */
    String setName(long qq, Integer st, String str);

    /**
     * get Intro
     *
     * @param id
     * @param st
     * @param str
     * @return
     */
    String getIntro(long id, Integer st, String str);

    /**
     * forget skill
     *
     * @param qq
     * @param st
     * @return
     */
    String forget(long qq, Integer st);
}
