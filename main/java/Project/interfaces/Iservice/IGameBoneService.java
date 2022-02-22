package Project.interfaces.Iservice;


import io.github.kloping.mirai0.Entitys.gameEntitys.AttributeBone;

import java.util.Map;

public interface IGameBoneService  {
    String getInfoAttributes(Long who);

    AttributeBone getAttribute(Long who);

    void PutAttributeMap(Long who, Map<Integer, Map.Entry<String, Integer>> map);

    Map<Integer, Map.Entry<String, Integer>> getAttributeMap(Long who, boolean k);

    String parseBone(Integer id, Long who);

    String unInstallBone(Integer id, Long who);
}