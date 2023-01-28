package Project.services.detailServices;

import Project.broadcast.game.HpChangeBroadcast;
import Project.controllers.gameControllers.GameConditionController;
import Project.dataBases.GameDataBase;
import Project.dataBases.skill.SkillDataBase;
import Project.services.detailServices.roles.DamageType;
import Project.services.detailServices.roles.v1.TagManagers;
import Project.skill.SkillFactory;
import io.github.kloping.MySpringTool.annotations.Entity;
import io.github.kloping.map.MapUtils;
import io.github.kloping.mirai0.commons.PersonInfo;
import io.github.kloping.mirai0.commons.Skill;
import io.github.kloping.mirai0.commons.game.*;
import io.github.kloping.mirai0.commons.gameEntitys.SkillInfo;
import io.github.kloping.mirai0.commons.gameEntitys.TagPack;
import io.github.kloping.mirai0.unitls.Tools.GameTool;
import io.github.kloping.object.ObjectUtils;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static Project.controllers.auto.ControllerSource.challengeDetailService;
import static Project.dataBases.GameDataBase.getInfo;
import static Project.dataBases.GameDataBase.putPerson;
import static io.github.kloping.mirai0.commons.resouce_and_tool.CommonSource.percentTo;
import static io.github.kloping.mirai0.commons.resouce_and_tool.ResourceSet.FinalString.NEWLINE;

/**
 * @author github-kloping
 */
@Entity
public class GameSkillDetailService {

    public static final Map<Integer, Long> JID2TIME = new HashMap<>();
    public static final Map<Long, List<AsynchronousThing>> ASYNCHRONOUS_THING_MAP = new HashMap<>();
    private static final Map<Integer, Number> BASE_PERCENT_MAP = new ConcurrentHashMap<>();

    static {
        BASE_PERCENT_MAP.put(0, 10);
        BASE_PERCENT_MAP.put(1, 6);
        BASE_PERCENT_MAP.put(2, 10);
        BASE_PERCENT_MAP.put(3, 5);
        BASE_PERCENT_MAP.put(4, 10);
        BASE_PERCENT_MAP.put(5, 6);
        BASE_PERCENT_MAP.put(6, 7);
        BASE_PERCENT_MAP.put(7, 5);
        BASE_PERCENT_MAP.put(8, 35);
        BASE_PERCENT_MAP.put(9, 4);
        BASE_PERCENT_MAP.put(10, 15);
        BASE_PERCENT_MAP.put(11, 25);
        BASE_PERCENT_MAP.put(12, 8);
        BASE_PERCENT_MAP.put(13, 7);
        BASE_PERCENT_MAP.put(15, 18);
        BASE_PERCENT_MAP.put(16, 50);
        BASE_PERCENT_MAP.put(17, 9);
        BASE_PERCENT_MAP.put(18, 40);
        BASE_PERCENT_MAP.put(19, 29);
        BASE_PERCENT_MAP.put(20, 1);
        BASE_PERCENT_MAP.put(21, 7);
        BASE_PERCENT_MAP.put(22, 16);
        BASE_PERCENT_MAP.put(23, 5000);
        BASE_PERCENT_MAP.put(24, 240000);
        BASE_PERCENT_MAP.put(25, 16);
        BASE_PERCENT_MAP.put(26, 1.2);
        //=====
        BASE_PERCENT_MAP.put(701, 42);
        BASE_PERCENT_MAP.put(702, 58);
        BASE_PERCENT_MAP.put(703, 24);
        BASE_PERCENT_MAP.put(704, 7);
        BASE_PERCENT_MAP.put(705, 5);
        BASE_PERCENT_MAP.put(706, 4);
        BASE_PERCENT_MAP.put(707, 52);
        BASE_PERCENT_MAP.put(708, 42);
        BASE_PERCENT_MAP.put(709, 43);
        BASE_PERCENT_MAP.put(710, 43);
        BASE_PERCENT_MAP.put(711, 50);
        BASE_PERCENT_MAP.put(712, 51);
        BASE_PERCENT_MAP.put(713, 43);
        BASE_PERCENT_MAP.put(714, 43);
        BASE_PERCENT_MAP.put(715, 34);
        BASE_PERCENT_MAP.put(716, 42);
        BASE_PERCENT_MAP.put(717, 39);
        BASE_PERCENT_MAP.put(718, 38);
        BASE_PERCENT_MAP.put(719, 3);
        BASE_PERCENT_MAP.put(720, 41);
        BASE_PERCENT_MAP.put(721, 12);
        BASE_PERCENT_MAP.put(722, 32);
        BASE_PERCENT_MAP.put(723, 32);
        BASE_PERCENT_MAP.put(724, 50);
        BASE_PERCENT_MAP.put(725, 42);
        BASE_PERCENT_MAP.put(726, 50);
        BASE_PERCENT_MAP.put(727, 45);
        BASE_PERCENT_MAP.put(728, 44);
        BASE_PERCENT_MAP.put(729, 54);
        BASE_PERCENT_MAP.put(730, 40);
        BASE_PERCENT_MAP.put(731, 40);
        //==
        BASE_PERCENT_MAP.put(1001, 54);
        BASE_PERCENT_MAP.put(1002, 75);
        BASE_PERCENT_MAP.put(1003, 10);
        BASE_PERCENT_MAP.put(1004, 80);
        BASE_PERCENT_MAP.put(1005, 12);
        BASE_PERCENT_MAP.put(1006, 10);
        BASE_PERCENT_MAP.put(1007, 10);
        BASE_PERCENT_MAP.put(1008, 10);
        BASE_PERCENT_MAP.put(1009, 20);
        BASE_PERCENT_MAP.put(1010, 3);
        BASE_PERCENT_MAP.put(1011, 6);
        BASE_PERCENT_MAP.put(1101, 1);
        BASE_PERCENT_MAP.put(1102, 3);
        BASE_PERCENT_MAP.put(1103, 30);
        //==魂骨加成
        BASE_PERCENT_MAP.put(1514, 60);
        BASE_PERCENT_MAP.put(1515, 10);
        //==
        BASE_PERCENT_MAP.put(8010, 20);
        BASE_PERCENT_MAP.put(8011, 10);
        BASE_PERCENT_MAP.put(8021, 37);
        BASE_PERCENT_MAP.put(8030, 32);
        BASE_PERCENT_MAP.put(8031, 40);
        BASE_PERCENT_MAP.put(8041, 42);
        BASE_PERCENT_MAP.put(8051, 28);
        BASE_PERCENT_MAP.put(8050, 1);
        BASE_PERCENT_MAP.put(8060, 18);
        BASE_PERCENT_MAP.put(8061, 8);
        BASE_PERCENT_MAP.put(8070, 20);
        BASE_PERCENT_MAP.put(8071, 26);
        BASE_PERCENT_MAP.put(8081, 3);
        BASE_PERCENT_MAP.put(8080, 34);
        BASE_PERCENT_MAP.put(8090, 30);
        BASE_PERCENT_MAP.put(8100, 14);
        BASE_PERCENT_MAP.put(8110, 28);
        BASE_PERCENT_MAP.put(8120, 10);
        BASE_PERCENT_MAP.put(8121, 15);
        BASE_PERCENT_MAP.put(8130, 13);
        BASE_PERCENT_MAP.put(8131, 26);
        BASE_PERCENT_MAP.put(8140, 10);
        BASE_PERCENT_MAP.put(8150, 35);
        BASE_PERCENT_MAP.put(8160, 32);
        BASE_PERCENT_MAP.put(8170, 39);
        BASE_PERCENT_MAP.put(8180, 33);
        BASE_PERCENT_MAP.put(8190, 36);
        BASE_PERCENT_MAP.put(8200, 15);
        BASE_PERCENT_MAP.put(8210, 25);
        BASE_PERCENT_MAP.put(8220, 8);
        BASE_PERCENT_MAP.put(8221, 22);
        BASE_PERCENT_MAP.put(8230, 8);
        BASE_PERCENT_MAP.put(8240, 32);
        BASE_PERCENT_MAP.put(8250, 10);
        BASE_PERCENT_MAP.put(8260, 30);
        BASE_PERCENT_MAP.put(8270, 33);
        BASE_PERCENT_MAP.put(8280, 15);
        BASE_PERCENT_MAP.put(8290, 48);
        BASE_PERCENT_MAP.put(8300, 36);
        BASE_PERCENT_MAP.put(8310, 33);
    }

    static {
        long twoMinutes = 120000;
        for (Integer integer : SkillFactory.skillListIds()) {
            JID2TIME.put(integer, twoMinutes);
        }
        JID2TIME.remove(0);
        JID2TIME.remove(1);
        JID2TIME.remove(2);
        JID2TIME.remove(3);
        JID2TIME.remove(8);
        JID2TIME.remove(10);
        JID2TIME.remove(13);
        JID2TIME.remove(14);
        JID2TIME.remove(15);
        JID2TIME.remove(16);
        JID2TIME.remove(17);
        JID2TIME.remove(18);
        JID2TIME.remove(19);
        JID2TIME.remove(20);
        JID2TIME.remove(21);
        JID2TIME.remove(22);
        JID2TIME.remove(701);
        JID2TIME.remove(704);
        JID2TIME.remove(706);
        JID2TIME.remove(710);
        JID2TIME.remove(8021);
        JID2TIME.remove(8030);
        JID2TIME.remove(8031);
        JID2TIME.remove(8041);
        JID2TIME.put(8050, 60000L);
        JID2TIME.remove(8051);
    }

    /**
     * 获取魂技基础加成
     *
     * @param id
     * @return
     */
    public static Double getBasePercent(Integer id) {
        if (BASE_PERCENT_MAP.containsKey(id.intValue())) {
            return BASE_PERCENT_MAP.get(id.intValue()).doubleValue();
        } else {
            return -1.0;
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

    public static Long getDuration(int jid) {
        return JID2TIME.containsKey(jid) ? JID2TIME.get(jid) : 0;
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
        if (st < 0)
            return 10;
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
        return 11 - n;
    }

    public static Number getUserPercent(int st, int jid) {
        if (st < 0) return 5;
        return (6 + (st - 1));
    }


    /**
     * 一个 player 对 另一个 player 的加血
     *
     * @param who  p1 加血者
     * @param who2 p2 被加血者
     * @param bf   比例
     */
    public static void addHp(Number who, long who2, float bf) {
        if (who2 > 0) {
            PersonInfo p1 = GameDataBase.getInfo(who);
            long v1 = percentTo((int) bf, p1.getHpL());
            PersonInfo p2 = GameDataBase.getInfo(who2);
            v1 = v1 > p2.getHpL() / 2 ? p2.getHpL() / 2 : v1;
            HpChangeBroadcast.INSTANCE.broadcast(who.longValue(), p2.getHp(), p2.getHp() + v1, v1, who.longValue(), HpChangeBroadcast.HpChangeReceiver.type.FROM_Q);
            p2.addHp(v1);
            putPerson(p2);
        }
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

    public static void addTagPack(NormalTagPack tagPack) {
        if (tagPack instanceof NormalWithWhoTagPack) {
            NormalWithWhoTagPack tag = (NormalWithWhoTagPack) tagPack;
            if (tag.getQ() < 0) {
                TagManagers.getTagManager(-tag.getWho()).addTag(tagPack);
            }
        } else {
            TagManagers.getTagManager(tagPack.getQ()).addTag(tagPack);
        }
    }

    /**
     * 添加护盾
     *
     * @param q 谁
     * @param v 值
     * @param t 时长 最大 30*60*1000
     */
    public static void addShield(long q, Long v, Long t) {
        addShield(q, v, getInfo(q).getHpL(), t);
    }

    /**
     * 添加护盾
     *
     * @param q
     * @param v
     */
    public static void addShield(long q, Long v) {
        addShield(q, v, 60 * 60 * 1000L);
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
        getInfo(q).addTag(SkillDataBase.TAG_SHIELD, v, maxV, t);
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

    public static long getCooling(Long qq, SkillInfo info) {
        if (challengeDetailService.isTemping(qq.longValue()) || GameConditionController.CONDITIONING.containsKey(qq.longValue())) {
            return System.currentTimeMillis() + info.getTimeL() / 40L;
        } else {
            return System.currentTimeMillis() + info.getTimeL();
        }
    }

    /**
     * 异步 攻击
     *
     * @param n
     * @param q1    被攻击者
     * @param q2    攻击者
     * @param value
     * @param eve
     */
    public static void addAttSchedule(int n, long q1, long q2, long value, long eve, long gid) {
        if (q1 == q2) {
            return;
        }
        AsynchronousThing thing = new AsynchronousAttack(n, q1, q2, value, eve, gid);
        thing.start();
        MapUtils.append(ASYNCHRONOUS_THING_MAP, q1, thing);
    }

    public static void addAttSchedule(int n, long q1, long q2, long value, long eve, long gid, String format) {
        addAttSchedule(n, q1, q2, value, eve, gid, format, DamageType.AD);
    }

    public static void addAttSchedule(int n, long q1, long q2, long value, long eve, long gid, String format, DamageType type) {
        if (q1 == q2) {
            return;
        }
        AsynchronousAttack thing = new AsynchronousAttack(n, q1, q2, value, eve, gid);
        thing.type = type;
        thing.setFormatStr(format).start();
        MapUtils.append(ASYNCHRONOUS_THING_MAP, q1, thing);
    }

    /**
     * 异步回血
     *
     * @param n     次数
     * @param q1    主动
     * @param q2    被动
     * @param value 值
     * @param eve   间隔
     * @param gid   gid
     */
    public static void addHFSchedule(int n, long q1, long value, long eve, long gid) {
        AsynchronousHf hf = new AsynchronousHf(n, q1, -1L, value, eve, gid);
        hf.start();
        MapUtils.append(ASYNCHRONOUS_THING_MAP, q1, hf);
    }

    public static String getTagDesc(long q) {
        StringBuilder sb = new StringBuilder();
        PersonInfo pInfo = getInfo(q);
        final int[] l0 = {0};
        SkillDataBase.TAG2NAME.forEach((k, v) -> {
            Number v0 = pInfo.getTagValue(k);
            if (v0 != null && v0.longValue() > 0) {
                String s0 = v + v0.toString() + ",";
                l0[0] = l0[0] + s0.length();
                if (l0[0] >= 15) {
                    sb.append(NEWLINE);
                    l0[0] = 0;
                }
                sb.append(s0);
            }
        });
        return sb.toString();
    }

}
