package Project.services.impl;


import io.github.kloping.mirai0.Entitys.TradingRecord;
import io.github.kloping.mirai0.Entitys.gameEntitys.PersonInfo;
import Project.dataBases.GameDataBase;
import io.github.kloping.mirai0.unitls.Tools.GameTool;
import io.github.kloping.mirai0.unitls.Tools.Tool;
import Project.broadcast.enums.ObjType;
import Project.interfaces.Iservice.IGameService;
import Project.interfaces.Iservice.IGameUseObjService;
import io.github.kloping.MySpringTool.annotations.Entity;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static Project.dataBases.GameDataBase.*;
import static Project.ResourceSet.FinalString.*;
import static Project.ResourceSet.FinalFormat.*;
import static io.github.kloping.mirai0.unitls.Tools.GameTool.getRandXl;
import static io.github.kloping.mirai0.unitls.Tools.Tool.getTimeTips;

/**
 * @author github-kloping
 */
@Entity
public class GameUseObjServiceImpl implements IGameUseObjService {

    private String getPic(Integer id) {
        return GameDataBase.getImgById(id) + "\r\n";
    }

    private final UseTool use = new UseTool();

    @Override
    public String useObj(Long who, int id) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        long l1 = GameDataBase.getInfo(who).getUk1();
        if (l1 >= System.currentTimeMillis()) {
            return String.format(USE_OBJ_WAIT_TIPS, getTimeTips(l1));
        }
        List<Integer> bgids = new ArrayList<>(Arrays.asList(GameDataBase.getBgs(who)));
        if (id > 200 && id < 300) return "请使用 \"吸收\" 使用魂环";
        if (bgids.contains(id)) {
            use.getClass().getMethod("before", long.class).invoke(use, who);
            Method method = use.getClass().getMethod("use" + id, long.class);
            String str = String.valueOf(method.invoke(use, who));
            putPerson(getInfo(who).setUk1(System.currentTimeMillis() + (long) (15 * 1000)));
            return getPic(id) + str;
        } else {
            return "你的背包里没有" + getNameById(id);
        }
    }

    private final IGameService gameService = new GameServiceImpl();

    @Override
    public String useObj(Long who, int id, int num) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        long l1 = GameDataBase.getInfo(who).getUk1();
        if (l1 >= System.currentTimeMillis()) {
            return String.format(USE_OBJ_WAIT_TIPS, getTimeTips(l1));
        }
        if (id == 109 || id == 110) {
            if (id == 109)
                if (getInfo(who).getBuyHelpC() >= MAX_HELP) {
                    return TODAY_BUY_UPPER_TIPS;
                } else {
                    putPerson(getInfo(who).addBuyHelpToC());
                }
            if (id == 110) {
                if (getInfo(who).getBuyHelpToC() >= MAX_HELP_TO) {
                    return TODAY_BUY_UPPER_TIPS;
                } else {
                    putPerson(getInfo(who).addBuyHelpToC());
                }
            }
        }
        String[] sss = gameService.getBags(who);
        if (num <= 0 || num > 50)
            return NUM_TOO_MUCH;
        if (GameDataBase.getNumForO(sss, getNameById(id)) >= num) {
            String str = new UseTool().UseObjNum(who, id, num);
            if (!Tool.findNumberFromString(str).isEmpty())
                putPerson(getInfo(who).setUk1(System.currentTimeMillis() + (long) (15 * 1000 * num * 1.25f)));
            return "批量使用" + getPic(id) + str;
        } else {
            return "你的背包里没有足够的" + getNameById(id);
        }
    }

    private static final int MAX_HELP = 3;
    private static final int MAX_HELP_TO = 3;

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
            putPerson(getInfo(who).setGk1(System.currentTimeMillis() + (long) (12 * 1000 * num * 1.25f))
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
            putPerson(getInfo(who).setGk1(System.currentTimeMillis() + 15 * 1000).addGold(-l
                    , new TradingRecord()
                            .setType1(TradingRecord.Type1.lost)
                            .setType0(TradingRecord.Type0.gold)
                            .setTo(-1)
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

    public class UseTool {

        public void remove(int id, long who) {
            GameDataBase.removeFromBgs(who, id, ObjType.use);
        }

        public PersonInfo personInfo;

        public void before(long who) {
            personInfo = getInfo(who);
        }

        public String UseObjNum(Long who, Integer id, Integer num) {
            PersonInfo personInfo = getInfo(who);
            long l = 0;
            switch (id) {
                case 102:
                    if (num > 6)
                        return SUPERFLUOUS_USE;
                    else {
                        String s0 = "";
                        long m = personInfo.getHpL();
                        long t = personInfo.getHp();
                        l = 0;
                        int i1 = personInfo.getLevel() / 10;
                        i1 = i1 < 4 ? 4 : i1;
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
                    }
                case 103:
                    if (personInfo.getLevel() >= 150) return "等级最大限制";
                    int c = (getRandXl(personInfo.getLevel()));
                    long xr = personInfo.getXpL() / c;
                    long mx = (long) (xr * 0.92f);
                    mx *= num;
                    putPerson(personInfo.addXp(mx));
                    removeFromBgs(Long.valueOf(who), id, num, ObjType.use);
                    return " 增加了:" + mx + "点经验";
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
                    l = getInfo(who).getLevel();
                    l = l * (l > 100 ? l / 3 : 35);
                    l *= num;
                    putPerson(getInfo(who).addHj(l).addHjL(l));
                    removeFromBgs(Long.valueOf(who), id, num, ObjType.use);
                    return "增加了" + l + "点最大精神力";
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
                l = m / i1;
                if (m - t < l) {
                    l = m - t;
                }
                if (GameTool.isATrue(who)) {
                    putPerson(getInfo(who).addHp(l / 2));
                    return "处于选择状态增加减半 加血=>" + (l / 2);
                } else
                    putPerson(getInfo(who).addHp(l));
                remove(102, who);

            }
            return "加血=>" + l;
        }

        public String use103(long who) {
            if (personInfo.getLevel() >= 150) return "等级最大限制";
            int c = (getRandXl(personInfo.getLevel()));
            long xr = personInfo.getXpL() / c;
            long mx = (long) (xr * 1.1f);
            putPerson(getInfo(who).addXp(mx));
            remove(103, who);
            return " 增加了:" + mx + "点经验";
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
            } else
                putPerson(personInfo.addHl(l));
            return "增加了" + l + "点魂力";
        }

        public String use107(long who) {
            String str = personInfo.getUsinged();
            if (str == null || str.equals("null") || str.isEmpty()) {
                remove(107, who);
                putPerson(personInfo.setUsinged("!@#$%^&*("));
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
            return "使用成功!!\r\n获得一次请求支援机会";
        }

        public String use110(long who) {
            remove(110, who);
            putPerson(getInfo(who).setHelpToc(personInfo.getHelpToc() - 1));
            return "使用成功!!\r\n获得一次支援机会";
        }

        public String use111(long who) {
            return "该物品自动使用.";
        }

        public String use112(long who) {
            long l = getInfo(who).getLevel();
            l = l * (l > 100 ? l / 3 : 35);
            putPerson(getInfo(who).addHj(l).addHjL(l));
            remove(112, who);
            return "增加了" + l + "点最大精神力";
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

    public static int maxSle = 2000;

    @Override
    public String SleObj(Long who, int id) {
        try {
            List<Integer> bgids = new ArrayList<>(Arrays.asList(GameDataBase.getBgs(who)));
            if (id == 206 || id == 207)
                return GameDataBase.getNameById(id) + ",太过昂贵,不可出售";
            if (bgids.contains(id)) {
                long l = 0;
                if (ID_2_SHOP_MAPS.containsKey(id)) l = GameDataBase.ID_2_SHOP_MAPS.get(id) / 3;
                else if (onlySle.containsKey(id)) l = onlySle.get(id).longValue() / 3;
                else return "商城中为发现此物品";

                GameDataBase.removeFromBgs(who, id, ObjType.sell);
                l = l > maxSle ? maxSle : l;
                putPerson(getInfo(who).addGold(l, new TradingRecord()
                        .setType1(TradingRecord.Type1.add)
                        .setType0(TradingRecord.Type0.gold)
                        .setTo(-1)
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
    public String SleObj(Long who, int id, Integer num) {
        if (contiansBgsNum(who, id, num)) {
            if (id == 206 || id == 207)
                return GameDataBase.getNameById(id) + ",太过昂贵,不可出售";
            removeFromBgs(who, id, num, ObjType.sell);
            long l;

            if (ID_2_SHOP_MAPS.containsKey(id)) l = GameDataBase.ID_2_SHOP_MAPS.get(id) / 3;
            else if (onlySle.containsKey(id)) l = onlySle.get(id).longValue() / 3;
            else return "商城中为发现此物品";

            l = l > maxSle ? maxSle : l;
            l *= num;
            putPerson(getInfo(who).addGold(l
                    , new TradingRecord()
                            .setType1(TradingRecord.Type1.add)
                            .setType0(TradingRecord.Type0.gold)
                            .setTo(-1)
                            .setMain(who)
                            .setFrom(who)
                            .setDesc("出售" + num + "个\"" + getNameById(id) + "\"")
                            .setMany(l)

            ));
            return getPic(id) + "批量 出售成功,你获得了 " + l + "个金魂币";
        } else
            return "你的背包里 没有足够的 " + getNameById(id);
    }

    @Override
    public String ObjTo(Long who, int id, Long whos, Integer num) {
        if (contiansBgsNum(who, id, num)) {
            GameDataBase.removeFromBgs(who, id, num, ObjType.transLost);
            GameDataBase.addToBgs(Long.valueOf(whos), id, num, ObjType.transGot);
            return "批量 转让 完成";
        } else
            return "你的背包里 没有足够的 " + getNameById(id);
    }

    @Override
    public String ObjTo(Long who, int id, Long whos) {
        List<Integer> bgids = new ArrayList<>(Arrays.asList(GameDataBase.getBgs(who)));
        if (bgids.contains(id)) {
            GameDataBase.removeFromBgs(who, id, ObjType.transLost);
            GameDataBase.addToBgs(Long.valueOf(whos), id, ObjType.transGot);
            return "转让完成";
        } else {
            return "你的背包里没有" + getNameById(id);
        }
    }

    /**
     * 仅可出售
     */
    public static final Map<Integer, Number> onlySle = new ConcurrentHashMap<>();

    static {
        //魂骨系列
        onlySle.put(1512, 1002);
        onlySle.put(1522, 1002);
        onlySle.put(1532, 1002);
        onlySle.put(1542, 1002);
        onlySle.put(1552, 1002);
        //升级券
        onlySle.put(1601, 420);
        onlySle.put(1602, 520);
        onlySle.put(1603, 620);
        onlySle.put(1604, 720);
    }
}






