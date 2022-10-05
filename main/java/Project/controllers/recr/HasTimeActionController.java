package Project.controllers.recr;

import Project.broadcast.game.GhostLostBroadcast;
import Project.dataBases.DataBase;
import Project.dataBases.GameDataBase;
import Project.dataBases.SourceDataBase;
import io.github.kloping.MySpringTool.annotations.*;
import io.github.kloping.MySpringTool.exceptions.NoRunException;
import io.github.kloping.mirai0.Main.ITools.MemberTools;
import io.github.kloping.mirai0.Main.ITools.MessageTools;
import io.github.kloping.mirai0.commons.GhostObj;
import io.github.kloping.mirai0.commons.Group;
import io.github.kloping.mirai0.commons.TradingRecord;
import io.github.kloping.mirai0.commons.User;
import io.github.kloping.mirai0.commons.broadcast.enums.ObjType;
import io.github.kloping.mirai0.unitls.Tools.Tool;
import io.github.kloping.mirai0.unitls.drawers.GameDrawer;
import io.github.kloping.mirai0.unitls.drawers.entity.GameMap;

import java.io.File;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import static Project.controllers.auto.ControllerTool.opened;
import static Project.dataBases.GameDataBase.addToBgs;
import static Project.dataBases.task.TaskCreator.getRandObj1000;
import static io.github.kloping.mirai0.Main.Resource.println;
import static io.github.kloping.mirai0.commons.resouce_and_tool.ResourceSet.FinalString.CLOSE_STR;
import static io.github.kloping.mirai0.commons.resouce_and_tool.ResourceSet.FinalString.OPEN_STR;
import static io.github.kloping.mirai0.commons.resouce_and_tool.ResourceSet.FinalValue.NOT_OPEN_NO_RUN_EXCEPTION;

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
        switch (Tool.tool.RANDOM.nextInt(10)) {
            case 0:
                GameDataBase.addToBgs(who, 7001, ObjType.got);
                return "使用成功获得一个粽子";
            case 1:
                r = Tool.tool.RANDOM.nextInt(900) + 100;
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
                r = Tool.tool.RANDOM.nextInt(9900) + 100;
                GameDataBase.getInfo(who).addXp((long) r).apply();
                return "获得" + r + "点经验";
            case 5:
                r = Tool.tool.RANDOM.nextInt(4) + 201;
                addToBgs(who, r, ObjType.got);
                return "获得" + SourceDataBase.getImgPathById(r);
            default:
                r = Tool.tool.RANDOM.nextInt(900) + 100;
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
    public void before(@AllMess String mess, Group group) throws NoRunException {
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
        String msg = "";
        int r = Tool.tool.RANDOM.nextInt(18);
        if (r == 0) {
            GameDataBase.addToBgs(qid, 130, 2, ObjType.got);
            msg = "获得两张奖券" + SourceDataBase.getImgPathById(130);
        } else if (r < 4) {
            GameDataBase.addToBgs(qid, 130, ObjType.got);
            msg = "获得一张奖券" + SourceDataBase.getImgPathById(130);
        }
        if (!msg.isEmpty()) {
            long gid = MemberTools.getRecentSpeechesGid(qid);
            MessageTools.instance.sendMessageInGroupWithAt(msg, gid, qid);
        }
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
        return Tool.tool.pathToImg(GameDrawer.drawerStatic(builder.build()));
    }

    @Action("抽奖")
    public String raffle(User user) throws Exception {
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
        int r = Tool.tool.RANDOM.nextInt(al.get());
        int id = am.get(r);
        int n = ai.get(r);
        GameDataBase.addToBgs(user.getId(), id, ObjType.got);
        GameDataBase.removeFromBgs(user.getId(), 130, ObjType.use);
        return Tool.tool.pathToImg(GameDrawer.drawerDynamic(builder.build(), n, SourceDataBase.getImgPathById(id, false), file));
    }

    @Action("抽奖十连")
    public String raffle10(User user) throws Exception {
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
            int r = Tool.tool.RANDOM.nextInt(al.get());
            int id = am.get(r);
            GameDataBase.addToBgs(user.getId(), id, ObjType.got);
            GameDataBase.removeFromBgs(user.getId(), 130, ObjType.use);
            Map.Entry<Integer,Integer> entry = i1toi2.get(i1);
            builder.append(entry.getKey(),entry.getValue(),SourceDataBase.getImgPathById(id, false));
        }
        return Tool.tool.pathToImg(GameDrawer.drawerDynamic(builder.build()));
    }

}
