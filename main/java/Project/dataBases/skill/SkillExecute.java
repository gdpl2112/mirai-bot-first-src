package Project.dataBases.skill;

import Project.services.detailServices.GameBoneDetailService;
import io.github.kloping.mirai0.Entitys.gameEntitys.GhostObj;
import io.github.kloping.mirai0.Entitys.gameEntitys.PersonInfo;
import io.github.kloping.mirai0.Entitys.gameEntitys.Skill;
import io.github.kloping.mirai0.Entitys.gameEntitys.SkillInfo;
import io.github.kloping.mirai0.Entitys.gameEntitys.base.BaseInfo;
import io.github.kloping.mirai0.unitls.Tools.Tool;

import java.util.concurrent.CopyOnWriteArrayList;

import static Project.dataBases.GameDataBase.*;
import static Project.dataBases.skill.SkillDataBase.*;
import static Project.services.detailServices.GameDetailServiceUtils.*;
import static Project.services.detailServices.GameJoinDetailService.getGhostObjFrom;
import static Project.services.detailServices.GameSkillDetailService.*;
import static io.github.kloping.mirai0.unitls.Tools.Tool.rand;

/**
 * @author github-kloping
 */
public class SkillExecute {

    /**
     * 单体加血
     *
     * @param id
     * @param who
     * @param nums
     * @return
     */
    private static Skill create0(SkillInfo info, Number who, Number... nums) {
        Skill skill = new Skill(info, who, new CopyOnWriteArrayList<>(nums), "单体加血技能") {
            @Override
            public void before() {
                Long q = oneNearest(who.longValue(), nums);
                if (!exist(q)) {
                    return;
                }
                addHp(who.longValue(), q, info.getAddPercent());
                setTips("作用于 " + Tool.At(q));
            }
        };
        return skill;
    }

    /**
     * 群体加血
     *
     * @param id
     * @param who
     * @param nums
     * @return
     */
    private static Skill create1(SkillInfo info, Number who, Number... nums) {
        Skill skill = new Skill(info, who, new CopyOnWriteArrayList<>(nums), "群体加血") {

            @Override
            public void before() {
                for (Long q : nearest(3, who.longValue(), nums)) {
                    addHp(who.longValue(), q, info.getAddPercent());
                    setTips("作用于 " + Tool.At(q));
                }
            }
        };
        return skill;
    }

    /**
     * 单体魂力
     *
     * @param id
     * @param who
     * @param nums
     * @return
     */
    private static Skill create2(SkillInfo info, Number who, Number... nums) {
        Skill skill = new Skill(info, who, new CopyOnWriteArrayList<>(nums), "单体加魂力技能") {
            @Override
            public void before() {
                Long q = oneNearest(who.longValue(), nums);
                if (!exist(q)) {
                    return;
                }
                addHl(who, q, info.getAddPercent());
                setTips("作用于 " + Tool.At(q));
            }
        };
        return skill;
    }

    /**
     * 群体加魂力
     *
     * @param id
     * @param who
     * @param nums
     * @return
     */
    private static Skill create3(SkillInfo info, Number who, Number... nums) {
        Skill skill = new Skill(info, who, new CopyOnWriteArrayList<>(nums), "群体加魂力") {

            @Override
            public void before() {
                for (Long q : nearest(3, who.longValue(), nums)) {
                    addHl(who.longValue(), q, info.getAddPercent());
                    setTips("作用于 " + Tool.At(q));
                }
            }
        };
        return skill;
    }

    /**
     * 单体加攻击
     *
     * @param id
     * @param who
     * @param nums
     * @return
     */
    private static Skill create4(SkillInfo info, Number who, Number... nums) {
        Skill skill = new Skill(info, who, new CopyOnWriteArrayList<>(nums), "单体加攻击技能") {
            @Override
            public void before() {
                Long q = oneNearest(who, nums);
                if (!exist(q)) {
                    return;
                }
                PersonInfo info_ = getInfo(q);
                Long lon = info_.getAtt();
                long v = percentTo(info.getAddPercent(), lon);
                v = v > info_.getAtt() ? info_.getAtt() : v;
                addAttHasTime(who.longValue(), new HasTimeAdder(System.currentTimeMillis() + t4, who.longValue(), v));
                setTips("作用于 " + Tool.At(q));
            }
        };
        return skill;
    }

    /**
     * 吸血
     *
     * @param info
     * @param who
     * @param nums
     * @return
     */
    private static Skill create6(SkillInfo info, Number who, Number... nums) {
        Skill skill = new Skill(info, who, new CopyOnWriteArrayList<>(nums), "吸血") {
            @Override
            public void before() {
                putPerson(getInfo(who).addTag(TAG_XX, info.getAddPercent()));
                setTips("作用于 " + Tool.At(who.longValue()));
            }

            @Override
            public void run() {
                super.run();
                try {
                    Thread.sleep(t6);
                    putPerson(getInfo(who).eddTag(TAG_XX, info.getAddPercent()));
                    setTips("吸血失效");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        return skill;
    }

    /**
     * 群体加攻击
     *
     * @param id
     * @param who
     * @param nums
     * @return
     */
    private static Skill create5(SkillInfo info, Number who, Number... nums) {
        Skill skill = new Skill(info, who, new CopyOnWriteArrayList<>(nums), "群体加攻击") {

            @Override
            public void before() {
                for (Long q : nearest(3, who.longValue(), nums)) {
                    if (!exist(q)) {
                        continue;
                    }
                    PersonInfo info_ = getInfo(q);
                    Long lon = info_.getAtt();
                    long v = percentTo(info.getAddPercent(), lon);
                    v = v > info_.getAtt() ? info_.getAtt() : v;
                    addAttHasTime(who.longValue(), new HasTimeAdder(System.currentTimeMillis() + t5, q.longValue(), v));
                    setTips("作用于 " + Tool.At(q));
                }
            }
        };
        return skill;
    }

    /**
     * 反甲
     *
     * @param info
     * @param who
     * @param nums
     * @return
     */
    private static Skill create7(SkillInfo info, Number who, Number... nums) {
        Skill skill = new Skill(info, who, new CopyOnWriteArrayList<>(nums), "反甲") {
            @Override
            public void before() {
                putPerson(getInfo(who).addTag(TAG_FJ, info.getAddPercent()));
                setTips("作用于 " + Tool.At(who.longValue()));
            }

            @Override
            public void run() {
                super.run();
                try {
                    Thread.sleep(t6);
                    putPerson(getInfo(who).eddTag(TAG_FJ, info.getAddPercent()));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        return skill;
    }

    /**
     * 单体攻击性
     *
     * @param info
     * @param who
     * @param nums
     * @return
     */
    private static Skill create8(SkillInfo info, Number who, Number... nums) {
        Skill skill = new Skill(info, who, new CopyOnWriteArrayList<>(nums), "单体攻击") {
            @Override
            public void before() {
                if (nums.length == 0) {
                    return;
                }
                StringBuilder sb = new StringBuilder();
                long v = percentTo(info.getAddPercent(), getInfo(who).getAtt());
                attGhostOrMan(sb, who, nums[0], v);
                setTips(sb.toString());
            }
        };
        return skill;
    }

    /**
     * 持续恢复技能
     *
     * @param info
     * @param who
     * @param nums
     * @return
     */
    private static Skill create9(SkillInfo info, Number who, Number... nums) {
        Skill skill = new Skill(info, who, new CopyOnWriteArrayList<>(nums), "__") {
            @Override
            public void before() {
                v = 1;
                q = oneNearest(who, nums);
                eve();
                setTips("作用于 " + Tool.At(q));
            }

            private Integer v;
            private long q;

            @Override
            public void run() {
                super.run();
                try {
                    Thread.sleep(24 * 1000);
                    while (eve()) {
                        continue;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            private boolean eve() {
                if (v++ >= 5) {
                    return false;
                }
                long l = getInfo(q).getHpL();
                putPerson(getInfo(q).addHp(percentTo(info.getAddPercent(), l)));
                return true;
            }
        };
        return skill;
    }

    /**
     * 名刀司命
     *
     * @param info
     * @param who
     * @param nums
     * @return
     */
    private static Skill create10(SkillInfo info, Number who, Number... nums) {
        Skill skill = new Skill(info, who, new CopyOnWriteArrayList<>(nums), "免死") {
            @Override
            public void before() {
                putPerson(getInfo(who).addTag(TAG_MS, 0));
                setTips("作用于 " + Tool.At(who.longValue()));
            }

            @Override
            public void run() {
                super.run();
                try {
                    Thread.sleep(info.getAddPercent() * 1000);
                    putPerson(getInfo(who).eddTag(TAG_MS, 0));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        return skill;
    }

    /**
     * 真伤
     *
     * @param info
     * @param who
     * @param nums
     * @return
     */
    private static Skill create11(SkillInfo info, Number who, Number... nums) {
        Skill skill = new Skill(info, who, new CopyOnWriteArrayList<>(nums), "狂热") {
            @Override
            public void before() {
                q = oneNearest(who, nums);
                putPerson(getInfo(q).addTag(TAG_TRUE, 1));
                setTips("作用于 " + Tool.At(q));
            }

            private Long q;

            @Override
            public void run() {
                super.run();
                try {
                    Thread.sleep(t11);
                    putPerson(getInfo(q).eddTag(TAG_TRUE, 1));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        return skill;
    }

    /**
     * 增速 闪避
     *
     * @param info
     * @param who
     * @param nums
     * @return
     */
    private static Skill create12(SkillInfo info, Number who, Number... nums) {
        Skill skill = new Skill(info, who, new CopyOnWriteArrayList<>(nums), "增速闪避") {

            @Override
            public void before() {
                q = oneNearest(who, nums);
                v = Long.valueOf(info.getAddPercent());
                GameBoneDetailService.addForAttr(q, v, GameBoneDetailService.Type.HIDE_PRO);
                setTips("作用于 " + Tool.At(q));
            }

            private Long q, v;

            @Override
            public void run() {
                super.run();
                try {
                    Thread.sleep(t12);
                    GameBoneDetailService.addForAttr(q, -v, GameBoneDetailService.Type.HIDE_PRO);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        return skill;
    }

    /**
     * 减魂力
     *
     * @param info
     * @param who
     * @param nums
     * @return
     */
    private static Skill create13(SkillInfo info, Number who, Number... nums) {
        Skill skill = new Skill(info, who, new CopyOnWriteArrayList<>(nums), "减魂力") {
            @Override
            public void before() {
                if (nums == null || nums.length == 0) {
                    setTips("该玩家未注册");
                    return;
                }
                PersonInfo pi = getInfo(nums[0]);
                long m = pi.getHll();
                long v = percentTo(info.getAddPercent(), m);
                v = v > pi.getHll() ? pi.getHll() : v;
                pi.addHl(-v);
                putPerson(pi);
                setTips("令" + Tool.At(nums[0].longValue()) + "魂力减少");
            }
        };
        return skill;
    }

    /**
     * 令无法躲避
     *
     * @param info
     * @param who
     * @param nums
     * @return
     */
    private static Skill create14(SkillInfo info, Number who, Number... nums) {
        Skill skill = new Skill(info, who, new CopyOnWriteArrayList<>(nums), "无法躲避") {
            private Long q;

            @Override
            public void before() {
                if (nums.length < 1) {
                    return;
                }
                q = nums[0].longValue();
                putPerson(getInfo(q).addTag(TAG_CANT_HIDE, 0));
                setTips("作用于 " + Tool.At(q));
            }
        };
        return skill;
    }

    /**
     * 永久护盾
     *
     * @param info
     * @param who
     * @param nums
     * @return
     */
    private static Skill create15(SkillInfo info, Number who, Number... nums) {
        Skill skill = new Skill(info, who, new CopyOnWriteArrayList<>(nums), "永久护盾") {
            @Override
            public void before() {
                long v = getInfo(who).getHp();
                int b = info.getAddPercent();
                long v2 = percentTo(b, v);
                putPerson(getInfo(who).addTag(TAG_SHIELD, v2));
                setTips("作用于 " + Tool.At(who.longValue()));
            }
        };
        return skill;
    }

    /**
     * 临时护盾
     *
     * @param info
     * @param who
     * @param nums
     * @return
     */
    private static Skill create16(SkillInfo info, Number who, Number... nums) {
        Skill skill = new Skill(info, who, new CopyOnWriteArrayList<>(nums), "临时护盾") {
            @Override
            public void before() {
                v = getInfo(who).getHp();
                int b = info.getAddPercent();
                v = (long) (b / 5f);
                long v2 = percentTo(b, v);
                putPerson(getInfo(who).addTag(TAG_SHIELD, v2));
                setTips("作用于 " + Tool.At(who.longValue()));
            }

            long v;

            @Override
            public void run() {
                super.run();
                try {
                    Thread.sleep(v * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                putPerson(getInfo(who).eddTag(TAG_SHIELD));
                setTips("护盾消失");
            }
        };
        return skill;
    }

    /**
     * 玄玉手
     *
     * @param info
     * @param who
     * @param nums
     * @return
     */
    private static Skill create17(SkillInfo info, Number who, Number... nums) {
        Skill skill = new Skill(info, who, new CopyOnWriteArrayList<>(nums), "玄玉手") {
            @Override
            public void before() {
                getInfo(who).addTag(TAG_XUAN_YU_S, 1).apply();
                setTips("作用于 " + Tool.At(who.longValue()));
            }

            @Override
            public void run() {
                super.run();
                try {
                    Thread.sleep(info.getAddPercent() * 1000L);
                    getInfo(who).eddTag(TAG_XUAN_YU_S, 1).apply();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        return skill;
    }

    /**
     * 斩杀技
     *
     * @param info
     * @param who
     * @param nums
     * @return
     */
    private static Skill create18(SkillInfo info, Number who, Number... nums) {
        Skill skill = new Skill(info, who, new CopyOnWriteArrayList<>(nums), "斩杀技") {
            @Override
            public void before() {
                if (nums.length == 0) {
                    setTips("未选择");
                    return;
                }
                long maxTemp = 0, thTemp = 0;
                if (nums[0].longValue() < 0 || nums[0].longValue() == -2) {
                    GhostObj ghostObj = getGhostObjFrom(who.longValue());
                    if (ghostObj == null) {
                        setTips("未遇见魂兽");
                        return;
                    }
                    maxTemp = ghostObj.getMaxHp();
                    thTemp = ghostObj.getHp();
                } else {
                    PersonInfo in = getInfo(nums[0]);
                    maxTemp = in.getHpL();
                    thTemp = in.getHp();
                }
                final long max = maxTemp > 0 ? maxTemp : 0;
                final long th = thTemp > 0 ? thTemp : 0;
                int b = info.getAddPercent();
                long att = getInfo(who).getAtt();
                int x = toPercent(th, max);
                x = 100 - x;
                int y = (int) (((float) x / 50f) * 100);
                long v = percentTo(b, att);
                v = percentTo(y, v);
                StringBuilder sb = new StringBuilder();
                v = v < 0 ? 0 : v;
                attGhostOrMan(sb, who, nums[0], v);
                setTips(sb.toString());
            }
        };
        return skill;
    }

    /**
     * 蓄力技
     *
     * @param info
     * @param who
     * @param nums
     * @return
     */
    private static Skill create19(SkillInfo info, Number who, Number... nums) {
        Skill skill = new Skill(info, who, new CopyOnWriteArrayList<>(nums), "蓄力1") {
            @Override
            public void before() {
                if (nums.length == 0) {
                    setTips("未选择");
                    return;
                }
                int r = rand.nextInt(20) - 10;
                if (nums[0].longValue() < 0) {
                    GhostObj ghostObj = getGhostObjFrom(who.longValue());
                    if (ghostObj == null) {
                        setTips("未遇见魂兽");
                    }
                } else {
                    PersonInfo in = getInfo(nums[0]);
                }
                b = info.getAddPercent() + r;
                setTips(String.format("将造成%s%%(%s)伤害", b, percentTo(b, getInfo(who).getAtt())));
            }

            private int b;

            @Override
            public void run() {
                super.run();
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                long att = getInfo(who).getAtt();
                long v = percentTo(b, att);
                StringBuilder sb = new StringBuilder();
                attGhostOrMan(sb, who, nums[0], v);
                setTips(sb.toString());
            }
        };
        return skill;
    }

    /**
     * 眩晕技
     *
     * @param info
     * @param who
     * @param nums
     * @return
     */
    private static Skill create20(SkillInfo info, Number who, Number... nums) {
        return new Skill(info, who, new CopyOnWriteArrayList<>(nums), "眩晕技") {
            BaseInfo b2 = null;

            float t = 0L;

            @Override
            public void before() {
                if (nums.length > 0) {
                    long q = nums[0].longValue();
                    BaseInfo b1 = getBaseInfoFromAny(who, who);
                    BaseInfo b2 = null;
                    b2 = getBaseInfoFromAny(who, q);
                    float i = info.getAddPercent();
                    if (b1.getHj() > b2.getHj() && b1.getHjL() > b2.getHjL()) {
                        i *= 2;
                    } else if (b2.getHj() > b1.getHj()) {
                        i /= 2;
                    }
                    t = i > 0 ? (long) i : 1L;
                    b2.letVertigo((long) (t * info.getAddPercent() * 1000L));
                    b2.apply();
                    setTips("作用于 " + b2.getName());
                } else {
                    setTips("未选择任何");
                }
            }

            @Override
            public void run() {
                super.run();
            }
        };
    }

    //=====================================================================================

    /**
     * 蓝电 霸王
     *
     * @param info
     * @param who
     * @param nums
     * @return
     */
    private static Skill create71(SkillInfo info, Number who, Number... nums) {
        Skill skill = new Skill(info, who, new CopyOnWriteArrayList<>(nums), "蓝电霸王龙武魂真身") {
            @Override
            public void before() {
                StringBuilder sb = new StringBuilder();
                for (Long q : nearest(2, nums)) {
                    long v = percentTo(60, getInfo(who).getAtt());
                    attGhostOrMan(sb, who, q, v);
                }
                setTips(sb.toString());
            }

            @Override
            public void run() {
                super.run();
                try {
                    Thread.sleep(10 * 1000);
                    StringBuilder sb = new StringBuilder();
                    for (Long q : nearest(2, nums)) {
                        long v = percentTo(30, getInfo(who).getAtt());
                        attGhostOrMan(sb, who, q, v);
                    }
                    setTips(sb.toString());
                    sb = new StringBuilder();
                    Thread.sleep(10 * 1000);
                    for (Long q : nearest(2, nums)) {
                        long v = percentTo(10, getInfo(who).getAtt());
                        attGhostOrMan(sb, who, q, v);
                    }
                    setTips(sb.toString());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                setTips("武魂真身失效");
            }
        };
        return skill;
    }

    /**
     * 昊天锤
     *
     * @param sb
     * @param v
     * @param nums
     * @param who
     */
    private static Skill create72(SkillInfo info, Number who, Number... nums) {
        Skill skill = new Skill(info, who, new CopyOnWriteArrayList<>(nums), "昊天真身") {

            @Override
            public void before() {
                Long q = who.longValue();
                PersonInfo pinfo = getInfo(q);
                Long lon = pinfo.getAtt();
                long v = percentTo(info.getAddPercent(), lon);
                addAttHasTime(who.longValue(), new HasTimeAdder(System.currentTimeMillis() + t72, who.longValue(), v));
            }

            @Override
            public void run() {
                super.run();
                try {
                    Thread.sleep(t72);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                setTips("武魂真身失效");
            }
        };
        return skill;
    }

    /**
     * 天使真身
     *
     * @param sb
     * @param v
     * @param nums
     * @param who
     */
    private static Skill create73(SkillInfo info, Number who, Number... nums) {
        Skill skill = new Skill(info, who, new CopyOnWriteArrayList<>(nums), "天使真身") {

            @Override
            public void before() {
                PersonInfo info1 = getInfo(who);
                long v = percentTo(info.getAddPercent(), info1.getAtt());
                addAttHasTime(who.longValue(), new HasTimeAdder(System.currentTimeMillis() + t73, who.longValue(), v));
                eve();
            }

            private Integer c = 1;

            @Override
            public void run() {
                super.run();
                try {
                    Thread.sleep(100000);
                    if (c++ > t73 / 100000) {
                        eve();
                        run();
                    } else {
                        over();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            private void over() {
                setTips("武魂真身失效");
            }

            private void eve() {
                PersonInfo info1 = getInfo(who);
                Long v1 = percentTo(5, info1.getHll());
                putPerson(getInfo(who).addHl(v1));
            }
        };
        return skill;
    }

    /**
     * 噬魂蛛皇
     *
     * @param info
     * @param who
     * @param nums
     * @return
     */
    private static Skill create74(SkillInfo info, Number who, Number... nums) {
        Skill skill = new Skill(info, who, new CopyOnWriteArrayList<>(nums), "噬魂蛛皇真身") {
            @Override
            public void before() {
                if (nums.length == 0) {
                    return;
                }
                long v1 = getAttFromAny(who, nums[0]);
                if (v1 == 0) {
                    setTips("该玩家未注册");
                    return;
                }
                v = percentTo(info.getAddPercent(), v1);
                eddAttAny(who, nums[0], v);
                putPerson(getInfo(who).addHp(v / 2));
            }

            long v;

            @Override
            public void run() {
                super.run();
                try {
                    Thread.sleep(t74);
                    eddAttAny(who, nums[0], -v);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                setTips("武魂真身失效");
            }
        };
        return skill;
    }

    /**
     * 蓝银皇真身
     *
     * @param sb
     * @param v
     * @param nums
     * @param who
     */
    private static Skill create75(SkillInfo info, Number who, Number... nums) {
        Skill skill = new Skill(info, who, new CopyOnWriteArrayList<>(nums), "蓝银皇真身") {

            @Override
            public void before() {
                Long q = who.longValue();
                if (!exist(q)) {
                    return;
                }
                PersonInfo pInfo = getInfo(q);
                Long lon = pInfo.getAtt();
                long v = percentTo(info.getAddPercent() * 4, lon);
                addAttHasTime(who.longValue(), new HasTimeAdder(System.currentTimeMillis() + t75, who.longValue(), v));
                putPerson(pInfo);
                eve();
            }

            private int c = 1;

            @Override
            public void run() {
                super.run();
                try {
                    if (c++ > t75C) {
                        setTips("武魂真身失效");
                        return;
                    }
                    Thread.sleep(t75);
                    eve();
                    run();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            public void eve() {
                PersonInfo info_ = getInfo(who);
                long v = percentTo(info.getAddPercent(), info_.getHp());
                putPerson(getInfo(who).addHp(v));
            }
        };
        return skill;
    }

    /**
     * 柔骨兔
     *
     * @param who
     * @param num
     * @param v
     * @return
     */
    private static Skill create76(SkillInfo info, Number who, Number... nums) {
        Skill skill = new Skill(info, who, new CopyOnWriteArrayList<>(nums), "柔骨兔真身") {
            @Override
            public void before() {
                putPerson(getInfo(who).addTag(TAG_WD, 0));
            }

            @Override
            public void run() {
                super.run();
                try {
                    Thread.sleep(info.getAddPercent() * 1000);
                    putPerson(getInfo(who).eddTag(TAG_WD, 0));
                    setTips("无敌失效");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        return skill;
    }

    /**
     * 白虎真身
     *
     * @param sb
     * @param v
     * @param nums
     * @param who
     */
    private static Skill create77(SkillInfo info, Number who, Number... nums) {
        Skill skill = new Skill(info, who, new CopyOnWriteArrayList<>(nums), "白虎真身") {
            @Override
            public void before() {
                Long q = who.longValue();
                if (!exist(q)) {
                    return;
                }
                long v = percentTo(info.getAddPercent(), getInfo(q).getAtt());
                addAttHasTime(who.longValue(), new HasTimeAdder(System.currentTimeMillis() + t77, who.longValue(), v));
            }
        };
        return skill;
    }

    /**
     * 邪火凤凰
     *
     * @param who
     * @param num
     * @param v
     * @return
     */
    private static Skill create78(SkillInfo info, Number who, Number... nums) {
        Skill skill = new Skill(info, who, new CopyOnWriteArrayList<>(nums), "邪火凤凰真身") {
            @Override
            public void before() {
                Long q = who.longValue();
                if (!exist(q)) {
                    return;
                }
                PersonInfo info1 = getInfo(who);
                int n = toPercent(info1.getHl(), info1.getHll());
                int n2 = n / 2;
                int a = info.getAddPercent() + n2;
                long v = percentTo(a, info1.getHll());
                addAttHasTime(who.longValue(), new HasTimeAdder(System.currentTimeMillis() + t78, who.longValue(), v));
                setTips("剩余" + n + "% 的魂力,增加" + a + "%的攻击力");
            }
        };
        return skill;
    }

    /**
     * 七杀真身
     *
     * @param sb
     * @param v
     * @param nums
     * @param who
     */
    private static Skill create79(SkillInfo info, Number who, Number... nums) {
        Skill skill = new Skill(info, who, new CopyOnWriteArrayList<>(nums), "七杀真身") {
            @Override
            public void before() {
                Long q = who.longValue();
                if (!exist(q)) {
                    return;
                }
                long v = percentTo(info.getAddPercent(), getInfo(q).getAtt());
                addAttHasTime(who.longValue(), new HasTimeAdder(System.currentTimeMillis() + t4, who.longValue(), v));
            }
        };
        return skill;
    }

    /**
     * 碧灵蛇皇
     *
     * @param info
     * @param who
     * @param nums
     * @return
     */
    private static Skill create710(SkillInfo info, Number who, Number... nums) {
        Skill skill = new Skill(info, who, new CopyOnWriteArrayList<>(nums), "碧灵蛇皇") {
            @Override
            public void before() {
                StringBuilder sb = new StringBuilder();
                for (Long number : nearest(2, nums)) {
                    long v = percentTo(30, getInfo(who).getAtt());
                    attGhostOrMan(sb, who, number, v);
                }
                setTips(sb.toString());
            }

            @Override
            public void run() {
                super.run();
                try {
                    Thread.sleep(10 * 1000);
                    StringBuilder sb = new StringBuilder();
                    for (Long number : nearest(2, nums)) {
                        long v = percentTo(30, getInfo(who).getAtt());
                        attGhostOrMan(sb, who, number, v);
                    }
                    setTips(sb.toString());
                    sb = new StringBuilder();
                    Thread.sleep(10 * 1000);
                    for (Long number : nearest(2, nums)) {
                        long v = percentTo(30, getInfo(who).getAtt());
                        attGhostOrMan(sb, who, number, v);
                    }
                    setTips(sb.toString());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                setTips("武魂真身失效");
            }
        };
        return skill;
    }

    /**
     * 破魂枪真身
     *
     * @param sb
     * @param v
     * @param nums
     * @param who
     */
    private static Skill create711(SkillInfo info, Number who, Number... nums) {
        Skill skill = new Skill(info, who, new CopyOnWriteArrayList<>(nums), "破魂枪真身") {

            @Override
            public void before() {
                Long q = who.longValue();
                if (!exist(q)) {
                    return;
                }
                long v = percentTo(info.getAddPercent(), getInfo(q).getAtt());
                addAttHasTime(who.longValue(), new HasTimeAdder(System.currentTimeMillis() + t4, who.longValue(), v));
            }
        };
        return skill;
    }

    /**
     * 大力金刚熊真身
     *
     * @param sb
     * @param v
     * @param nums
     * @param who
     */
    private static Skill create712(SkillInfo info, Number who, Number... nums) {
        Skill skill = new Skill(info, who, new CopyOnWriteArrayList<>(nums), "大力金刚熊真身") {
            @Override
            public void before() {
                Long q = who.longValue();
                if (!exist(q)) {
                    return;
                }
                long v = percentTo(info.getAddPercent(), getInfo(q).getAtt());
                addAttHasTime(who.longValue(), new HasTimeAdder(System.currentTimeMillis() + t4, who.longValue(), v));
            }
        };
        return skill;
    }

    /**
     * 奇茸通天菊真身
     *
     * @param sb
     * @param v
     * @param nums
     * @param who
     */
    private static Skill create713(SkillInfo info, Number who, Number... nums) {
        Skill skill = new Skill(info, who, new CopyOnWriteArrayList<>(nums), "奇茸通天菊真身") {
            @Override
            public void before() {
                Long q = who.longValue();
                PersonInfo info_ = getInfo(q);
                Long lon = info_.getAtt();
                long v = percentTo(info.getAddPercent(), lon);
                addAttHasTime(who.longValue(), new HasTimeAdder(System.currentTimeMillis() + t4, who.longValue(), v));
                try {
                    if (nums.length != 0) {
                        putPerson(getInfo(nums[0]).addTag(SkillDataBase.TAG_CANT_HIDE, 0));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        return skill;
    }

    /**
     * 鬼魅真身
     *
     * @param sb
     * @param v
     * @param nums
     * @param who
     */
    private static Skill create714(SkillInfo info, Number who, Number... nums) {
        Skill skill = new Skill(info, who, new CopyOnWriteArrayList<>(nums), "鬼魅真身") {

            @Override
            public void before() {
                Long q = who.longValue();
                if (!exist(q)) {
                    return;
                }
                Long lon = getInfo(q).getAtt();
                long v = percentTo(info.getAddPercent(), lon);
                addAttHasTime(who.longValue(), new HasTimeAdder(System.currentTimeMillis() + t4, who.longValue(), v));
                try {
                    if (nums.length != 0) {
                        putPerson(getInfo(nums[0]).addTag(SkillDataBase.TAG_CANT_HIDE, 0));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        return skill;
    }

    /**
     * 刺豚真身
     *
     * @param sb
     * @param v
     * @param nums
     * @param who
     */
    private static Skill create715(SkillInfo info, Number who, Number... nums) {
        Skill skill = new Skill(info, who, new CopyOnWriteArrayList<>(nums), "刺豚真身") {
            private Long v = (long) (info.getAddPercent() / 2);
            private Long eveV;

            @Override
            public void before() {
                PersonInfo info_ = getInfo(who);
                long v = info_.getHpL();
                int p = info.getAddPercent();
                long o = percentTo(p, v);
                info_.addTag(TAG_SHIELD, o);
                putPerson(info_);
                eveV = info_.getHpL() / 50;
            }

            @Override
            public void run() {
                super.run();
                try {
                    if (v < 10) {
                        setTips("武魂真身失效.");
                        return;
                    }
                    Thread.sleep(10 * 1000);
                    v -= 10;
                    putPerson(getInfo(who).addHp(eveV));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        return skill;
    }

    /**
     * 蛇矛真身
     *
     * @param info
     * @param who
     * @param nums
     * @return
     */
    private static Skill create716(SkillInfo info, Number who, Number... nums) {
        return new Skill(info, who, new CopyOnWriteArrayList<>(nums), "蛇矛真身") {
            @Override
            public void before() {
                Long q = who.longValue();
                if (!exist(q)) {
                    return;
                }
                PersonInfo info_ = getInfo(q);
                Long lon = info_.getAtt();
                int p = info.getAddPercent();
                long v = percentTo(p, lon);
                long o = percentTo(p, v);
                info_.addTag(TAG_SHIELD, o);
                addAttHasTime(who.longValue(), new HasTimeAdder(System.currentTimeMillis() + t716, who.longValue(), v));
                putPerson(info_.addTag(TAG_XX, info.getAddPercent() / 8));
            }
        };
    }

    /**
     * 骨龙真身
     *
     * @param info
     * @param who
     * @param nums
     * @return
     */
    private static Skill create717(SkillInfo info, Number who, Number... nums) {
        return new Skill(info, who, new CopyOnWriteArrayList<>(nums), "骨龙") {
            @Override
            public void before() {
                Long q = who.longValue();
                PersonInfo info_ = getInfo(q);
                Long lon = info_.getAtt();
                long v = percentTo(info.getAddPercent(), lon);
                info_.addTag(TAG_SHIELD, percentTo(info.getAddPercent(), info_.getHpL()));
                putPerson(info_);
                addAttHasTime(who.longValue(), new HasTimeAdder(System.currentTimeMillis() + t717, who.longValue(), v));
            }
        };
    }

    /**
     * 蛇杖真身
     *
     * @param info
     * @param who
     * @param nums
     * @return
     */
    private static Skill create718(SkillInfo info, Number who, Number... nums) {
        return new Skill(info, who, new CopyOnWriteArrayList<>(nums), "蛇杖真身") {
            @Override
            public void before() {
                Long q = who.longValue();
                PersonInfo info_ = getInfo(q);
                Long lon = info_.getAtt();
                int p = info.getAddPercent();
                long v = percentTo(p, lon);
                long o = percentTo(p, v);
                info_.addTag(TAG_SHIELD, o);
                addAttHasTime(who.longValue(), new HasTimeAdder(System.currentTimeMillis() + t718, who.longValue(), v));
                putPerson(info_.addTag(TAG_XX, info.getAddPercent() / 8));
            }
        };
    }

    /**
     * 蓝银草真身
     *
     * @param sb
     * @param v
     * @param nums
     * @param who
     */
    private static Skill create719(SkillInfo info, Number who, Number... nums) {
        Skill skill = new Skill(info, who, new CopyOnWriteArrayList<>(nums), "蓝银草真身") {

            @Override
            public void before() {
                eve();
            }

            private int c = 1;

            @Override
            public void run() {
                super.run();
                try {
                    if (c++ > t79C) {
                        setTips("武魂真身失效");
                        return;
                    }
                    Thread.sleep(t79);
                    eve();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            public void eve() {
                addHp(who, who.longValue(), info.getAddPercent());
            }
        };
        return skill;
    }


    /**
     * 玄龟真身
     *
     * @param sb
     * @param v
     * @param nums
     * @param who
     */
    private static Skill create720(SkillInfo info, Number who, Number... nums) {
        Skill skill = new Skill(info, who, new CopyOnWriteArrayList<>(nums), "玄龟真身") {
            @Override
            public void before() {
                PersonInfo info_ = getInfo(who);
                long v = info_.getHpL();
                int p = info.getAddPercent();
                long o = percentTo(p, v);
                info_.addTag(TAG_SHIELD, o);
                putPerson(info_);
            }
        };
        return skill;
    }

    /**
     * 幽冥真身
     *
     * @param info
     * @param who
     * @param nums
     * @return
     */
    private static Skill create721(SkillInfo info, Number who, Number... nums) {
        return new Skill(info, who, new CopyOnWriteArrayList<>(nums), "幽冥真身") {
            @Override
            public void before() {
                Long q = who.longValue();
                PersonInfo p_info = getInfo(q);
                Long lon = p_info.getAtt();
                v2 = percentTo(info.getAddPercent(), lon);
                addAttHasTime(who.longValue(), new HasTimeAdder(System.currentTimeMillis() + t721, who.longValue(), v2));
                q1 = Long.valueOf(who + "");
                v = Long.valueOf(info.getAddPercent());
                GameBoneDetailService.addForAttr(q1, v, GameBoneDetailService.Type.HIDE_PRO);
            }

            private long v;
            private Long v2;
            private Long q1;

            @Override
            public void run() {
                super.run();
                try {
                    Thread.sleep(t721);
                    GameBoneDetailService.addForAttr(q1, -v, GameBoneDetailService.Type.HIDE_PRO);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }

    /**
     * 光明圣龙
     *
     * @param sb
     * @param v
     * @param nums
     * @param who
     */
    private static Skill create722(SkillInfo info, Number who, Number... nums) {
        Skill skill = new Skill(info, who, new CopyOnWriteArrayList<>(nums), "光明圣龙真身") {
            private Long v1;

            @Override
            public void before() {
                Long q = who.longValue();
                PersonInfo info_ = getInfo(q);
                Long lon = info_.getAtt();
                int b = info.getAddPercent();
                v1 = percentTo(b, lon);
                long v2 = percentTo(b / 2, info_.getHpL());
                long v3 = percentTo(b / 2, info_.getHll());
                long v4 = percentTo(b / 2, info_.getHjL());
                info_.addHp(v2);
                info_.addHl(v3);
                info_.addHj(v4);
                putPerson(info_);
                addAttHasTime(who.longValue(), new HasTimeAdder(System.currentTimeMillis() + t722, who.longValue(), v1));
            }
        };
        return skill;
    }

    /**
     * 黑暗圣龙
     *
     * @param sb
     * @param v
     * @param nums
     * @param who
     */
    private static Skill create723(SkillInfo info, Number who, Number... nums) {
        Skill skill = new Skill(info, who, new CopyOnWriteArrayList<>(nums), "黑暗圣龙真身") {
            private Long v1;

            @Override
            public void before() {
                Long q = who.longValue();
                PersonInfo info_ = getInfo(q);
                Long lon = info_.getAtt();
                int b = info.getAddPercent();
                v1 = percentTo(b, lon);
                long v4 = percentTo(b / 2, info_.getHjL());
                info_.addHj(v4);
                info_.addTag(TAG_XX, b / 5);
                putPerson(info_);
                addAttHasTime(who.longValue(), new HasTimeAdder(System.currentTimeMillis() + t723, who.longValue(), v1));
            }
        };
        return skill;
    }

    /**
     * 修罗神剑
     *
     * @param sb
     * @param v
     * @param nums
     * @param who
     */
    private static Skill create724(SkillInfo info, Number who, Number... nums) {
        Skill skill = new Skill(info, who, new CopyOnWriteArrayList<>(nums), "修罗神剑") {
            @Override
            public void before() {
                Long q = who.longValue();
                PersonInfo info_ = getInfo(q);
                Long lon = info_.getAtt();
                long v = percentTo(info.getAddPercent(), lon);
                info_.addHl(percentTo(info.getAddPercent() / 3, info_.getHll()));
                putPerson(info_.addTag(TAG_TRUE, 1));
                addAttHasTime(who.longValue(), new HasTimeAdder(System.currentTimeMillis() + t724, who.longValue(), v));
            }

            @Override
            public void run() {
                super.run();
                try {
                    Thread.sleep(t724);
                    putPerson(getInfo(who).eddTag(TAG_TRUE, 1));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        return skill;
    }

    /**
     * 青龙真身
     *
     * @param info
     * @param who
     * @param nums
     * @return
     */
    private static Skill create725(SkillInfo info, Number who, Number... nums) {
        return new Skill(info, who, new CopyOnWriteArrayList<>(nums), "青龙真身") {
            @Override
            public void before() {
                Long q = who.longValue();
                PersonInfo info_ = getInfo(q);
                Long lon = info_.getAtt();
                int b = info.getAddPercent();
                v1 = percentTo(b, lon);
                info_.addTag(TAG_FJ, b / 3);
                putPerson(info_);
                addAttHasTime(who.longValue(), new HasTimeAdder(System.currentTimeMillis() + t725, who.longValue(), v1));
            }

            long v1;

            @Override
            public void run() {
                super.run();
                try {
                    Thread.sleep(t725);
                    putPerson(getInfo(who).eddTag(TAG_FJ));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                setTips("武魂真身失效");
            }
        };
    }

    /**
     * 海神
     *
     * @param info
     * @param who
     * @param nums
     * @return
     */
    private static Skill create726(SkillInfo info, Number who, Number... nums) {
        return new Skill(info, who, new CopyOnWriteArrayList<>(nums), "海神") {
            @Override
            public void before() {
                Long q = who.longValue();
                PersonInfo info_ = getInfo(q);
                Long lon = info_.getAtt();
                int b = info.getAddPercent();
                v1 = percentTo(b, lon);
                putPerson(info_.eddTag(TAG_SHE, b / 8));
                addAttHasTime(who.longValue(), new HasTimeAdder(System.currentTimeMillis() + t726, who.longValue(), v1));
            }

            private long v1;

            @Override
            public void run() {
                super.run();
                try {
                    Thread.sleep(t726);
                    putPerson(getInfo(who).eddTag(TAG_SHE));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                setTips("武魂真身失效");
            }
        };
    }

    /**
     * 锄头
     *
     * @param sb
     * @param v
     * @param nums
     * @param who
     */
    private static Skill create727(SkillInfo info, Number who, Number... nums) {
        Skill skill = new Skill(info, who, new CopyOnWriteArrayList<>(nums), "锄头真身") {
            @Override
            public void before() {
                Long q = who.longValue();
                PersonInfo info_ = getInfo(q);
                Long lon = info_.getAtt();
                long v = percentTo(info.getAddPercent(), lon);
                putPerson(info_);
                addAttHasTime(who.longValue(), new HasTimeAdder(System.currentTimeMillis() + t727, who.longValue(), v));
            }
        };
        return skill;
    }

    /**
     * 斧头天锤
     *
     * @param sb
     * @param v
     * @param nums
     * @param who
     */
    private static Skill create728(SkillInfo info, Number who, Number... nums) {
        Skill skill = new Skill(info, who, new CopyOnWriteArrayList<>(nums), "斧头真身") {
            @Override
            public void before() {
                Long q = who.longValue();
                PersonInfo info_ = getInfo(q);
                Long lon = info_.getAtt();
                long v = percentTo(info.getAddPercent(), lon);
                putPerson(info_);
                addAttHasTime(who.longValue(), new HasTimeAdder(System.currentTimeMillis() + t728, who.longValue(), v));
            }
        };
        return skill;
    }

    /**
     * 杀神昊天锤
     *
     * @param info
     * @param who
     * @param nums
     * @return
     */
    private static Skill create729(SkillInfo info, Number who, Number... nums) {
        return new Skill(info, who, new CopyOnWriteArrayList<>(nums), "杀神昊天锤") {
            @Override
            public void before() {
                Long q = who.longValue();
                PersonInfo info_ = getInfo(q);
                Long lon = info_.getAtt();
                int b = info.getAddPercent();
                int id = getSkillInfo(who.longValue()).get(0).getId();
                SkillInfo i1 = getSkillInfo(who.longValue()).get(0);
                i1.setTime(System.currentTimeMillis() + 1000 * 60 * 30);
                updateSkillInfo(i1);
                id -= 200;
                b += id;
                long v = percentTo(b, lon);
                addAttHasTime(who.longValue(), new HasTimeAdder(System.currentTimeMillis() + t729, who.longValue(), v));
                setTips(String.format("增加%s%%(%s)攻击", b, percentTo(b, getInfo(who).getAtt())));
            }
        };
    }

    /**
     * 魔神剑
     *
     * @param info
     * @param who
     * @param nums
     * @return
     */
    private static Skill create730(SkillInfo info, Number who, Number... nums) {
        return new Skill(info, who, new CopyOnWriteArrayList<>(nums), "魔神剑") {
            @Override
            public void before() {
                if (nums.length == 0) {
                    setTips("未选择任何..");
                }
                long v = getHjFromAny(who, nums[0].longValue());
                PersonInfo in = getInfo(who);
                long vv = percentTo(info.getAddPercent(), in.getAtt());
                addAttHasTime(who.longValue(), new HasTimeAdder(System.currentTimeMillis() + t730, who.longValue(), vv));
                int b = toPercent(v, in.getHjL());
                b = b > 15 ? 15 : b <= 2 ? 3 : b;
                long v1 = percentTo(b, in.getHpL());
                long v2 = percentTo(b, in.getHll());
                long v3 = percentTo(b, in.getHjL());
                in.addHp(v1);
                in.addHl(v2);
                in.addHj(v3);
                putPerson(in);
            }
        };
    }

    /**
     * 暗金恐爪熊
     *
     * @param info
     * @param who
     * @param nums
     * @return
     */
    private static Skill create731(SkillInfo info, Number who, Number... nums) {
        return new Skill(info, who, new CopyOnWriteArrayList<>(nums), "暗金恐爪熊") {
            @Override
            public void before() {
                PersonInfo in = getInfo(who);
                v_ = percentTo(info.getAddPercent(), in.getAtt());
                long v = percentTo(info.getAddPercent(), in.getHpL());
                addAttHasTime(who.longValue(), new HasTimeAdder(System.currentTimeMillis() + t731, who.longValue(), v));
                in.addTag(TAG_SHIELD, v);
                putPerson(in);
            }

            private long v_;

            @Override
            public void run() {
                super.run();
                try {
                    Thread.sleep(t731);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                putPerson(getInfo(who).eddTag(TAG_SHIELD));
                setTips("武魂真身失效");
            }
        };
    }

    private static Skill create7_(SkillInfo info, Number who, Number... nums) {
        return new Skill(info, who, new CopyOnWriteArrayList<>(nums), "") {
            @Override
            public void before() {
            }

            @Override
            public void run() {
                super.run();
            }
        };
    }
}
