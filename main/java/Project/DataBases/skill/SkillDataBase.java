package Project.DataBases.skill;

import io.github.kloping.map.MapUtils;
import io.github.kloping.mirai0.Entitys.gameEntitys.Skill;
import io.github.kloping.mirai0.Entitys.gameEntitys.SkillInfo;
import io.github.kloping.mirai0.unitls.Tools.JSONUtils;

import java.io.File;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static Project.services.DetailServices.GameSkillDetailService.getUserPercent;
import static io.github.kloping.mirai0.unitls.Tools.Tool.getStringFromFile;
import static io.github.kloping.mirai0.unitls.Tools.Tool.putStringInFile;

/**
 * @author github-kloping
 */
public class SkillDataBase {

    public static String path;

    public SkillDataBase(String path) {
        SkillDataBase.path = path + "/dates/games/skills";
        File file = new File(SkillDataBase.path);
        if (!file.exists())
            file.mkdirs();
        initMap();
    }

    private void initMap() {
        File[] files = new File(path).listFiles();
        for (File files1 : files) {
            if (files1.isDirectory()) {
                for (File file : files1.listFiles()) {
                    try {
                        String json = getStringFromFile(file.getPath(), "utf-8");
                        SkillInfo info = JSONUtils.jsonStringToObject(json, SkillInfo.class);
                        info.setState(0);
                        info.setUsePercent(getUserPercent(info.getSt(), info.getJid()).intValue());
                        appendInfo(info);
                    } catch (Exception e) {
                        System.err.println(file.getPath() + "读取和初始化失败");
                    }
                }
            }
        }
    }

    public static final Map<Long, Map<Integer, SkillInfo>> QQ_2_ST_2_MAP = new ConcurrentHashMap<>();

    public static final Map<Integer, SkillInfo> getSkillInfo(Long qq) {
        if (!QQ_2_ST_2_MAP.containsKey(qq)) return new ConcurrentHashMap<>();
        Map<Integer, SkillInfo> map = QQ_2_ST_2_MAP.get(qq);
        if (map == null)
            map = new ConcurrentHashMap<>();
        return map;
    }

    public static synchronized final void update() {
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
    public static final void saveSkillInfo(SkillInfo info) {
        appendInfo(info);
        String uuid = info.getQq() + "." + info.getSt();
        info.setUUID(uuid);
        File file = new File(path + "/" + info.getQq() + "/" + uuid);
        if (!file.exists())
            if (!file.getParentFile().exists())
                file.getParentFile().mkdirs();
        String json = JSONUtils.objectToJsonString(info);
        putStringInFile(json, file.getPath(), "utf-8");
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
        File file = new File(path + "/" + info.getQq() + "/" + info.getUUID());
        file.delete();
        QQ_2_ST_2_MAP.put(qq, map);
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
            if (map == null)
                map = new ConcurrentHashMap<>();
            map.clear();
            File file = new File(path + "/" + who);
            if (!file.exists())
                return true;
            for (File file_ : file.listFiles()) {
                file_.delete();
            }
            file.delete();
            QQ_2_ST_2_MAP.put(qq, map);
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
    private static final void appendInfo(SkillInfo info) {
        long qq = Long.valueOf(info.getQq() + "");
        Map<Integer, SkillInfo> map = QQ_2_ST_2_MAP.get(qq);
        if (map == null)
            map = new ConcurrentHashMap<>();
        map.put(info.getSt(), info);
        QQ_2_ST_2_MAP.put(qq, map);
    }

    //============================================================================================================================
    public static ExecutorService threads = Executors.newFixedThreadPool(50);

    /**
     * 业务端需要完成 判断 魂力 是否 足够 以及 选择器的数量是否合法
     *
     * @param id
     * @return
     */
    public static Skill get(Number qq, SkillInfo info, Number[] numbers) {
        try {
            Method method = SkillExecute.class
                    .getDeclaredMethod("create" + info.getJid(), SkillInfo.class, Number.class, Number[].class);
            method.setAccessible(true);
            Skill skill = (Skill) method.invoke(null, info, qq, numbers);
            return skill;
        } catch (Exception e) {
            return null;
        }
    }

    //===============================================================================================
    public static final int t4 = 60000 * 2;
    public static final int t5 = 60000 * 2;
    public static final int t6 = 60000 * 2;
    public static final int t9 = 60000 * 2;
    public static final int t10 = 60000 * 2;
    public static final int t11 = 60000 * 2;
    public static final int t12 = 60000 * 2;
    public static final int t14 = 60000 * 2;
    public static final int t72 = 60000 * 2;
    public static final int t73 = 60000 * 2;
    public static final int t74 = 60000;
    public static final int t75 = 1000 * 15;
    public static final int t75C = 6;
    public static final int t77 = 60000 * 2;
    public static final int t78 = 60000 * 2;
    public static final int t79C = 6;
    public static final int t79 = 1000 * 15;
    public static final int t711 = 60000 * 2;
    public static final int t712 = 60000 * 2;
    public static final int t713 = 60000 * 2;
    public static final int t714 = 60000 * 2;
    public static final int t716 = 60000 * 2;
    public static final int t717 = 60000 * 2;
    public static final int t718 = 60000 * 2;
    public static final int t719 = 60000 * 2;
    public static final int t720 = 60000 * 2;
    public static final int t721 = 60000 * 2;
    public static final int t722 = 60000 * 2;
    public static final int t723 = 60000 * 2;
    public static final int t724 = 60000 * 2;
    public static final int t725 = 60000 * 2;
    public static final int t726 = 60000 * 2;
    public static final int t727 = 60000 * 2;
    public static final int t728 = 60000 * 2;
    public static final int t729 = 60000 * 2;
    public static final int t730 = 60000 * 2;
    public static final int t731 = 60000 * 2;

    //==========
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
    public static final String TAG_XUAN_YU_S = "h";

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

    public static final Map<Long, List<HasTimeAdder>> HAS_ADDER_MAP_LIST = new ConcurrentHashMap<>();

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

    public static void addAttHasTime(long who, HasTimeAdder adder) {
        MapUtils.append(HAS_ADDER_MAP_LIST, who, adder);
    }
}
