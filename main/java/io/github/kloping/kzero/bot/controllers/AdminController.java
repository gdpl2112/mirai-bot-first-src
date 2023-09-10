package io.github.kloping.kzero.bot.controllers;

import io.github.kloping.MySpringTool.annotations.*;
import io.github.kloping.MySpringTool.exceptions.NoRunException;
import io.github.kloping.kzero.bot.database.DataBase;
import io.github.kloping.kzero.main.api.KZeroBot;
import io.github.kloping.kzero.main.api.MessagePack;
import io.github.kloping.kzero.spring.dao.Father;
import io.github.kloping.kzero.spring.dao.GroupConf;
import io.github.kloping.kzero.spring.mapper.FatherMapper;
import io.github.kloping.kzero.spring.mapper.GroupConfMapper;

/**
 * @author github.kloping
 */
@Controller
public class AdminController {
    @AutoStand
    GroupConfMapper groupConfMapper;

    @AutoStand
    DataBase dataBase;

    @AutoStand
    FatherMapper fatherMapper;

    @AutoStand(id = "super_id")
    String superId;

    @Before
    public void before(@AllMess String msg, KZeroBot kZeroBot, MessagePack pack) {
        if (superId.equals(pack.getSenderId())) return;
        Father father = dataBase.getFather(pack.getSenderId());
        if (father != null && father.permissionsList().contains(pack.getSubjectId())) {
            return;
        } else throw new NoRunException("无权限!");
    }

    @Action("开启")
    public String open(MessagePack pack) {
        GroupConf groupConf = dataBase.getConf(pack.getSubjectId());
        groupConf.setOpen(true);
        groupConfMapper.updateById(groupConf);
        return "OK!";
    }

    @Action("关闭")
    public String close(MessagePack pack) {
        GroupConf groupConf = dataBase.getConf(pack.getSubjectId());
        groupConf.setOpen(false);
        groupConfMapper.updateById(groupConf);
        return "OK!";
    }
}
