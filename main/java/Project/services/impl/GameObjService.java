package Project.services.impl;

import Project.DataBases.GameDataBase;
import Project.services.Iservice.IGameObjService;
import Project.services.Iservice.IGameWeaService;
import Project.broadcast.enums.ObjType;
import io.github.kloping.MySpringTool.annotations.AutoStand;
import io.github.kloping.MySpringTool.annotations.Entity;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static Project.DataBases.GameDataBase.*;
import static Project.Tools.Tool.getEntry;

/**
 * @author github-kloping
 */
@Entity
public class GameObjService implements IGameObjService {
    @AutoStand
    IGameWeaService gameWeaService;

    @Override
    public String compound(long q, int id) {
        //拦截暗器
        if ((id > 1000 && id < 1200)) {
            return gameWeaService.makeAq(q, id);
        }
        Map.Entry<Integer, Integer> entry = needNums.get(id);
        if (entry == null) return "该物品 暂时不可合成!";
        int needId = entry.getKey().intValue();
        int needNum = entry.getValue();
        if (GameDataBase.contiansBgsNum(q, needId, needNum)) {
            GameDataBase.removeFromBgs(q, needId, needNum, ObjType.use);
            addToBgs(q, id, ObjType.got);
            return String.format("合成%s消耗了%s个%s\n%s", getNameById(id), needNum
                    , getNameById(needId), getImgById(id));
        } else return String.format("您需要%s个%s 才可合成%s", needNum, getNameById(needId), getNameById(id));
    }

    /**
     * 制作
     * id 为 key 的东西
     * 需要
     * id 为 entry key 物品
     * entry value 个
     */
    private static final Map<Integer, Map.Entry<Integer, Integer>> needNums = new ConcurrentHashMap<>();

    static {
        needNums.put(1602, getEntry(1601, 3));
        needNums.put(1603, getEntry(1602, 4));
        needNums.put(1604, getEntry(1603, 4));
        needNums.put(1605, getEntry(1604, 5));
    }
}
