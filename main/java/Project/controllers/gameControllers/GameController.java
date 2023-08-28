package Project.controllers.gameControllers;


import Project.aSpring.SpringBootResource;
import Project.aSpring.dao.PersonInfo;
import Project.aSpring.dao.TradingRecord;
import Project.aSpring.dao.Warp;
import Project.commons.SpGroup;
import Project.commons.SpUser;
import Project.commons.broadcast.enums.ObjType;
import Project.controllers.auto.ControllerSource;
import Project.controllers.auto.TimerController;
import Project.controllers.normalController.ScoreController;
import Project.dataBases.GameDataBase;
import Project.interfaces.Iservice.IGameService;
import Project.services.player.PlayerBehavioralManager;
import Project.utils.KlopingWebDataBaseBoolean;
import Project.utils.Tools.Tool;
import io.github.kloping.MySpringTool.annotations.*;
import io.github.kloping.MySpringTool.exceptions.NoRunException;
import io.github.kloping.judge.Judge;
import io.github.kloping.mirai0.Main.BootstarpResource;
import io.github.kloping.mirai0.Main.iutils.MemberUtils;
import io.github.kloping.mirai0.Main.iutils.MessageUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static Project.commons.rt.ResourceSet.FinalNormalString.BG_TIPS;
import static Project.commons.rt.ResourceSet.FinalString.*;
import static Project.commons.rt.ResourceSet.FinalValue.NOT_OPEN_NO_RUN_EXCEPTION;
import static Project.controllers.auto.ControllerSource.challengeDetailService;
import static Project.controllers.auto.ControllerTool.opened;
import static Project.controllers.auto.GameConfSource.DELETE_MAX;
import static Project.dataBases.GameDataBase.*;
import static Project.utils.Tools.GameTool.*;
import static Project.utils.Tools.Tool.DAY_LONG;
import static Project.utils.drawers.Drawer.drawWarp;
import static Project.utils.drawers.Drawer.getImageFromStrings;
import static io.github.kloping.mirai0.Main.BootstarpResource.START_AFTER;
import static io.github.kloping.mirai0.Main.BootstarpResource.println;

@Controller
public class GameController {
    public static final float MAX_XP = 1.5f;
    public static final Map<Long, Integer> DELETE_C = new ConcurrentHashMap<>();
    public static final List<String> LIST_FX = new ArrayList<>();
    public static final String PWD_FORMAT = "hsgameqd:%s:b:%s";
    @AutoStand
    public static IGameService gameService;
    private static String COM13 = "";

    static {
        LIST_FX.add("购买金魂币");
        LIST_FX.add("信息");
        LIST_FX.add("觉醒");
        LIST_FX.add("背包");
        LIST_FX.add("等级排行");
        LIST_FX.add("排行");
        LIST_FX.add("称号");
        LIST_FX.add("转生");
        LIST_FX.add("魂师签到");
    }

    private static final SimpleDateFormat DFN = new SimpleDateFormat("yyyy/MM/dd");

    @AutoStand
    ScoreController c1;
    @AutoStand
    GameJoinAcController c2;
    @AutoStand
    PlayerBehavioralManager manager;
    @AutoStand
    private GameBoneController gameBoneController;

    public GameController() {
        println(this.getClass().getSimpleName() + "构建");
        START_AFTER.add(() -> PINFO_LIST.clear());
    }

    @Before
    public void before(SpUser qq, SpGroup group, @AllMess String mess) throws NoRunException {
        if (!opened(group.getId(), this.getClass())) {
            throw NOT_OPEN_NO_RUN_EXCEPTION;
        }
        if (getInfo(qq.getId()).getHp() <= 0) {
            if (Tool.INSTANCE.EveListStartWith(LIST_FX, mess) == -1) {
                MessageUtils.INSTANCE.sendMessageInGroupWithAt("无状态", group.getId(), qq.getId());
                throw new NoRunException("无状态");
            }
        }
        if (getInfo(qq.getId()).isBg()) {
            MessageUtils.INSTANCE.sendMessageInGroupWithAt(BG_TIPS, group.getId(), qq.getId());
            throw new NoRunException(BG_TIPS);
        }
    }

    private Integer getThisWeekDays(Long qq) {
        StringBuilder sb = new StringBuilder();
        int n = Tool.INSTANCE.getOldestWeekOne();
        for (int i = 0; i <= n; i++) {
            String pwd = String.format(PWD_FORMAT, DFN.format(new Date(System.currentTimeMillis() - (DAY_LONG * i))), BootstarpResource.BOT.getId());
            sb.append(pwd).append(",");
        }
        String pwd = sb.toString();
        pwd = pwd.substring(0, pwd.length() - 1);
        String countStr = ControllerSource.klopingWeb.containsPwds(qq.toString(), "true", pwd);
        try {
            Integer count = Integer.valueOf(countStr);
            return count;
        } catch (NumberFormatException e) {
            throw new RuntimeException(e);
        }
    }

    private String jl(Integer n, Long qq) {
        switch (n) {
            case 1:
                getInfo(qq).addGold(100L, new TradingRecord().setFrom(-1).setMain(qq).setDesc("签到获得").setTo(qq).setMany(100).setType0(TradingRecord.Type0.gold).setType1(TradingRecord.Type1.add)).apply();
                return "签到成功!\n本周签到1天\n获得100金魂币";
            case 2:
                addToBgs(qq, 107, 2, ObjType.got);
                return "签到成功!\n本周签到2天\n获得2个仙品花瓣";
            case 3:
                getInfo(qq).addGold(200L, new TradingRecord().setFrom(-1).setMain(qq).setDesc("签到获得").setTo(qq).setMany(100).setType0(TradingRecord.Type0.gold).setType1(TradingRecord.Type1.add)).apply();
                return "签到成功!\n本周签到3天\n获得200金魂币";
            case 4:
                addToBgs(qq, 103, 3, ObjType.got);
                return "签到成功!\n本周签到4天\n获得5个大瓶经验";
            case 5:
                addToBgs(qq, 130, 3, ObjType.got);
                return "签到成功!\n本周签到5天\n获得3个奖券";
            case 6:
                getInfo(qq).addGold(500L, new TradingRecord().setFrom(-1).setMain(qq).setDesc("签到获得").setTo(qq).setMany(100).setType0(TradingRecord.Type0.gold).setType1(TradingRecord.Type1.add)).apply();
                return "签到成功!\n本周签到6天\n获得700金魂币";
            case 7:
                addToBgs(qq, 1601, 2, ObjType.got);
                return "签到成功!\n本周签到7天\n获得两白升级券";
            default:
                return "未知异常";
        }
    }

    static {
        TimerController.ZERO_RUNS.add(() -> {
            try {
                String ws = Tool.INSTANCE.getWeekOfDate(new Date());
                if (ws.equals(Tool.INSTANCE.WEEK_DAYS[1])) {
                    for (int i = 1; i <= 7; i++) {
                        String pwd = String.format(PWD_FORMAT,
                                DFN.format(new Date(System.currentTimeMillis() -
                                        1000 * 60 * 60 * 24 * i
                                )), BootstarpResource.BOT.getId());
                        ControllerSource.klopingWeb.del("", pwd);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Action("魂师签到")
    public String qd(Long qq, SpGroup group) {
        String pwd = String.format(PWD_FORMAT, DFN.format(new Date()), BootstarpResource.BOT.getId());
        KlopingWebDataBaseBoolean dbb = new KlopingWebDataBaseBoolean(pwd, false);
        Boolean v = dbb.getValue(qq);
        if (!v) {
            dbb.setValue(qq, true);
            Integer n = getThisWeekDays(qq);
            return jl(n, qq);
        } else {
            return "签到失败!";
        }
    }

    @Action("修炼")
    public String Xl(SpUser qq, SpGroup group) {
        return gameService.xl(qq.getId());
    }

    @Action("信息")
    public String info(SpUser qq, SpGroup group) {
        return gameService.info(qq.getId());
    }

    @Action("升级")
    public String newLevel(SpUser qq, SpGroup group) {
        return gameService.upUp(qq.getId());
    }

    @Action(value = "觉醒", otherName = {"武魂觉醒", "觉醒武魂"})
    public String openEye(SpUser qq, SpGroup group) {
        return gameService.openEyeWh(qq.getId());
    }

    @Action("重置武魂")
    public String delete(SpUser qq) {
        if (challengeDetailService.isTemping(qq.getId())) {
            return CHALLENGE_ING;
        }
        try {
            if (DELETE_C.containsKey(qq.getId())) if (DELETE_C.get(qq.getId()) >= DELETE_MAX) return "当日重置次数上限";
            return gameService.returnA(qq.getId());
        } finally {
            if (DELETE_C.containsKey(qq.getId())) DELETE_C.put(qq.getId(), DELETE_C.get(qq.getId()) + 1);
            else DELETE_C.put(qq.getId(), 1);
        }
    }

    @Action("双修打工进入.*+")
    public Object o1(SpUser user, SpGroup group, @AllMess String s0) {
        MessageUtils.INSTANCE.sendMessageInGroupWithAt(Xl2(user, group), group.getId(), user.getId());
        MessageUtils.INSTANCE.sendMessageInGroupWithAt(c1.aJob(user, group), group.getId(), user.getId());
        String name = s0.replace("双修", "").replace("打工", "").replace("进入", "");
        return c2.com1(group, name, user);
    }

    @Action("修炼打工进入.*+")
    public Object o2(SpUser user, SpGroup group, @AllMess String s0) {
        MessageUtils.INSTANCE.sendMessageInGroupWithAt(Xl(user, group), group.getId(), user.getId());
        MessageUtils.INSTANCE.sendMessageInGroupWithAt(c1.aJob(user, group), group.getId(), user.getId());
        String name = s0.replace("修炼", "").replace("打工", "").replace("进入", "");
        return c2.com1(group, name, user);
    }

    @Action("吸收魂环<.{0,}=>name>")
    public String joinHh(SpUser qq, @Param("name") String name, SpGroup group) {
        try {
            Integer id = GameDataBase.NAME_2_ID_MAPS.get(name.trim());
            String sss = gameService.parseHh(qq.getId(), id);
            return sss;
        } catch (Exception e) {
            return "系统未找到:" + name;
        }
    }

    @Action("侦查.+")
    public String Look(SpUser qq, @AllMess String chain, SpGroup group) {
        long who = Project.utils.Utils.getAtFromString(chain);
        if (who == -1) return NOT_FOUND_AT;
        if (!GameDataBase.exist(who)) return (PLAYER_NOT_REGISTERED);
        PersonInfo I = getInfo(qq.getId());
        PersonInfo Y = getInfo(who);
        if (I.getLevel() >= Y.getLevel()) {
            getInfo(qq.getId()).addHl(-10L).apply();
            StringBuilder m1 = new StringBuilder();
            m1.append("侦查成功,消耗十点魂力\n");
            m1.append(MemberUtils.getNameFromGroup(who, group));
            m1.append("的信息\n");
            String sss = gameService.info(who);
            m1.append(sss);
            return m1.toString();
        } else {
            return ("对方魂力等级比你高,无法侦查");
        }
    }

    @Action("换积分<\\d{1,}=>num>")
    public String getScore(@Param("num") String num, SpUser qq, SpGroup group) {
        String ll = Tool.INSTANCE.findNumberFromString(num);
        if (ll == null || ll.isEmpty()) return NOT_FOUND_VALUE;
        long l = Long.parseLong(ll);
        String sss = gameService.getScoreFromGold(qq.getId(), l);
        return sss;
    }

    @Action("等级排行.*?")
    public String pH(@AllMess String num) {
        int n;
        String ll = Tool.INSTANCE.findNumberFromString(num);
        if (ll == null || ll.isEmpty()) n = 10;
        else n = Integer.parseInt(ll);
        n = n > 100 ? 100 : n;
        StringBuilder sb = new StringBuilder();
        int r = 1;
        for (Map.Entry<String, Integer> entry : phGet(n)) {
            String sn = getFhName(Long.valueOf(entry.getKey()));
            sb.append("第" + (r++)).append(":").append(sn.isEmpty() ? entry.getKey() : sn).append("==>\r").append(n <= 10 ? "" : "\r\n\t").append(entry.getValue()).append("级(");
            if (entry.getValue() >= 150) {
                sb.append(Tool.INSTANCE.filterBigNum(String.valueOf(getInfo(entry.getKey()).getXp()))).append(")");
            }
            sb.append("\r\n");
        }
        return n <= 10 ? getImageFromStrings(false, sb.toString().split("\r\n")) : sb.toString();
    }

    @Action("称号")
    public String com13() {
        if (Judge.isEmpty(COM13)) {
            StringBuilder sb = new StringBuilder();
            sb.append("1-10=>");
            sb.append("\r\n\t");
            sb.append(getFH(1));
            sb.append("\r\n");
            sb.append("10-20=>");
            sb.append("\r\n\t");
            sb.append(getFH(10));
            sb.append("\r\n");
            sb.append("20-30=>");
            sb.append("\r\n\t");
            sb.append(getFH(20));
            sb.append("\r\n");
            sb.append("30-40=>");
            sb.append("\r\n\t");
            sb.append(getFH(30));
            sb.append("\r\n");
            sb.append("40-50=>");
            sb.append("\r\n\t");
            sb.append(getFH(40));
            sb.append("\r\n");
            sb.append("50-60=>");
            sb.append("\r\n\t");
            sb.append(getFH(50));
            sb.append("\r\n");
            sb.append("60-70=>");
            sb.append("\r\n\t");
            sb.append(getFH(60));
            sb.append("\r\n");
            sb.append("70-80=>");
            sb.append("\r\n\t");
            sb.append(getFH(70));
            sb.append("\r\n");
            sb.append("80-90=>");
            sb.append("\r\n\t");
            sb.append(getFH(80));
            sb.append("\r\n");
            sb.append("90-95=>");
            sb.append("\r\n\t");
            sb.append(getFH(90));
            sb.append("\r\n");
            sb.append("95-100=>");
            sb.append("\r\n\t");
            sb.append(getFH(95));
            sb.append("\r\n");
            sb.append("100-110=>");
            sb.append("\r\n\t");
            sb.append(getFH(100));
            sb.append("\r\n");
            sb.append("110-120=>");
            sb.append("\r\n\t");
            sb.append(getFH(110));
            sb.append("\r\n");
            sb.append("120-150=>");
            sb.append("\r\n\t");
            sb.append(getFH(120));
            sb.append("\r\n");
            sb.append("150--=>");
            sb.append("\r\n\t");
            sb.append(getFH(150));
            COM13 = sb.toString();
        }
        return COM13;
    }

    @Action("吸收<.{0,}=>str>")
    public String Xsh(@AllMess String message, SpGroup group, @Param("str") String str, SpUser qq) {
        try {
            Integer id = GameDataBase.NAME_2_ID_MAPS.get(str);
            if (id > 200 && id < 210) {
                return joinHh(qq, str, group);
            } else if (id > 1500 && id < 1600) {
                try {
                    gameBoneController.before(qq, group, message);
                    return gameBoneController.parseBone(str, qq.getId(), group);
                } catch (NoRunException e) {
                    return null;
                }
            } else {
                return ERR_TIPS;
            }
        } catch (Exception e) {
        }
        return ERR_TIPS;
    }

    @Action("双修")
    public String Xl2(SpUser qq, SpGroup group) {
        if (getWarp(qq.getId()).getBindQ().longValue() == -1) return "未融合";
        String str = gameService.xl2(qq.getId());
        return str;
    }

    @Action("关系列表")
    public String warps(long q) {
        Warp warp = getWarp(q);
        warp.apply();
        return Tool.INSTANCE.pathToImg(drawWarp(warp));
    }

    @Action(value = "精神攻击.*?", otherName = {"精神冲击.*?"})
    public String SpAtt(long q, @AllMess String mss) {
        long at = Project.utils.Utils.getAtFromString(mss);
        if (at == -1) {
            if (mss.contains("#")) {
                at = -2;
                mss = mss.replaceAll("#", "");
            } else {
                return "未选择目标";
            }
        }
        mss = mss.replace("[@" + at + "]", "");
        int br = (int) Tool.INSTANCE.randA(12, 20);
        String m = Tool.INSTANCE.findNumberFromString(mss);
        try {
            br = Integer.parseInt(m);
        } catch (Exception e) {
        }
        return gameService.attByHj(q, at, br);
    }

    @Action("切换武魂")
    private String handoff(long q) {
        PersonInfo pinfo = getInfo(q);
        int c = pinfo.getWhc();
        if (c >= 2) {
            int p = pinfo.getP();
            if (p < c) {
                p++;
            } else {
                p = 1;
            }
            pinfo.setP(p);
            pinfo.apply();
            return "切换成功";
        } else {
            return "没有其他武魂";
        }
    }

    @Action("金魂币消费记录")
    public String m0(long q) {
        return "点击=>" + String.format(SpringBootResource.address + "/record.html?qid=" + q);
    }
}
