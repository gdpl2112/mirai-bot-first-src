package Project.dataBases.skill;

import Project.aSpring.SpringBootResource;
import Project.skill.SkillFactory;
import io.github.kloping.map.MapUtils;
import io.github.kloping.mirai0.Main.Resource;
import io.github.kloping.mirai0.commons.Skill;
import io.github.kloping.mirai0.commons.gameEntitys.SkillInfo;
import io.github.kloping.mirai0.unitls.Tools.GameTool;

import java.util.*;
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
    /**
     * 额外受伤
     */
    public static final String TAG_EXTRA_DAMAGE = "n";
    /**
     * 额外受盾
     */
    public static final String TAG_EXTRA_SHIELD = "o";
    /**
     * 蛇杖标记
     */
    public static final String TAG_818 = "p";
    /**
     * 减伤
     */
    public static final String TAG_EDD_ATT = "q";
    /**
     * 增伤
     */
    public static final String TAG_ADD_ATT = "r";
    /**
     * 强击
     */
    public static final String TAG_STRENGTHEN_ATT = "s";
    /**
     * 精神护罩
     */
    public static final String TAG_HJ_IMMUNITY = "t";

    public static final Map<String, String> TAG2NAME = new HashMap<>();
    /**
     * 负面效果
     */
    public static final Set<String> NEGATIVE_TAGS = new HashSet<>();
    /**
     * 控制时可用魂技
     */
    public static final Set<Integer> AVAILABLE_IN_CONTROL = new HashSet<>();
    public static final Map<Long, Set<HasTimeAdder>> HAS_ADDER_MAP_LIST = new ConcurrentHashMap<>();
    public static String path;
    public static ExecutorService threads = Executors.newFixedThreadPool(50);

    static {
        TAG2NAME.put(TAG_HJ_IMMUNITY, "精神护罩");
        TAG2NAME.put(TAG_STRENGTHEN_ATT, "强击");
        TAG2NAME.put(TAG_ADD_ATT, "增伤");
        TAG2NAME.put(TAG_EDD_ATT, "减伤");
        TAG2NAME.put(TAG_818, "蛇杖标记");
        TAG2NAME.put(TAG_EXTRA_SHIELD, "受盾");
        TAG2NAME.put(TAG_EXTRA_DAMAGE, "额外受伤");
        TAG2NAME.put(TAG_XX, "吸血");
        TAG2NAME.put(TAG_FJ, "反甲");
        TAG2NAME.put(TAG_MS, "免死");
        TAG2NAME.put(TAG_WD, "无敌");
        TAG2NAME.put(TAG_CANT_HIDE, "无法躲避");
        TAG2NAME.put(TAG_SHIELD, "护盾");
        TAG2NAME.put(TAG_TRUE, "真伤");
        TAG2NAME.put(TAG_SHE, "护盾额外");
        TAG2NAME.put(TAG_XUAN_YU_S, "下次免疫");
        TAG2NAME.put(TAG_DAMAGE_REDUCTION, "免伤");
        TAG2NAME.put(TAG_LIGHT_ATT, "雷电攻击");
        TAG2NAME.put(TAG_LIGHT_F, "雷电反甲");
        TAG2NAME.put(TAG_CANT_USE, "魂技限制");
    }

    static {
        NEGATIVE_TAGS.add(TAG_EDD_ATT);
        NEGATIVE_TAGS.add(TAG_818);
        NEGATIVE_TAGS.add(TAG_EXTRA_DAMAGE);
        NEGATIVE_TAGS.add(TAG_CANT_HIDE);
        NEGATIVE_TAGS.add(TAG_CANT_USE);
    }

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
            e.printStackTrace();
            return null;
        }
    }

    public static void addAttHasTime(long who, HasTimeAdder adder) {
        clearOld();
        MapUtils.appendSet(HAS_ADDER_MAP_LIST, who, adder, HashSet.class);
    }

    private static void clearOld() {
        Iterator<Set<HasTimeAdder>> iterator = HAS_ADDER_MAP_LIST.values().iterator();
        while (iterator.hasNext()) {
            Iterator<HasTimeAdder> iterator0 = iterator.next().iterator();
            while (iterator0.hasNext()) {
                HasTimeAdder adder = iterator0.next();
                if (!adder.test()) {
                    iterator0.remove();
                }
            }
        }
    }

    public static void reMap() {
        QQ_2_ST_2_MAP.clear();
        List<SkillInfo> list = SpringBootResource.getSkillInfoMapper().selectAll();
        for (SkillInfo info : list) {
            info.setState(0);
            info.setAddPercent((int) (getBasePercent(info.getJid()) * GameTool.getAHBl_(info.getId())));
            info.setUsePercent(getUserPercent(info.getSt(), info.getJid()).intValue());
            appendInfo(info);
        }
    }

    private void initMap() {
        Resource.START_AFTER.add(() -> {
            List<SkillInfo> list = SpringBootResource.getSkillInfoMapper().selectAll();
            for (SkillInfo info : list) {
                info.setState(0);
                double add = (getBasePercent(info.getJid()));
                if (info.getSt() > 0)
                    add *= GameTool.getAHBl_(info.getId());
                info.setAddPercent((int) add);
                info.setUsePercent(getUserPercent(info.getSt(), info.getJid()).intValue());
                appendInfo(info);
            }
        });
    }

    public static class HasTimeAdder {
        private long toTime;
        private long who;
        private Number value;
        private Integer jid;

        public HasTimeAdder(long toTime, long who, Number value, int jid) {
            this.toTime = toTime;
            this.who = who;
            this.value = value;
            this.jid = jid;
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

        public HasTimeAdder setJid(Integer jid) {
            this.jid = jid;
            return this;
        }


        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            HasTimeAdder adder = (HasTimeAdder) o;
            return who == adder.who && Objects.equals(jid, adder.jid);
        }

        @Override
        public int hashCode() {
            return Objects.hash(jid);
        }
    }
}
