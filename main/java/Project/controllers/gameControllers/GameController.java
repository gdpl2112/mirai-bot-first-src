package Project.controllers.gameControllers;


import io.github.kloping.mirai0.commons.Group;
import io.github.kloping.mirai0.commons.User;
import io.github.kloping.mirai0.commons.PersonInfo;
import io.github.kloping.mirai0.commons.Warp;
import Project.controllers.auto.ConfirmController;
import Project.dataBases.GameDataBase;
import Project.interfaces.Iservice.IGameService;
import io.github.kloping.mirai0.Main.ITools.MemberTools;
import io.github.kloping.mirai0.Main.ITools.MessageTools;
import io.github.kloping.MySpringTool.annotations.*;
import io.github.kloping.MySpringTool.exceptions.NoRunException;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static Project.controllers.auto.ControllerTool.opened;
import static Project.dataBases.GameDataBase.*;
import static io.github.kloping.mirai0.unitls.Tools.GameTool.*;
import static io.github.kloping.mirai0.unitls.Tools.Tool.*;
import static io.github.kloping.mirai0.unitls.drawers.Drawer.drawWarp;
import static io.github.kloping.mirai0.unitls.drawers.Drawer.getImageFromStrings;
import static io.github.kloping.mirai0.Main.Resource.START_AFTER;
import static io.github.kloping.mirai0.Main.Resource.println;

@Controller
public class GameController {

    public static final float maxXp = 1.5f;

    public GameController() {
        println(this.getClass().getSimpleName() + "构建");
        START_AFTER.add(() -> HIST_INFOS.clear());
    }

    @AutoStand
    IGameService gameService;

    private static List<String> listFx = new ArrayList<>();

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

    @Before
    public void before(User qq, Group group, @AllMess String mess) throws NoRunException {
        if (!opened(group.getId(), this.getClass())) {
            throw new NoRunException("未开启");
        }
        if (getInfo(qq.getId()).getHp() <= 0) {
            if (EveListStartWith(listFx, mess) == -1) {
                MessageTools.sendMessageInGroupWithAt("无状态", group.getId(), qq.getId());
                throw new NoRunException("无状态");
            }
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

    public static final Map<Long, Integer> deleteC = new ConcurrentHashMap<>();

    @Action("转生")
    public String delete(User qq) {
        try {
            if (deleteC.containsKey(qq.getId()))
                if (deleteC.get(qq.getId()) >= 3)
                    return "一天仅可转生三次";
            return gameService.returnA(qq.getId());
        } finally {
            if (deleteC.containsKey(qq.getId()))
                deleteC.put(qq.getId(), deleteC.get(qq.getId()) + 1);
            else deleteC.put(qq.getId(), 1);
        }
    }

    @Action(value = "背包", otherName = "我的背包")
    public String bgs(User qq, Group group) {
        String str = getImageFromStrings(gameService.getBags(qq.getId()));
        return str;
    }

    @Action(value = "购买金魂币<\\d{1,}=>num>", otherName = {"兑换金魂币<\\d{1,}=>num>"})
    public String BuyGold(User qq, @Param("num") String num, Group group) {
        try {
            Long nu = Long.valueOf(num);
            String str = gameService.buyGold(qq.getId(), nu);
            return str;
        } catch (NumberFormatException e) {
            return "买多少呢";
        }
    }

    @Action(value = "魂环配置", otherName = {"我的魂环"})
    public String showHh(User qq, String num, Group group) {
        String str = gameService.showHh(qq.getId());
        return str;
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
        if (who == -1)
            return ("谁?");
        long at = getInfo(qq.getId()).getAk1();
        if (at > System.currentTimeMillis())
            return ("攻击冷却中..=>" + getTimeHHMM(at));
        if (!GameDataBase.exist(who)) return ("该玩家尚未注册");
        String sss = gameService.attWhos(qq.getId(), who, group);
        getInfo(qq.getId()).setAk1(System.currentTimeMillis() + 1000 * 30).apply();
        return sss;
    }

    @Action("侦查.+")
    public String Look(User qq, @AllMess String chain, Group group) {
        long who = MessageTools.getAtFromString(chain);
        if (who == -1)
            return ("谁?");
        if (!GameDataBase.exist(who)) return ("该玩家尚未注册");
        PersonInfo I = getInfo(qq.getId());
        PersonInfo Y = getInfo(who);
        if (I.getLevel() >= Y.getLevel()) {
            putPerson(getInfo(qq.getId()).addHl(-10L));
            //GameDetailService.UseHl(who, -10);
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
        if (ll == null || ll.isEmpty()) return ("给个数值");
        long l = Long.parseLong(ll);
        String sss = gameService.getScoreFromGold(qq.getId(), l);
        return sss;
    }

    @Action("等级排行<\\d{1,}=>num>")
    public String pH(@Param("num") String num) {
        int n;
        String ll = findNumberFromString(num);
        if (ll == null || ll.isEmpty()) {
            n = 10;
        } else {
            n = Integer.parseInt(ll);
        }
        StringBuilder sb = new StringBuilder();
        int r = 1;
        for (Map.Entry<String, Integer> entry : phGet(n)) {
            String sn = getFhName(Long.valueOf(entry.getKey()));
            sb.append("第" + (r++)).append(":QQ:")
                    .append(sn.isEmpty() ? entry.getKey() : sn)
                    .append("==>\r\n\t")
                    .append(entry.getValue())
                    .append("级\r\n");
        }
        return sb.toString();
    }

    @Action("等级排行")
    public String pH10() {
        StringBuilder sb = new StringBuilder();
        int r = 1;
        for (Map.Entry<String, Integer> entry : phGet(10)) {
            String sn = getFhName(Long.valueOf(entry.getKey()));
            sb.append("第" + (r++)).append(":QQ:")
                    .append(sn.isEmpty() ? entry.getKey() : sn)
                    .append("==>")
                    .append(entry.getValue())
                    .append("级\r\n");
        }
        return getImageFromStrings(false, sb.toString().split("\r\n"));
    }

    private static String com13 = "";

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
        com13 = sb.toString();
    }

    @Action("称号")
    public String com13() {
        return com13;
    }

    @AutoStand
    private GameBoneController gameBoneController;

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
                return ("???");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ("错误...");
    }

    @Action("取名封号<.+=>name>")
    public String SnameMake(@Param("name") String name, Group group, User qq) {
        return gameService.makeSname(qq.getId(), name, group);
    }

    @Action("双修")
    public String Xl2(User qq, Group group) {
        if (getWarp(qq.getId()).getBindQ().longValue() == -1)
            return "未融合";
        String str = gameService.xl2(qq.getId());
        return str;
    }

    @Action(value = "融合武魂<.+=>str>", otherName = {"武魂融合<.+=>str>"})
    public String Fusion(@Param("str") String str, Group group, User qq) {
        Long q2 = MessageTools.getAtFromString(str);
        if (q2 == -1)
            throw new RuntimeException();
        String s1 = gameService.fusion(qq.getId(), q2, group);
        return s1;
    }

    public String RemoveFusionNow(Long qq) {
        Warp warp = getWarp(qq);
        if (warp.getBindQ().longValue() != -1) {
            long q1 = qq;
            long q2 = warp.getBindQ().longValue();
            Warp warp2 = getWarp(q2);
            warp.setBindQ(-1L);
            warp2.setBindQ(-1L);
            setWarp(warp);
            setWarp(warp2);
            return "解除成功";
        } else return "你没有与任何人融合";
    }


    @Action("解除武魂融合")
    public String RemoveFusion(User qq) {
        try {
            Method method = this.getClass().getDeclaredMethod("RemoveFusionNow", Long.class);
            ConfirmController.regConfirm(qq.getId(),method, this, new Object[]{qq.getId()});
            return "您确定要解除吗?\r\n请在30秒内回复\r\n确定/取消";
        } catch (Exception e) {
            return "解除异常";
        }
    }

    @Action(value = "我的武魂类型", otherName = {"武魂类型"})
    public String myType(long q) {
        PersonInfo p1 = getInfo(q);
        if (p1.getWh() > 0)
            return String.format("你的武魂是\"%s\"属于\"%s\"", getNameById(p1.getWh()), getWhType(p1.getWhType()));
        else return "您还没有武魂";
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
