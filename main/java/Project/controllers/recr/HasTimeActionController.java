package Project.controllers.recr;

import Project.broadcast.game.GhostLostBroadcast;
import Project.commons.SpGroup;
import Project.commons.SpUser;
import Project.commons.TradingRecord;
import Project.commons.broadcast.enums.ObjType;
import Project.controllers.auto.TimerController;
import Project.dataBases.DataBase;
import Project.dataBases.GameDataBase;
import Project.dataBases.SourceDataBase;
import Project.utils.KlopingWebDataBaseInteger;
import Project.utils.Tools.Tool;
import Project.utils.VelocityUtils;
import Project.utils.drawers.ImageDrawer;
import io.github.kloping.MySpringTool.annotations.*;
import io.github.kloping.MySpringTool.exceptions.NoRunException;
import io.github.kloping.common.Public;
import io.github.kloping.mirai0.Main.BotStarter;
import io.github.kloping.mirai0.Main.iutils.MessageUtils;
import io.github.kloping.mirai0.commons.GhostObj;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static Project.commons.rt.ResourceSet.FinalString.*;
import static Project.commons.rt.ResourceSet.FinalValue.NOT_OPEN_NO_RUN_EXCEPTION;
import static Project.controllers.auto.ControllerTool.opened;
import static Project.dataBases.GameDataBase.*;
import static Project.dataBases.task.TaskCreator.getRandObj1000;
import static io.github.kloping.mirai0.Main.BootstarpResource.println;

/**
 * @author github.kloping
 */
@Controller
public class HasTimeActionController {
    private static final Map<Integer, Map.Entry<Integer, Integer>> AC_ITEMS_MAP = new LinkedHashMap<>();

    static {
//        AC_ITEMS_MAP.put(120, new AbstractMap.SimpleEntry<>(3, 7002));
//        AC_ITEMS_MAP.put(123, new AbstractMap.SimpleEntry<>(10, 7002));
//        AC_ITEMS_MAP.put(124, new AbstractMap.SimpleEntry<>(38, 7002));
//        AC_ITEMS_MAP.put(125, new AbstractMap.SimpleEntry<>(54, 7002));
        //放置武魂合成
        for (int id = 1; id < 31; id++) {
            AC_ITEMS_MAP.put(id, new AbstractMap.SimpleEntry<>(8, 0));
        }
        AC_ITEMS_MAP.put(2, new AbstractMap.SimpleEntry<>(10, 0));
        AC_ITEMS_MAP.put(26, new AbstractMap.SimpleEntry<>(10, 0));
        AC_ITEMS_MAP.put(27, new AbstractMap.SimpleEntry<>(10, 0));
        AC_ITEMS_MAP.put(29, new AbstractMap.SimpleEntry<>(10, 0));
        AC_ITEMS_MAP.put(50, new AbstractMap.SimpleEntry<>(12, 0));
        //放置武魂合成end
        //放置中秋合成
        //32个粽子 => 武魂晶元
        AC_ITEMS_MAP.put(0, new AbstractMap.SimpleEntry<>(32, 7001));
        //4个粽子 => 奖券
        AC_ITEMS_MAP.put(130, new AbstractMap.SimpleEntry<>(4, 7001));
        //放置中秋合成end

        GhostLostBroadcast.INSTANCE.add(new GhostLostBroadcast.GhostLostReceiver() {
            @Override
            public void onReceive(long who, Set<Long> withs, GhostObj ghostObj, GhostLostBroadcast.KillType killType) {
//                HasTimeActionController.rand99(who);
//                HasTimeActionController.rand101(who);
//                HasTimeActionController.rand51(who, ghostObj.getLevel());
            }
        });
    }

//    private static void rand51(long qid, Integer level) {
//        String msg = "";
//        if (level >= 100000) {
//            int r = Tool.INSTANCE.RANDOM.nextInt(5);
//            if (r == 0) {
//                GameDataBase.addToBgs(qid, 130, 2, ObjType.got);
//                msg = "获得两张奖券" + SourceDataBase.getImgPathById(130);
//            } else {
//                GameDataBase.addToBgs(qid, 130, ObjType.got);
//                msg = "获得一张奖券" + SourceDataBase.getImgPathById(130);
//            }
//        }
//        if (!msg.isEmpty()) {
//            long gid = MemberUtils.getRecentSpeechesGid(qid);
//            MessageUtils.INSTANCE.sendMessageInGroupWithAt(msg, gid, qid);
//        }
//    }
//

    public static Set<Long> received = new HashSet<>();

    static {
        TimerController.ZERO_RUNS.add(() -> received.clear());
    }

//    @Action("领取粽子")
//    public String a0(SpUser user) {
//        if (received.contains(user.getId())) {
//            return "您今天已经领取过了哦";
//        } else {
//            received.add(user.getId());
//            GameDataBase.addToBgs(user.getId(), 7001, ObjType.got);
//            return "领取成功,已发放至背包" + SourceDataBase.getImgPathById(7001);
//        }
//    }

    private String a2 = "";

    public HasTimeActionController() {
        println(this.getClass().getSimpleName() + "构建");
    }

    /**
     * 使用粽子 <br>
     * 获得一个粽子 <br>
     * 获得100-1000积分 <br>
     * 获得100-1000金魂币 <br>
     * 获得随机道具 <br>
     * 获得100-1w经验 <br>
     * 获得十年-万年魂环 <br>
     *
     * @param who
     * @return
     */
    public static String use7001(long who) {
        int r = 0;
        switch (Tool.INSTANCE.RANDOM.nextInt(10)) {
            case 0:
                GameDataBase.addToBgs(who, 7001, ObjType.got);
                return "使用成功获得一个粽子";
            case 1:
                r = Tool.INSTANCE.RANDOM.nextInt(900) + 100;
                GameDataBase.getInfo(who).addGold((long) r, new TradingRecord().setFrom(-1).setMain(who).setDesc("从粽子获得").setTo(who).setMany(r).setType0(TradingRecord.Type0.gold).setType1(TradingRecord.Type1.add)).apply();
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

    public static void rand815(long qid) {
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

    @Before
    public void before(@AllMess String mess, SpGroup group) throws NoRunException {
        if (mess.contains(OPEN_STR) || mess.contains(CLOSE_STR)) {
            return;
        }
        if (!opened(group.getId(), this.getClass())) {
            throw NOT_OPEN_NO_RUN_EXCEPTION;
        }
    }

    @Action("活动列表")
    public Object acList() {
        return VelocityUtils.getTemplateToString("ac.list.intro");
    }

    @Action("兑换<.+=>str>")
    public Object a1(@Param("str") String str, Long q) {
        try {
            int id = GameDataBase.NAME_2_ID_MAPS.get(str.trim());
            KlopingWebDataBaseInteger kwdb = new KlopingWebDataBaseInteger("dh-622-0", 0);
            int n = kwdb.getValue(q);
            if (id == 0) {
                if (n >= MAX_622) return "兑换上限";
            }
            Map.Entry<Integer, Integer> entry = AC_ITEMS_MAP.get(id);
            if (entry == null) return "活动物品不存在";
            int needId = entry.getValue();
            int needNum = entry.getKey();
            if (GameDataBase.containsBgsNum(q, needId, needNum)) {
                GameDataBase.removeFromBgs(q, needId, needNum, ObjType.use);
                if (id >= 124 && id <= 127) {
                    addToAqBgs(q, id, (ID_2_WEA_O_NUM_MAPS.get(id)));
                } else {
                    addToBgs(q, id, ObjType.got);
                }
                if (id == 0) {
                    kwdb.setValue(q, n + 1);
                }
                return String.format("兑换了%s用了%s个%s\n%s", getNameById(id), needNum, getNameById(needId), SourceDataBase.getImgPathById(id));
            } else return String.format("您需要%s个%s 才能兑换%s", needNum, getNameById(needId), getNameById(id));
        } catch (Exception e) {
            return "未找到相关物品";
        }
    }

    @Action("兑换列表")
    private synchronized String a2() {
        if (a2.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            AC_ITEMS_MAP.forEach((k, kv) -> {
                sb.append(kv.getKey()).append("个").append(ID_2_NAME_MAPS.get(kv.getValue())).append("兑换")
                        .append(ID_2_NAME_MAPS.get(k)).append(NEWLINE);
            });
            return a2 = sb.toString();
        } else return a2;
    }

//  红包雨
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

    public static final Integer MAX_622 = 4;
    private static final SimpleDateFormat DF_620 = new SimpleDateFormat("yyyy-MM-dd:HH:mm:ss");
    public static final Map<Long, Long> DRAGON_BOAT_CD = new HashMap<>();
    public static long START_622;
    public static long END_622;

    static {
        try {
            END_622 = DF_620.parse("2023-06-24:21:30:00").getTime();
            START_622 = DF_620.parse("2023-06-21:12:30:00").getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Action("划龙舟")
    public Object dragonBoating(SpUser user, SpGroup group) {
        long now = System.currentTimeMillis();
        boolean in = now > START_622 && now < END_622;
        if (in || BotStarter.test) {
            if (DRAGON_BOAT_CD.containsKey(user.getId())) {
                long cd = DRAGON_BOAT_CD.get(user.getId());
                if (now < cd) return "龙舟冷却中...\n" + Tool.INSTANCE.getTimeTips(cd);
            }
            int r = Tool.INSTANCE.RANDOM.nextInt(120);
            int oid;
            int num;
            if (r == 0) {
                oid = 0;
                num = 1;
            } else if (r <= 31) {
                num = 2;
                oid = 7001;
            } else if (r <= 91) {
                oid = 7001;
                num = 1;
            } else {
                oid = 130;
                num = 1;
            }
            GameDataBase.addToBgs(user.getId(), oid, num, ObjType.got);
            Public.EXECUTOR_SERVICE.submit(() -> {
                try {
                    String path = ImageDrawer.dragonBot(user.getId(), oid, num);
                    MessageUtils.INSTANCE.sendMessageInGroupWithAt(Tool.INSTANCE.pathToImg(path), group.getId(), user.getId());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            long cd = Tool.INSTANCE.randB(40, 60) * 60000;
            DRAGON_BOAT_CD.put(user.getId(), System.currentTimeMillis() + cd);
            return String.format("获得%s个%s", num, GameDataBase.getNameById(oid));
        }
        return "活动未开启";
    }
}
