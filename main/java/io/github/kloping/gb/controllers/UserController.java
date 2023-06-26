package io.github.kloping.gb.controllers;

import io.github.kloping.MySpringTool.annotations.*;
import io.github.kloping.gb.DataAt;
import io.github.kloping.gb.MessageContext;
import io.github.kloping.gb.services.UserService;
import io.github.kloping.gb.spring.dao.UserScore;
import io.github.kloping.number.NumberUtils;

/**
 * @author github.kloping
 */
@Controller
public class UserController {

    @AutoStand
    UserService service;

    @Action("积分查询")
    public String select(String id) {
        UserScore score = service.getUserScore(id, true);
        return String.format("当前积分: %s\n存储积分: %s\n打劫次数: %s", score.getScore(), score.getSScore(), score.getFz());
    }

    @Action("取积分<\\d{1,}=>str>")
    public String getScore(String id, @Param("str") String str) {
        long score = 0L;
        try {
            score = Long.valueOf(str);
        } catch (Exception e) {
            score = 0;
        }
        return service.getScore(id, score);
    }

    @Action("存积分<\\d{1,}=>str>")
    public String putScore(String id, @Param("str") String str) {
        long score = 0L;
        try {
            score = Long.valueOf(str);
        } catch (Exception e) {
            score = 0;
        }
        return service.putScore(id, score);
    }

    @Action(value = "积分转让.+", otherName = {"转让积分.+"})
    public String transfer(String qid, @AllMess String str, MessageContext context) {
        DataAt at = context.getAt();
        if (at == null) return null;
        Long m0 = null;
        try {
            m0 = Long.valueOf(NumberUtils.findNumberFromString(str.replace(at.getId(), "")));
        } catch (NumberFormatException e) {
            m0 = 0L;
        }
        return service.transfer(qid, m0, at.getId());
    }
}
