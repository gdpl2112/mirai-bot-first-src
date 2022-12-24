package io.github.kloping.mirai0.commons;

import Project.dataBases.DataBase;
import Project.dataBases.GameDataBase;
import io.github.kloping.mirai0.commons.broadcast.enums.ObjType;
import io.github.kloping.mirai0.unitls.Tools.Tool;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author github.kloping
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode
public abstract class RedPacket {

    public enum IdType {
        /**
         * 积分
         */
        SCORE,
        /**
         * 金魂币
         */
        GOLD,
        /**
         * 大瓶经验
         */
        OBJ0
    }

    private final Integer num;
    private final Integer value;
    private Integer v0;
    private Integer n0;
    private Long sender;
    private Long gid;
    private IdType id;
    private Map<Long, Integer> record = new LinkedHashMap<>();

    public RedPacket(Integer num, Integer value, Long sender, Long gid, IdType id) {
        this.num = num;
        this.value = value;
        this.v0 = value;
        this.n0 = num;
        this.sender = sender;
        this.gid = gid;
        this.id = id;
    }

    public Integer getOne(long qid) {
        try {
            int v = 0;
            if (n0 == 1) {
                v = v0;
            } else {
                int max = v0 - n0 + 1;
                int min = 1;
                if (max == min) {
                    v = max;
                } else {
                    v = Tool.tool.RANDOM.nextInt(max - min) + min;
                }
            }
            v0 -= v;
            n0--;
            record.put(qid, v);
            return v;
        } finally {
            if (n0 == 0)
                finish();
        }

    }

    /**
     * 完
     */
    public abstract void finish();

    public Long getMax() {
        AtomicReference<Long> qid = new AtomicReference<>(-1L);
        AtomicReference<Long> v0 = new AtomicReference<>(-1L);
        record.forEach((k, v) -> {
            if (qid.get() == -1L || v0.get() < v.longValue()) {
                qid.set(k);
                v0.set(v.longValue());
            }
        });
        return qid.get();
    }

    public String getName() {
        switch (this.id) {
            case SCORE:
                return "积分";
            case GOLD:
                return "金魂币";
            case OBJ0:
                return "大瓶经验";
            default:
                return "未知物品";
        }
    }

    public static boolean judge(long qid, IdType type, int value) {
        switch (type) {
            case SCORE:
                return DataBase.getAllInfo(qid).getScore() >= value;
            case GOLD:
                return GameDataBase.getInfo(qid).getGold() >= value;
            case OBJ0:
                return GameDataBase.containsBgsNum(qid, 103, value);
            default:
                return false;
        }
    }

    public static void app(long qid, IdType type, int value) {
        switch (type) {
            case SCORE:
                DataBase.putInfo(DataBase.getAllInfo(qid).addScore(-value));
                break;
            case GOLD:
                GameDataBase.getInfo(qid).addGold((long) -value, new TradingRecord()
                        .setType1(TradingRecord.Type1.lost)
                        .setType0(TradingRecord.Type0.gold)
                        .setTo(-1L)
                        .setMain(qid)
                        .setFrom(qid)
                        .setDesc("发红包")
                        .setMany(-value)
                ).apply();
                break;
            case OBJ0:
                GameDataBase.removeFromBgs(qid, 103, value, ObjType.use);
                break;
            default:
        }
    }

    public static void add(long qid, IdType type, int num) {
        switch (type) {
            case SCORE:
                DataBase.putInfo(DataBase.getAllInfo(qid).addScore(num));
                break;
            case GOLD:
                GameDataBase.getInfo(qid).addGold((long) -num, new TradingRecord()
                        .setType1(TradingRecord.Type1.lost)
                        .setType0(TradingRecord.Type0.gold)
                        .setTo(-1L)
                        .setMain(qid)
                        .setFrom(qid)
                        .setDesc("抢红包获得")
                        .setMany(num)
                ).apply();
                break;
            case OBJ0:
                GameDataBase.addToBgs(qid, 103, num, ObjType.got);
                break;
            default:
        }
    }
}
