package Project.services.detailServices;

import Project.broadcast.game.HpChangeBroadcast;
import Project.dataBases.GameDataBase;
import Project.dataBases.skill.SkillDataBase;
import io.github.kloping.MySpringTool.annotations.Entity;
import io.github.kloping.clasz.ClassUtils;
import io.github.kloping.date.FrameUtils;
import io.github.kloping.mirai0.commons.*;
import io.github.kloping.mirai0.commons.gameEntitys.TagPack;
import io.github.kloping.mirai0.unitls.Tools.GameTool;
import io.github.kloping.object.ObjectUtils;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static Project.dataBases.GameDataBase.getInfo;
import static Project.dataBases.GameDataBase.putPerson;
import static Project.dataBases.skill.SkillDataBase.percentTo;

/**
 * @author github-kloping
 */
@Entity
public class GameSkillDetailService {

    private static final Map<Integer, Integer> BASE_PERCENT_MAP = new ConcurrentHashMap<>();

    static {
        BASE_PERCENT_MAP.put(0, 10);
        BASE_PERCENT_MAP.put(1, 4);
        BASE_PERCENT_MAP.put(2, 10);
        BASE_PERCENT_MAP.put(3, 4);
        BASE_PERCENT_MAP.put(4, 10);
        BASE_PERCENT_MAP.put(5, 4);
        BASE_PERCENT_MAP.put(6, 5);
        BASE_PERCENT_MAP.put(7, 5);
        BASE_PERCENT_MAP.put(8, 35);
        BASE_PERCENT_MAP.put(9, 4);
        BASE_PERCENT_MAP.put(10, 5);
        BASE_PERCENT_MAP.put(11, 25);
        BASE_PERCENT_MAP.put(12, 8);
        BASE_PERCENT_MAP.put(13, 25);
        BASE_PERCENT_MAP.put(15, 18);
        BASE_PERCENT_MAP.put(16, 50);
        BASE_PERCENT_MAP.put(17, 9);
        BASE_PERCENT_MAP.put(18, 40);
        BASE_PERCENT_MAP.put(19, 40);
        BASE_PERCENT_MAP.put(20, 1);
        BASE_PERCENT_MAP.put(21, 7);
        BASE_PERCENT_MAP.put(22, 16);
        //=
        BASE_PERCENT_MAP.put(71, 42);
        BASE_PERCENT_MAP.put(72, 58);
        BASE_PERCENT_MAP.put(73, 5);
        BASE_PERCENT_MAP.put(74, 7);
        BASE_PERCENT_MAP.put(75, 5);
        BASE_PERCENT_MAP.put(76, 3);
        BASE_PERCENT_MAP.put(77, 52);
        BASE_PERCENT_MAP.put(78, 42);
        BASE_PERCENT_MAP.put(711, 50);
        BASE_PERCENT_MAP.put(712, 51);
        BASE_PERCENT_MAP.put(713, 43);
        BASE_PERCENT_MAP.put(714, 43);
        BASE_PERCENT_MAP.put(715, 34);
        BASE_PERCENT_MAP.put(716, 42);
        BASE_PERCENT_MAP.put(717, 39);
        BASE_PERCENT_MAP.put(718, 38);
        BASE_PERCENT_MAP.put(719, 5);
        BASE_PERCENT_MAP.put(720, 41);
        BASE_PERCENT_MAP.put(721, 12);
        BASE_PERCENT_MAP.put(722, 32);
        BASE_PERCENT_MAP.put(723, 32);
        BASE_PERCENT_MAP.put(724, 50);
        BASE_PERCENT_MAP.put(725, 42);
        BASE_PERCENT_MAP.put(726, 50);
        BASE_PERCENT_MAP.put(727, 40);
        BASE_PERCENT_MAP.put(728, 40);
        BASE_PERCENT_MAP.put(729, 54);
        BASE_PERCENT_MAP.put(730, 40);
        BASE_PERCENT_MAP.put(731, 40);
    }


    /**
     * 获取魂技基础加成
     *
     * @param id
     * @return
     */
    public static Integer getBasePercent(Integer id) {
        if (BASE_PERCENT_MAP.containsKey(id.intValue())) {
            return BASE_PERCENT_MAP.get(id.intValue());
        } else {
            return -1;
        }
    }

    /**
     * @param jid 魂技ID
     * @param id  魂环ID
     * @return
     */
    public static Long getAddP(Integer jid, Integer id) {
        return (long) (getBasePercent(jid) * GameTool.getAHBl_(id));
    }

    public static Number getDuration(int jid) {
        try {
            Field field = SkillDataBase.class.getDeclaredField("t" + jid);
            return (Number) field.get(null);
        } catch (Exception e) {
        }
        return -1;
    }

    public static final class WhTypes {
        public static final SkillIntro.Type[] T0 = new SkillIntro.Type[]{SkillIntro.Type.Add, SkillIntro.Type.ToOne, SkillIntro.Type.OneTime};
        public static final SkillIntro.Type[] T1 = new SkillIntro.Type[]{SkillIntro.Type.Add, SkillIntro.Type.ToNum, SkillIntro.Type.OneTime};
        public static final SkillIntro.Type[] T4 = new SkillIntro.Type[]{SkillIntro.Type.Add, SkillIntro.Type.ToOne, SkillIntro.Type.HasTime};
        public static final SkillIntro.Type[] T5 = new SkillIntro.Type[]{SkillIntro.Type.Add, SkillIntro.Type.ToNum, SkillIntro.Type.HasTime};
        public static final SkillIntro.Type[] T6 = new SkillIntro.Type[]{SkillIntro.Type.Special, SkillIntro.Type.ToOne, SkillIntro.Type.Mark};
        public static final SkillIntro.Type[] T8 = new SkillIntro.Type[]{SkillIntro.Type.Att, SkillIntro.Type.ToOne, SkillIntro.Type.OneTime};
        public static final SkillIntro.Type[] T72 = new SkillIntro.Type[]{SkillIntro.Type.WHZs, SkillIntro.Type.Add, SkillIntro.Type.HasTime};

    }


    /**
     * 获取魂技冷却时间
     *
     * @param id  魂环id
     * @param jid 魂技id
     * @param wh  武魂id
     * @param st  第几魂技
     * @return
     */
    public static Number getCoolTime(int id, int jid, int wh, int st) {
        if (st == 7) {
            if (wh == 6) {
                return 30;
            }
        }
        if (jid == 14) {
            return 18;
        }
        if (jid == 20) {
            return 12;
        }
        int n = 205 - id;
        return 12 - n;
    }

    public static Number getUserPercent(int st, int jid) {
        return (7 + (st - 1));
    }

    /**
     * 获取tag值
     *
     * @param qq
     * @param tag
     * @return
     */
    public static Number getTagValue(Number qq, String tag) {
        PersonInfo info = GameDataBase.getInfo(qq);
        String sb = info.getMyTag();
        int i = sb.indexOf(tag);
        if (i < 0) {
            return -1;
        }
        sb = sb.substring(i);
        int i2 = sb.indexOf(",");
        String vs = sb.substring(1, i2);
        return Integer.parseInt(vs);
    }

    /**
     * 减少任何攻击
     *
     * @param who
     * @param num
     * @param v
     * @return
     */
    public static boolean eddAttAny(Number who, Number num, long v) {
        if (num.longValue() != -2) {
            if (!GameDataBase.exist(num.longValue())) {
                return false;
            }
            putPerson(GameDataBase.getInfo(num).addAtt(-v));
        } else {
            GhostObj ghostObj = GameJoinDetailService.getGhostObjFrom(who.longValue());
            ghostObj.setAtt(ghostObj.getAtt() - v);
            GameJoinDetailService.saveGhostObjIn(who.longValue(), ghostObj);
        }
        return true;
    }

    /**
     * 一个 player 对 另一个 player 的加血
     *
     * @param who  p1 加血者
     * @param who2 p2 被加血者
     * @param bf   比例
     */
    public static void addHp(Number who, long who2, float bf) {
        PersonInfo p1 = GameDataBase.getInfo(who);
        long v1 = percentTo((int) bf, p1.getHpL());
        PersonInfo p2 = GameDataBase.getInfo(who2);
        v1 = v1 > p2.getHpL() / 2 ? p2.getHpL() / 2 : v1;
        HpChangeBroadcast.INSTANCE.broadcast(who.longValue(), p2.getHp(),
                p2.getHp() + v1, v1, who.longValue(), HpChangeBroadcast.HpChangeReceiver.type.fromQ);
        p2.addHp(v1);
        putPerson(p2);
    }

    /**
     * 一个 player 对 另一个 player 的加魂力
     *
     * @param who  p1 加者
     * @param who2 p2 被加者
     * @param bf   比例
     */
    public static void addHl(Number who, long who2, float bf) {
        PersonInfo p1 = GameDataBase.getInfo(who);
        long v1 = percentTo((int) bf, p1.getHll());
        PersonInfo p2 = GameDataBase.getInfo(who2);
        v1 = v1 > p2.getHll() / 2 ? p2.getHll() / 2 : v1;
        HpChangeBroadcast.INSTANCE.broadcast(who.longValue(), p2.getHl(),
                p2.getHl() + v1, v1, who.longValue(), HpChangeBroadcast.HpChangeReceiver.type.fromQ);
        p2.addHl(v1);
        putPerson(p2);
    }

    public static long oneNearest(Number who, Number[] nums) {
        return nums.length > 0 ? nums[0].longValue() : who.longValue();
    }

    public static Long[] nearest(int n, long who, Number[] nums) {
        Set<Long> ls = new LinkedHashSet<>();
        for (Number num : nums) {
            if (ls.size() != n) {
                ls.add(num.longValue());
            } else {
                break;
            }
        }
        if (ls.size() < n) {
            ls.add(who);
        }
        return ls.toArray(new Long[0]);
    }

    public static Long[] nearest(int n, Number[] nums) {
        Set<Long> ls = new LinkedHashSet<>();
        for (Number num : nums) {
            if (ls.size() != n) {
                ls.add(num.longValue());
            } else {
                break;
            }
        }
        return ls.toArray(new Long[0]);
    }

    private static List<TagPack> tagPacks = new LinkedList<>();

    static {
        FrameUtils.add(() -> {
            Iterator<TagPack> iterator = tagPacks.listIterator();
            while (iterator.hasNext()) {
                TagPack tagP = iterator.next();
                if (!tagP.getEffected()) {
                    tagP.effect();
                } else if (tagP.over()) {
                    tagP.loseEffect();
                    iterator.remove();
                }
            }
        });
    }

    public static void addTagPack(TagPack tagPack) {
        tagPacks.add(tagPack);
    }

    /**
     * 添加护盾
     *
     * @param q 谁
     * @param v 值
     * @param t 时长 最大 30*60*1000
     */
    public static void addShield(long q, Long v, Long t) {
        ShieldPack shieldPack = new ShieldPack();
        shieldPack.setQ(q).setValue(v).setEffected(false);
        shieldPack.setMax(getInfo(q).getHpL()).setTime(System.currentTimeMillis() + t);
        tagPacks.add(shieldPack);
    }

    /**
     * 添加护盾
     *
     * @param q
     * @param v
     */
    public static void addShield(long q, Long v) {
        ShieldPack shieldPack = new ShieldPack();
        shieldPack.setQ(q).setValue(v).setEffected(false);
        shieldPack.setMax(getInfo(q).getHpL()).setTime(System.currentTimeMillis() + 30 * 60 * 1000);
        tagPacks.add(shieldPack);
    }

    /**
     * 添加护盾
     *
     * @param q    谁
     * @param v    值
     * @param maxV 最大值
     * @param t    时长 最大 30*60*1000
     */
    public static void addShield(long q, Long v, Long maxV, Long t) {
        ShieldPack shieldPack = new ShieldPack();
        shieldPack.setQ(q).setValue(v).setEffected(false);
        shieldPack.setMax(maxV).setTime(System.currentTimeMillis() + t);
        tagPacks.add(shieldPack);
    }

    /**
     * 从 {@link Skill#getArgs()} 中获取指定类型的参数
     *
     * @param skill
     * @param cla
     * @param <T>
     * @return
     */
    public static <T> T getArgFromSkillArgs(Skill skill, Class<T> cla) {
        T t = null;
        Object[] os = skill.getArgs();
        if (os != null) {
            for (Object o : os) {
                if (o == null) continue;
                try {
                    if (ObjectUtils.isBaseOrPack(cla)) {
                        Class c0 = ObjectUtils.baseToPack(cla);
                        Method method = c0.getDeclaredMethod("valueOf", String.class);
                        method.setAccessible(true);
                        t = (T) method.invoke(null, o.toString());
                        break;
                    } else if (ObjectUtils.isSuperOrInterface(o.getClass(), cla)) {
                        t = (T) o;
                        break;
                    }
                } catch (Exception e) {
                    continue;
                }
            }
        }
        return t;
    }

    public static <T> T getArgFromSkillArgs(Skill skill, Class<T> cla, T defaultValue) {
        T t = getArgFromSkillArgs(skill, cla);
        if (t == null) {
            return defaultValue;
        }
        return t;
    }
}
