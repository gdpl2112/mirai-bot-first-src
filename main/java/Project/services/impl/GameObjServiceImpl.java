package Project.services.impl;

import Project.commons.broadcast.enums.ObjType;
import Project.dataBases.GameDataBase;
import Project.dataBases.SourceDataBase;
import Project.interfaces.Iservice.IGameObjService;
import Project.interfaces.Iservice.IGameWeaService;
import io.github.kloping.MySpringTool.annotations.AutoStand;
import io.github.kloping.MySpringTool.annotations.Entity;
import io.github.kloping.mirai0.unitls.Tools.Tool;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static Project.commons.rt.ResourceSet.FinalString.NEWLINE;
import static Project.dataBases.GameDataBase.*;

/**
 * @author github-kloping
 */
@Entity
public class GameObjServiceImpl implements IGameObjService {
    /**
     * 制作
     * id 为 key 的东西
     * 需要
     * id 为 entry key 物品
     * entry value 个
     */
    private static final Map<Integer, Map.Entry<Integer, Integer>> NEED_NUMS = new ConcurrentHashMap<>();

    static {
        NEED_NUMS.put(1602, Tool.INSTANCE.getEntry(1601, 3));
        NEED_NUMS.put(1603, Tool.INSTANCE.getEntry(1602, 4));
        NEED_NUMS.put(1604, Tool.INSTANCE.getEntry(1603, 4));
        NEED_NUMS.put(1605, Tool.INSTANCE.getEntry(1604, 5));
        NEED_NUMS.put(115, Tool.INSTANCE.getEntry(114, 6));

        NEED_NUMS.put(122, Tool.INSTANCE.getEntry(121, 4));
        NEED_NUMS.put(123, Tool.INSTANCE.getEntry(122, 5));

        NEED_NUMS.put(124, Tool.INSTANCE.getEntry(122, 13));
        NEED_NUMS.put(125, Tool.INSTANCE.getEntry(123, 10));
        NEED_NUMS.put(126, Tool.INSTANCE.getEntry(122, 13));
        NEED_NUMS.put(127, Tool.INSTANCE.getEntry(123, 11));
    }

    @AutoStand
    IGameWeaService gameWeaService;

    @Override
    public String compound(long q, int id, int num) {
        //拦截暗器
        if ((id > 1000 && id < 1200)) return gameWeaService.makeAq(q, id);
        Map.Entry<Integer, Integer> entry = NEED_NUMS.get(id);
        if (entry == null) return "该物品 暂时不可合成!";
        int needId = entry.getKey().intValue();
        int needNum = entry.getValue();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < num; i++) {
            if (GameDataBase.containsBgsNum(q, needId, needNum)) {
                GameDataBase.removeFromBgs(q, needId, needNum, ObjType.use);
                if (id >= 124 && id <= 127) {
                    addToAqBgs(q, id, (ID_2_WEA_O_NUM_MAPS.get(id)));
                } else {
                    addToBgs(q, id, ObjType.got);
                }
                sb.append(String.format("合成%s消耗了%s个%s",
                        getNameById(id), needNum, getNameById(needId)));
            } else sb.append(String.format("您需要%s个%s 才可合成%s", needNum, getNameById(needId), getNameById(id)));
            sb.append(NEWLINE);
        }
        return sb.toString();
    }
}
