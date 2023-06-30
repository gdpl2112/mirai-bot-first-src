package io.github.kloping.gb.controllers;

import io.github.kloping.MySpringTool.annotations.*;
import io.github.kloping.MySpringTool.exceptions.NoRunException;
import io.github.kloping.date.DateUtils;
import io.github.kloping.gb.BotInterface;
import io.github.kloping.gb.DataAt;
import io.github.kloping.gb.MessageContext;
import io.github.kloping.gb.Utils;
import io.github.kloping.gb.finals.FinalStrings;
import io.github.kloping.gb.services.UserService;
import io.github.kloping.gb.spring.dao.UserScore;
import io.github.kloping.gb.spring.mapper.SingListMapper;
import io.github.kloping.gb.spring.mapper.UserScoreMapper;

import java.util.List;


/**
 * @author github.kloping
 */
@Controller
public class UserController {

    @AutoStand
    ManagerController managerController;

    @Before
    private void before(MessageContext context) throws NoRunException {
        if (!managerController.isOpen(context.getGid(), this.getClass())) throw new NoRunException();
    }

    @AutoStand
    UserService service;

    @Action("积分查询")
    public String select(String id) {
        UserScore score = service.getUserScore(id, true);
        return String.format("当前积分: %s\n存储积分: %s\n打劫次数: %s",
                score.getScore(), score.getSScore(), score.getFz());
    }

    @Action("取积分<\\d{1,}=>str>")
    public String getScore(String id, @Param("str") String str) {
        long score = Utils.getLong(str, null, 0L);
        return service.getScore(id, score);
    }

    @Action("存积分<\\d{1,}=>str>")
    public String putScore(String id, @Param("str") String str) {
        long score = Utils.getLong(str, null, 0L);
        return service.putScore(id, score);
    }

    @Action(value = "积分转让.+", otherName = {"转让积分.+"})
    public String transfer(String qid, @AllMess String str, MessageContext context) {
        DataAt at = context.getAt();
        if (at == null) return FinalStrings.NOT_FOUND_AT;
        Long m0 = Utils.getLong(str, at.getId(), 0L);
        return service.transfer(qid, m0, at.getId());
    }

    @Action(value = "我的收益", otherName = {"收益详情", "积分收益"})
    public String earnings(String id) {
        return service.earnings(id);
    }

    @Action(value = "捐款")
    public String donate(String qid) {
        return service.donate(qid);
    }

    @Action(value = "抢劫.+", otherName = {"打劫.+"})
    public String robbery(String qid, @AllMess String str, MessageContext context) {
        DataAt at = context.getAt();
        if (at == null) return FinalStrings.NOT_FOUND_AT;
        Integer c = Utils.getInteger(str, at.getId(), 1);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < c; i++) {
            sb.append(service.robbery(qid, at.getId())).append("\n");
        }
        return sb.toString().trim();
    }

    @Action("猜拳<.+=>str>")
    public String mora(String qid, @Param("str") String str) {
        return service.mora(qid, str);
    }

    @Action(value = "签到")
    public Object sign(String id) throws Exception {
        return service.sign(id);
    }


    @AutoStand
    SingListMapper singListMapper;

    @Action(value = "今日签榜", otherName = {"签榜"})
    public String todayList(MessageContext context, BotInterface botInterface) {
        List<String> list = singListMapper.selectDay(Utils.getTodayDetialString());
        int n = 1;
        StringBuilder sb = new StringBuilder();
        sb.append("今日" + DateUtils.getDay() + "号:VV\r\n");
        for (String id : list) {
            String name = botInterface.getInfoGetter(context).getNameFromEnv(context.getSid());
            name = name.isEmpty() ? id : name;
            sb.append("第").append(Utils.trans(n++)).append(":\r\n=>").append(name).append("\r\n");
        }
        return sb.toString();
    }

    @AutoStand
    UserScoreMapper userScoreMapper;

    @Action("积分排行.*?")
    public String scorePh(@AllMess String all, BotInterface bot, MessageContext context) {
        Integer s0 = Utils.getInteger(all, 10);
        s0 = s0 == null ? 10 : s0;
        s0 = s0 > 20 ? 20 : s0;
        List<UserScore> list = userScoreMapper.phScore(s0);
        StringBuilder sb = new StringBuilder();
        int na = 0;
        for (UserScore score : list) {
            ++na;
            String id = score.getId();
            String name = bot.getInfoGetter(context).getNameFromEnv(id);
            name = name == null || name.isEmpty() ? id : name;
            sb.append("第").append(na).append(": ").
                    append(name)
                    .append("=>\n\t").append(score.getScore()).append("积分\n");
        }
        return sb.toString().isEmpty() ? "暂无记录" : sb.toString().trim();
    }
}
