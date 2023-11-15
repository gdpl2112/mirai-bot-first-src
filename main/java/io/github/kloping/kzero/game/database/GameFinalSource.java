package io.github.kloping.kzero.game.database;

import io.github.kloping.kzero.main.ResourceSet;
import io.github.kloping.kzero.spring.dao.WhInfo;
import io.github.kloping.rand.RandomUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author github-kloping
 */
public class GameFinalSource {
    public static final Map<Integer, String> ID_2_NAME_MAPS = new ConcurrentHashMap<>();
    public static final Map<Integer, String> ID_2_INTRO_MAPS = new ConcurrentHashMap<>();
    public static final Map<Integer, String> NAME_2_INTRO_MAPS = new ConcurrentHashMap<>();
    public static final Map<String, Integer> NAME_2_ID_MAPS = new ConcurrentHashMap<>();
    public static final Map<Integer, Long> ID_2_SHOP_MAPS = new ConcurrentHashMap<>();
    public static final Map<Integer, Integer> ID_2_WEA_MAPS = new ConcurrentHashMap<>();
    public static final Map<Integer, Integer> ID_2_WEA_O_NUM_MAPS = new ConcurrentHashMap<>();
    public static final Map<Integer, Integer> WH_2_TYPE = new ConcurrentHashMap<>();

    static {
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
        ID_2_NAME_MAPS.put(0, "武魂晶元");
        ID_2_NAME_MAPS.put(1, "蓝电霸王龙");
        ID_2_NAME_MAPS.put(2, "昊天锤");
        ID_2_NAME_MAPS.put(3, "六翼天使");
        ID_2_NAME_MAPS.put(4, "噬魂珠皇");
        ID_2_NAME_MAPS.put(5, "蓝银皇");
        ID_2_NAME_MAPS.put(6, "柔骨兔");
        ID_2_NAME_MAPS.put(7, "邪眸白虎");
        ID_2_NAME_MAPS.put(8, "邪火凤凰");
        ID_2_NAME_MAPS.put(9, "七杀剑");
        ID_2_NAME_MAPS.put(10, "碧灵蛇皇");
        ID_2_NAME_MAPS.put(11, "破魂枪");
        ID_2_NAME_MAPS.put(12, "大力金刚熊");
        ID_2_NAME_MAPS.put(13, "奇茸通天草");
        ID_2_NAME_MAPS.put(14, "鬼魅");
        ID_2_NAME_MAPS.put(15, "刺豚");
        ID_2_NAME_MAPS.put(16, "蛇矛");
        ID_2_NAME_MAPS.put(17, "骨龙");
        ID_2_NAME_MAPS.put(18, "蛇杖");
        ID_2_NAME_MAPS.put(19, "蓝银花");
        ID_2_NAME_MAPS.put(20, "玄龟");
        ID_2_NAME_MAPS.put(21, "幽冥灵猫");
        ID_2_NAME_MAPS.put(22, "光明圣龙");
        ID_2_NAME_MAPS.put(23, "黑暗圣龙");
        ID_2_NAME_MAPS.put(24, "修罗神剑");
        ID_2_NAME_MAPS.put(25, "青龙");
        ID_2_NAME_MAPS.put(26, "海神");
        ID_2_NAME_MAPS.put(27, "九心海棠");
        ID_2_NAME_MAPS.put(28, "落日神弓");
        ID_2_NAME_MAPS.put(29, "杀神昊天锤");
        ID_2_NAME_MAPS.put(30, "魔神剑");
        ID_2_NAME_MAPS.put(31, "暗金恐爪熊");
        //==
        ID_2_NAME_MAPS.put(50, "九宝琉璃塔");
        //==
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
        ID_2_NAME_MAPS.put(114, "落叶碎片");
        ID_2_NAME_MAPS.put(115, "落日花瓣");
        ID_2_NAME_MAPS.put(116, "冷却药水");
        ID_2_NAME_MAPS.put(117, "大冷却药水");
        ID_2_NAME_MAPS.put(118, "忘忧之香");
        ID_2_NAME_MAPS.put(119, "净化药水");
        ID_2_NAME_MAPS.put(120, "变异魂环");

        ID_2_NAME_MAPS.put(121, "普通魂导材料");
        ID_2_NAME_MAPS.put(122, "中级魂导材料");
        ID_2_NAME_MAPS.put(123, "高级魂导材料");

        ID_2_NAME_MAPS.put(124, "魂导护盾");
        ID_2_NAME_MAPS.put(125, "高级魂导护盾");

        ID_2_NAME_MAPS.put(126, "魂导炮");
        ID_2_NAME_MAPS.put(127, "高级魂导炮");

        ID_2_NAME_MAPS.put(128, "升级券");

        ID_2_NAME_MAPS.put(129, "魂兽挑战券");
        ID_2_NAME_MAPS.put(130, "奖券");
        //==
        ID_2_NAME_MAPS.put(201, "十年魂环");
        ID_2_NAME_MAPS.put(202, "百年魂环");
        ID_2_NAME_MAPS.put(203, "千年魂环");
        ID_2_NAME_MAPS.put(204, "万年魂环");
        ID_2_NAME_MAPS.put(205, "十万年魂环");
        ID_2_NAME_MAPS.put(206, "百万年魂环");
        ID_2_NAME_MAPS.put(207, "神级魂环");
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
        //===================
        ID_2_NAME_MAPS.put(601, "泰坦雪魔王");
        ID_2_NAME_MAPS.put(602, "冰帝帝皇蝎");
        ID_2_NAME_MAPS.put(603, "极地冰凤凰");
        //==================
        ID_2_NAME_MAPS.put(701, "枯木翼龙");
        ID_2_NAME_MAPS.put(702, "幻夕魔兽");
        ID_2_NAME_MAPS.put(703, "熔岩烈兽");
        ID_2_NAME_MAPS.put(704, "狱火岩魔龙");
        ID_2_NAME_MAPS.put(705, "地魁岩兽");
        ID_2_NAME_MAPS.put(710, "泰坦巨猿");
        ID_2_NAME_MAPS.put(711, "天青牛蟒");
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
        //==================
        ID_2_NAME_MAPS.put(1514, "泰坦巨猿头部魂骨");
        ID_2_NAME_MAPS.put(1515, "天青牛蟒头部魂骨");
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
        //==
        //临时活动物品
        ID_2_NAME_MAPS.put(7001, "粽子");
        ID_2_NAME_MAPS.put(7002, "月饼");
        ID_2_NAME_MAPS.put(7003, "月饼二");

        ID_2_NAME_MAPS.forEach((k, v) -> {
            NAME_2_ID_MAPS.put(v, k);
        });
    }

    private static void initIntro() {
        ID_2_INTRO_MAPS.put(0, "武魂晶元,用于'激活第二武魂',和兑换相应武魂.");
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
        ID_2_INTRO_MAPS.put(112, "精神神石,恢复精神力");
        ID_2_INTRO_MAPS.put(113, "遗忘药水,用来忘掉某些事");
        ID_2_INTRO_MAPS.put(114, "落日碎片,用于合成落日花瓣");
        ID_2_INTRO_MAPS.put(115, "落日花瓣,下次进落日森林一定遇到魂兽");
        ID_2_INTRO_MAPS.put(116, "冷却药水,使指定一个魂技减少" + ResourceSet.FinalValue.OBJ116_VALUE / 60000 + "分钟冷却,使用示例,使用冷却药水2");
        ID_2_INTRO_MAPS.put(117, "大冷却药水,使所有魂技立刻完成冷却,但每日只能使用3次");
        ID_2_INTRO_MAPS.put(118, "忘忧之香,使用之后记录当前所有状态(血量,魂力,精神力)若6秒后仍有状态,则忘掉这6秒内的事情(恢复至记录时状态)");
        ID_2_INTRO_MAPS.put(119, "净化药水,解除自身所有负面效果,解除控制效果并持续5秒");
        ID_2_INTRO_MAPS.put(120, "变异魂环,用于分解获得魂导材料");
        ID_2_INTRO_MAPS.put(121, "普通魂导材料,合成更高级的材料");
        ID_2_INTRO_MAPS.put(122, "中级魂导材料,合成更高级的材料,制作魂导器.例:'合成高级魂导护盾'");
        ID_2_INTRO_MAPS.put(123, "高级魂导材料,制作魂导器.例:'合成高级魂导护盾'");
        ID_2_INTRO_MAPS.put(124, "魂导护盾,使用魂导护盾.增加50%生命值得长久护盾");
        ID_2_INTRO_MAPS.put(125, "高级魂导护盾,使用高级魂导护盾,增加100%生命值得长久护盾");
        ID_2_INTRO_MAPS.put(126, "魂导炮,使用魂导炮<选择器>.对指定敌人造成50%的伤害(不计算额外加成的攻击力)");
        ID_2_INTRO_MAPS.put(127, "高级魂导炮,使用高级魂导炮<选择器>.对指定敌人造成100%的伤害(不计算额外加成的攻击力)");
        ID_2_INTRO_MAPS.put(128, "升级券,立即提升1级,不可在等级瓶颈,151级时使用");
        ID_2_INTRO_MAPS.put(129, "魂兽挑战券,挑战任意魂兽,,见魂兽列表 例如:挑战枯木翼龙");
        ID_2_INTRO_MAPS.put(130, "奖券,用来抽奖");

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
        ID_2_INTRO_MAPS.put(710, "泰坦巨猿,自身含有15%的免伤");
        ID_2_INTRO_MAPS.put(711, "天青牛蟒,每次受到伤害对敌人造成1s的眩晕");

        ID_2_INTRO_MAPS.put(19, "蓝银花,蓝银草的变异,拥有艳丽的花瓣,在拥有蓝银草顽强的生命力的同时也含有毒性");

        ID_2_INTRO_MAPS.put(7001, "粽子,活动物品,用来随机获取奖励");
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
        ID_2_SHOP_MAPS.put(113, 789L);
        ID_2_SHOP_MAPS.put(116, 889L);
        ID_2_SHOP_MAPS.put(117, 18888L);
        ID_2_SHOP_MAPS.put(118, 1012L);
        ID_2_SHOP_MAPS.put(119, 678L);

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
        ID_2_WEA_O_NUM_MAPS.put(124, 40);
        ID_2_WEA_O_NUM_MAPS.put(125, 85);
        ID_2_WEA_O_NUM_MAPS.put(126, 45);
        ID_2_WEA_O_NUM_MAPS.put(127, 90);
    }

    private static void initWhType() {
        WH_2_TYPE.put(1, 0);
        WH_2_TYPE.put(2, 1);
        WH_2_TYPE.put(3, 0);
        WH_2_TYPE.put(4, 0);
        WH_2_TYPE.put(5, 2);
        WH_2_TYPE.put(6, 0);
        WH_2_TYPE.put(7, 0);
        WH_2_TYPE.put(8, 0);
        WH_2_TYPE.put(9, 1);
        WH_2_TYPE.put(10, 0);
        WH_2_TYPE.put(11, 1);
        WH_2_TYPE.put(12, 0);
        WH_2_TYPE.put(13, 1);
        WH_2_TYPE.put(14, 0);
        WH_2_TYPE.put(15, 0);
        WH_2_TYPE.put(16, 2);
        WH_2_TYPE.put(17, 0);
        WH_2_TYPE.put(18, 2);
        WH_2_TYPE.put(19, 0);
        WH_2_TYPE.put(20, 0);
        WH_2_TYPE.put(21, 0);
        WH_2_TYPE.put(22, 0);
        WH_2_TYPE.put(23, 0);
        WH_2_TYPE.put(24, 1);
        WH_2_TYPE.put(25, 0);
        WH_2_TYPE.put(26, 1);
        WH_2_TYPE.put(27, 1);
        WH_2_TYPE.put(28, 1);
        WH_2_TYPE.put(29, 1);
        WH_2_TYPE.put(30, 1);
        WH_2_TYPE.put(31, 0);
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
     * 1~9级魂士；10~19级魂师。
     * <p>
     * 20~29级大魂师 ；  30~39级魂尊。
     * <p>
     * 40~49级魂宗  ； 50~59级魂王。
     * <p>
     * 60~69级魂帝 ； 70~79级魂圣。
     * <p>
     * 80~89级魂斗罗  ； 90~99级封号斗罗。
     * <p>
     * 95~99级巅峰斗罗 ；100级神。
     *
     * @param level
     * @return
     */
    public static String getFhName(Integer level) {
        if (level < 10) return "魂士";
        else if (level < 20) return "魂师";
        else if (level < 30) return "大魂师";
        else if (level < 40) return "魂尊";
        else if (level < 50) return "魂宗";
        else if (level < 60) return "魂王";
        else if (level < 70) return "魂帝";
        else if (level < 80) return "魂圣";
        else if (level < 90) return "魂斗罗";
        else if (level < 95) return "封号斗罗";
        else if (level < 100) return "巅峰斗罗";
        else if (level < 110) return "三级神祇";
        else if (level < 120) return "二级神祇";
        else if (level < 150) return "一级神祇";
        else if (level < 160) return "神王";
        return "垃圾废铁";
    }

    /**
     * 是否瓶颈
     *
     * @param who
     * @return
     */
    public static boolean isJTop(WhInfo whInfo) {
        int level = whInfo.getLevel();
        level++;
        boolean k1 = level % 10 == 0;
        boolean k2 = whInfo.getXp() >= whInfo.getXpl();
        return k1 && k2;
    }

    /**
     * 魂环加成
     *
     * @param id
     * @return
     */
    public static double getAHBl(int id) {
        switch (id) {
            case 201:
                return 0.1;
            case 202:
                return 0.2;
            case 203:
                return 0.36;
            case 204:
                return 0.48;
            case 205:
                return 0.78;
            case 206:
                return 1.2;
            case 207:
                return 1.4;
        }
        return 0;
    }


    /**
     * 获取魂环加成的魂技
     *
     * @param id
     * @return
     */
    public static double getAHBl_(int id) {
        switch (id) {
            case 201:
                return 0.9;
            case 202:
                return 1.3;
            case 203:
                return 1.6;
            case 204:
                return 1.9;
            case 205:
                return 2.2;
            case 206:
                return 2.6;
            case 207:
                return 3.0;
        }
        return 1;
    }

    public static int Lmax(int level) {
        switch (level) {
            case 10:
                return 99;
            case 100:
                return 999;
            case 1000:
                return 9999;
            case 10000:
                return 99999;
            case 100000:
                return 999999;
            case 1000000:
                return 9999999;
            case 10000000:
                return 99999999;
        }
        return -1;
    }

    /**
     * 玩家等级 => 魂兽等级
     *
     * @param l
     * @return
     */
    public static long getLtoGhsL(long l) {
        if (l < 10) return RandomUtils.getRandInteger(100, 200);
        else if (l < 20) return RandomUtils.getRandInteger(200, 1000);
        else if (l < 30) return RandomUtils.getRandInteger(1000, 10000);
        else if (l < 40) return RandomUtils.getRandInteger(10000, 20000);
        else if (l < 60) return RandomUtils.getRandInteger(20000, 40000);
        else if (l < 80) return RandomUtils.getRandInteger(40000, 80000);
        else if (l < 90) return RandomUtils.getRandInteger(80000, 120000);
        else if (l < 95) return RandomUtils.getRandInteger(120000, 200000);
        else if (l < 100) return RandomUtils.getRandInteger(200000, 1000000);
        else if (l < 110) return RandomUtils.getRandInteger(300000, 1500000);
        else if (l < 120) return RandomUtils.getRandInteger(800000, 2000000);
        else if (l < 130) return RandomUtils.getRandInteger(900000, 3000000);
        else if (l <= 151) return RandomUtils.getRandInteger(990000, 10010000);
        else return 1L;
    }

    /**
     * 玩家等级 => 升级加成
     *
     * @param level
     * @return
     */
    public static long getPromoteProperties(int level) {
        if (level < 10) return RandomUtils.getRandInteger(80, 120);
        else if (level < 40) return RandomUtils.getRandInteger(290, 315);
        else if (level < 60) return RandomUtils.getRandInteger(900, 1100);
        else if (level < 80) return RandomUtils.getRandInteger(2500, 2600);
        else if (level < 90) return RandomUtils.getRandInteger(3900, 4000);
        else if (level < 95) return RandomUtils.getRandInteger(39000, 40000);
        else if (level < 100) return RandomUtils.getRandInteger(95000, 100000);
        else if (level < 110) return RandomUtils.getRandInteger(179500, 190000);
        else if (level < 120) return RandomUtils.getRandInteger(280000, 285000);
        else return RandomUtils.getRandInteger(160000, 166667);
    }

    /**
     * 获取每次修炼 的 加的 比例 例如 100 26 次修炼才可升级
     *
     * @param level
     * @return
     */
    public static int getRandXl(int level) {
        if (level < 5) return 2;
        else if (level < 20) return 3;
        else if (level < 40) return 4;
        else if (level < 60) return 7;
        else if (level < 70) return 8;
        else if (level < 80) return 11;
        else if (level < 90) return 14;
        else if (level < 95) return 19;
        else if (level < 98) return 24;
        else if (level < 100) return 29;
        else if (level < 110) return 33;
        else if (level < 120) return 46;
        else if (level < 130) return 68;
        else if (level < 145) return 80;
        else if (level < 147) return 120;
        else if (level < 148) return 168;
        else if (level < 150) return 1200;
        else if (level == 150) return 700;
        else return 99999;
    }

    /**
     * 等级 => 魂环
     *
     * @param level
     * @return
     */
    public static Integer getHhByGh(int level) {
        if (level < 100) return 201;
        else if (level < 1000) return 202;
        else if (level < 10000) return 203;
        else if (level < 100000) return 204;
        else if (level < 1000000) return 205;
        else if (level < 10000000) return 206;
        else if (level < 100000000) return 207;
        return -1;
    }


    public static String getLevelByGhostId(Integer id) {
        if (id < 503) return "十";
        else if (id < 505) return "百";
        else if (id < 507) return "千";
        else if (id < 510) return "万";
        else if (id < 514) return "十万";
        else if (id < 518) return "百万";
        else if (id < 521) return "神";
        else if (id < 1000) return "全";
        else return "未知";
    }

    /**
     * 获取 魂兽最大支援数量
     *
     * @param id
     * @param level
     * @return
     */
    public static int getMaxHelpNumByGhostIdAndLevel(Integer id, Integer level) {
        if (level >= 10000000) return 2;
        else if (id < 800) return 1;
        else return 0;
    }
}