package Project.dataBases.skill;

import Project.aSpring.SpringBootResource;
import Project.skill.SkillFactory;
import io.github.kloping.map.MapUtils;
import io.github.kloping.mirai0.Main.Resource;
import io.github.kloping.mirai0.commons.Skill;
import io.github.kloping.mirai0.commons.gameEntitys.SkillInfo;
import io.github.kloping.mirai0.unitls.Tools.GameTool;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static Project.services.detailServices.GameSkillDetailService.getBasePercent;
import static Project.services.detailServices.GameSkillDetailService.getUserPercent;

/**
 * @author github-kloping
 */
public class SkillDataBase {

    public static final Map<Long, Map<Integer, SkillInfo>> QQ_2_ST_2_MAP = new ConcurrentHashMap<>();

    /**
     * 吸血
     */
    public static final String TAG_XX = "a";
    /**
     * 反甲
     */
    public static final String TAG_FJ = "b";
    /**
     * 名刀
     */
    public static final String TAG_MS = "c";
    /**
     * 无敌
     */
    public static final String TAG_WD = "d";
    /**
     * 不能躲避
     */
    public static final String TAG_CANT_HIDE = "e";
    /**
     * 护盾
     */
    public static final String TAG_SHIELD = "f";
    /**
     * 真伤
     */
    public static final String TAG_TRUE = "g";
    /**
     * 护盾额外
     */
    public static final String TAG_SHE = "h";
    /**
     * 下次免疫伤害
     */
    public static final String TAG_XUAN_YU_S = "i";
    /**
     * 免伤
     */
    public static final String TAG_DAMAGE_REDUCTION = "j";
    /**
     * 雷电攻击
     */
    public static final String TAG_LIGHT_ATT = "k";
    /**
     * 雷电反甲
     */
    public static final String TAG_LIGHT_F = "l";
    /**
     * 限制魂技
     */
    public static final String TAG_CANT_USE = "m";

    public static final Map<Long, List<HasTimeAdder>> HAS_ADDER_MAP_LIST = new ConcurrentHashMap<>();

    public static String path;

    public static ExecutorService threads = Executors.newFixedThreadPool(50);

    public SkillDataBase(String path) {
        initMap();
    }

    public static final Map<Integer, SkillInfo> getSkillInfo(Long qq) {
        if (!QQ_2_ST_2_MAP.containsKey(qq)) return new ConcurrentHashMap<>();
        Map<Integer, SkillInfo> map = QQ_2_ST_2_MAP.get(qq);
        if (map == null)
            map = new ConcurrentHashMap<>();
        return map;
    }

    public static final void update() {
        for (Long k1 : QQ_2_ST_2_MAP.keySet()) {
            for (SkillInfo info : QQ_2_ST_2_MAP.get(k1).values()) {
                break;
            }
        }
    }

    /**
     * 保存魂技信息
     *
     * @param info
     */
    public static void saveSkillInfo(SkillInfo info) {
        appendInfo(info);
        SpringBootResource.getSkillInfoMapper().insert(info);
    }

    public static void updateSkillInfo(SkillInfo info) {
        SpringBootResource.getSkillInfoMapper().updateById(info);
    }

    /**
     * 移除某魂技信息
     *
     * @param info
     */
    public static final void remove(SkillInfo info) {
        long qq = Long.valueOf(info.getQq() + "");
        Map<Integer, SkillInfo> map = QQ_2_ST_2_MAP.get(qq);
        if (map == null)
            map = new ConcurrentHashMap<>();
        if (map.containsKey(info.getSt()))
            map.remove(info.getSt());
        QQ_2_ST_2_MAP.put(qq, map);
        SpringBootResource.getSkillInfoMapper().deleteById(info.getUuid());
    }

    /**
     * 移除某人的全部魂技信息
     *
     * @param who
     * @return
     */
    public static final boolean remove(Number who) {
        try {
            long qq = who.longValue();
            Map<Integer, SkillInfo> map = QQ_2_ST_2_MAP.get(qq);
            if (map != null) {
                map.forEach((k, v) -> {
                    SpringBootResource.getSkillInfoMapper().deleteById(v.getUuid());
                });
            }
            QQ_2_ST_2_MAP.remove(qq);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 添加魂技值内存
     *
     * @param info
     */
    public static final void appendInfo(SkillInfo info) {
        long qq = Long.valueOf(info.getQq() + "");
        Map<Integer, SkillInfo> map = QQ_2_ST_2_MAP.get(qq);
        if (map == null)
            map = new ConcurrentHashMap<>();
        map.put(info.getSt(), info);
        QQ_2_ST_2_MAP.put(qq, map);
    }

    /**
     * 业务端需要完成 判断 魂力 是否 足够 以及 选择器的数量是否合法
     *
     * @param id
     * @return
     */
    public static Skill get(Number qq, SkillInfo info, Number[] numbers) {
        try {
            return SkillFactory.factory(info.getJid()).create(info, qq, numbers);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 计算百分比
     *
     * @param b b%
     * @param v
     * @return v 的 b%
     */
    public final static Long percentTo(Integer b, Number v) {
        if (v.longValue() < 100) {
            float f = b / 100f;
            return (long) (f * (v.intValue()));
        }
        double d = v.longValue();
        d /= 100f;
        d *= b;
        long v1 = (long) d;
        return v1;
    }

    /**
     * @param v1
     * @param v2
     * @return v1/v2 => %
     */
    public final static Integer toPercent(Number v1, Number v2) {
        double dv1 = (double) v1.longValue();
        double dv2 = (double) v2.longValue();
        double dv3 = dv1 / dv2;
        dv3 *= 100f;
        int v3 = (int) dv3;
        return v3;
    }

    public static void addAttHasTime(long who, HasTimeAdder adder) {
        MapUtils.append(HAS_ADDER_MAP_LIST, who, adder, ArrayList.class);
    }

    private void initMap() {
        Resource.START_AFTER.add(() -> {
            List<SkillInfo> list = SpringBootResource.getSkillInfoMapper().selectAll();
            for (SkillInfo info : list) {
                info.setState(0);
                info.setAddPercent((int) (getBasePercent(info.getJid()) * GameTool.getAHBl_(info.getId())));
                info.setUsePercent(getUserPercent(info.getSt(), info.getJid()).intValue());
                appendInfo(info);
            }
        });
    }

    public static class HasTimeAdder {
        private long toTime;
        private long who;
        private Number value;

        public HasTimeAdder(long toTime, long who, Number value) {
            this.toTime = toTime;
            this.who = who;
            this.value = value;
        }

        public boolean test() {
            return System.currentTimeMillis() < toTime;
        }

        public long getToTime() {
            return toTime;
        }

        public long getWho() {
            return who;
        }

        public Number getValue() {
            return value;
        }
    }
}
