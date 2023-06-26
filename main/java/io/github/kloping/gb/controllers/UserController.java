package io.github.kloping.gb.controllers;

import io.github.kloping.MySpringTool.annotations.Action;
import io.github.kloping.MySpringTool.annotations.AutoStand;
import io.github.kloping.MySpringTool.annotations.Controller;
import io.github.kloping.gb.services.UserService;
import io.github.kloping.gb.spring.dao.UserScore;

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
}
