package Project.interfaces.Iservice;


import Project.aSpring.dao.SoulAttribute;
import Project.aSpring.dao.SoulBone;

import java.util.List;

/**
 * @author github-kloping
 */
public interface IGameBoneService {
    /**
     * get info for attr
     *
     * @param who
     * @return
     */
    String getInfoAttributes(Long who);

    /**
     * get soul attr
     *
     * @param who
     * @return
     */
    SoulAttribute getSoulAttribute(Long who);

    /**
     * get bones
     *
     * @param who
     * @return
     */
    List<SoulBone> getSoulBones(Long who);

    /**
     * parse a bone
     *
     * @param id
     * @param qq
     * @return
     */
    String parseBone(int id, long qq);

    /**
     * un bone
     *
     * @param id
     * @param who
     * @return
     */
    String unInstallBone(Integer id, Long who);
}