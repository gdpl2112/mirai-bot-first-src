package io.github.kloping.mirai0.commons;

import Project.dataBases.DataBase;
import Project.dataBases.GameDataBase;
import io.github.kloping.mirai0.commons.broadcast.enums.ObjType;
import io.github.kloping.mirai0.unitls.Tools.Tool;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.*;
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
        OBJ0;
    }

    private final Integer num;

    private final Integer value;
    private Integer v0;
    private Integer n0;
    private Long sender;
    private Long gid;
    private Long qid;
    private IdType id;
    private Integer hour = getHour();
    private Map<Long, Integer> record = new LinkedHashMap<>();
    private Iterator<Integer> iteratore = null;

    public RedPacket(Integer num, Integer value, Long sender, Long gid, IdType id) {
        this.num = num;
        this.value = value;
        this.v0 = value;
        this.n0 = num;
        this.sender = sender;
        this.gid = gid;
        this.id = id;
        List<Integer> list = new ArrayList<>();
        for (Integer i = 0; i < num; i++) {
            int v0 = getOne0();
            list.add(v0);
        }
        Collections.shuffle(list);
        iteratore = list.iterator();
        v0 = value;
        n0 = num;
    }

    public void back() {
        Integer sv = 0;
        while (iteratore.hasNext()) {
            int v = iteratore.next();
            sv += v;
            app(sender, this.id, -sv);
        }
        finish("");
    }

    private Integer getOne0() {
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
        return v;
    }

    public Integer getOne(long qid) {
        try {
            int v = iteratore.next();
            record.put(qid, v);
            n0--;
            return v;
        } finally {
            if (!iteratore.hasNext())
                finish("");
        }
    }

    /**
     * 完
     */
    public abstract void finish(String tips);

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

    public void sendBack() {
        while (iteratore.hasNext()) {
            int v = iteratore.next();
            add(qid, getId(), v);
        }
        finish("未抢的部分已退还");
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
                if (value < 0) {
                    GameDataBase.addToBgs(qid, 103, value, ObjType.got);
                } else {
                    GameDataBase.removeFromBgs(qid, 103, value, ObjType.use);
                }
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
                GameDataBase.getInfo(qid).addGold((long) num, new TradingRecord()
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
