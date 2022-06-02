package Project.controllers.gameControllers;


import Project.controllers.normalController.ScoreController;
import Project.dataBases.GameDataBase;
import Project.interfaces.Iservice.IGameService;
import Project.services.player.PlayerBehavioralManager;
import io.github.kloping.MySpringTool.annotations.*;
import io.github.kloping.MySpringTool.exceptions.NoRunException;
import io.github.kloping.mirai0.Main.ITools.MemberTools;
import io.github.kloping.mirai0.Main.ITools.MessageTools;
import io.github.kloping.mirai0.commons.Group;
import io.github.kloping.mirai0.commons.PersonInfo;
import io.github.kloping.mirai0.commons.User;
import io.github.kloping.mirai0.commons.Warp;
import io.github.kloping.mirai0.commons.resouce_and_tool.ResourceSet;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static Project.controllers.auto.ControllerSource.challengeDetailService;
import static Project.controllers.auto.ControllerTool.opened;
import static Project.controllers.auto.GameConfSource.DELETE_MAX;
import static Project.dataBases.GameDataBase.*;
import static io.github.kloping.mirai0.Main.Resource.START_AFTER;
import static io.github.kloping.mirai0.Main.Resource.println;
import static io.github.kloping.mirai0.commons.resouce_and_tool.ResourceSet.FinalNormalString.BG_TIPS;
import static io.github.kloping.mirai0.commons.resouce_and_tool.ResourceSet.FinalString.*;
import static io.github.kloping.mirai0.commons.resouce_and_tool.ResourceSet.FinalValue.NOT_OPEN_NO_RUN_EXCEPTION;
import static io.github.kloping.mirai0.unitls.Tools.GameTool.*;
import static io.github.kloping.mirai0.unitls.Tools.Tool.*;
import static io.github.kloping.mirai0.unitls.drawers.Drawer.drawWarp;
import static io.github.kloping.mirai0.unitls.drawers.Drawer.getImageFromStrings;

@Controller
public class GameController {

    public static final float MAX_XP = 1.5f;
    public static final Map<Long, Integer> DELETE_C = new ConcurrentHashMap<>();
    private static List<String> listFx = new ArrayList<>();
    private static String COM13 = "";

    static {
        listFx.add("购买金魂币");
        listFx.add("信息");
        listFx.add("觉醒");
        listFx.add("背包");
        listFx.add("等级排行");
        listFx.add("排行");
        listFx.add("称号");
        listFx.add("转生");
    }

    static {
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

    @AutoStand
    IGameService gameService;
    @AutoStand
    GameController c0;
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
        START_AFTER.add(() -> HIST_INFOS.clear());
    }

    @Before
    public void before(User qq, Group group, @AllMess String mess) throws NoRunException {
        if (!opened(group.getId(), this.getClass())) {
            throw NOT_OPEN_NO_RUN_EXCEPTION;
        }
        if (getInfo(qq.getId()).getHp() <= 0) {
            if (EveListStartWith(listFx, mess) == -1) {
                MessageTools.sendMessageInGroupWithAt("无状态", group.getId(), qq.getId());
                throw new NoRunException("无状态");
            }
        }
        if (getInfo(qq.getId()).isBg()) {
            MessageTools.sendMessageInGroupWithAt(BG_TIPS, group.getId(), qq.getId());
            throw new NoRunException(BG_TIPS);
        }
    }

    @Action("修炼")
    public String Xl(User qq, Group group) {
        String str = gameService.xl(qq.getId());
        return str;
    }

    @Action("信息")
    public String info(User qq, Group group) {
        String str = gameService.info(qq.getId());
        return str;
    }

    @Action("升级")
    public String newLevel(User qq, Group group) {
        String str = gameService.upUp(qq.getId());
        return str;
    }

    @Action(value = "觉醒", otherName = {"武魂觉醒", "觉醒武魂"})
    public String openEye(User qq, Group group) {
        String str = gameService.openEyeWh(qq.getId());
        return str;
    }

    @Action("转生")
    public String delete(User qq) {
        if (challengeDetailService.isTemping(qq.getId())) {
            return CHALLENGE_ING;
        }
        try {
            if (DELETE_C.containsKey(qq.getId()))
                if (DELETE_C.get(qq.getId()) >= DELETE_MAX)
                    return "当日转生次数上限";
            return gameService.returnA(qq.getId());
        } finally {
            if (DELETE_C.containsKey(qq.getId()))
                DELETE_C.put(qq.getId(), DELETE_C.get(qq.getId()) + 1);
            else DELETE_C.put(qq.getId(), 1);
        }
    }

    @Action("双修打工进入.*+")
    public Object o1(User user, Group group, @AllMess String s0) {
        MessageTools.sendMessageInGroupWithAt(c0.Xl2(user, group), group.getId(), user.getId());
        MessageTools.sendMessageInGroupWithAt(c1.aJob(user, group), group.getId(), user.getId());
        String name = s0.replace("双修", "").replace("打工", "").replace("进入", "");
        return c2.com1(group, name, user);
    }

    @Action("修炼打工进入.*+")
    public Object o2(User user, Group group, @AllMess String s0) {
        MessageTools.sendMessageInGroupWithAt(c0.Xl(user, group), group.getId(), user.getId());
        MessageTools.sendMessageInGroupWithAt(c1.aJob(user, group), group.getId(), user.getId());
        String name = s0.replace("修炼", "").replace("打工", "").replace("进入", "");
        return c2.com1(group, name, user);
    }

    @Action("吸收魂环<.{0,}=>name>")
    public String joinHh(User qq, @Param("name") String name, Group group) {
        try {
            Integer id = GameDataBase.NAME_2_ID_MAPS.get(name.trim());
            String sss = gameService.parseHh(qq.getId(), id);
            return sss;
        } catch (Exception e) {
            return "系统未找到:" + name;
        }
    }

    @Action("攻击.+")
    public String AttWho(User qq, @AllMess String chain, Group group) {
        long who = MessageTools.getAtFromString(chain);
        if (who == -1) return NOT_FOUND_AT;
        long at = getInfo(qq.getId()).getAk1();
        if (at > System.currentTimeMillis())
            return String.format(ResourceSet.FinalFormat.ATT_WAIT_TIPS, getTimeTips(at));
        if (!GameDataBase.exist(who)) return (PLAYER_NOT_REGISTERED);
        String sss = gameService.att(qq.getId(), who, group);
        getInfo(qq.getId()).setAk1(System.currentTimeMillis() + manager.getAttPost(qq.getId())).apply();
        return sss;
    }

    @Action("侦查.+")
    public String Look(User qq, @AllMess String chain, Group group) {
        long who = MessageTools.getAtFromString(chain);
        if (who == -1)
            return NOT_FOUND_AT;
        if (!GameDataBase.exist(who)) return (PLAYER_NOT_REGISTERED);
        PersonInfo I = getInfo(qq.getId());
        PersonInfo Y = getInfo(who);
        if (I.getLevel() >= Y.getLevel()) {
            putPerson(getInfo(qq.getId()).addHl(-10L));
            StringBuilder m1 = new StringBuilder();
            m1.append("侦查成功,消耗十点魂力\n");
            m1.append(MemberTools.getNameFromGroup(who, group));
            m1.append("的信息\n");
            String sss = gameService.info(who);
            m1.append(sss);
            return m1.toString();
        } else {
            return ("对方魂力等级比你高,无法侦查");
        }
    }

    @Action("换积分<\\d{1,}=>num>")
    public String getScore(@Param("num") String num, User qq, Group group) {
        String ll = findNumberFromString(num);
        if (ll == null || ll.isEmpty()) return NOT_FOUND_VALUE;
        long l = Long.parseLong(ll);
        String sss = gameService.getScoreFromGold(qq.getId(), l);
        return sss;
    }

    @Action("等级排行.*?")
    public String pH(@AllMess String num) {
        int n;
        String ll = findNumberFromString(num);
        if (ll == null || ll.isEmpty()) {
            n = 10;
        } else {
            n = Integer.parseInt(ll);
        }
        n = n > 100 ? 100 : n;
        StringBuilder sb = new StringBuilder();
        int r = 1;
        for (Map.Entry<String, Integer> entry : phGet(n)) {
            String sn = getFhName(Long.valueOf(entry.getKey()));
            sb.append("第" + (r++)).append(":QQ:")
                    .append(sn.isEmpty() ? entry.getKey() : sn)
                    .append("==>\r").append(n <= 10 ? "" : "\n\t")
                    .append(entry.getValue())
                    .append("级\r\n");
        }
        return n <= 10 ? getImageFromStrings(false, sb.toString().split("\r\n")) : sb.toString();
    }

    @Action("称号")
    public String com13() {
        return COM13;
    }

    @Action("吸收<.{0,}=>str>")
    public String Xsh(@AllMess String message, Group group, @Param("str") String str, User qq) {
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
            e.printStackTrace();
        }
        return ERR_TIPS;
    }

    @Action("双修")
    public String Xl2(User qq, Group group) {
        if (getWarp(qq.getId()).getBindQ().longValue() == -1)
            return "未融合";
        String str = gameService.xl2(qq.getId());
        return str;
    }

    @Action("关系列表")
    public String warps(long q) {
        Warp warp = getWarp(q);
        return pathToImg(drawWarp(warp));
    }

    @Action(value = "精神攻击.*?", otherName = {"精神冲击.*?"})
    public String SpAtt(long q, @AllMess String mss) {
        long at = MessageTools.getAtFromString(mss);
        if (at == -1) {
            if (mss.contains("#")) {
                at = -2;
                mss = mss.replaceAll("#", "");
            } else {
                return "未选择目标";
            }
        }
        mss = mss.replace("[@" + at + "]", "");
        int br = (int) randA(12, 20);
        String m = findNumberFromString(mss);
        try {
            br = Integer.parseInt(m);
        } catch (Exception e) {
        }
        return gameService.attByHj(q, at, br);
    }
}
