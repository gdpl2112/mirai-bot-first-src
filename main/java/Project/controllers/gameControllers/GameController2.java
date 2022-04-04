package Project.controllers.gameControllers;


import Project.controllers.auto.ConfirmController;
import Project.controllers.normalController.ScoreController;
import Project.dataBases.GameDataBase;
import Project.interfaces.Iservice.IGameObjService;
import Project.interfaces.Iservice.IGameService;
import io.github.kloping.MySpringTool.annotations.*;
import io.github.kloping.MySpringTool.exceptions.NoRunException;
import io.github.kloping.mirai0.Main.ITools.MessageTools;
import io.github.kloping.mirai0.commons.GhostObj;
import io.github.kloping.mirai0.commons.Group;
import io.github.kloping.mirai0.commons.PersonInfo;
import io.github.kloping.mirai0.commons.User;
import io.github.kloping.mirai0.commons.resouce_and_tool.ResourceSet;
import io.github.kloping.mirai0.unitls.Tools.Tool;
import io.github.kloping.number.NumberUtils;

import java.lang.reflect.Method;

import static Project.controllers.auto.ControllerTool.opened;
import static Project.dataBases.GameDataBase.getInfo;
import static Project.services.detailServices.GameJoinDetailService.getGhostObjFrom;
import static io.github.kloping.mirai0.Main.Resource.println;
import static io.github.kloping.mirai0.commons.resouce_and_tool.ResourceSet.FinalFormat.BG_WAIT_TIPS;
import static io.github.kloping.mirai0.commons.resouce_and_tool.ResourceSet.FinalString.IN_SELECT;
import static io.github.kloping.mirai0.commons.resouce_and_tool.ResourceSet.FinalValue.NOT_OPEN_NO_RUN_EXCEPTION;
import static io.github.kloping.mirai0.unitls.Tools.GameTool.isATrue;
import static io.github.kloping.mirai0.unitls.Tools.Tool.getTimeTips;

/**
 * @author github-kloping
 */
@Controller
public class GameController2 {
    @AutoStand
    IGameService service;
    @AutoStand
    IGameObjService gameObjService;

    public GameController2() {
        println(this.getClass().getSimpleName() + "构建");
    }

    @Before
    public void before(User qq, Group group, @AllMess String str) throws NoRunException {
        if (!opened(group.getId(), this.getClass())) {
            throw NOT_OPEN_NO_RUN_EXCEPTION;
        }
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

    @Action("升级第<.+=>str>")
    public String upda(@Param("str") String str, long q) {
        if (str.contains("魂环")) {
            str = str.replace("魂环", "").replace("第", "");
            String s1 = Tool.findNumberZh(str);
            Integer st = Integer.valueOf(Tool.chineseNumber2Int(s1));
            return service.upHh(q, st);
        }
        throw new NoRunException();
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

    @AutoStand
    GameController c0;

    @AutoStand
    ScoreController c1;

    @AutoStand
    GameJoinAcController c2;

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

    @Action("闭关")
    public Object o3(User user) throws NoSuchMethodException {
        Method m0 = this.getClass().getDeclaredMethod("bg", Long.class);
        ConfirmController.regConfirm(user.getId(), m0, this, Long.valueOf(user.getId()));
        return "确认闭关吗?\n闭关后不可做任何事,取消闭关后即可\n一小时后可取消";
    }

    private Object bg(Long q) {
        PersonInfo p0 = getInfo(q);
        if (System.currentTimeMillis() < p0.getBgk()) {
            return String.format(BG_WAIT_TIPS, getTimeTips(p0.getBgk()));
        }
        GhostObj ghostObj = getGhostObjFrom(q);
        if (ghostObj != null && ghostObj.getState() == GhostObj.HELPING) {
            if (isATrue(Long.valueOf(ghostObj.getForWhoStr()))) {
                return IN_SELECT;
            }
        }
        p0.setBg(true);
        p0.setBgk(System.currentTimeMillis() + ResourceSet.FinalValue.BG_CD);
        p0.apply();
        return "闭关完成!";
    }

    private Object unBg(Long q) {
        PersonInfo p0 = getInfo(q);
        if (System.currentTimeMillis() < p0.getBgk()) {
            return String.format(BG_WAIT_TIPS, getTimeTips(p0.getBgk()));
        }
        p0.setBg(false);
        p0.setBgk(System.currentTimeMillis() + ResourceSet.FinalValue.BG_CD);
        p0.apply();
        return "取消闭关完成";
    }

    @Action("取消闭关")
    public Object o4(User user) throws NoSuchMethodException {
        if (getInfo(user.getId()).isBg()) {
            Method m0 = this.getClass().getDeclaredMethod("unBg", Long.class);
            ConfirmController.regConfirm(user.getId(), m0, this, Long.valueOf(user.getId()));
            return "确认取消闭关吗?\n一小时后可再次闭关";
        } else {
            return "没有在闭关";
        }
    }
}
