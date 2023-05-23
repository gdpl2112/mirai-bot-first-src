package Project.controllers;

import Project.aSpring.SpringBootResource;
import Project.commons.RaffleE;
import Project.commons.SpGroup;
import Project.commons.SpUser;
import Project.commons.TradingRecord;
import Project.commons.broadcast.enums.ObjType;
import Project.controllers.auto.ControllerSource;
import Project.dataBases.GameDataBase;
import Project.dataBases.SourceDataBase;
import Project.utils.KlopingWebDataBaseInteger;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.github.kloping.MySpringTool.annotations.*;
import io.github.kloping.MySpringTool.exceptions.NoRunException;
import io.github.kloping.mirai0.Main.BootstarpResource;
import io.github.kloping.mirai0.Main.iutils.MessageUtils;
import io.github.kloping.mirai0.commons.PersonInfo;
import io.github.kloping.mirai0.commons.RedPacket;
import io.github.kloping.mirai0.unitls.Tools.Tool;
import io.github.kloping.mirai0.unitls.drawers.GameDrawer;
import io.github.kloping.mirai0.unitls.drawers.entity.GameMap;

import java.io.File;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static Project.commons.rt.ResourceSet.FinalString.CLOSE_STR;
import static Project.commons.rt.ResourceSet.FinalString.OPEN_STR;
import static Project.commons.rt.ResourceSet.FinalValue.NOT_OPEN_NO_RUN_EXCEPTION;
import static Project.controllers.auto.ControllerTool.opened;
import static Project.controllers.auto.TimerController.MORNING_RUNNABLE;
import static Project.controllers.gameControllers.shoperController.ShopController.getNumAndPrice;
import static Project.dataBases.GameDataBase.getInfo;
import static io.github.kloping.mirai0.Main.BootstarpResource.println;

/**
 * @author github.kloping
 */
@Controller
public class BaseController {
    public BaseController() {
        println(this.getClass().getSimpleName() + "构建");
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
        String name = str.replace(ll[0] + "个", "").replace(ll[1] + "", "");
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
                MessageUtils.INSTANCE.sendMessageInGroupWithAt(t.trim(), group.getId(), qid);
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
            return String.format("已经抢到%s个%s了哦", REDPACKET.getRecord().get(qid), name);
        }
        Integer n0 = REDPACKET.getN0();
        RedPacket.IdType type = REDPACKET.getId();
        Integer val = REDPACKET.getOne(qid);
        RedPacket.add(qid, type, val);
        return String.format("成功抢到%s个%s,红包剩余%s个", val, name, n0 - 1);
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

    /**
     * id => lj
     */
    public static final Map<Integer, Integer> ID2JL = new LinkedHashMap<>();

    static {
        ID2JL.put(101, 6);//时光胶囊 6
        ID2JL.put(102, 6);//恢复药水 12
        ID2JL.put(103, 6);//大瓶经验 18
        ID2JL.put(116, 5);//冷却药水 23
        ID2JL.put(106, 5);//魂力神石 28
        ID2JL.put(107, 4);//仙品花瓣 32
        ID2JL.put(115, 3);//落日花瓣 35
        ID2JL.put(120, 3);//变异魂环 38
        ID2JL.put(121, 3);//普材料 41
        ID2JL.put(129, 2);//挑战券 43
        ID2JL.put(1601, 1);//升级券 44
        ID2JL.put(0, 1);//武魂晶元 45
    }

    private static final int BD = 60;

    private int bd(int id, SpUser user) {
        QueryWrapper<RaffleE> qw = new QueryWrapper<>();
        qw.eq("qid", user.getId());
        RaffleE e = SpringBootResource.getRaffleMapper().selectOne(qw);
        if (e == null) {
            e = new RaffleE();
            e.setQid(user.getId());
            e.setC1(0);
            e.setC2(0);
            SpringBootResource.getRaffleMapper().insert(e);
        }
        if (e.getC1() >= 9) {
            if (id != 1601 || id != 129) {
                id = Tool.INSTANCE.getRandT(1601, 129);
            }
        } else if (e.getC2() >= BD - 1) {
            if (id != 0) {
                id = 0;
            }
        }
        if (id == 0) {
            e.setC2(0);
        } else if (id == 1601 || id == 129) {
            e.setC1(0);
        } else {
            e.addC1();
            e.addC2();
        }
        SpringBootResource.getRaffleMapper().updateById(e);
        return id;
    }

    private String getBd(SpUser user) {
        QueryWrapper<RaffleE> qw = new QueryWrapper<>();
        qw.eq("qid", user.getId());
        RaffleE e = SpringBootResource.getRaffleMapper().selectOne(qw);
        if (e == null) {
            e = new RaffleE();
            e.setQid(user.getId());
            e.setC1(0);
            e.setC2(0);
            SpringBootResource.getRaffleMapper().insert(e);
        }
        return e.getC1() + "/" + e.getC2() + "\n";
    }

    @Action("奖池")
    public String jackpot(SpUser user) throws Exception {
        GameMap.GameMapBuilder builder = new GameMap.GameMapBuilder();
        builder.setWidth(5).setHeight(3);
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
        return "每10次抽奖必得挑战券/升级券\n每60次抽奖必得武魂晶元\n" + getBd(user) + Tool.INSTANCE.pathToImg(GameDrawer.drawerStatic(builder.build()));
    }

    @Action("抽奖")
    public String raffle(SpUser user) throws Exception {
        if (!GameDataBase.containsInBg(130, user.getId())) {
            return "没有足够的奖券";
        }
        GameMap.GameMapBuilder builder = new GameMap.GameMapBuilder();
        builder.setWidth(5).setHeight(3);
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
        id = bd(id, user);
        GameDataBase.addToBgs(user.getId(), id, ObjType.got);
        GameDataBase.removeFromBgs(user.getId(), 130, ObjType.use);
        return getBd(user) + GameDataBase.getNameById(id) + "\n" + Tool.INSTANCE.pathToImg(GameDrawer.drawerDynamic(builder.build(), n, SourceDataBase.getImgPathById(id, false), file));
    }

    @Action("抽奖十连")
    public String raffle10(SpUser user) throws Exception {
        if (!GameDataBase.containsBgsNum(user.getId(), 130, 10)) {
            return "没有足够的奖券";
        }
        GameMap.GameMapBuilder builder = new GameMap.GameMapBuilder();
        builder.setWidth(5).setHeight(2);
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
            id = bd(id, user);
            GameDataBase.addToBgs(user.getId(), id, ObjType.got);
            GameDataBase.removeFromBgs(user.getId(), 130, ObjType.use);
            Map.Entry<Integer, Integer> entry = i1toi2.get(i1);
            builder.append(entry.getKey(), entry.getValue(), SourceDataBase.getImgPathById(id, false));
        }
        return getBd(user) + Tool.INSTANCE.pathToImg(GameDrawer.drawerDynamic(builder.build()));
    }

    public static final String DH_PWD_FORMAT = "duihgameqd:%s-%s:%s";

    static {
        MORNING_RUNNABLE.add(() -> {
            boolean k1 = Tool.INSTANCE.getWeekOfDate(new Date()).equals(Tool.INSTANCE.WEEK_DAYS[1]);
            boolean k2 = Tool.INSTANCE.getWeekOfDate(new Date()).equals(Tool.INSTANCE.WEEK_DAYS[5]);
            if (k1 || k2) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(System.currentTimeMillis() - 1000 * 60 * 60 * 24 * 6);
                calendar.setFirstDayOfWeek(Calendar.MONDAY);
                calendar.setMinimalDaysInFirstWeek(4);
                int year = calendar.get(Calendar.YEAR);
                int weekOfYear = calendar.get(Calendar.WEEK_OF_YEAR);
                String pwd = String.format(DH_PWD_FORMAT, year, weekOfYear, BootstarpResource.BOT.getId());
                ControllerSource.klopingWeb.del("", pwd);
            }
        });
    }

    private static final Integer JQ_MAX = 7;

    @Action("兑换奖券.*?")
    private String duih(SpUser user, @AllMess String num) throws Exception {
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.setMinimalDaysInFirstWeek(4);
        int year = calendar.get(Calendar.YEAR);
        calendar.setTimeInMillis(System.currentTimeMillis());
        int weekOfYear = calendar.get(Calendar.WEEK_OF_YEAR);
        String pwd = String.format(DH_PWD_FORMAT, year, weekOfYear, BootstarpResource.BOT.getId());
        KlopingWebDataBaseInteger dbb = new KlopingWebDataBaseInteger(pwd, 0);
        long qid = user.getId();
        Integer v = dbb.getValue(qid);
        if (v < JQ_MAX) {
            int n = 1;
            String ll = Tool.INSTANCE.findNumberFromString(num);
            if (ll != null && !ll.isEmpty()) n = Integer.parseInt(ll);
            if (v + n > JQ_MAX) {
                return "超额兑换!";
            } else {
                long nm = n * 2000;
                PersonInfo pInfo = getInfo(qid);
                if (pInfo.getGold() < nm) {
                    return "金魂币不足!";
                } else {
                    pInfo.addGold(-nm, new TradingRecord()
                            .setType1(TradingRecord.Type1.lost)
                            .setType0(TradingRecord.Type0.gold)
                            .setTo(-1).setMain(qid).setFrom(qid)
                            .setDesc("兑换奖券").setMany(nm));
                    GameDataBase.addToBgs(qid, 130, n, ObjType.got);
                    dbb.setValue(qid, v + n);
                    return "兑换成功";
                }
            }
        } else {
            return "兑换上限!";
        }
    }
}
