package Project.services.impl;


import Project.aSpring.dao.GInfo;
import Project.aSpring.dao.PersonInfo;
import Project.aSpring.dao.TradingRecord;
import Project.commons.SpGroup;
import Project.commons.broadcast.enums.ObjType;
import Project.dataBases.GameDataBase;
import Project.dataBases.SourceDataBase;
import Project.interfaces.Iservice.IGameService;
import Project.interfaces.Iservice.IGameUseObjService;
import Project.interfaces.Iservice.IGameWeaService;
import Project.services.detailServices.shopItems.UseTool;
import Project.services.player.UseRestrictions;
import Project.utils.Tools.Tool;
import io.github.kloping.MySpringTool.annotations.AutoStand;
import io.github.kloping.MySpringTool.annotations.Entity;
import io.github.kloping.common.Public;
import io.github.kloping.mirai0.Main.iutils.MessageUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static Project.commons.rt.ResourceSet.FinalFormat.*;
import static Project.commons.rt.ResourceSet.FinalNormalString.USE_UPPER_LIMIT_TIPS;
import static Project.commons.rt.ResourceSet.FinalString.*;
import static Project.commons.rt.ResourceSet.FinalValue.SLE_ONE_MAX;
import static Project.commons.rt.ResourceSet.FinalValue.TRANSFER_ONE_MAX;
import static Project.controllers.auto.ControllerSource.challengeDetailService;
import static Project.dataBases.GameDataBase.*;

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
        //活动物品
        ONLY_SLE.put(7001, 3);
        ONLY_SLE.put(7002, 15);
        ONLY_SLE.put(7003, 35);
    }

    private final UseTool USE_TOOL = new UseTool();
    private final IGameService gameService = new GameServiceImpl();
    @AutoStand
    IGameWeaService gameWeaService;

    /**
     * @param sss 背包
     * @param s1  物品名
     * @return 数量
     */
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
            return String.format(USE_OBJ_WAIT_TIPS, Tool.INSTANCE.getTimeTips(l1));
        }
        if (UseRestrictions.cant(who.longValue(), id)) return USE_UPPER_LIMIT_TIPS;
        List<Integer> bgids = new ArrayList<>(Arrays.asList(GameDataBase.getBgs(who)));
        if (id > 200 && id < 300) return "请使用 \"吸收\" 使用魂环";
        long cd0 = 1000;
        if (challengeDetailService.isTemping(who)) {
            cd0 *= 5;
        }
        if (bgids.contains(id)) {
            if (id >= 1 && id <= 100) {
                if (getWhInfo(who).getP() >= 2) {
                    removeFromBgs(who, id, ObjType.use);
                    getWhInfo(who).setWh(id).setWhType(getWhTypeByWh(id)).apply();
                    return "使用成功";
                } else {
                    return "不可用于第一武魂";
                }
            }
            Method method = USE_TOOL.getClass().getMethod("use" + id, long.class);
            String str = String.valueOf(method.invoke(USE_TOOL, who));
            (GameDataBase.getInfo(who).setUk1(System.currentTimeMillis() + cd0)).apply();
            return getPic(id) + str;
        } else {
            return "你的背包里没有" + getNameById(id);
        }
    }

    @Override
    public String useObj(Long who, int id, int num) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        long l1 = GameDataBase.getInfo(who).getUk1();
        if (l1 >= System.currentTimeMillis()) {
            return String.format(USE_OBJ_WAIT_TIPS, Tool.INSTANCE.getTimeTips(l1));
        }
        if (UseRestrictions.cant(who.longValue(), id)) return USE_UPPER_LIMIT_TIPS;
        String[] sss = gameService.getBags(who);
        if (num <= 0 || num > 50) return NUM_TOO_MUCH;
        boolean enough = false;
        long cd0 = (long) (7000 * num * 1.25f);
        //冷却药水
        if (id == 116) {
            if (getNumForO(sss, getNameById(id)) > 0) {
                enough = true;
                cd0 = 1000;
                if (challengeDetailService.isTemping(who)) {
                    cd0 *= 5;
                }
            }
        } else {
            enough = getNumForO(sss, getNameById(id)) >= num;
            if (challengeDetailService.isTemping(who)) {
                return CHALLENGE_ING;
            }
        }
        if (enough) {
            String str = new UseTool().useObjNum(who, id, num);
            if (!Tool.INSTANCE.findNumberFromString(str).isEmpty()) {
                (GameDataBase.getInfo(who).setUk1(System.currentTimeMillis() + cd0)).apply();
            }
            return "批量使用" + getPic(id) + str;
        } else {
            return "你的背包里没有足够的" + getNameById(id);
        }
    }

    @Override
    public String buyObj(Long who, int id, Integer num) {
        PersonInfo pi = GameDataBase.getInfo(who);
        if (pi.getGk1() >= System.currentTimeMillis()) {
            return String.format(BUY_OBJ_WAIT_TIPS, Tool.INSTANCE.getTimeTips(pi.getGk1()));
        }
        if (num <= 0 || num > 50) return NUM_TOO_MUCH;
        if (id == 109 || id == 110) {
            if (num > 5) return BUY_NUM_TOO_MUCH_TIPS;
            if (id == 109) if (pi.getBuyHelpC() >= MAX_HELP) {
                return TODAY_BUY_UPPER_TIPS;
            } else (pi.setBuyHelpC(pi.getBuyHelpC() + num)).apply();
            if (id == 110) {
                if (pi.getBuyHelpToC() >= MAX_HELP_TO) {
                    return TODAY_BUY_UPPER_TIPS;
                } else {
                    pi.addBuyHelpToC().setBuyHelpToC(pi.getBuyHelpToC() + num).apply();
                }
            }
        }
        long l = GameDataBase.ID_2_SHOP_MAPS.get(id);
        long Ig = pi.getGold();
        long wl = l * num + (num * 15L);
        if (Ig >= wl) {
            GameDataBase.addToBgs(who, id, num, ObjType.buy);
            pi.setGk1(System.currentTimeMillis() + (long) (8000 * num * 1.25f)).addGold(-wl, new TradingRecord().setType1(TradingRecord.Type1.lost).setType0(TradingRecord.Type0.gold).setTo(-1).setMain(who).setFrom(who).setDesc("购买" + num + "个\"" + getNameById(id) + "\"").setMany(wl)).apply();
            GInfo.getInstance(who).addBuyc().apply();
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
        PersonInfo pi = GameDataBase.getInfo(who);
        long l1 = pi.getGk1();
        if (l1 >= System.currentTimeMillis()) {
            return String.format(BUY_OBJ_WAIT_TIPS, Tool.INSTANCE.getTimeTips(l1));
        }
        long l = GameDataBase.ID_2_SHOP_MAPS.get(id);
        long Ig = pi.getGold();
        if (Ig >= l) {
            GameDataBase.addToBgs(who, id, ObjType.buy);
            pi.setGk1(System.currentTimeMillis() + 1000).addGold(-l, new TradingRecord().setType1(TradingRecord.Type1.lost).setType0(TradingRecord.Type0.gold).setTo(-1L).setMain(who).setFrom(who).setDesc("购买" + getNameById(id) + "\"").setMany(l)).apply();
            GInfo.getInstance(who).addBuyc().apply();
            return String.format(TIPS_BUY_SUCCEED, getPic(id));
        } else {
            return NOT_ENOUGH_GOLD;
        }
    }

    @Override
    public String sleObj(Long who, int id) {
        try {
            List<Integer> bgids = new ArrayList<>(Arrays.asList(GameDataBase.getBgs(who)));
            if (bgids.contains(id)) {
                long l = 0;
                if (ID_2_SHOP_MAPS.containsKey(id)) l = GameDataBase.ID_2_SHOP_MAPS.get(id) / 3;
                else if (ONLY_SLE.containsKey(id)) l = ONLY_SLE.get(id).longValue() / 3;
                else return NOT_FOUND_ABOUT_OBJ;
                GameDataBase.removeFromBgs(who, id, ObjType.sell);
                l = l > maxSle ? maxSle : l;
                GameDataBase.getInfo(who).addGold(l, new TradingRecord().setType1(TradingRecord.Type1.add).setType0(TradingRecord.Type0.gold).setTo(-1L).setMain(who).setFrom(who).setDesc("出售\"" + getNameById(id) + "\"").setMany(l)).apply();
                return getPic(id) + "出售成功,你获得了 " + l + "个金魂币";
            } else {
                return "你的背包里没有" + getNameById(id);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ERR_TIPS;
        }
    }

    @Override
    public String sleObj(Long who, int id, Integer num, SpGroup group) {
        if (num > SLE_ONE_MAX) {
            Public.EXECUTOR_SERVICE.submit(() -> {
                final int END = num;
                int r = 0;
                StringBuilder sb = new StringBuilder();
                while (true) {
                    if (END - r >= SLE_ONE_MAX) {
                        sb.append(sleObj(who, id, SLE_ONE_MAX, group)).append(NEWLINE);
                        r += SLE_ONE_MAX;
                    } else {
                        int n = END - r;
                        sb.append(sleObj(who, id, n, group)).append(NEWLINE);
                        r += n;
                        break;
                    }
                }
                MessageUtils.INSTANCE.sendMessageInGroupWithAt(sb.toString(), group.getId(), who);
            });
            return ASYNCHRONOUS_SLE;
        }
        if (containsBgsNum(who, id, num)) {
            removeFromBgs(who, id, num, ObjType.sell);
            long l;
            if (ID_2_SHOP_MAPS.containsKey(id)) l = GameDataBase.ID_2_SHOP_MAPS.get(id) / 3;
            else if (ONLY_SLE.containsKey(id)) l = ONLY_SLE.get(id).longValue() / 3;
            else return NOT_FOUND_ABOUT_OBJ;
            l = l > maxSle ? maxSle : l;
            l *= num;
            GameDataBase.getInfo(who).addGold(l, new TradingRecord().setType1(TradingRecord.Type1.add).setType0(TradingRecord.Type0.gold).setTo(-1L).setMain(who).setFrom(who).setDesc("出售" + num + "个\"" + getNameById(id) + "\"").setMany(l)).apply();
            GInfo.getInstance(who).addSalec().apply();
            return getPic(id) + "批量 出售成功,你获得了 " + l + "个金魂币";
        } else {
            return "你的背包里 没有足够的 " + getNameById(id);
        }
    }

    @Override
    public String objTo(Long who, int id, Long whos, Integer num, SpGroup group) {
        if (num > TRANSFER_ONE_MAX) {
            Public.EXECUTOR_SERVICE.submit(() -> {
                final int END = num;
                int r = 0;
                StringBuilder sb = new StringBuilder();
                while (true) {
                    if (END - r >= TRANSFER_ONE_MAX) {
                        sb.append(objTo(who, id, whos, TRANSFER_ONE_MAX, group)).append(NEWLINE);
                        r += TRANSFER_ONE_MAX;
                    } else {
                        int n = END - r;
                        sb.append(objTo(who, id, whos, n, group)).append(NEWLINE);
                        r += n;
                        break;
                    }
                }
                MessageUtils.INSTANCE.sendMessageInGroupWithAt(sb.toString(), group.getId(), who);
            });
            return ASYNCHRONOUS_TRANSFER;
        }
        if ((id > 1000 && id <= 1007) || (id <= 127 && id >= 124)) {
            return gameWeaService.objTo(who, id, whos);
        } else if (containsBgsNum(who, id, num)) {
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
        if ((id > 1000 && id <= 1007) || (id <= 127 && id >= 124)) {
            return gameWeaService.objTo(who, id, whos);
        } else if (bgids.contains(id)) {
            GameDataBase.removeFromBgs(who, id, ObjType.transLost);
            GameDataBase.addToBgs(Long.valueOf(whos), id, ObjType.transGot);
            return "转让完成";
        } else {
            return "你的背包里没有" + getNameById(id);
        }
    }
}






