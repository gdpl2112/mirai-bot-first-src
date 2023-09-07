package io.github.kloping.kzero.mirai;

import io.github.kloping.MySpringTool.annotations.*;
import io.github.kloping.MySpringTool.exceptions.NoRunException;
import io.github.kloping.kzero.main.api.KZeroBot;
import io.github.kloping.kzero.main.api.MessagePack;
import io.github.kloping.kzero.spring.dao.Father;
import io.github.kloping.kzero.spring.mapper.FatherMapper;
import io.github.kloping.kzero.utils.Utils;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.contact.MemberPermission;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageSource;
import net.mamoe.mirai.message.data.QuoteReply;
import net.mamoe.mirai.message.data.SingleMessage;

/**
 * @author github.kloping
 */
@Controller
public class PluginController {
    @AutoStand
    FatherMapper fatherMapper;

    @AutoStand(id = "super_id")
    String superId;

    @Before
    public void before(@AllMess String msg, KZeroBot kZeroBot, MessagePack pack) {
        if (!(kZeroBot.getSelf() instanceof Bot)) throw new NoRunException("mirai-bot专属扩展");
        if (msg.contains("我要头衔")) return;
        else if (superId.equals(pack.getSenderId())) return;
        else {
            Father father = getFather(pack.getSenderId());
            if (father != null && father.permissionsList().contains(pack.getSubjectId())) {
            } else throw new NoRunException("无权限!");
        }
        GroupMessageEvent event = (GroupMessageEvent) pack.getRaw();
        MessageSource.recall(event.getSource());
    }

    private Father getFather(String sid) {
        return getFather(sid, false);
    }

    private Father getFather(String sid, boolean compulsion) {
        Father father = fatherMapper.selectById(sid);
        if (father == null && compulsion) {
            father = new Father();
            father.setSid(sid);
            fatherMapper.insert(father);
        }
        return father;
    }

    private class Result0<T> {
        private Long gid;
        private Long sid;
        private Group group;
        private T data;

        public Result0(Long gid, Long sid, Group group, T data) {
            this.gid = gid;
            this.sid = sid;
            this.group = group;
            this.data = data;
        }
    }

    private Result0<Boolean> isOwner(MessagePack pack, KZeroBot<MessageChain, Bot> bot) {
        Long gid = Long.valueOf(pack.getSubjectId());
        Long sid = Long.valueOf(pack.getSenderId());
        Group group = bot.getSelf().getGroup(gid);
        MemberPermission p0 = group.getBotAsMember().getPermission();
        if (p0 != MemberPermission.OWNER) return new Result0<>(gid, sid, group, true);
        return new Result0<>(gid, sid, group, true);
    }

    @Action("我要头衔<.+=>name>")
    public String wanner(@Param("name") String name, MessagePack pack, KZeroBot<MessageChain, Bot> bot) {
        Result0<Boolean> result = isOwner(pack, bot);
        if (!result.data) return null;
        result.group.get(result.sid).setSpecialTitle(name);
        return "OK!";
    }

    @Action("setAdmin.+")
    public String setAdmin(@AllMess String msg, MessagePack pack, KZeroBot<MessageChain, Bot> bot) {
        Result0<Boolean> result = isOwner(pack, bot);
        if (!result.data) return null;
        String aid = Utils.getAtFromString(msg);
        if (aid == null) return null;
        result.group.get(Long.valueOf(aid)).modifyAdmin(true);
        return "OK!";
    }

    @Action("unAdmin.+")
    public String unAdmin(@AllMess String msg, MessagePack pack, KZeroBot<MessageChain, Bot> bot) {
        Result0<Boolean> result = isOwner(pack, bot);
        if (!result.data) return null;
        String aid = Utils.getAtFromString(msg);
        if (aid == null) return null;
        result.group.get(Long.valueOf(aid)).modifyAdmin(false);
        return "OK!";
    }

    @Action("setTitle<.+=>name>")
    public String setTitle(@Param("name") String name, MessagePack pack, KZeroBot<MessageChain, Bot> bot) {
        Result0<Boolean> result = isOwner(pack, bot);
        if (!result.data) return null;
        String aid = Utils.getAtFromString(name);
        if (aid == null) return null;
        name = name.replace("<at:" + aid + ">", "");
        result.group.get(Long.valueOf(aid)).setSpecialTitle(name.trim());
        return "OK!";
    }

    @Action("setName<.+=>name>")
    public String setName(@Param("name") String name, MessagePack pack, KZeroBot<MessageChain, Bot> bot) {
        Result0<Boolean> result = isOwner(pack, bot);
        if (!result.data) return null;
        String aid = Utils.getAtFromString(name);
        if (aid == null) return null;
        name = name.replace("<at:" + aid + ">", "");
        result.group.get(Long.valueOf(aid)).setNameCard(name.trim());
        return "OK!";
    }

    @Action("recall")
    public String setName(MessagePack pack, KZeroBot<MessageChain, Bot> bot) {
        Result0<Boolean> result = isOwner(pack, bot);
        if (!result.data) return null;
        GroupMessageEvent event = (GroupMessageEvent) pack.getRaw();
        for (SingleMessage singleMessage : event.getMessage()) {
            if (singleMessage instanceof QuoteReply) {
                QuoteReply qr = (QuoteReply) singleMessage;
                MessageSource.recall(qr.getSource());
            }
        }
        return null;
    }

    @Action("addAdmin.+")
    public String addAdmin(@AllMess String msg, MessagePack pack, KZeroBot<MessageChain, Bot> bot) {
        if (!superId.equals(pack.getSenderId())) return null;
        Result0<Boolean> result = isOwner(pack, bot);
        if (!result.data) return null;
        String aid = Utils.getAtFromString(msg);
        if (aid == null) return null;
        return "state: " + fatherMapper.updateById(getFather(aid, true).addPermission(pack.getSubjectId()));
    }

    @Action("rmAdmin.+")
    public String removeAdmin(@AllMess String msg, MessagePack pack, KZeroBot<MessageChain, Bot> bot) {
        if (!superId.equals(pack.getSenderId())) return null;
        Result0<Boolean> result = isOwner(pack, bot);
        if (!result.data) return null;
        String aid = Utils.getAtFromString(msg);
        if (aid == null) return null;
        return "state: " + fatherMapper.updateById(getFather(aid, true).removePermission(pack.getSubjectId()));
    }
}
