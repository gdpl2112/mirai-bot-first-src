package Project.controllers.gameControllers;


import Project.aSpring.SpringBootResource;
import Project.aSpring.mcs.controllers.DetailController;
import Project.commons.SpGroup;
import Project.commons.SpUser;
import Project.controllers.auto.ConfirmController;
import Project.dataBases.GameDataBase;
import Project.utils.VelocityUtils;
import Project.interfaces.Iservice.IGameObjService;
import Project.interfaces.Iservice.IGameService;
import Project.interfaces.httpApi.KlopingWeb;
import io.github.kloping.MySpringTool.annotations.*;
import io.github.kloping.MySpringTool.exceptions.NoRunException;
import io.github.kloping.mirai0.commons.*;
import Project.commons.resouce_and_tool.ResourceSet;
import io.github.kloping.mirai0.unitls.Tools.Tool;
import io.github.kloping.number.NumberUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static Project.controllers.auto.ControllerSource.challengeDetailService;
import static Project.controllers.auto.ControllerTool.opened;
import static Project.dataBases.GameDataBase.*;
import static Project.services.detailServices.ac.GameJoinDetailService.getGhostObjFrom;
import static io.github.kloping.mirai0.Main.BootstarpResource.BOT;
import static io.github.kloping.mirai0.Main.BootstarpResource.println;
import static Project.commons.resouce_and_tool.ResourceSet.FinalFormat.BG_WAIT_TIPS;
import static Project.commons.resouce_and_tool.ResourceSet.FinalString.CHALLENGE_ING;
import static Project.commons.resouce_and_tool.ResourceSet.FinalString.IN_SELECT;
import static Project.commons.resouce_and_tool.ResourceSet.FinalValue.NOT_OPEN_NO_RUN_EXCEPTION;
import static io.github.kloping.mirai0.unitls.drawers.Drawer.getImageFromStrings;

/**
 * @author github-kloping
 */
@Controller
public class GameController2 {
    @AutoStand
    IGameService service;
    @AutoStand
    IGameObjService gameObjService;
    @AutoStand
    IGameService gameService;
    @AutoStand
    private GameBoneController gameBoneController;

    public GameController2() {
        println(this.getClass().getSimpleName() + "构建");
    }

    @Before
    public void before(SpUser qq, SpGroup group, @AllMess String str) throws NoRunException {
        if (!opened(group.getId(), this.getClass())) {
            throw NOT_OPEN_NO_RUN_EXCEPTION;
        }
    }

    @Action(value = "购买金魂币<\\d{1,}=>num>", otherName = {"兑换金魂币<\\d{1,}=>num>"})
    public String buyGold(SpUser qq, @Param("num") String num, SpGroup group) {
        if (challengeDetailService.isTemping(qq.getId())) {
            return CHALLENGE_ING;
        }
        try {
            Long nu = Long.valueOf(num);
            String str = gameService.buyGold(qq.getId(), nu);
            return str;
        } catch (NumberFormatException e) {
            return "买多少呢";
        }
    }

    @Action(value = "魂环配置", otherName = {"我的魂环"})
    public String showHh(SpUser qq, String num, SpGroup group) {
        String str = gameService.showHh(qq.getId());
        return str;
    }

    @Action(value = "详细信息", otherName = {"详情信息"})
    public String m1(long q) {
        return service.detailInfo(q);
    }

    @Action("收徒<.+=>str>")
    public String m2(long q, @Param("str") String str) {
        long q2 = Long.parseLong(NumberUtils.findNumberFromString(str).trim());
        if (q == q2) return "scram";
        return service.shouTu(q, q2);
    }

    @Action(value = "出师")
    public String m3(long q) {
        return service.chuShi(q);
    }

    @Action(value = "出徒.*?")
    public String m4(long q, @AllMess String mess) {
        return service.chuTu(q, Tool.INSTANCE.getInteagerFromStr(mess));
    }

    @Action("升级第<.+=>str>")
    public String upda(@Param("str") String str, long q) {
        if (str.contains("魂环")) {
            str = str.replace("魂环", "").replace("第", "");
            String s1 = Tool.INSTANCE.findNumberZh(str);
            Integer st = Integer.valueOf(Tool.INSTANCE.chineseNumber2Int(s1));
            return service.upHh(q, st);
        }
        throw new NoRunException();
    }

    @Action(value = "融合武魂<.+=>str>", otherName = {"武魂融合<.+=>str>"})
    public String fusion(@Param("str") String str, SpGroup group, SpUser qq) {
        Long q2 = Project.utils.Utils.getAtFromString(str);
        if (q2 == -1)
            throw new RuntimeException();
        String s1 = gameService.fusion(qq.getId(), q2, group);
        return s1;
    }

    public String removeFusionNow(Long qq) {
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
        } else {
            return "你没有与任何人融合";
        }
    }

    @Action("取名封号<.+=>name>")
    public String makeSName(@Param("name") String name, SpGroup group, SpUser qq) {
        return gameService.makeSname(qq.getId(), name, group);
    }

    @Action("解除武魂融合")
    public String removeFusion(SpUser qq) {
        try {
            Method method = this.getClass().getDeclaredMethod("removeFusionNow", Long.class);
            ConfirmController.regConfirm(qq.getId(), method, this, new Object[]{qq.getId()});
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

    @Action("合成<.+=>name>")
    public String m1(@Param("name") String name, long q) {
        try {
            int id = GameDataBase.NAME_2_ID_MAPS.get(name);
            return gameObjService.compound(q, id);
        } catch (Exception e) {
            return "未找到相关物品";
        }
    }

    private static final Integer PAGE_SIZE = 12;

    @Action(value = "背包.*", otherName = "我的背包.*")
    public String bgs(SpUser qq, SpGroup group, @AllMess String s0) {
        return getImageFromStrings(bgs0(qq, s0));
    }

    public String[] bgs0(SpUser qq, @AllMess String s0) {
        List<String> list = gameService.getBags0(qq.getId());
        List<String> endList = new ArrayList<>();
        String str = null;
        Integer num = Tool.INSTANCE.getInteagerFromStr(s0);
        num = num == null ? 1 : num;
        if (num == 0) {
            return list.toArray(new String[0]);
        }
        Integer max = 0;
        int index = list.size();
        while (index > 0) {
            index -= PAGE_SIZE;
            max++;
        }
        num = num >= max ? max : num;
        endList.add("PAGE: " + num + "/" + max);
        if (list.size() > PAGE_SIZE) {
            for (int i = 0; i < PAGE_SIZE; i++) {
                int i1 = (PAGE_SIZE * (num - 1)) + i;
                if (i1 >= list.size()) break;
                endList.add(list.get(i1));
            }
        } else {
            endList.addAll(list);
        }
        return endList.toArray(new String[0]);
    }

    @AutoStand
    public KlopingWeb klopingWeb;

    @Action("闭关")
    public Object o3(SpUser user) throws NoSuchMethodException {
        Method m0 = this.getClass().getDeclaredMethod("bg", Long.class);
        ConfirmController.regConfirm(user.getId(), m0, this, Long.valueOf(user.getId()));
        return VelocityUtils.getTemplateToString("retreat.tips", EVE_GET);
    }

    private static final Integer EVE_GET = 20;
    private static final String BG_PWD = "挂机池进入时间戳";

    private Object bg(Long q) {
        PersonInfo p0 = getInfo(q);
        if (System.currentTimeMillis() < p0.getBgk()) {
            return String.format(BG_WAIT_TIPS, Tool.INSTANCE.getTimeTips(p0.getBgk()));
        }
        GhostObj ghostObj = getGhostObjFrom(q);
        if (ghostObj != null) {
            return IN_SELECT;
        }
        p0.setBg(true);
        p0.setBgk(System.currentTimeMillis() + ResourceSet.FinalValue.BG_CD);
        p0.apply();
        klopingWeb.put(q.toString(), String.valueOf(System.currentTimeMillis()), BG_PWD);
        return "闭关完成!";
    }

    private Object unBg(Long q) {
        PersonInfo p0 = getInfo(q);
        if (System.currentTimeMillis() < p0.getBgk()) {
            return String.format(BG_WAIT_TIPS, Tool.INSTANCE.getTimeTips(p0.getBgk()));
        }
        p0.setBg(false);
        p0.setBgk(System.currentTimeMillis() + ResourceSet.FinalValue.BG_CD);
        String t0 = klopingWeb.get(q.toString(), BG_PWD);
        Long t = System.currentTimeMillis() - Long.valueOf(t0);
        Long min = (t / 60000L);
        Long v = EVE_GET * min;
        p0.addXp(v);
        p0.apply();
        return VelocityUtils.getTemplateToString("un.retreat.tips", min, v);
    }

    @Action("取消闭关")
    public Object o4(SpUser user) throws NoSuchMethodException {
        if (getInfo(user.getId()).isBg()) {
            Method m0 = this.getClass().getDeclaredMethod("unBg", Long.class);
            ConfirmController.regConfirm(user.getId(), m0, this, Long.valueOf(user.getId()));
            return "确认取消闭关吗?\n半小时后可再次闭关";
        } else {
            return "没有在闭关";
        }
    }

    @Action("信息预览")
    public void messagePre(SpUser user, SpGroup group) {
        Long q = null;
        if (DetailController.RID2QID.values().contains(user.getId())) {
            for (Integer e : DetailController.RID2QID.keySet()) {
                if (DetailController.RID2QID.get(e) == user.getId()) {
                    q = e.longValue();
                    break;
                }
            }
        } else {
            Integer id = Tool.INSTANCE.RANDOM.nextInt(90000) + 1000;
            DetailController.RID2QID.put(id, user.getId());
            q = id.longValue();
        }
        BOT.getGroup(group.getId()).getMembers().get(user.getId()).sendMessage(
                "点击=>" + String.format(SpringBootResource.address + "/detail.html?qid=" + q)
        );
    }
}
