package io.github.kloping.kzero.bot.controllers;


import io.github.kloping.MySpringTool.annotations.Action;
import io.github.kloping.MySpringTool.annotations.AllMess;
import io.github.kloping.MySpringTool.annotations.AutoStand;
import io.github.kloping.MySpringTool.annotations.Controller;
import io.github.kloping.judge.Judge;
import io.github.kloping.kzero.bot.database.DataBase;
import io.github.kloping.kzero.bot.services.ScoreService;
import io.github.kloping.kzero.rt.ResourceSet;
import io.github.kloping.kzero.utils.Utils;
import io.github.kloping.number.NumberUtils;


/**
 * @author github-kloping
 */
@Controller
public class ScoreController {
    @AutoStand
    ScoreService scoreService;

    @Action(value = "积分查询", otherName = {"查询积分"})
    public String selectScore(String sid) {
        return scoreService.selectInfo(sid);
    }

    @Action("取积分.+")
    public String getScore(String sid, @AllMess String str) {
        Integer sc = NumberUtils.getIntegerFromString(str, 1);
        return scoreService.getScore(sid, sc);
    }

    @Action("存积分.+")
    public String putScore(String sid, @AllMess String str) {
        Integer sc = NumberUtils.getIntegerFromString(str, 1);
        return scoreService.putScore(sid, sc);
    }

    @AutoStand
    DataBase dataBase;

    @Action(value = "积分转让.+", otherName = {"转让积分.+"})
    public String transfer(String sid, @AllMess String str) {
        Long num = null;
        String tid = Utils.getAtFromString(str);
        if (Judge.isEmpty(tid)) return ResourceSet.FinalString.NOT_FOUND_AT;
        if (!dataBase.exists(sid)) return ResourceSet.FinalString.PLAYER_NOT_REGISTERED;
        str = str.replaceFirst(tid, "");
        num = Long.valueOf(NumberUtils.getIntegerFromString(str, 0));
        return scoreService.getScoreTo(sid, tid, num);
    }

    @Action("积分排行.*?")
    public String scorePh(@AllMess String s) {
        Integer s0 = NumberUtils.getIntegerFromString(s);
        s0 = s0 == null ? 10 : s0;
        s0 = s0 > 20 ? 20 : s0;
        return scoreService.scorePh(s0);
    }
}
