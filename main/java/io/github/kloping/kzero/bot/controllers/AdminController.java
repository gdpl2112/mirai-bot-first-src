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
import io.github.kloping.kzero.utils.Utils;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.message.data.MessageChain;

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
        if (father != null && father.hasPerm(pack.getSubjectId())) return;
        throw new NoRunException("无权限!");
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

    @Action("开启<.+=>id>")
    public String open(@Param("id") String id, MessagePack pack) {
        GroupConf groupConf = dataBase.getConf(id);
        groupConf.setOpen(true);
        groupConfMapper.updateById(groupConf);
        return "OK!";
    }

    @Action("关闭<.+=>id>")
    public String close(@Param("id") String id, MessagePack pack) {
        GroupConf groupConf = dataBase.getConf(id);
        groupConf.setOpen(false);
        groupConfMapper.updateById(groupConf);
        return "OK!";
    }

    @Action("addAdmin.+")
    public String addAdmin(@AllMess String msg, MessagePack pack, KZeroBot<MessageChain, Bot> bot) {
        if (!superId.equals(pack.getSenderId())) return null;
        String aid = Utils.getAtFormat(msg);
        if (aid == null) aid = msg;
        return "state: " + fatherMapper.updateById(dataBase.getFather(aid, true).addPermission(pack.getSubjectId()));
    }

    @Action("addSuperAdmin.+")
    public String addSuperAdmin(@AllMess String msg, MessagePack pack, KZeroBot<MessageChain, Bot> bot) {
        if (!superId.equals(pack.getSenderId())) return null;
        String aid = Utils.getAtFormat(msg);
        if (aid == null) aid = msg;
        return "state: " + fatherMapper.updateById(dataBase.getFather(aid, true).addPermission(Father.SUPER_PER));
    }

    @Action("rmAdmin.+")
    public String removeAdmin(@AllMess String msg, MessagePack pack, KZeroBot<MessageChain, Bot> bot) {
        if (!superId.equals(pack.getSenderId())) return null;
        String aid = Utils.getAtFormat(msg);
        if (aid == null) aid = msg;
        return "state: " + fatherMapper.updateById(dataBase.getFather(aid, true).removePermission(pack.getSubjectId()));
    }
}
