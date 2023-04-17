package Project.controllers.recr;

import Project.broadcast.game.GhostLostBroadcast;
import Project.commons.SpGroup;
import Project.commons.SpUser;
import Project.commons.TradingRecord;
import Project.dataBases.DataBase;
import Project.dataBases.GameDataBase;
import Project.dataBases.SourceDataBase;
import io.github.kloping.MySpringTool.annotations.*;
import io.github.kloping.MySpringTool.exceptions.NoRunException;
import io.github.kloping.mirai0.Main.iutils.MessageUtils;
import io.github.kloping.mirai0.commons.*;
import Project.commons.broadcast.enums.ObjType;
import io.github.kloping.mirai0.unitls.Tools.Tool;
import io.github.kloping.mirai0.unitls.drawers.GameDrawer;
import io.github.kloping.mirai0.unitls.drawers.entity.GameMap;

import java.io.File;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import static Project.controllers.auto.ControllerTool.opened;
import static Project.controllers.gameControllers.shoperController.ShopController.getNumAndPrice;
import static Project.dataBases.GameDataBase.addToBgs;
import static Project.dataBases.GameDataBase.getInfo;
import static Project.dataBases.task.TaskCreator.getRandObj1000;
import static io.github.kloping.mirai0.Main.BootstarpResource.println;
import static Project.commons.resouce_and_tool.ResourceSet.FinalString.CLOSE_STR;
import static Project.commons.resouce_and_tool.ResourceSet.FinalString.OPEN_STR;
import static Project.commons.resouce_and_tool.ResourceSet.FinalValue.NOT_OPEN_NO_RUN_EXCEPTION;

/**
 * @author github.kloping
 */
@Controller
public class HasTimeActionController {
    public HasTimeActionController() {
        println(this.getClass().getSimpleName() + "构建");
    }


    /**
     * 使用粽子
     * 获得一个粽子
     * 获得100-1000积分
     * 获得100-1000金魂币
     * 获得随机道具
     * 获得100-1w经验
     * 获得十年-万年魂环
     *
     * @param who
     * @return
     */
    public static String use(long who) {
        int r = 0;
        switch (Tool.INSTANCE.RANDOM.nextInt(10)) {
            case 0:
                GameDataBase.addToBgs(who, 7001, ObjType.got);
                return "使用成功获得一个粽子";
            case 1:
                r = Tool.INSTANCE.RANDOM.nextInt(900) + 100;
                GameDataBase.getInfo(who).addGold((long) r, new TradingRecord()
                        .setFrom(-1)
                        .setMain(who).setDesc("从粽子获得")
                        .setTo(who)
                        .setMany(r)
                        .setType0(TradingRecord.Type0.gold)
                        .setType1(TradingRecord.Type1.add)).apply();
                return "获得" + r + "金魂币";
            case 3:
                int id = getRandObj1000();
                addToBgs(who, id, ObjType.got);
                return "获得" + SourceDataBase.getImgPathById(id);
            case 4:
                r = Tool.INSTANCE.RANDOM.nextInt(9900) + 100;
                GameDataBase.getInfo(who).addXp((long) r).apply();
                return "获得" + r + "点经验";
            case 5:
                r = Tool.INSTANCE.RANDOM.nextInt(4) + 201;
                addToBgs(who, r, ObjType.got);
                return "获得" + SourceDataBase.getImgPathById(r);
            default:
                r = Tool.INSTANCE.RANDOM.nextInt(900) + 100;
                DataBase.addScore(r, who);
                return "获得" + r + "积分";
        }
    }

    public static String use7003(long who) {
        GameDataBase.addToBgs(who, 7002, ObjType.got);
        GameDataBase.addToBgs(who, 7002, ObjType.got);
        return "使用成功获得两个月饼";
    }

    public static void rand99(long qid) {
//        int r = Tool.tool.RANDOM.nextInt(100);
//        String msg = "";
//        if (r < 2) {
//            GameDataBase.addToBgs(qid, 7003, ObjType.got);
//            msg = "获得一个月饼二" + SourceDataBase.getImgPathById(7003);
//        } else if (r < 10) {
//            GameDataBase.addToBgs(qid, 7002, ObjType.got);
//            msg = "获得一个月饼" + SourceDataBase.getImgPathById(7002);
//        }
//        if (!msg.isEmpty()) {
//            long gid = MemberTools.getRecentSpeechesGid(qid);
//            MessageTools.instance.sendMessageInGroupWithAt(msg, gid, qid);
//        }
    }

    @Before
    public void before(@AllMess String mess, SpGroup group) throws NoRunException {
        if (mess.contains(OPEN_STR) || mess.contains(CLOSE_STR)) {
            return;
        }
        if (!opened(group.getId(), this.getClass())) {
            throw NOT_OPEN_NO_RUN_EXCEPTION;
        }
    }

//    public static Set<Long> received = new HashSet<>();
//
//    static {
//        TimerController.ZERO_RUNS.add(() -> received.clear());
//    }
//
//    @Action("领取粽子")
//    public String a0(User user) {
//        if (received.contains(user.getId())) {
//            return "您今天已经领取过了哦";
//        } else {
//            received.add(user.getId());
//            GameDataBase.addToBgs(user.getId(), 7001, ObjType.got);
//            return "领取成功,已发放至背包" + SourceDataBase.getImgPathById(7001);
//        }
//    }

    private static final Map<Integer, Map.Entry<Integer, Integer>> AC_ITEMS_MAP = new ConcurrentHashMap<>();

    static {
        AC_ITEMS_MAP.put(120, new AbstractMap.SimpleEntry<>(3, 7002));
        AC_ITEMS_MAP.put(123, new AbstractMap.SimpleEntry<>(10, 7002));
        AC_ITEMS_MAP.put(124, new AbstractMap.SimpleEntry<>(38, 7002));
        AC_ITEMS_MAP.put(125, new AbstractMap.SimpleEntry<>(54, 7002));
        GhostLostBroadcast.INSTANCE.add(new GhostLostBroadcast.GhostLostReceiver() {
            @Override
            public void onReceive(long who, Set<Long> withs, GhostObj ghostObj, GhostLostBroadcast.KillType killType) {
                HasTimeActionController.rand99(who);
                HasTimeActionController.rand101(who);
            }
        });
    }

    @Action("兑换<.+=>str>")
    public Object a1(@Param("str") String str, Long q) {
        return "活动未开启";
//        try {
//            int id = GameDataBase.NAME_2_ID_MAPS.get(str.trim());
//            Map.Entry<Integer, Integer> entry = AC_ITEMS_MAP.get(id);
//            if (entry == null) return "活动物品不存在";
//            int needId = entry.getValue();
//            int needNum = entry.getKey();
//            if (GameDataBase.contiansBgsNum(q, needId, needNum)) {
//                GameDataBase.removeFromBgs(q, needId, needNum, ObjType.use);
//                if (id >= 124 && id <= 127) {
//                    addToAqBgs(q, id, (ID_2_WEA_O_NUM_MAPS.get(id)));
//                } else {
//                    addToBgs(q, id, ObjType.got);
//                }
//                return String.format("兑换了%s用了%s个%s\n%s", getNameById(id), needNum, getNameById(needId), SourceDataBase.getImgPathById(id));
//            } else return String.format("您需要%s个%s 才能兑换%s", needNum, getNameById(needId), getNameById(id));
//        } catch (Exception e) {
//            return "未找到相关物品";
//        }
    }

    private String a2 = "";

    @Action("兑换列表")
    private synchronized String a2() {
        return "活动未开启";
//        if (a2.isEmpty()) {
//            StringBuilder sb = new StringBuilder();
//            AC_ITEMS_MAP.forEach((k, kv) -> {
//                sb.append(kv.getKey()).append("个").append(ID_2_NAME_MAPS.get(kv.getValue())).append("兑换")
//                        .append(ID_2_INTRO_MAPS.get(k)).append(NEWLINE);
//            });
//            return a2 = sb.toString();
//        } else return a2;
    }

    private static void rand101(long qid) {
//        String msg = "";
//        int r = Tool.tool.RANDOM.nextInt(18);
//        if (r == 0) {
//            GameDataBase.addToBgs(qid, 130, 2, ObjType.got);
//            msg = "获得两张奖券" + SourceDataBase.getImgPathById(130);
//        } else if (r < 4) {
//            GameDataBase.addToBgs(qid, 130, ObjType.got);
//            msg = "获得一张奖券" + SourceDataBase.getImgPathById(130);
//        }
//        if (!msg.isEmpty()) {
//            long gid = MemberTools.getRecentSpeechesGid(qid);
//            MessageTools.instance.sendMessageInGroupWithAt(msg, gid, qid);
//        }
    }

    /**
     * id => lj
     */
    public static final Map<Integer, Integer> ID2JL = new LinkedHashMap<>();

    static {
        ID2JL.put(101, 5);
        ID2JL.put(102, 4);
        ID2JL.put(103, 4);
        ID2JL.put(116, 3);
        ID2JL.put(1601, 1);//升级券
        ID2JL.put(106, 5);
        ID2JL.put(107, 3);
        ID2JL.put(115, 4);
        ID2JL.put(120, 1);//变异魂环
        ID2JL.put(121, 1);//普材料
        ID2JL.put(129, 1);//挑战券
        ID2JL.put(113, 4);
    }

    @Action("奖池")
    public String jackpot() throws Exception {
        GameMap.GameMapBuilder builder = new GameMap.GameMapBuilder();
        builder.setWidth(5)
                .setHeight(3);
        int i = 0;
        List<Integer> list = new LinkedList<>(ID2JL.keySet());
        for (int i1 = 0; i1 < 5; i1++) {
            builder.append(i1 + 1, 1, SourceDataBase.getImgPathById(list.get(i++), false));
        }
        builder.append(5, 2, SourceDataBase.getImgPathById(list.get(i++), false));
        for (int i1 = 5; i1 > 0; i1--) {
            builder.append(i1, 3, SourceDataBase.getImgPathById(list.get(i++), false));
        }
        builder.append(1, 2, SourceDataBase.getImgPathById(list.get(i++), false));
        return Tool.INSTANCE.pathToImg(GameDrawer.drawerStatic(builder.build()));
    }

    @Action("抽奖")
    public String raffle(SpUser user) throws Exception {
        if (!GameDataBase.containsInBg(130, user.getId())) {
            return "没有足够的奖券";
        }
        GameMap.GameMapBuilder builder = new GameMap.GameMapBuilder();
        builder.setWidth(5)
                .setHeight(3);
        int i = 0;
        List<Integer> list = new LinkedList<>(ID2JL.keySet());
        for (int i1 = 0; i1 < 5; i1++) {
            builder.append(i1 + 1, 1, SourceDataBase.getImgPathById(list.get(i++), false));
        }
        builder.append(5, 2, SourceDataBase.getImgPathById(list.get(i++), false));
        for (int i1 = 5; i1 > 0; i1--) {
            builder.append(i1, 3, SourceDataBase.getImgPathById(list.get(i++), false));
        }
        builder.append(1, 2, SourceDataBase.getImgPathById(list.get(i++), false));
        String name = UUID.randomUUID() + ".gif";
        new File("./temp").mkdirs();
        File file = new File("./temp/" + name);
        AtomicInteger al = new AtomicInteger();
        Map<Integer, Integer> am = new HashMap<>();
        Map<Integer, Integer> ai = new HashMap<>();
        AtomicInteger st = new AtomicInteger();
        ID2JL.forEach((k, v) -> {
            for (Integer integer = 0; integer < v; integer++) {
                ai.put(al.get(), st.get());
                am.put(al.getAndIncrement(), k);
            }
            st.getAndIncrement();
        });
        int r = Tool.INSTANCE.RANDOM.nextInt(al.get());
        int id = am.get(r);
        int n = ai.get(r);
        GameDataBase.addToBgs(user.getId(), id, ObjType.got);
        GameDataBase.removeFromBgs(user.getId(), 130, ObjType.use);
        return Tool.INSTANCE.pathToImg(GameDrawer.drawerDynamic(builder.build(), n, SourceDataBase.getImgPathById(id, false), file));
    }

    @Action("抽奖十连")
    public String raffle10(SpUser user) throws Exception {
        if (!GameDataBase.containsBgsNum(user.getId(), 130, 10)) {
            return "没有足够的奖券";
        }
        GameMap.GameMapBuilder builder = new GameMap.GameMapBuilder();
        builder.setWidth(5)
                .setHeight(2);
        AtomicInteger al = new AtomicInteger();
        //序号=>id
        Map<Integer, Integer> am = new HashMap<>();
        AtomicInteger st = new AtomicInteger();
        ID2JL.forEach((k, v) -> {
            for (Integer integer = 0; integer < v; integer++) {
                am.put(al.getAndIncrement(), k);
            }
            st.getAndIncrement();
        });
        Map<Integer, Map.Entry<Integer, Integer>> i1toi2 = new HashMap<>();
        int i1i2i = 0;
        for (int i1 = 1; i1 <= 2; i1++) {
            for (int i2 = 1; i2 <= 5; i2++) {
                i1toi2.put(i1i2i++, new AbstractMap.SimpleEntry<>(i2, i1));
            }
        }
        for (int i1 = 0; i1 < 10; i1++) {
            int r = Tool.INSTANCE.RANDOM.nextInt(al.get());
            int id = am.get(r);
            GameDataBase.addToBgs(user.getId(), id, ObjType.got);
            GameDataBase.removeFromBgs(user.getId(), 130, ObjType.use);
            Map.Entry<Integer, Integer> entry = i1toi2.get(i1);
            builder.append(entry.getKey(), entry.getValue(), SourceDataBase.getImgPathById(id, false));
        }
        return Tool.INSTANCE.pathToImg(GameDrawer.drawerDynamic(builder.build()));
    }

    public static RedPacket REDPACKET = null;

    @Action("发红包<.+=>str>")
    public Object sendRedPacket(@Param("str") String str, SpGroup group, long qid) {
        Long[] ll = getNumAndPrice(str);
        if (ll == null || ll.length < 2) return "发红包示例:\n 发红包10个1000<积分>\n 积分可替换为'金魂币','大瓶经验'";
        int num = ll[0].intValue();
        int value = ll[1].intValue();
        if (value > 100000) return "红包最大值10w";
        if (value < num) return "总值不得小于总数";
        if (num <= 0 || num > 10) return "非法的总数";
        if (REDPACKET != null) return "当前尚有红包未领取";
        String name = str.replace(ll[0] + "个", "")
                .replace(ll[1] + "", "");
        RedPacket.IdType type = RedPacket.IdType.SCORE;
        switch (name) {
            case "积分":
                type = RedPacket.IdType.SCORE;
                break;
            case "金魂币":
                type = RedPacket.IdType.GOLD;
                break;
            case "大瓶经验":
                type = RedPacket.IdType.OBJ0;
                break;
            default:
                type = RedPacket.IdType.SCORE;
                break;
        }
        if (!RedPacket.judge(qid, type, value)) return "未拥有足够数量的物品";
        REDPACKET = new RedPacket(num, value, qid, group.getId(), type) {
            @Override
            public void finish(String tips) {
                REDPACKET = null;
                String t = "红包全部领取,手气最佳=>" + Tool.INSTANCE.at(getMax()) + "\n" + tips;
                MessageUtils.INSTANCE
                        .sendMessageInGroupWithAt(t.trim(), group.getId(), qid);
            }
        };
        REDPACKET.setQid(qid);
        RedPacket.app(qid, type, value);
        return "发红包成功,发送\"抢红包\"即可参与";
    }

    @Action("抢红包")
    public Object getRedPacket(long qid) {
        if (REDPACKET == null) return "无红包!";
        String name = REDPACKET.getName();
        if (REDPACKET.getRecord().containsKey(qid)) {
            return String.format("已经抢到%s个%s了哦",
                    REDPACKET.getRecord().get(qid), name);
        }
        Integer n0 = REDPACKET.getN0();
        RedPacket.IdType type = REDPACKET.getId();
        Integer val = REDPACKET.getOne(qid);
        RedPacket.add(qid, type, val);
        return String.format("成功抢到%s个%s,红包剩余%s个",
                val, name, n0 - 1);
    }

    @CronSchedule("0 0 6-20/12 * * ?")
    public void redPacket0() {
        if (REDPACKET == null) return;
        int h0 = REDPACKET.getHour();
        int h1 = Tool.INSTANCE.getHour();
        h1 = h1 < h0 ? h1 + 24 : h1;
        if (h1 - h0 >= 12) {
            REDPACKET.back();
        }
    }
//
//    @CronSchedule("0 0 6-22/1 25 12 ? ")
//    public void redPacket0() {
//        int r = Tool.tool.RANDOM.nextInt(3);
//        RedPacket.IdType type = RedPacket.IdType.SCORE;
//        int value = 0;
//        switch (r) {
//            case 0:
//                type = RedPacket.IdType.SCORE;
//                value = Tool.tool.RANDOM.nextInt(50000) + 50000;
//                break;
//            case 1:
//                type = RedPacket.IdType.GOLD;
//                value = Tool.tool.RANDOM.nextInt(5000) + 5000;
//                break;
//            case 2:
//                type = RedPacket.IdType.OBJ0;
//                value = Tool.tool.RANDOM.nextInt(50) + 50;
//                break;
//            default:
//                break;
//        }
//        Group group = null;
//        if (Resource.BOT.getGroups().contains(278681553L))
//            group = Group.get(278681553L);
//        else if (Resource.BOT.getGroups().contains(954303690L))
//            group = Group.get(954303690L);
//        Group finalGroup = group;
//        long qid = Resource.BOT.getId();
//        REDPACKET = new RedPacket(10, value, qid, finalGroup.getId(), type) {
//            @Override
//            public void finish() {
//                REDPACKET = null;
//                MessageTools.instance
//                        .sendMessageInGroupWithAt(
//                                "红包全部领取,手气最佳=>" + Tool.tool.at(getMax()), finalGroup.getId(), qid);
//            }
//        };
//        MessageTools.instance
//                .sendMessageInGroup("发红包成功,发送\"抢红包\"即可参与\n圣诞活动,活动当天6-22时,每小时随机发放随机红包", group.getId());
//    }

//    @Action("领取补偿")
//    public String got(Long qid) {
//        if (Resource.BOT.getId() == 291841860L || Resource.BOT.getId() == 392801250L) {
//            KlopingWebDataBaseBoolean db0 = new KlopingWebDataBaseBoolean("compensate:" + Resource.BOT.getId(), false);
//            if (!db0.getValue(qid)) {
//                db0.setValue(qid, true);
//                DataBase.getAllInfo(qid).addScore(100000);
//                return "领取成功!";
//            } else {
//                return "已经领取!";
//            }
//        } else return null;
//    }
}
