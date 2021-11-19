package Project.Services.IServer;


import Project.Bases.BaseIService;
import Entitys.AttributeBone;

import java.util.Map;

public interface IGameBoneService extends BaseIService {
    String getInfoAttributes(Long who);

    AttributeBone getAttribute(Long who);

    void PutAttributeMap(Long who, Map<Integer, Map.Entry<String, Integer>> map);

    Map<Integer, Map.Entry<String, Integer>> getAttributeMap(Long who, boolean k);

    String ParseBone(Integer id, Long who);

    String UnInstallBone(Integer id, Long who);
}