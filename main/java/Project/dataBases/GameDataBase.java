package Project.dataBases;


import Project.aSpring.SpringBootResource;
import Project.broadcast.enums.ObjType;
import Project.broadcast.game.GotOrLostObjBroadcast;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.github.kloping.initialize.FileInitializeValue;
import io.github.kloping.mirai0.Entitys.gameEntitys.PersonInfo;
import io.github.kloping.mirai0.Entitys.gameEntitys.Warp;
import io.github.kloping.mirai0.unitls.Tools.Tool;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static io.github.kloping.mirai0.unitls.Tools.Tool.*;

/**
 * @author github-kloping
 */
public class GameDataBase {
    public static String path;

    public static final Map<Integer, String> ID_2_NAME_MAPS = new ConcurrentHashMap<>();
    public static final Map<Integer, String> ID_2_INTRO_MAPS = new ConcurrentHashMap<>();
    public static final Map<Integer, String> NAME_2_INTRO_MAPS = new ConcurrentHashMap<>();
    public static final Map<String, Integer> NAME_2_ID_MAPS = new ConcurrentHashMap<>();
    public static final Map<Integer, Long> ID_2_SHOP_MAPS = new ConcurrentHashMap<>();
    public static final Map<Integer, Integer> ID_2_WEA_MAPS = new ConcurrentHashMap<>();
    public static final Map<Integer, Integer> ID_2_WEA_O_NUM_MAPS = new ConcurrentHashMap<>();
    public static final Map<Integer, Integer> WH_2_TYPE = new ConcurrentHashMap<>();

    public GameDataBase(String mainPath) {
        try {
            path = mainPath + "/dates/games";
            File file = new File(path);
            if (!file.exists()) {
                new File(path + "/dates/users").mkdirs();
                new File(path + "/dates/system").mkdirs();
                new File(path + "/mainfist").mkdirs();
                new File(path + "/mainfist/ids").createNewFile();
                new File(path + "/mainfist/intros").createNewFile();
                new File(path + "/mainfist/shop").createNewFile();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        intiObj();
    }

    private static void intiObj() {
        initName();
        initIntro();
        initShop();
        initWeaList();
        initWeaONumList();
        initWhType();
    }

    private static void initName() {
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
                "31:暗金恐爪熊\n";
        for (String s : whs.trim().split("\n")) {
            if (s.isEmpty()) {
                continue;
            }
            String[] ss = s.split(":");
            int id = Integer.parseInt(ss[0]);
            ID_2_NAME_MAPS.put(id, ss[1]);
        }

        ID_2_NAME_MAPS.put(201, "十年魂环");
        ID_2_NAME_MAPS.put(202, "百年魂环");
        ID_2_NAME_MAPS.put(203, "千年魂环");
        ID_2_NAME_MAPS.put(204, "万年魂环");
        ID_2_NAME_MAPS.put(205, "十万年魂环");
        ID_2_NAME_MAPS.put(206, "百万年魂环");
        ID_2_NAME_MAPS.put(207, "神级魂环");

        ID_2_NAME_MAPS.put(101, "时光胶囊");
        ID_2_NAME_MAPS.put(102, "恢复药水");
        ID_2_NAME_MAPS.put(103, "大瓶经验");
        ID_2_NAME_MAPS.put(104, "力量神石");
        ID_2_NAME_MAPS.put(105, "生命神石");
        ID_2_NAME_MAPS.put(106, "魂力神石");
        ID_2_NAME_MAPS.put(107, "仙品花瓣");
        ID_2_NAME_MAPS.put(108, "极寒花瓣");
        ID_2_NAME_MAPS.put(109, "救援卡");
        ID_2_NAME_MAPS.put(110, "支援卡");
        ID_2_NAME_MAPS.put(111, "融合戒指");
        ID_2_NAME_MAPS.put(112, "精神神石");
        ID_2_NAME_MAPS.put(113, "遗忘药水");
        //====
        ID_2_NAME_MAPS.put(501, "未知生物1");
        ID_2_NAME_MAPS.put(502, "唤象魔者");
        ID_2_NAME_MAPS.put(503, "森林巨猩");
        ID_2_NAME_MAPS.put(504, "鬼根藤");
        ID_2_NAME_MAPS.put(505, "毛刺虫魔");
        ID_2_NAME_MAPS.put(506, "亡灵骷髅");
        ID_2_NAME_MAPS.put(507, "未知生物2");
        ID_2_NAME_MAPS.put(508, "亡灵魔狼");
        ID_2_NAME_MAPS.put(509, "血根魔");
        ID_2_NAME_MAPS.put(510, "千足刀树魔");
        ID_2_NAME_MAPS.put(511, "血人蛛王");
        ID_2_NAME_MAPS.put(512, "根焰异羊兽");
        ID_2_NAME_MAPS.put(513, "面噬羊魔");
        ID_2_NAME_MAPS.put(514, "食人铜魔");
        ID_2_NAME_MAPS.put(515, "铜面牛妖");
        ID_2_NAME_MAPS.put(516, "死亡领主");
        ID_2_NAME_MAPS.put(517, "变异蜻蜓妖");
        ID_2_NAME_MAPS.put(518, "红衣掠头者");
        ID_2_NAME_MAPS.put(519, "法老审判者");
        ID_2_NAME_MAPS.put(520, "天巨领主");
        ID_2_NAME_MAPS.put(601, "泰坦雪魔王");
        ID_2_NAME_MAPS.put(602, "冰帝帝皇蝎");
        ID_2_NAME_MAPS.put(603, "极地冰凤凰");
        //==================
        ID_2_NAME_MAPS.put(701, "枯木翼龙");
        ID_2_NAME_MAPS.put(702, "幻夕魔兽");
        ID_2_NAME_MAPS.put(703, "熔岩烈兽");
        ID_2_NAME_MAPS.put(704, "狱火岩魔龙");
        ID_2_NAME_MAPS.put(705, "地魁岩兽");
        //===================
        ID_2_NAME_MAPS.put(1000, "暗器零件");
        ID_2_NAME_MAPS.put(1001, "诸葛神弩");
        ID_2_NAME_MAPS.put(1002, "龙须针");
        ID_2_NAME_MAPS.put(1003, "含沙射影");
        ID_2_NAME_MAPS.put(1004, "子母追魂夺命胆");
        ID_2_NAME_MAPS.put(1005, "孔雀翎");
        ID_2_NAME_MAPS.put(1006, "暴雨梨花针");
        ID_2_NAME_MAPS.put(1007, "佛怒唐莲");
        //=======================
        ID_2_NAME_MAPS.put(1511, "低级头部魂骨");
        ID_2_NAME_MAPS.put(1512, "中级头部魂骨");
        ID_2_NAME_MAPS.put(1513, "高级头部魂骨");
        //===
        ID_2_NAME_MAPS.put(1521, "低级左臂魂骨");
        ID_2_NAME_MAPS.put(1522, "中级左臂魂骨");
        ID_2_NAME_MAPS.put(1523, "高级左臂魂骨");
        //===
        ID_2_NAME_MAPS.put(1531, "低级右臂魂骨");
        ID_2_NAME_MAPS.put(1532, "中级右臂魂骨");
        ID_2_NAME_MAPS.put(1533, "高级右臂魂骨");
        //===
        ID_2_NAME_MAPS.put(1541, "低级左腿魂骨");
        ID_2_NAME_MAPS.put(1542, "中级左腿魂骨");
        ID_2_NAME_MAPS.put(1543, "高级左腿魂骨");
        //===
        ID_2_NAME_MAPS.put(1551, "低级右腿魂骨");
        ID_2_NAME_MAPS.put(1552, "中级右腿魂骨");
        ID_2_NAME_MAPS.put(1553, "高级右腿魂骨");
        //===
        ID_2_NAME_MAPS.put(1601, "白升级券");
        ID_2_NAME_MAPS.put(1602, "黄升级券");
        ID_2_NAME_MAPS.put(1603, "紫升级券");
        ID_2_NAME_MAPS.put(1604, "黑升级券");
        ID_2_NAME_MAPS.put(1605, "红升级券");

        ID_2_NAME_MAPS.forEach((k, v) -> {
            NAME_2_ID_MAPS.put(v, k);
        });
    }

    private static void initIntro() {
        ID_2_INTRO_MAPS.put(201, "十年魂环,使用=>吸收魂环 十年魂环");
        ID_2_INTRO_MAPS.put(202, "百年魂环,使用=>吸收魂环 百年魂环");
        ID_2_INTRO_MAPS.put(203, "千年魂环,使用=>吸收魂环 千年魂环");
        ID_2_INTRO_MAPS.put(204, "万年魂环,使用=>吸收魂环 万年魂环");
        ID_2_INTRO_MAPS.put(205, "十万年魂环,使用=>吸收魂环 十万年魂环");
        ID_2_INTRO_MAPS.put(206, "百万年魂环,使用=>吸收魂环 百万年魂环");
        ID_2_INTRO_MAPS.put(207, "神级年魂环,使用=>吸收魂环 神级年魂环");

        ID_2_INTRO_MAPS.put(101, "时光胶囊清除修炼,进入冷却");
        ID_2_INTRO_MAPS.put(102, "恢复药水恢复血量");
        ID_2_INTRO_MAPS.put(103, "大瓶经验增加经验");
        ID_2_INTRO_MAPS.put(104, "力量神石,增加攻击");
        ID_2_INTRO_MAPS.put(105, "生命神石,增加最大生命值");
        ID_2_INTRO_MAPS.put(106, "魂力神石,恢复魂力大量");
        ID_2_INTRO_MAPS.put(107, "仙品花瓣,下次进星斗森林一定遇到魂兽,其中34%几率遇到与自己实力匹配的!");
        ID_2_INTRO_MAPS.put(108, "极寒花瓣,下次进极北之地一定遇到魂兽");
        ID_2_INTRO_MAPS.put(109, "救援卡,使用后获得请求支援次数");
        ID_2_INTRO_MAPS.put(110, "支援卡,使用后获得支援次数");
        ID_2_INTRO_MAPS.put(111, "融合戒指,用于与ta人融合武魂,以激活双修");
        ID_2_INTRO_MAPS.put(112, "精神神石,增加最大精神力");
        ID_2_INTRO_MAPS.put(113, "遗忘药水,用来忘掉某些事");

        ID_2_INTRO_MAPS.put(1000, "暗器零件,用于制作暗器;");
        ID_2_INTRO_MAPS.put(1001, "暗器,诸葛神弩,单体伤害,造成 500+攻击x0.9的伤害,需要选择某(所有暗器,不消耗魂力)");
        ID_2_INTRO_MAPS.put(1002, "暗器,龙须针,多体伤害,最大3个,伤害,攻击,等级x1000,@即可,#为当前魂兽");
        ID_2_INTRO_MAPS.put(1003, "暗器,含沙射影,多体伤害,最大2个,伤害攻击x0.6");
        ID_2_INTRO_MAPS.put(1004, "暗器,子母追魂夺命胆,多体伤害,最大4个,伤害1500+攻击x0.45");
        ID_2_INTRO_MAPS.put(1005, "暗器,孔雀翎,多体伤害,最大3个,伤害,攻击x0.65");
        ID_2_INTRO_MAPS.put(1006, "暗器,暴雨梨花针,单体伤害 3000+攻击x2.8");
        ID_2_INTRO_MAPS.put(1007, "暗器,佛怒唐莲,多体伤害,最大三个,伤害 4500+攻击x0.72+等级x10");

        ID_2_INTRO_MAPS.put(1511, "低级头部魂骨,增强属性,质量随魂骨品质越好而越好");
        ID_2_INTRO_MAPS.put(1512, "中级头部魂骨,增强属性,质量随魂骨品质越好而越好");
        ID_2_INTRO_MAPS.put(1513, "高级头部魂骨,增强属性,质量随魂骨品质越好而越好");
        //===
        ID_2_INTRO_MAPS.put(1521, "低级左臂魂骨,增强属性,质量随魂骨品质越好而越好");
        ID_2_INTRO_MAPS.put(1522, "中级左臂魂骨,增强属性,质量随魂骨品质越好而越好");
        ID_2_INTRO_MAPS.put(1523, "高级左臂魂骨,增强属性,质量随魂骨品质越好而越好");
        //===
        ID_2_INTRO_MAPS.put(1531, "低级右臂魂骨,增强属性,质量随魂骨品质越好而越好");
        ID_2_INTRO_MAPS.put(1532, "中级右臂魂骨,增强属性,质量随魂骨品质越好而越好");
        ID_2_INTRO_MAPS.put(1533, "高级右臂魂骨,增强属性,质量随魂骨品质越好而越好");
        //===
        ID_2_INTRO_MAPS.put(1541, "低级左腿魂骨,增强属性,质量随魂骨品质越好而越好");
        ID_2_INTRO_MAPS.put(1542, "中级左腿魂骨,增强属性,质量随魂骨品质越好而越好");
        ID_2_INTRO_MAPS.put(1543, "高级左腿魂骨,增强属性,质量随魂骨品质越好而越好");
        //===
        ID_2_INTRO_MAPS.put(1551, "低级右腿魂骨,增强属性,质量随魂骨品质越好而越好");
        ID_2_INTRO_MAPS.put(1552, "中级右腿魂骨,增强属性,质量随魂骨品质越好而越好");
        ID_2_INTRO_MAPS.put(1553, "高级右腿魂骨,增强属性,质量随魂骨品质越好而越好");
        //===
        ID_2_INTRO_MAPS.put(1601, "白升级券,用于合成更高记得升级券,升级魂环");
        ID_2_INTRO_MAPS.put(1602, "黄升级券,用于合成更高记得升级券,升级魂环");
        ID_2_INTRO_MAPS.put(1603, "紫升级券,用于合成更高记得升级券,升级魂环");
        ID_2_INTRO_MAPS.put(1604, "黑升级券,用于合成更高记得升级券,升级魂环");
        ID_2_INTRO_MAPS.put(1605, "红升级券,升级魂环");
        //====
        ID_2_INTRO_MAPS.put(701, "每5秒对敌人造成枯木翼龙的最大生命值得1%的伤害");
        ID_2_INTRO_MAPS.put(702, "出生自带20%的最大生命值得护盾");
        ID_2_INTRO_MAPS.put(703, "被攻击时随50%几率造成攻击的15%的反弹伤害");
        ID_2_INTRO_MAPS.put(704, "来自地狱魔魔兽,免疫一次死亡");
        ID_2_INTRO_MAPS.put(705, "吸收大地之力,血量低于25%时恢复已损失18%的生命值,冷却30秒");
    }

    private static void initShop() {
        ID_2_SHOP_MAPS.put(201, 500L);
        ID_2_SHOP_MAPS.put(202, 2000L);
        ID_2_SHOP_MAPS.put(203, 5000L);
        ID_2_SHOP_MAPS.put(204, 10000L);
        ID_2_SHOP_MAPS.put(205, 120000L);
        ID_2_SHOP_MAPS.put(206, 1500000L);
        ID_2_SHOP_MAPS.put(207, 9999999L);

        ID_2_SHOP_MAPS.put(101, 535L);
        ID_2_SHOP_MAPS.put(102, 266L);
        ID_2_SHOP_MAPS.put(103, 496L);
        ID_2_SHOP_MAPS.put(104, 499L);
        ID_2_SHOP_MAPS.put(105, 390L);
        ID_2_SHOP_MAPS.put(106, 360L);
        ID_2_SHOP_MAPS.put(107, 567L);
        ID_2_SHOP_MAPS.put(108, 621L);
        ID_2_SHOP_MAPS.put(109, 489L);
        ID_2_SHOP_MAPS.put(110, 498L);
        ID_2_SHOP_MAPS.put(111, 10000L);
        ID_2_SHOP_MAPS.put(112, 525L);
        ID_2_SHOP_MAPS.put(113, 889L);

        ID_2_SHOP_MAPS.put(1000, 215L);

        ID_2_SHOP_MAPS.put(1511, 630L);
        ID_2_SHOP_MAPS.put(1521, 630L);
        ID_2_SHOP_MAPS.put(1531, 630L);
        ID_2_SHOP_MAPS.put(1541, 630L);
        ID_2_SHOP_MAPS.put(1551, 630L);
    }

    private static void initWeaList() {
        ID_2_WEA_MAPS.put(1001, 4);
        ID_2_WEA_MAPS.put(1002, 8);
        ID_2_WEA_MAPS.put(1003, 10);
        ID_2_WEA_MAPS.put(1004, 15);
        ID_2_WEA_MAPS.put(1005, 16);
        ID_2_WEA_MAPS.put(1006, 17);
        ID_2_WEA_MAPS.put(1007, 19);
    }

    private static void initWeaONumList() {
        ID_2_WEA_O_NUM_MAPS.put(1001, 2);
        ID_2_WEA_O_NUM_MAPS.put(1002, 2);
        ID_2_WEA_O_NUM_MAPS.put(1003, 2);
        ID_2_WEA_O_NUM_MAPS.put(1004, 2);
        ID_2_WEA_O_NUM_MAPS.put(1005, 2);
        ID_2_WEA_O_NUM_MAPS.put(1006, 2);
        ID_2_WEA_O_NUM_MAPS.put(1007, 2);
    }

    private static void initWhType() {
        WH_2_TYPE.put(-1, -1);
        WH_2_TYPE.put(1, 0);
        WH_2_TYPE.put(2, 1);//1
        WH_2_TYPE.put(3, 0);
        WH_2_TYPE.put(4, 0);
        WH_2_TYPE.put(5, 2);
        WH_2_TYPE.put(6, 0);
        WH_2_TYPE.put(7, 0);
        WH_2_TYPE.put(8, 0);
        WH_2_TYPE.put(9, 1);//2
        WH_2_TYPE.put(10, 0);
        WH_2_TYPE.put(11, 1);//3
        WH_2_TYPE.put(12, 0);
        WH_2_TYPE.put(13, 1);//4
        WH_2_TYPE.put(14, 0);
        WH_2_TYPE.put(15, 0);
        WH_2_TYPE.put(16, 2);//5
        WH_2_TYPE.put(17, 0);
        WH_2_TYPE.put(18, 2);
        WH_2_TYPE.put(19, 0);
        WH_2_TYPE.put(20, 0);
        WH_2_TYPE.put(21, 0);
        WH_2_TYPE.put(22, 0);
        WH_2_TYPE.put(23, 0);
        WH_2_TYPE.put(24, 1);//6
        WH_2_TYPE.put(25, 0);
        WH_2_TYPE.put(26, 1);//7
        WH_2_TYPE.put(27, 1);//8
        WH_2_TYPE.put(28, 1);//9
        WH_2_TYPE.put(29, 1);//10
        WH_2_TYPE.put(30, 1);//11
        WH_2_TYPE.put(31, 0);
    }

    public static void onKilled(Number who) {
        Integer n0 = SpringBootResource.getKillGhostMapper().getNum(who.longValue());
        n0 = n0 == null ? 1 : n0 + 1;
        SpringBootResource.getKillGhostMapper().update(n0, who.longValue());
    }

    public static final String getWhType(int type) {
        switch (type) {
            case 0:
                return "兽武魂";
            case 1:
                return "器武魂";
            case 2:
                return "兽器武魂";
            default:
                return "未知类型";
        }
    }

    /**
     * 检测存在 若无 则创建
     *
     * @param who
     */
    public static void testMan(Long who) {
        if (!exist(who))
            regPerson(new PersonInfo().setName(String.valueOf(who)));
    }

    public static boolean exist(Long who) {
        return SpringBootResource.getPersonInfoMapper().selectById(who.toString()) != null;
    }

    //=========================================

    /**
     * 获取背包
     *
     * @param who
     * @return
     */
    public static Integer[] getBgs(Long who) {
        List<Integer> bags = SpringBootResource.getBagMapper().selectAll(who.longValue());
        return bags.toArray(new Integer[bags.size()]);
    }

    public static Integer[] getBgsFromFile(Long who) {
        testMan(who);
        String pathN = path + "/dates/users/" + who;
        File file = new File(pathN + "/bgs");
        List<Integer> list = new ArrayList<>();
        for (String s : getStringsFromFile(file.getPath())) {
            s = s.trim();
            if (s.startsWith("//") || s.startsWith("#") || s.contains(":") || s.equals("0"))
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
        Map<Integer, Map.Entry<Integer, Integer>> maps = new LinkedHashMap<>();
        int i = 1;
        for (Map<String, Integer> map : SpringBootResource.getAqBagMapper().selectAq(who.longValue())) {
            maps.put(i++, getEntry(map.get("oid"), map.get("num")));
        }
        return maps;
    }

    public static Map<Integer, Map.Entry<Integer, Integer>> getBgswFromFile(Long who) {
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

    /**
     * 获取玩家信息
     *
     * @param who
     * @return
     */
    public static PersonInfo getInfo(Long who) {
        QueryWrapper<PersonInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", who.toString());
        PersonInfo info = SpringBootResource.getPersonInfoMapper().selectOne(queryWrapper);
        return info;
    }

    public static PersonInfo getInfoFromFile(Long who) {
        if (who == null || who.longValue() <= 0) return null;
        File file;
        String lines;
        if (new File(path + "/dates/users/" + who + "/infos_temp").exists()) {
            file = new File(path + "/dates/users/" + who + "/infos_temp");
        } else {
            file = new File(path + "/dates/users/" + who + "/infos");
        }
        try {
            PersonInfo personInfo;
            if (HIST_INFOS.containsKey(who)) {
                personInfo = HIST_INFOS.get(who);
            } else {
                lines = getStringFromFile(file.getPath());
                if (lines == null || lines.isEmpty()) {
                    return null;
                }
                personInfo = new PersonInfo();
                personInfo = parseObj(personInfo, lines);
                HIST_INFOS.put(who, personInfo);
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

    public static final Map<Long, PersonInfo> HIST_INFOS = new ConcurrentHashMap<>();

    private final static <T extends Object> T parseObj(T obj, String line) {
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
                    if ("level".equals(k)) k = "Level";
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
        HIST_INFOS.put(Long.parseLong(personInfo.getName()), personInfo);
        SpringBootResource.getPersonInfoMapper().updateById(personInfo);
//        if (new File(path + "/dates/users/" + personInfo.getName() + "/infos_temp").exists()) {
//            personInfo.setTemp(true);
//            Tool.putStringInFile(personInfo.toString(), path + "/dates/users/" + personInfo.getName() + "/infos_temp",
//                    "utf-8");
//        } else {
//            Tool.putStringInFile(personInfo.toString(), path + "/dates/users/" + personInfo.getName() + "/infos",
//                    "utf-8");
//        }
    }

    /**
     * 注册玩家
     *
     * @param personInfo
     */
    public static void regPerson(PersonInfo personInfo) {
        SpringBootResource.getPersonInfoMapper().insert(personInfo);
//        try {
//            String pathN = path + "/dates/users/" + personInfo.getName();
//            File file = new File(path + "/dates/users/" + personInfo.getName() + "/infos");
//            file.getParentFile().mkdirs();
//            file.createNewFile();
//            Tool.putStringInFile(personInfo.toString(), file.getPath(), "utf-8");
//            new File(pathN + "/bgs").createNewFile();
//            new File(pathN + "/Aqbgs").createNewFile();
//            new File(pathN + "/hhpz").createNewFile();
//            new File(pathN + "/decide").createNewFile();
//            new File(pathN + "/AttributeBone").createNewFile();
//            FileInitializeValue.putValues(pathN + "/warp", new Warp().setId(personInfo.getName()));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

    }

    public static Warp getWarp(Number id) {
        return SpringBootResource.getWarpMapper().selectById(id.longValue());
    }

    public static Warp getWarpFromFile(Number id) {
        String pathN = path + "/dates/users/" + id;
        Warp warp = new Warp().setId(id.toString());
        return FileInitializeValue.getValue(pathN + "/warp", warp, true);
    }

    public static Warp setWarp(Warp warp) {
        SpringBootResource.getWarpMapper().updateById(warp);
        return warp;
    }

    /**
     * 获得所有 魂环Id
     *
     * @param who
     * @return
     */
    public static Integer[] getHhs(Long who) {
        List<Integer> ls = SpringBootResource.getHhpzMapper().select(who.longValue());
        return ls.toArray(new Integer[ls.size()]);
    }

    public static Integer[] getHhsFromFile(Long who) {
        testMan(who);
        List<Integer> ls = new ArrayList<>();
        String pathN = path + "/dates/users/" + who;
        String[] sss = getStringsFromFile(pathN + "/hhpz");
        for (String s : sss) {
            ls.add(Integer.valueOf(s));
        }
        if (ls.isEmpty()) {
            return new Integer[]{0};
        }
        return ls.toArray(new Integer[ls.size()]);
    }

    /**
     * 更新魂环
     *
     * @param who
     * @param ints
     */
    public static void upHh(Long who, Integer st, Integer oid) {
        Integer id = SpringBootResource.getHhpzMapper().selectIds(who.longValue()).get(st);
        SpringBootResource.getHhpzMapper().update(st, oid);
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
        return ID_2_NAME_MAPS.get((int) id);
    }

    /**
     * 获取介绍 通过 Id
     *
     * @param id
     * @return
     */
    public static String getIntroById(int id) {
        return ID_2_INTRO_MAPS.get(id);
    }

    /**
     * 获取介绍
     *
     * @param name
     * @return
     */
    public static String getIntroById(String name) {
        return "";
    }


    /**
     * 获取图片通过 Id
     *
     * @param id
     * @param k  true 时 pathToImg false 真实路径
     * @return
     */
    public static String getImgById(int id, boolean k) {
        File f = null;
        File file = new File(path + "/mainfist/images");
        if (!file.exists()) file = new File("./images/game");
        file.mkdirs();
        if (id < 0) {
            switch (id) {
                case -1:
                    f = new File(file, "gold.png");
                    break;
                case -2:
                    f = new File(file, "sword.png");
                    break;
            }
        } else if (id < 50) {
            f = new File(file, "WH(" + id + ").jpg");
        } else if (id < 150) {
            f = new File(file, "Obj_" + (id - 100) + ".png");
        } else if (id < 250) {
            f = new File(file, "Hh(" + (id - 200) + ").png");
        } else if (id < 400) {
            f = new File(file, "Hh-" + (id - 300) + ".gif");
        } else if (id < 800) {
            f = new File(file, "ghost (" + (id - 500) + ").jpg");
        } else if (id < 1100) {
            f = new File(file, "Weapon_" + (id - 1000) + ".png");
        } else if (id < 1600) {
            f = new File(file, getBoneImg(id));
        } else if (id < 1650) {
            f = new File(file, "upQ" + (id - 1600) + ".png");
        }
        if (f != null)
            if (k)
                return pathToImg(f.getAbsolutePath());
            else return f.getAbsolutePath();
        throw new RuntimeException();
    }

    /**
     * 获取图片通过 Id
     *
     * @param id
     * @return
     */
    public static String getImgById(int id) {
        return getImgById(id, true);
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
        for (Integer i : ID_2_SHOP_MAPS.keySet()) {
            is.add(i);
        }
        Collections.sort(is, new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o1 - o2;
            }
        });
        for (Integer i : is) {
            ls.add(getNameById(i) + "=>" + ID_2_SHOP_MAPS.get(i) + "个金魂币");
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
        SpringBootResource.getHhpzMapper().insert(who.longValue(), id, System.currentTimeMillis());
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
        SpringBootResource.getBagMapper().insert(id, who.longValue(), System.currentTimeMillis());
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
        for (Integer j = 0; j < num; j++) {
            SpringBootResource.getBagMapper().insert(id, who.longValue(), System.currentTimeMillis());
        }
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
        if (SpringBootResource.getBagMapper().update(SpringBootResource.getBagMapper().selectId(who.longValue(), id)) > 0) {
            GotOrLostObjBroadcast.INSTANCE.broadcast(who, id, 1,
                    type);
            return "OK";
        }
        return "err";
    }

    /**
     * 从背包 移除物品 通过 Id
     *
     * @param who
     * @param id
     * @return
     */
    public static String removeFromBgs(Long who, int id, int num, ObjType type) {
        for (Integer sid : SpringBootResource.getBagMapper().selectIds(who.longValue(), id, num)) {
            if (SpringBootResource.getBagMapper().update(sid) > 0) {
                GotOrLostObjBroadcast.INSTANCE.broadcast(who, sid, num, type);
            }
        }
        return "OK";
    }

    /**
     * 添加暗器 到 背包
     *
     * @param who
     * @param o
     * @return
     */
    public static String addToAqBgs(Long who, Integer oid, Integer num) {
        SpringBootResource.getAqBagMapper().insert(oid, who.longValue(), num, System.currentTimeMillis());
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
    private static boolean putData(Long who, String dataName, Object data) {
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
    public static boolean putDataString(Long who, String dataName, Object data) {
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
    private static Object getData(Long who, String dataName) {
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
    public static Object getDataString(Long who, String dataName) {
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
