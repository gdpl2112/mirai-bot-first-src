package Project.services.impl;


import Project.dataBases.GameDataBase;
import Project.dataBases.SourceDataBase;
import Project.interfaces.Iservice.IGameService;
import Project.interfaces.Iservice.IGameUseObjService;
import Project.services.detailServices.ChallengeDetailService;
import Project.services.player.UseRestrictions;
import io.github.kloping.MySpringTool.annotations.Entity;
import io.github.kloping.mirai0.commons.PersonInfo;
import io.github.kloping.mirai0.commons.TradingRecord;
import io.github.kloping.mirai0.commons.broadcast.enums.ObjType;
import io.github.kloping.mirai0.commons.gameEntitys.SkillInfo;
import io.github.kloping.mirai0.unitls.Tools.GameTool;
import io.github.kloping.mirai0.unitls.Tools.Tool;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static Project.controllers.auto.ControllerSource.challengeDetailService;
import static Project.dataBases.GameDataBase.*;
import static Project.dataBases.skill.SkillDataBase.getSkillInfo;
import static Project.dataBases.skill.SkillDataBase.updateSkillInfo;
import static io.github.kloping.mirai0.commons.resouce_and_tool.ResourceSet.FinalFormat.*;
import static io.github.kloping.mirai0.commons.resouce_and_tool.ResourceSet.FinalNormalString.USE_UPPER_LIMIT_TIPS;
import static io.github.kloping.mirai0.commons.resouce_and_tool.ResourceSet.FinalString.*;
import static io.github.kloping.mirai0.commons.resouce_and_tool.ResourceSet.FinalValue.*;
import static io.github.kloping.mirai0.unitls.Tools.GameTool.getRandXl;
import static io.github.kloping.mirai0.unitls.Tools.Tool.*;

/**
 * @author github-kloping
 */
@Entity
public class GameUseObjServiceImpl implements IGameUseObjService {

    /**
     * 仅可出售
     */
    public static final Map<Integer, Number> ONLY_SLE = new ConcurrentHashMap<>();
    private static final int MAX_HELP = 3;
    private static final int MAX_HELP_TO = 3;
    public static int maxSle = 2000;

    static {
        //魂骨系列
        ONLY_SLE.put(1512, 1002);
        ONLY_SLE.put(1522, 1002);
        ONLY_SLE.put(1532, 1002);
        ONLY_SLE.put(1542, 1002);
        ONLY_SLE.put(1552, 1002);
        //升级券
        ONLY_SLE.put(1601, 420);
        ONLY_SLE.put(1602, 520);
        ONLY_SLE.put(1603, 620);
        ONLY_SLE.put(1604, 720);
    }

    private final UseTool use = new UseTool();
    private final IGameService gameService = new GameServiceImpl();

    public static Integer getNumForO(String[] sss, String s1) {
        for (String str : sss) {
            if (str.startsWith(s1)) {
                if (str.contains("x")) {
                    return Integer.valueOf(str.split("x")[1]);
                } else {
                    return 1;
                }
            }
        }
        return 0;
    }

    private String getPic(Integer id) {
        return SourceDataBase.getImgPathById(id) + "\r\n";
    }

    @Override
    public String useObj(Long who, int id) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        long l1 = GameDataBase.getInfo(who).getUk1();
        if (l1 >= System.currentTimeMillis()) {
            return String.format(USE_OBJ_WAIT_TIPS, getTimeTips(l1));
        }
        if (UseRestrictions.cant(who.longValue(), id)) return USE_UPPER_LIMIT_TIPS;
        List<Integer> bgids = new ArrayList<>(Arrays.asList(GameDataBase.getBgs(who)));
        if (id > 200 && id < 300) return "请使用 \"吸收\" 使用魂环";
        if (bgids.contains(id)) {
            use.getClass().getMethod("before", long.class).invoke(use, who);
            Method method = use.getClass().getMethod("use" + id, long.class);
            String str = String.valueOf(method.invoke(use, who));
            putPerson(getInfo(who).setUk1(System.currentTimeMillis() + (long) (1000)));
            if (challengeDetailService.isTemping(who)) {
                if (ChallengeDetailService.USED.containsKey(who) && ChallengeDetailService.USED.get(who)) {
                    return CHALLENGE_USED;
                } else {
                    ChallengeDetailService.USED.put(who, true);
                }
            }
            return getPic(id) + str;
        } else {
            return "你的背包里没有" + getNameById(id);
        }
    }

    @Override
    public String useObj(Long who, int id, int num) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        long l1 = GameDataBase.getInfo(who).getUk1();
        if (l1 >= System.currentTimeMillis()) {
            return String.format(USE_OBJ_WAIT_TIPS, getTimeTips(l1));
        }
        if (UseRestrictions.cant(who.longValue(), id)) return USE_UPPER_LIMIT_TIPS;
        String[] sss = gameService.getBags(who);
        if (num <= 0 || num > 50)
            return NUM_TOO_MUCH;
        boolean enough = false;
        if (id == 116) {
            if (getNumForO(sss, getNameById(id)) > 0) {
                enough = true;
            }
        } else {
            enough = getNumForO(sss, getNameById(id)) >= num;
        }
        if (enough) {
            String str = new UseTool().useObjNum(who, id, num);
            if (!Tool.findNumberFromString(str).isEmpty())
                putPerson(getInfo(who).setUk1(System.currentTimeMillis() + (long) (8000 * num * 1.25f)));
            return "批量使用" + getPic(id) + str;
        } else {
            return "你的背包里没有足够的" + getNameById(id);
        }
    }

    @Override
    public String buyObj(Long who, int id, Integer num) {
        long l1 = GameDataBase.getGk1(who);
        if (l1 >= System.currentTimeMillis()) {
            return String.format(BUY_OBJ_WAIT_TIPS, getTimeTips(l1));
        }
        if (num <= 0 || num > 50)
            return NUM_TOO_MUCH;
        if (id == 109 || id == 110) {
            if (num > 5) return BUY_NUM_TOO_MUCH_TIPS;
            if (id == 109)
                if (getInfo(who).getBuyHelpC() >= MAX_HELP) {
                    return TODAY_BUY_UPPER_TIPS;
                } else
                    putPerson(getInfo(who).setBuyHelpC(getInfo(who).getBuyHelpC() + num));
            if (id == 110) {
                if (getInfo(who).getBuyHelpToC() >= MAX_HELP_TO) {
                    return TODAY_BUY_UPPER_TIPS;
                } else {
                    putPerson(getInfo(who).addBuyHelpToC());
                    putPerson(getInfo(who).setBuyHelpToC(getInfo(who).getBuyHelpToC() + num));
                }
            }
        }
        long l = GameDataBase.ID_2_SHOP_MAPS.get(id);
        long Ig = GameDataBase.getInfo(who).getGold();
        long wl = l * num + (num * 15L);
        if (Ig >= wl) {
            GameDataBase.addToBgs(who, id, num, ObjType.buy);
            putPerson(getInfo(who).setGk1(System.currentTimeMillis() + (long) (8000 * num * 1.25f))
                    .addGold(-wl, new TradingRecord()
                            .setType1(TradingRecord.Type1.lost)
                            .setType0(TradingRecord.Type0.gold)
                            .setTo(-1)
                            .setMain(who)
                            .setFrom(who)
                            .setDesc("购买" + num + "个\"" + getNameById(id) + "\"")
                            .setMany(wl)
                    ));

            return getPic(id) + "额外花费了" + num * 15 + "成功批量购买";
        } else {
            return BUY_NUM_NOT_ENOUGH_GOLD_TIPS;
        }
    }

    @Override
    public String getIntro(int id) {
        String intro = GameDataBase.getIntroById(id);
        intro = intro == null ? NO_INTRO_NOW : intro;
        return getPic(id) + intro;
    }

    @Override
    public String buyObj(Long who, int id) {
        long l1 = GameDataBase.getGk1(who);
        if (l1 >= System.currentTimeMillis()) {
            return String.format(BUY_OBJ_WAIT_TIPS, getTimeTips(l1));
        }
        long l = GameDataBase.ID_2_SHOP_MAPS.get(id);
        long Ig = GameDataBase.getInfo(who).getGold();
        if (Ig >= l) {
            GameDataBase.addToBgs(who, id, ObjType.buy);
            putPerson(getInfo(who).setGk1(System.currentTimeMillis() + 1000).addGold(-l
                    , new TradingRecord()
                            .setType1(TradingRecord.Type1.lost)
                            .setType0(TradingRecord.Type0.gold)
                            .setTo(-1L)
                            .setMain(who)
                            .setFrom(who)
                            .setDesc("购买" + getNameById(id) + "\"")
                            .setMany(l)
            ));
            return String.format(TIPS_BUY_SUCCEED, getPic(id));
        } else {
            return NOT_ENOUGH_GOLD;
        }
    }

    @Override
    public String sleObj(Long who, int id) {
        try {
            List<Integer> bgids = new ArrayList<>(Arrays.asList(GameDataBase.getBgs(who)));
            if (id == 206 || id == 207)
                return GameDataBase.getNameById(id) + ",太过昂贵,不可出售";
            if (bgids.contains(id)) {
                long l = 0;
                if (ID_2_SHOP_MAPS.containsKey(id)) l = GameDataBase.ID_2_SHOP_MAPS.get(id) / 3;
                else if (ONLY_SLE.containsKey(id)) l = ONLY_SLE.get(id).longValue() / 3;
                else return "商城中为发现此物品";
                GameDataBase.removeFromBgs(who, id, ObjType.sell);
                l = l > maxSle ? maxSle : l;
                putPerson(getInfo(who).addGold(l, new TradingRecord()
                        .setType1(TradingRecord.Type1.add)
                        .setType0(TradingRecord.Type0.gold)
                        .setTo(-1L)
                        .setMain(who)
                        .setFrom(who)
                        .setDesc("出售\"" + getNameById(id) + "\"")
                        .setMany(l)
                ));
                return getPic(id) + "出售成功,你获得了 " + l + "个金魂币";
            } else {
                return "你的背包里没有" + getNameById(id);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "出售 异常 ";
        }
    }

    @Override
    public String sleObj(Long who, int id, Integer num) {
        if (num > SLE_ONE_MAX) {
            return SLE_TOO_MUCH;
        }
        if (contiansBgsNum(who, id, num)) {
            if (id == 206 || id == 207)
                return GameDataBase.getNameById(id) + ",太过昂贵,不可出售";
            removeFromBgs(who, id, num, ObjType.sell);
            long l;
            if (ID_2_SHOP_MAPS.containsKey(id)) l = GameDataBase.ID_2_SHOP_MAPS.get(id) / 3;
            else if (ONLY_SLE.containsKey(id)) l = ONLY_SLE.get(id).longValue() / 3;
            else return "商城中为发现此物品";

            l = l > maxSle ? maxSle : l;
            l *= num;
            putPerson(getInfo(who).addGold(l
                    , new TradingRecord()
                            .setType1(TradingRecord.Type1.add)
                            .setType0(TradingRecord.Type0.gold)
                            .setTo(-1L)
                            .setMain(who)
                            .setFrom(who)
                            .setDesc("出售" + num + "个\"" + getNameById(id) + "\"")
                            .setMany(l)
            ));
            return getPic(id) + "批量 出售成功,你获得了 " + l + "个金魂币";
        } else {
            return "你的背包里 没有足够的 " + getNameById(id);
        }
    }

    @Override
    public String objTo(Long who, int id, Long whos, Integer num) {
        if (num > TRANSFER_ONE_MAX) {
            return TRANSFER_TOO_MUCH;
        }
        if (contiansBgsNum(who, id, num)) {
            GameDataBase.removeFromBgs(who, id, num, ObjType.transLost);
            GameDataBase.addToBgs(Long.valueOf(whos), id, num, ObjType.transGot);
            return "批量 转让 完成";
        } else {
            return "你的背包里 没有足够的 " + getNameById(id);
        }
    }

    @Override
    public String objTo(Long who, int id, Long whos) {
        List<Integer> bgids = new ArrayList<>(Arrays.asList(GameDataBase.getBgs(who)));
        if (bgids.contains(id)) {
            GameDataBase.removeFromBgs(who, id, ObjType.transLost);
            GameDataBase.addToBgs(Long.valueOf(whos), id, ObjType.transGot);
            return "转让完成";
        } else {
            return "你的背包里没有" + getNameById(id);
        }
    }

    public class UseTool {

        public PersonInfo personInfo;

        public void remove(int id, long who) {
            GameDataBase.removeFromBgs(who, id, ObjType.use);
        }

        public void before(long who) {
            personInfo = getInfo(who);
        }

        public String useObjNum(Long who, Integer id, Integer num) {
            PersonInfo personInfo = getInfo(who);
            long l = 0;
            if (id != 116) {
                for (Integer integer = 0; integer < num; integer++) {
                    UseRestrictions.record(who, id);
                }
            } else {
                UseRestrictions.record(who, id);
            }
            if (UseRestrictions.cant(who.longValue(), id)) return USE_UPPER_LIMIT_TIPS;
            switch (id) {
                case 102:
                    String s0 = "";
                    long m = personInfo.getHpL();
                    long t = personInfo.getHp();
                    l = 0;
                    int i1 = personInfo.getLevel() / 10;
                    i1 = i1 < 4 ? 4 : i1;
                    i1 = i1 > 7 ? 7 : i1;
                    l = m / i1;
                    l *= num;
                    if (m - t < l) {
                            l = m - t;
                        }
                    if (GameTool.isATrue(who)) {
                        personInfo.addHp(l / 2);
                        s0 = "处于选择状态增加减半 加血=>" + (l / 2);
                    } else {
                        personInfo.addHp(l);
                        s0 = "加血=>" + l;
                    }
                    putPerson(personInfo);
                    removeFromBgs(Long.valueOf(who), id, num, ObjType.use);
                    return s0;
                case 103:
                    int c = (getRandXl(personInfo.getLevel()));
                    long xr = personInfo.getXpL() / c;
                    long mx = (long) (xr * 0.92f);
                    mx *= num;
                    putPerson(personInfo.addXp(mx));
                    removeFromBgs(Long.valueOf(who), id, num, ObjType.use);
                    return "增加了:" + mx + "点经验";
                case 104:
                    long att = personInfo.getLevel() * 25;
                    att *= num;
                    putPerson(personInfo.addAtt(att));
                    removeFromBgs(Long.valueOf(who), id, num, ObjType.use);
                    return "增加了" + att + "点攻击";
                case 105:
                    l = personInfo.getLevel() * 35;
                    l *= num;
                    putPerson(personInfo.addHp(l).addHpl(l));
                    removeFromBgs(Long.valueOf(who), id, num, ObjType.use);
                    return "增加了" + l + "点最大生命";
                case 112:
                    long v = percentTo((int) randA(10, 15), getInfo(who).getHjL());
                    v = v < 0 ? 1 : v;
                    v *= num;
                    putPerson(getInfo(who).addHj(l).addHjL(l));
                    removeFromBgs(Long.valueOf(who), id, num, ObjType.use);
                    return "恢复了" + v + "点精神力";
                case 116:
                    Map<Integer, SkillInfo> infos = getSkillInfo(who.longValue());
                    if (infos.containsKey(num)) {
                        SkillInfo skillInfo = infos.get(num);
                        skillInfo.setTime(skillInfo.getTime() - OBJ116_VALUE);
                        updateSkillInfo(skillInfo);
                        removeFromBgs(Long.valueOf(who), id, 1, ObjType.use);
                        UseRestrictions.record(who.longValue(), id);
                        return "使用成功";
                    } else {
                        return ("其魂技未解锁");
                    }
                default:
                    return NOT_SUPPORTED_NUM_USE;
            }
        }

        public String use101(long who) {
            remove(101, who);
            putPerson(getInfo(who).setK1(1L).setK2(1L));
            return "清空修炼和进入冷却";
        }

        public String use102(long who) {
            long m = personInfo.getHpL();
            long t = personInfo.getHp();
            long l = 0;
            if (t >= m) {
                return "满状态无需使用";
            } else {
                int i1 = personInfo.getLevel() / 10;
                i1 = i1 < 4 ? 4 : i1;
                i1 = i1 > 7 ? 7 : i1;
                l = m / i1;
                if (m - t < l) {
                    l = m - t;
                }
                remove(102, who);
                UseRestrictions.record(who, 102);
                if (GameTool.isATrue(who)) {
                    putPerson(getInfo(who).addHp(l / 2));
                    return "处于选择状态增加减半 加血=>" + (l / 2);
                } else {
                    putPerson(getInfo(who).addHp(l));
                }
            }
            return "加血=>" + l;
        }

        public String use103(long who) {
            int c = (getRandXl(personInfo.getLevel()));
            long xr = personInfo.getXpL() / c;
            long mx = (long) (xr * 1.1f);
            putPerson(getInfo(who).addXp(mx));
            remove(103, who);
            return "增加了:" + mx + "点经验";
        }

        public String use104(long who) {
            long att = personInfo.getLevel() * 25;
            putPerson(personInfo.addAtt(att));
            remove(104, who);
            return "增加了" + att + "点攻击";
        }

        public String use105(long who) {
            long l = getInfo(who).getLevel() * 35;
            putPerson(getInfo(who).addHp(l).addHpl(l));
            remove(105, who);
            return "增加了" + l + "点最大生命";
        }

        public String use106(long who) {
            remove(106, who);
            UseRestrictions.record(who, 106);
            long m = personInfo.getHll();
            long t = personInfo.getHl();
            long l = 0;
            if (t >= m) {
                return "满状态无需使用";
            } else {
                l = (long) (m / 3.5f);
                if (m - t < l) {
                    l = m - t;
                }
            }
            if (GameTool.isATrue(who)) {
                putPerson(getInfo(who).addHl(l / 2));
                return "处于选择状态增加减半 增加了" + (l / 2) + "点魂力";
            } else {
                putPerson(personInfo.addHl(l));
            }
            return "增加了" + l + "点魂力";
        }

        public String use107(long who) {
            int r = personInfo.getNextR1();
            if (r != -2) {
                remove(107, who);
                putPerson(personInfo.setNextR1(-2));
                return "使用成功!!";
            } else {
                return "使用失败,另一个正在使用..";
            }
        }

        public String use108(long who) {
            Integer nr = personInfo.getNextR2();
            if (nr != -2) {
                remove(108, who);
                putPerson(personInfo.setNextR2(-2));
                return "使用成功!!";
            } else {
                return "使用失败,另一个正在使用..";
            }
        }

        public String use109(long who) {
            remove(109, who);
            putPerson(getInfo(who).setHelpC(personInfo.getHelpC() - 1));
            UseRestrictions.record(who, 109);
            return "使用成功!!\r\n获得一次请求支援机会";
        }

        public String use110(long who) {
            remove(110, who);
            UseRestrictions.record(who, 110);
            putPerson(getInfo(who).setHelpToc(personInfo.getHelpToc() - 1));
            return "使用成功!!\r\n获得一次支援机会";
        }

        public String use111(long who) {
            return "该物品自动使用.";
        }

        public String use112(long who) {
            long v = percentTo((int) randA(11, 17), getInfo(who).getHjL());
            v = v < 0 ? 1 : v;
            putPerson(getInfo(who).addHj(v));
            remove(112, who);
            UseRestrictions.record(who, 112);
            return "恢复了" + v + "点精神力";
        }

        public String use115(long who) {
            Integer nr = personInfo.getNextR3();
            if (nr != -2) {
                remove(115, who);
                putPerson(personInfo.setNextR3(-2));
                return "使用成功!!";
            } else {
                return "使用失败,另一个正在使用..";
            }
        }

        public String use117(long who) {
            UseRestrictions.record(who, 117);
            Map<Integer, SkillInfo> infos = getSkillInfo(who);
            for (SkillInfo value : infos.values()) {
                value.setTime(1L);
                updateSkillInfo(value);
            }
            removeFromBgs(Long.valueOf(who), 117, 1, ObjType.use);
            return "使用成功";
        }

        public String use1000(long who) {
            return "参见=>暗器菜单";
        }

        private String use160x(long who) {
            return "升级券,自动使用,升级第<几>魂环";
        }

        public String use1601(long who) {
            return use160x(who);
        }

        public String use1602(long who) {
            return use160x(who);
        }

        public String use1603(long who) {
            return use160x(who);
        }

        public String use1604(long who) {
            return use160x(who);
        }

        public String use1605(long who) {
            return use160x(who);
        }
    }
}






