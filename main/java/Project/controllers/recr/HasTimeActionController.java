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
import io.github.kloping.mirai0.commons.broadcast.enums.ObjType;
import io.github.kloping.mirai0.unitls.Tools.Tool;

import java.util.AbstractMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static Project.controllers.auto.ControllerTool.opened;
import static Project.dataBases.GameDataBase.*;
import static Project.dataBases.task.TaskCreator.getRandObj1000;
import static io.github.kloping.mirai0.Main.Resource.println;
import static io.github.kloping.mirai0.commons.resouce_and_tool.ResourceSet.FinalString.*;
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
        int r = Tool.tool.RANDOM.nextInt(100);
        String msg = "";
        if (r < 5) {
            GameDataBase.addToBgs(qid, 7003, ObjType.got);
            msg = "获得一个月饼二" + SourceDataBase.getImgPathById(7003);
        } else if (r < 15) {
            GameDataBase.addToBgs(qid, 7002, ObjType.got);
            msg = "获得一个月饼" + SourceDataBase.getImgPathById(7002);
        }
        if (!msg.isEmpty()) {
            long gid = MemberTools.getRecentSpeechesGid(qid);
            MessageTools.instance.sendMessageInGroupWithAt(msg, gid, qid);
        }
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
        AC_ITEMS_MAP.put(120, new AbstractMap.SimpleEntry<>(3,7002 ));
        AC_ITEMS_MAP.put(123, new AbstractMap.SimpleEntry<>(10, 7002));
        AC_ITEMS_MAP.put(124, new AbstractMap.SimpleEntry<>(38, 7002));
        AC_ITEMS_MAP.put(125, new AbstractMap.SimpleEntry<>(54, 7002));
        GhostLostBroadcast.INSTANCE.add(new GhostLostBroadcast.GhostLostReceiver() {
            @Override
            public void onReceive(long who, Long with, GhostObj ghostObj, GhostLostBroadcast.KillType killType) {
                HasTimeActionController.rand99(who);
            }
        });
    }

    @Action("兑换<.+=>str>")
    public Object a1(@Param("str") String str, Long q) {
        try {
            int id = GameDataBase.NAME_2_ID_MAPS.get(str.trim());
            Map.Entry<Integer, Integer> entry = AC_ITEMS_MAP.get(id);
            if (entry == null) return "活动物品不存在";
            int needId = entry.getKey().intValue();
            int needNum = entry.getValue();
            if (GameDataBase.contiansBgsNum(q, needId, needNum)) {
                GameDataBase.removeFromBgs(q, needId, needNum, ObjType.use);
                if (id >= 124 && id <= 127) {
                    addToAqBgs(q, id, (ID_2_WEA_O_NUM_MAPS.get(id)));
                } else {
                    addToBgs(q, id, ObjType.got);
                }
                return String.format("兑换了%s用了%s个%s\n%s", getNameById(id), needNum, getNameById(needId), SourceDataBase.getImgPathById(id));
            } else return String.format("您需要%s个%s 才能兑换%s", needNum, getNameById(needId), getNameById(id));
        } catch (Exception e) {
            return "未找到相关物品";
        }
    }

    private String a2 = "";

    @Action("兑换列表")
    private synchronized String a2() {
        if (a2.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            AC_ITEMS_MAP.forEach((k, kv) -> {
                sb.append(kv.getKey()).append("个").append(ID_2_NAME_MAPS.get(kv.getValue())).append("兑换")
                        .append(ID_2_INTRO_MAPS.get(k)).append(NEWLINE);
            });
            return a2 = sb.toString();
        } else return a2;
    }
}
