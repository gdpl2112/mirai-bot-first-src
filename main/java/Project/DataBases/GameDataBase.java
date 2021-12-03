package Project.DataBases;


import Entitys.gameEntitys.PersonInfo;
import Entitys.gameEntitys.Warp;
import Project.Tools.Tool;
import Project.broadcast.GotOrLostObjBroadcast;
import Project.broadcast.enums.ObjType;
import io.github.kloping.initialize.FileInitializeValue;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static Project.Tools.Tool.*;

public class GameDataBase {
    public static String path;

    public static final Map<Integer, String> id2NameMaps = new ConcurrentHashMap<>();
    public static final Map<Integer, String> id2IntroMaps = new ConcurrentHashMap<>();
    public static final Map<String, Integer> Name2idMaps = new ConcurrentHashMap<>();
    public static final Map<Integer, Long> id2ShopMaps = new ConcurrentHashMap<>();
    public static final Map<Integer, Integer> id2WeaMaps = new ConcurrentHashMap<>();
    public static final Map<Integer, Integer> id2WeaONumMaps = new ConcurrentHashMap<>();
    public static final Map<Integer, Integer> wh2Type = new ConcurrentHashMap<>();
    public static final Map<Number, Integer> killedC = new LinkedHashMap<>();

    public GameDataBase(String mainPath) {
        try {
            path = mainPath + "/dates/games";
            File file = new File(path);
            if (!file.exists()) {
                new File(path + "/dates/users").mkdirs();
                new File(path + "/dates/system").mkdirs();
                new File(path + "/mainfist").mkdirs();
                new File(path + "/mainfist/images").mkdirs();
                new File(path + "/mainfist/ids").createNewFile();
                new File(path + "/mainfist/intros").createNewFile();
                new File(path + "/mainfist/shop").createNewFile();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        IntiObj();
        InitMaps();
    }

    private static void IntiObj() {
        InitName();
        InitIntro();
        InitShop();
        InitWeaList();
        InitWeaONumList();
        InitWhType();
        InitKilledC();
    }

    private static void InitName() {
        List<String> ids = new ArrayList<>();
        String whs = "1:蓝电霸王龙\n" +
                "2:昊天锤\n" +
                "3:六翼天使\n" +
                "4:噬魂珠皇\n" +
                "5:蓝银皇\n" +
                "6:柔骨兔\n" +
                "7:邪眸白虎\n" +
                "8:邪火凤凰\n" +
                "9:七杀剑\n" +
                "10:碧灵蛇皇\n" +
                "11:破魂枪\n" +
                "12:大力金刚熊\n" +
                "13:奇茸通天菊\n" +
                "14:鬼魅\n" +
                "15:刺豚\n" +
                "16:蛇矛\n" +
                "17:骨龙\n" +
                "18:蛇杖\n" +
                "19:蓝银草\n" +
                "20:玄龟\n" +
                "21:幽冥灵猫\n" +
                "22:光明圣龙\n" +
                "23:黑暗圣龙\n" +
                "24:修罗神剑\n" +
                "25:青龙\n" +
                "26:海神\n" +
                "27:锄头\n" +
                "28:斧头\n" +
                "29:杀神昊天锤\n" +
                "30:魔神剑\n" +
                "31:暗金恐爪熊\n" +
                "";
        for (String s : whs.trim().split("\n")) {
            if (s.isEmpty()) continue;
            ids.add(s.trim());
        }

        ids.add("201:十年魂环");
        ids.add("202:百年魂环");
        ids.add("203:千年魂环");
        ids.add("204:万年魂环");
        ids.add("205:十万年魂环");
        ids.add("206:百万年魂环");
        ids.add("207:神级魂环");

        ids.add("101:时光胶囊");
        ids.add("102:恢复药水");
        ids.add("103:大瓶经验");
        ids.add("104:力量神石");
        ids.add("105:生命神石");
        ids.add("106:魂力神石");
        ids.add("107:仙品花瓣");
        ids.add("108:极寒花瓣");
        ids.add("109:救援卡");
        ids.add("110:支援卡");
        ids.add("111:融合戒指");
        ids.add("112:精神神石");
        ids.add("113:遗忘药水");

        ids.add("501:未知生物1");
        ids.add("502:唤象魔者");
        ids.add("503:森林巨猩");
        ids.add("504:鬼根藤");
        ids.add("505:毛刺虫魔");
        ids.add("506:亡灵骷髅");
        ids.add("507:未知生物2");
        ids.add("508:亡灵魔狼");
        ids.add("509:血根魔");
        ids.add("510:千足刀树魔");
        ids.add("511:血人蛛王");
        ids.add("512:根焰异羊兽");
        ids.add("513:面噬羊魔");
        ids.add("514:食人铜魔");
        ids.add("515:铜面牛妖");
        ids.add("516:死亡领主");
        ids.add("517:变异蜻蜓妖");
        ids.add("518:红衣掠头者");
        ids.add("519:法老审判者");
        ids.add("520:天巨领主");
        ids.add("601:泰坦雪魔王");
        ids.add("602:冰帝帝皇蝎");
        ids.add("603:极地冰凤凰");
        //===================
        ids.add("1000:暗器零件");
        ids.add("1001:诸葛神弩");
        ids.add("1002:龙须针");
        ids.add("1003:含沙射影");
        ids.add("1004:子母追魂夺命胆");
        ids.add("1005:孔雀翎");
        ids.add("1006:暴雨梨花针");
        ids.add("1007:佛怒唐莲");
        //=======================
        ids.add("1511:低级头部魂骨");
        ids.add("1512:中级头部魂骨");
        ids.add("1513:高级头部魂骨");
        //===
        ids.add("1521:低级左臂魂骨");
        ids.add("1522:中级左臂魂骨");
        ids.add("1523:高级左臂魂骨");
        //===
        ids.add("1531:低级右臂魂骨");
        ids.add("1532:中级右臂魂骨");
        ids.add("1533:高级右臂魂骨");
        //===
        ids.add("1541:低级左腿魂骨");
        ids.add("1542:中级左腿魂骨");
        ids.add("1543:高级左腿魂骨");
        //===
        ids.add("1551:低级右腿魂骨");
        ids.add("1552:中级右腿魂骨");
        ids.add("1553:高级右腿魂骨");
        //==========================
        StringBuilder sb = new StringBuilder();
        for (String s : ids) {
            sb.append(s).append("\r\n");
        }
        Tool.putStringInFile(sb.toString(), path + "/mainfist/ids", "utf-8");
    }

    private static void InitIntro() {
        List<String> tros = new ArrayList<>();

        tros.add("201:十年魂环,使用=>吸收魂环 十年魂环");
        tros.add("202:百年魂环,使用=>吸收魂环 百年魂环");
        tros.add("203:千年魂环,使用=>吸收魂环 千年魂环");
        tros.add("204:万年魂环,使用=>吸收魂环 万年魂环");
        tros.add("205:十万年魂环,使用=>吸收魂环 十万年魂环");
        tros.add("206:百万年魂环,使用=>吸收魂环 百万年魂环");
        tros.add("207:十神级年魂环,使用=>吸收魂环 神级年魂环");

        tros.add("101:时光胶囊清除修炼,进入冷却");
        tros.add("102:恢复药水恢复血量");
        tros.add("103:大瓶经验增加经验");
        tros.add("104:力量神石,增加攻击");
        tros.add("105:生命神石,增加最大生命值");
        tros.add("106:魂力神石,恢复魂力大量");
        tros.add("107:仙品花瓣,下次进星斗森林一定遇到魂兽,其中40%几率遇到与自己实力匹配的!");
        tros.add("108:极寒花瓣,下次进极北之地一定遇到魂兽");
        tros.add("109:救援卡,使用后获得请求支援次数");
        tros.add("110:支援卡,使用后获得支援次数");
        tros.add("111:融合戒指,用于与ta人融合武魂,以激活双修");
        tros.add("112:精神神石,增加最大精神力");
        tros.add("113:遗忘药水,用来忘掉某些事");

        tros.add("1000:暗器零件,用于制作暗器;");
        tros.add("1001:暗器,诸葛神弩,单体伤害,造成 500+攻击x0.9的伤害,需要选择某(所有暗器,不消耗魂力)");
        tros.add("1002:暗器,龙须针,多体伤害,最大3个,伤害:攻击,等级x1000,@即可,#为当前魂兽");
        tros.add("1003:暗器,含沙射影,多体伤害,最大2个,伤害攻击x0.6");
        tros.add("1004:暗器,母追魂夺命胆,多体伤害,最大4个,伤害1500+攻击x0.45");
        tros.add("1005:暗器,孔雀翎,多体伤害,最大3个,伤害,攻击x0.65");
        tros.add("1006:暗器,暴雨梨花针,单体伤害 3000+攻击x2.8");
        tros.add("1007:暗器,佛怒唐莲,多体伤害,最大三个,伤害 4500+攻击x0.72+等级x10");

        tros.add("1511:低级头部魂骨,增强属性,质量随魂骨品质越好而越好");
        tros.add("1512:中级头部魂骨,增强属性,质量随魂骨品质越好而越好");
        tros.add("1513:高级头部魂骨,增强属性,质量随魂骨品质越好而越好");
        //===
        tros.add("1521:低级左臂魂骨,增强属性,质量随魂骨品质越好而越好");
        tros.add("1522:中级左臂魂骨,增强属性,质量随魂骨品质越好而越好");
        tros.add("1523:高级左臂魂骨,增强属性,质量随魂骨品质越好而越好");
        //===
        tros.add("1531:低级右臂魂骨,增强属性,质量随魂骨品质越好而越好");
        tros.add("1532:中级右臂魂骨,增强属性,质量随魂骨品质越好而越好");
        tros.add("1533:高级右臂魂骨,增强属性,质量随魂骨品质越好而越好");
        //===
        tros.add("1541:低级左腿魂骨,增强属性,质量随魂骨品质越好而越好");
        tros.add("1542:中级左腿魂骨,增强属性,质量随魂骨品质越好而越好");
        tros.add("1543:高级左腿魂骨,增强属性,质量随魂骨品质越好而越好");
        //===
        tros.add("1551:低级右腿魂骨,增强属性,质量随魂骨品质越好而越好");
        tros.add("1552:中级右腿魂骨,增强属性,质量随魂骨品质越好而越好");
        tros.add("1553:高级右腿魂骨,增强属性,质量随魂骨品质越好而越好");

        StringBuilder sb = new StringBuilder();
        for (String s : tros) {
            sb.append(s).append("\r\n");
        }
        Tool.putStringInFile(sb.toString(), path + "/mainfist/intros", "utf-8");
    }

    private static void InitShop() {
        List<String> ids = new LinkedList<>();

        ids.add("201:500");
        ids.add("202:2000");
        ids.add("203:5000");
        ids.add("204:10000");
        ids.add("205:120000");
        ids.add("206:1500000");
        ids.add("207:9999999");

        ids.add("101:535");
        ids.add("102:266");
        ids.add("103:496");
        ids.add("104:499");
        ids.add("105:390");
        ids.add("106:360");
        ids.add("107:567");
        ids.add("108:621");
        ids.add("109:489");
        ids.add("110:498");
        ids.add("111:10000");
        ids.add("112:525");
        ids.add("113:889");

        ids.add("1000:215");
        ids.add("1511:630");
        ids.add("1521:630");
        ids.add("1531:630");
        ids.add("1541:630");
        ids.add("1551:630");

        StringBuilder sb = new StringBuilder();
        for (String s : ids) {
            sb.append(s).append("\r\n");
        }
        Tool.putStringInFile(sb.toString(), path + "/mainfist/shop", "utf-8");
    }

    private static void InitWeaList() {
        List<String> ids = new ArrayList<>();

        ids.add("1001:4");
        ids.add("1002:8");
        ids.add("1003:10");
        ids.add("1004:15");
        ids.add("1005:16");
        ids.add("1006:17");
        ids.add("1007:19");

        StringBuilder sb = new StringBuilder();
        for (String s : ids) {
            sb.append(s).append("\r\n");
        }
        Tool.putStringInFile(sb.toString(), path + "/dates/system/WeaList", "utf-8");
    }

    private static void InitWeaONumList() {
        List<String> ids = new ArrayList<>();

        ids.add("1001:2");
        ids.add("1002:2");
        ids.add("1003:2");
        ids.add("1004:2");
        ids.add("1005:2");
        ids.add("1006:2");
        ids.add("1007:2");

        StringBuilder sb = new StringBuilder();
        for (String s : ids) {
            sb.append(s).append("\r\n");
        }
        Tool.putStringInFile(sb.toString(), path + "/dates/system/WeaONumList", "utf-8");
    }

    private static void InitMaps() {
        if (id2NameMaps.isEmpty() || Name2idMaps.isEmpty()) {
            id2NameMaps.clear();
            Name2idMaps.clear();
            String[] str = getStringsFromFile(path + "/mainfist/ids", "utf-8");
            for (String s : str) {
                if (s.isEmpty()) continue;
                String[] ss = s.split(":");
                int i = Integer.parseInt(ss[0].trim());
                id2NameMaps.put(i, ss[1].trim());
                Name2idMaps.put(ss[1].trim(), i);
            }
        }
        if (id2IntroMaps.isEmpty()) {
            id2IntroMaps.clear();
            String[] str = getStringsFromFile(path + "/mainfist/intros", "utf-8");
            for (String s : str) {
                if (s.isEmpty()) continue;
                String[] ss = s.split(":");
                int i = Integer.parseInt(ss[0].trim());
                id2IntroMaps.put(i, ss[1].trim());
            }
        }
        if (id2ShopMaps.isEmpty()) {
            id2ShopMaps.clear();
            String[] str = getStringsFromFile(path + "/mainfist/shop", "utf-8");
            for (String s : str) {
                if (s.isEmpty()) continue;
                String[] ss = s.split(":");
                Integer i1 = Integer.valueOf(ss[0]);
                Long l1 = Long.valueOf(ss[1]);
                id2ShopMaps.put(i1, l1);
            }
        }
        if (id2WeaMaps.isEmpty()) {
            id2WeaMaps.clear();
            String[] sss = getStringsFromFile(path + "/dates/system/WeaList", "utf-8");
            for (String s : sss) {
                if (s.isEmpty()) continue;
                String[] ss = s.split(":");
                Integer i1 = Integer.valueOf(ss[0]);
                Integer l1 = Integer.valueOf(ss[1]);
                id2WeaMaps.put(i1, l1);
            }
        }
        if (id2WeaONumMaps.isEmpty()) {
            id2WeaONumMaps.clear();
            String[] sss = getStringsFromFile(path + "/dates/system/WeaONumList", "utf-8");
            for (String s : sss) {
                if (s.isEmpty()) continue;
                String[] ss = s.split(":");
                Integer i1 = Integer.valueOf(ss[0]);
                Integer l1 = Integer.valueOf(ss[1]);
                id2WeaONumMaps.put(i1, l1);
            }
        }
    }

    private static void InitKilledC() {
        try {
            String[] sss = getStringsFromFile(path + "/dates/system/killedsc");
            for (String s1 : sss) {
                if (s1.trim().isEmpty()) continue;
                String[] ss = s1.split("=");
                Number number = Long.valueOf(ss[0]);
                Integer c = Integer.valueOf(ss[1]);
                killedC.put(number, c);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static int indexKC = 0;

    public static synchronized void OnKilldc(Number who) {
        if (killedC.containsKey(who)) {
            killedC.put(who, killedC.get(who) + 1);
        } else killedC.put(who, 1);
        if (indexKC++ % 10 == 0) {
            flushKilledCMap();
        }
    }

    private static void flushKilledCMap() {
        StringBuilder sb = new StringBuilder();
        for (Number number : killedC.keySet()) {
            sb.append(String.format("%s=%s", number, killedC.get(number))).append("\n");
        }
        putStringInFile(sb.toString(), path + "/dates/system/killedsc");
    }

    /**
     * 1:蓝电霸王龙 2:昊天锤
     * 3:六翼天使 4:噬魂珠皇 5:蓝银皇 6:柔骨兔 7:邪眸白虎 8:邪火凤凰 9:七杀剑 10:碧灵蛇皇 11:破魂枪 12:大力金刚熊
     * 13:奇茸通天菊 14:鬼魅 15:刺豚 16:蛇矛 17:骨龙 18:蛇杖 19:蓝银草 20:玄龟 21:幽冥灵猫  22:光明圣龙
     * 23:黑暗圣龙 24:修罗神剑  25:青龙  26:海神  27:锄头  28:斧头  29:杀神昊天锤 30:魔神剑 31:暗金恐爪熊
     */
    private static void InitWhType() {
        wh2Type.put(-1, -1);
        wh2Type.put(1, 0);
        wh2Type.put(2, 1);//1
        wh2Type.put(3, 0);
        wh2Type.put(4, 0);
        wh2Type.put(5, 2);
        wh2Type.put(6, 0);
        wh2Type.put(7, 0);
        wh2Type.put(8, 0);
        wh2Type.put(9, 1);//2
        wh2Type.put(10, 0);
        wh2Type.put(11, 1);//3
        wh2Type.put(12, 0);
        wh2Type.put(13, 1);//4
        wh2Type.put(14, 0);
        wh2Type.put(15, 0);
        wh2Type.put(16, 2);//5
        wh2Type.put(17, 0);
        wh2Type.put(18, 2);
        wh2Type.put(19, 0);
        wh2Type.put(20, 0);
        wh2Type.put(21, 0);
        wh2Type.put(22, 0);
        wh2Type.put(23, 0);
        wh2Type.put(24, 1);//6
        wh2Type.put(25, 0);
        wh2Type.put(26, 1);//7
        wh2Type.put(27, 1);//8
        wh2Type.put(28, 1);//9
        wh2Type.put(29, 1);//10
        wh2Type.put(30, 1);//11
        wh2Type.put(31, 0);
    }

    public static final String getWhType(int type) {
        switch (type) {
            case 0:
                return "兽武魂";
            case 1:
                return "器武魂";
            case 2:
                return "兽器武魂";
        }
        return "";
    }

    /**
     * 检测存在 若无 则创建
     *
     * @param who
     */
    public static void testMan(Long who) {
        if (!exist(who))
            RegPerson(new PersonInfo().setName(String.valueOf(who)));
    }

    public static boolean exist(Long who) {
        String pathN = path + "/dates/users/" + who;
        File file = new File(pathN);
        boolean k = file.exists();
        System.gc();
        return k;
    }
    //=========================================

    /**
     * 获取背包
     *
     * @param who
     * @return
     */
    public static Integer[] getBgs(Long who) {
        testMan(who);
        String pathN = path + "/dates/users/" + who;
        File file = new File(pathN + "/bgs");
        List<Integer> list = new ArrayList<>();
        for (String s : getStringsFromFile(file.getPath())) {
            if (s.contains(":") || s.equals("0"))
                continue;
            list.add(Integer.valueOf(s));
        }
        return list.toArray(new Integer[list.size()]);
    }

    /**
     * 判断背包是否有足够的 物品
     *
     * @param who 谁
     * @param id  什么东西
     * @param num 有多少
     * @return
     */
    public static boolean contiansBgsNum(Long who, int id, int num) {
        Integer[] bis = getBgs(who);
        int n1 = 0;
        for (int i : bis) {
            if (i == id) {
                n1++;
            }
        }
        return n1 >= num;
    }

    /**
     * 获取 背包里 有多少个 指定物品
     *
     * @param who
     * @param id
     * @return
     */
    public static Integer getNumFromBgs(Long who, int id) {
        Integer[] bis = getBgs(who);
        int n1 = 0;
        for (int i : bis) {
            if (i == id) {
                n1++;
            }
        }
        return n1;
    }

    public static Integer getNumForO(String[] sss, String s1) {
        for (String str : sss) {
            if (str.startsWith(s1)) {
                if (str.contains("x")) {
                    return Integer.valueOf(str.split("x")[1]);
                } else return 1;
            }
        }
        return 0;
    }

    /**
     * 判断某人背包是否存在 指定东西
     *
     * @param id
     * @param who
     * @return
     */
    public static boolean containsInBg(Integer id, Long who) {
        List<Integer> list = Arrays.asList(getBgs(who));
        return list.contains(id);
    }

    /**
     * 获取 玩家 所有暗器
     *
     * @param who
     * @return Map<K ( 序列 ), Entry < K ( 暗器ID ), V ( 暗器次数 )>>
     */
    public static Map<Integer, Map.Entry<Integer, Integer>> getBgsw(Long who) {
        String pathN = path + "/dates/users/" + who;
        File file = new File(pathN + "/Aqbgs");
        Map<Integer, Map.Entry<Integer, Integer>> maps = new LinkedHashMap<>();
        int i = 1;
        for (String s : getStringsFromFile(file.getPath())) {
            if (s.contains(":")) {
                String[] ss = s.split(":");
                maps.put(i, getEntry(Integer.valueOf(ss[0]), Integer.valueOf(ss[1])));
                i++;
            }
        }
        return maps;
    }

    public static PersonInfo createTempInfo(PersonInfo personInfo) {
        personInfo.setTemp(true);
        histInfos.put(Long.parseLong(personInfo.getName()), personInfo);
        Tool.putStringInFile(personInfo.toString(), path + "/dates/users/" + personInfo.getName() + "/infos_temp",
                "utf-8");
        return personInfo;
    }

    public static PersonInfo deleteTempInfo(PersonInfo personInfo) {
        histInfos.remove(Long.parseLong(personInfo.getName()));
        new File(path + "/dates/users/" + personInfo.getName() + "/infos_temp").delete();
        return getInfo(personInfo.getName());
    }

    /**
     * 获取玩家信息
     *
     * @param who
     * @return
     */
    public static PersonInfo getInfo(Long who) {
        if (who == null || who.longValue() <= 0) return null;
        File file;
        String lines;
        if (new File(path + "/dates/users/" + who + "/infos_temp").exists()) {
            file = new File(path + "/dates/users/" + who + "/infos_temp");
        } else {
            file = new File(path + "/dates/users/" + who + "/infos");
        }
        testMan(who);
        try {
            lines = getStringFromFile(file.getPath());
            if (lines == null || lines.isEmpty()) {
                return null;
            }
            PersonInfo personInfo = new PersonInfo();
            if (histInfos.containsKey(who)) {
                personInfo = histInfos.get(who);
            } else {
                personInfo = ParseObj(personInfo, lines);
                histInfos.put(who, personInfo);
            }
            return personInfo;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static PersonInfo getInfo(Number qq) {
        return getInfo(qq.longValue());
    }

    public static PersonInfo getInfo(String who) {
        return getInfo(Long.parseLong(who));
    }

    public static boolean canFix = false;

    private final static void AutoFix(long who) {
        System.err.println("开始修复 游戏数据 =》 " + who);
        if (!canFix) return;
        File file = new File(path + "/dates/users/" + who + "/infos");
        String lines = getStringFromFile(file.getPath());
        if (lines == null || lines.isEmpty())
            RegPerson(new PersonInfo().setName(who + ""));
    }

    public static final Map<Long, PersonInfo> histInfos = new ConcurrentHashMap<>();

    private final static <T extends Object> T ParseObj(T obj, String line) {
        try {
            String[] lines = null;
            lines = line.split(line.contains("\r") ? "\r\n" : "\n");
            for (String s1 : lines) {
                String[] kv = s1.split("=");
                if (kv.length == 1) continue;
                String k = kv[0].trim();
                String v = kv[1].trim();
                if (v.trim().isEmpty()) continue;
                Field field = null;
                Class cls = null;
                try {
                    if (k.equals("level")) k = "Level";
                    field = obj.getClass().getDeclaredField(k);
                    cls = field.getType();
                } catch (Exception e) {
                    continue;
                }
                field.setAccessible(true);
                try {
                    Method method = cls.getMethod("valueOf", String.class);
                    field.set(obj, method.invoke(null, v));
                } catch (Exception e) {
                    if (cls == Number.class)
                        field.set(obj, Long.valueOf(v.toString()));
                    else if (cls == boolean.class || cls == Boolean.class)
                        field.set(obj, Boolean.valueOf(v.toString()));
                    else field.set(obj, v);
                }
            }
            return obj;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 更新信息
     *
     * @param personInfo
     */
    public static void putPerson(PersonInfo personInfo) {
        testMan(Long.valueOf(personInfo.getName()));
        histInfos.put(Long.parseLong(personInfo.getName()), personInfo);
        if (new File(path + "/dates/users/" + personInfo.getName() + "/infos_temp").exists()) {
            personInfo.setTemp(true);
            Tool.putStringInFile(personInfo.toString(), path + "/dates/users/" + personInfo.getName() + "/infos_temp",
                    "utf-8");
        } else {
            Tool.putStringInFile(personInfo.toString(), path + "/dates/users/" + personInfo.getName() + "/infos",
                    "utf-8");
        }
    }

    /**
     * 注册玩家
     *
     * @param personInfo
     */
    public static synchronized void RegPerson(PersonInfo personInfo) {
        try {
            String pathN = path + "/dates/users/" + personInfo.getName();
            File file = new File(path + "/dates/users/" + personInfo.getName() + "/infos");
            file.getParentFile().mkdirs();
            file.createNewFile();
            Tool.putStringInFile(personInfo.toString(), file.getPath(), "utf-8");
            new File(pathN + "/bgs").createNewFile();
            new File(pathN + "/Aqbgs").createNewFile();
            new File(pathN + "/hhpz").createNewFile();
            new File(pathN + "/decide").createNewFile();
            new File(pathN + "/AttributeBone").createNewFile();
            FileInitializeValue.putValues(pathN + "/warp", new Warp().setId(personInfo.getName()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static synchronized Warp getWarp(Number id) {
        testMan(id.longValue());
        String pathN = path + "/dates/users/" + id;
        Warp warp = new Warp().setId(id.toString());
        return FileInitializeValue.getValue(pathN + "/warp", warp, true);
    }

    public static synchronized Warp setWarp(Warp warp) {
        String pathN = path + "/dates/users/" + warp.getId();
        return FileInitializeValue.putValues(pathN + "/warp", warp, true);
    }

    /**
     * 获得所有 魂环Id
     *
     * @param who
     * @return
     */
    public static Integer[] getHhs(Long who) {
        testMan(who);
        List<Integer> ls = new ArrayList<>();
        String pathN = path + "/dates/users/" + who;
        String[] sss = getStringsFromFile(pathN + "/hhpz");
        for (String s : sss) {
            ls.add(Integer.valueOf(s));
        }
        if (ls.isEmpty())
            return new Integer[]{0};
        return ls.toArray(new Integer[ls.size()]);
    }

    /**
     * 获取修炼冷却
     *
     * @param who
     * @return
     */
    public static long getK1(Long who) {
        return getInfo(who).getK1();
    }

    /**
     * 获取进入冷却
     *
     * @param who
     * @return
     */
    public static long getK2(Long who) {
        return getInfo(who).getK2();
    }

    /**
     * 设置修炼冷却
     *
     * @param who
     * @param l
     * @return
     */
    public static long setK1(Long who, long l) {
        putPerson(getInfo(who).setK1(l));
        return l;
    }

    /**
     * 设置进入冷却
     *
     * @param who
     * @param l
     * @return
     */
    public static long setK2(Long who, long l) {
        putPerson(getInfo(who).setK2(l));
        return l;
    }

    /**
     * 获取Id 通过 名字
     *
     * @param id
     * @return
     */
    public static String getNameById(long id) {
        return id2NameMaps.get((int) id);
    }

    /**
     * 获取介绍 通过 Id
     *
     * @param id
     * @return
     */
    public static String getIntroById(int id) {
        return id2IntroMaps.get(id);
    }

    /**
     * 获取图片通过 Id
     *
     * @param id
     * @return
     */
    public static String getImgById(int id) {
        if (id < 50) {
            File file = new File(path + "/mainfist/images");
            File f = new File(file, "WH(" + id + ").jpg");
            return pathToImg(f.getPath());
        } else if (id < 150) {
            File file = new File(path + "/mainfist/images");
            File f = new File(file, "Obj_" + (id - 100) + ".png");
            return pathToImg(f.getPath());
        } else if (id < 250) {
            File file = new File(path + "/mainfist/images");
            File f = new File(file, "Hh(" + (id - 200) + ").png");
            return pathToImg(f.getPath());
        } else if (id < 400) {
            File file = new File(path + "/mainfist/images");
            File f = new File(file, "Hh-" + (id - 300) + ".gif");
            return pathToImg(f.getPath());
        } else if (id < 700) {
            File file = new File(path + "/mainfist/images");
            File f = new File(file, "ghost (" + (id - 500) + ").jpg");
            return pathToImg(f.getPath());
        } else if (id < 1100) {
            File file = new File(path + "/mainfist/images");
            File f = new File(file, "Weapon_" + (id - 1000) + ".png");
            return pathToImg(f.getPath());
        } else if (id < 1600) {
            File file = new File(path + "/mainfist/images");
            File f = new File(file, getBoneImg(id));
            return pathToImg(f.getPath());
        }
        throw new RuntimeException();
    }

    public static String getImgById(int id, boolean k) {
        if (id < 50) {
            File file = new File(path + "/mainfist/images");
            File f = new File(file, "WH(" + id + ").jpg");
            return pathToImg(f.getPath());
        } else if (id < 150) {
            File file = new File(path + "/mainfist/images");
            File f = new File(file, "Obj_" + (id - 100) + ".png");
            return pathToImg(f.getPath());
        } else if (id < 250) {
            File file = new File(path + "/mainfist/images");
            File f = new File(file, "Hh(" + (id - 200) + ").png");
            return pathToImg(f.getPath());
        } else if (id < 400) {
            File file = new File(path + "/mainfist/images");
            File f = new File(file, "Hh-" + (id - 300) + ".gif");
            return pathToImg(f.getPath());
        } else if (id < 700) {
            File file = new File(path + "/mainfist/images");
            File f = new File(file, "ghost (" + (id - 500) + ").jpg");
            return pathToImg(f.getPath());
        } else if (id < 1100) {
            File file = new File(path + "/mainfist/images");
            File f = new File(file, "Weapon_" + (id - 1000) + ".png");
            return pathToImg(f.getPath());
        } else if (id < 1600) {
            File file = new File(path + "/mainfist/images");
            File f = new File(file, getBoneImg(id));
            return k ? pathToImg(f.getPath()) : f.getPath();
        }
        throw new RuntimeException();
    }

    private static String getBoneImg(Integer id) {
        String name = "";
        if (id > 1500 && id < 1600) {
            int ni;
            if (id < 1520) {
                ni = id - 1510;
                name = "Head_" + ni + ".png";
            } else if (id < 1530) {
                ni = id - 1520;
                name = "HandLeft_" + ni + ".png";
            } else if (id < 1540) {
                ni = id - 1530;
                name = "HandRight_" + ni + ".png";
            } else if (id < 1550) {
                ni = id - 1540;
                name = "LegLeft_" + ni + ".png";
            } else if (id < 1560) {
                ni = id - 1550;
                name = "LegRight_" + ni + ".png";
            }
        }
        return name;
    }

    /**
     * 获取商店字符数组
     *
     * @return
     */
    public static String[] getShop() {
        List<String> ls = new LinkedList<>();
        List<Integer> is = new LinkedList<>();
        for (Integer i : id2ShopMaps.keySet()) {
            is.add(i);
        }
        Collections.sort(is, new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o1 - o2;
            }
        });
        for (Integer i : is) {
            ls.add(getNameById(i) + "=>" + id2ShopMaps.get(i) + "个金魂币");
        }
        return ls.toArray(new String[ls.size()]);
    }

    /**
     * 添加一个魂环
     *
     * @param who
     * @param id
     * @return
     */
    public static String addHh(Long who, int id) {
        String pathN = path + "/dates/users/" + who + "/hhpz";
        addStingInFile(id + "", pathN, "utf-8");
        return "OK";
    }

    /**
     * 添加 物品 到 背包
     *
     * @param who
     * @param id
     * @return
     */
    public static String addToBgs(Long who, int id, ObjType type) {
        String pathN = path + "/dates/users/" + who + "/bgs";
        addStingInFile(id + "", pathN, "utf-8");
        GotOrLostObjBroadcast.INSTANCE.broadcast(who, id, 1,
                type);
        return "OK";
    }

    /**
     * 添加 物品 到 背包
     *
     * @param who
     * @param id
     * @return
     */
    public static String addToBgs(Long who, int id, Integer num, ObjType type) {
        String pathN = path + "/dates/users/" + who + "/bgs";
        for (int i = 0; i < num; i++)
            addStingInFile(id + "", pathN, "utf-8");
        GotOrLostObjBroadcast.INSTANCE.broadcast(who, id, num,
               type);
        return "OK";
    }

    /**
     * 从背部 移除物品 通过 Id
     *
     * @param who
     * @param id
     * @return
     */
    public static String removeFromBgs(Long who, int id, ObjType type) {
        String pathN = path + "/dates/users/" + who + "/bgs";
        String ids = id + "";
        List<String> ss = new ArrayList<>(Arrays.asList(getStringsFromFile(pathN)));
        if (ss.remove(ids)) {
            Tool.putStringInFile("", pathN, "utf-8");
            for (String s : ss) {
                addStingInFile(s, pathN, "utf-8");
            }
        }
        GotOrLostObjBroadcast.INSTANCE.broadcast(who, id, 1,
                type);
        return "OK";
    }

    /**
     * 从背包 移除物品 通过 Id
     *
     * @param who
     * @param id
     * @return
     */
    public static String removeFromBgs(Long who, int id, int num, ObjType type) {
        String pathN = path + "/dates/users/" + who + "/bgs";
        String ids = id + "";
        List<String> ss = new ArrayList<>(Arrays.asList(getStringsFromFile(pathN)));
        boolean k = false;
        for (int i = num; i > 0; i--) {
            if (ss.remove(ids)) {
                k = true;
            }
        }
        if (k) {
            Tool.putStringInFile("", pathN, "utf-8");
            for (String s : ss) {
                addStingInFile(s, pathN, "utf-8");
            }
        }
        GotOrLostObjBroadcast.INSTANCE.broadcast(who, id, num,
                type);
        return "OK";
    }

    /**
     * 添加暗器 到 背包
     *
     * @param who
     * @param o
     * @return
     */
    public static String addToAqBgs(Long who, String o) {
        String pathN = path + "/dates/users/" + who + "/Aqbgs";
        addStingInFile(o, pathN, "utf-8");
        return "OK";
    }

    /**
     * 移除 暗器
     *
     * @param who
     * @param o
     * @return
     */
    public static String removeFromAqBgs(Long who, String o) {
        String pathN = path + "/dates/users/" + who + "/Aqbgs";
        String ids = o + "";
        List<String> ss = new ArrayList<>(Arrays.asList(getStringsFromFile(pathN)));
        if (ss.remove(ids)) {
            Tool.putStringInFile("", pathN, "utf-8");
            for (String s : ss) {
                addStingInFile(s, pathN, "utf-8");
            }
        }
        return "OK";
    }

    public static String removeFromAqBgs(String who, String o) {
        String pathN = path + "/dates/users/" + who + "/Aqbgs";
        String ids = o + "";
        List<String> ss = new ArrayList<>(Arrays.asList(getStringsFromFile(pathN)));
        if (ss.remove(ids)) {
            Tool.putStringInFile("", pathN, "utf-8");
            for (String s : ss) {
                addStingInFile(s, pathN, "utf-8");
            }
        }
        return "OK";
    }

    /**
     * 获取 购买冷却
     *
     * @param who
     * @return
     */
    public static long getGk1(Long who) {
        return getInfo(who).getGk1();
    }

    /**
     * 获取数据
     *
     * @param who
     * @param dataName
     * @param data
     * @return
     */
    private static synchronized boolean putData(Long who, String dataName, Object data) {
        String pathN = path + "/dates/users/" + who + "/" + dataName;
        testFile(pathN);
        try {
            if (data == null) {
                return Tool.putStringInFile("", pathN);
            } else {
                ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(pathN));
                oos.writeObject(data);
                oos.close();
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 获取数据
     *
     * @param who
     * @param dataName
     * @param data
     * @return
     */
    public static synchronized boolean putDataString(Long who, String dataName, Object data) {
        String pathN = path + "/dates/users/" + who + "/" + dataName;
        testFile(pathN);
        try {
            return Tool.putStringInFile(data == null ? "" : data.toString(), pathN);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 获取数据
     *
     * @param who
     * @param dataName
     * @return
     */
    private static synchronized Object getData(Long who, String dataName) {
        String pathN = path + "/dates/users/" + who + "/" + dataName;
        testFile(pathN);
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(pathN));
            Object o = ois.readObject();
            ois.close();
            return o;
        } catch (Exception e) {
            System.err.println("Objective-C get Failed");
        }
        return null;
    }

    /**
     * 获取数据
     *
     * @param who
     * @param dataName
     * @return
     */
    public static synchronized Object getDataString(Long who, String dataName) {
        String pathN = path + "/dates/users/" + who + "/" + dataName;
        testFile(pathN);
        try {
            StringBuffer sb = new StringBuffer();
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(pathN)));
            String line = null;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();
        } catch (Exception e) {
            System.err.println("Objective-C get Failed");
        }
        return null;
    }

    public static String getStringFromData(Long who, String DataName) {
        String pathN = path + "/dates/users/" + who + "/" + DataName;
        return getStringFromFile(pathN, "utf-8");
    }

    public static boolean putStringFromData(Long who, String DataName, String line) {
        String pathN = path + "/dates/users/" + who + "/" + DataName;
        return Tool.putStringInFile(line, pathN, "utf-8");
    }
}
