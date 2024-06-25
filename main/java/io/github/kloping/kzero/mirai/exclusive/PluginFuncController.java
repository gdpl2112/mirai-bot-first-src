package io.github.kloping.kzero.mirai.exclusive;

import io.github.kloping.spt.annotations.*;
import io.github.kloping.spt.exceptions.NoRunException;
import io.github.kloping.kzero.main.api.KZeroBot;
import io.github.kloping.kzero.main.api.MessagePack;
import io.github.kloping.kzero.utils.Utils;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.contact.MemberPermission;
import net.mamoe.mirai.contact.NormalMember;
import net.mamoe.mirai.message.data.Message;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author github.kloping
 */
@Controller
public class PluginFuncController {
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

    private Result0<Boolean> isOwner(MessagePack pack, KZeroBot<Message, Bot> bot) {
        Long gid = Long.valueOf(pack.getSubjectId());
        Long sid = Long.valueOf(pack.getSenderId());
        Group group = bot.getSelf().getGroup(gid);
        MemberPermission p0 = group.getBotAsMember().getPermission();
        if (p0 != MemberPermission.OWNER) return new Result0<>(gid, sid, group, true);
        return new Result0<>(gid, sid, group, true);
    }

    @Action("我要头衔<.+=>name>")
    public String wanner(@Param("name") String name, MessagePack pack, KZeroBot<Message, Bot> bot) {
        Result0<Boolean> result = isOwner(pack, bot);
        if (!result.data) return null;
        result.group.get(result.sid).setSpecialTitle(name);
        return "OK!";
    }

    private static final SimpleDateFormat SF_0 = new SimpleDateFormat("yyyy-MM-dd:HH");

    @Action("获取<.+=>str>")
    public Object getAllInfo(MessagePack pack, @Param("str") String str) {
        String aid = Utils.getAtFormat(str);
        if (aid == null) return null;
        Bot bot = Bot.getInstances().iterator().next();
        long gid = Long.parseLong(pack.getSubjectId());
        long qq = Long.parseLong(pack.getSenderId());
        NormalMember member = bot.getGroup(gid).get(qq);
        StringBuilder sb = new StringBuilder();
        sb.append("QQ:").append(qq).append("\r\n");
        sb.append("身份:").append(getPermission(member.getPermission().getLevel())).append("\r\n");
        sb.append("群内名:").append(member.getNameCard()).append("\r\n");
        sb.append("QQ名:").append(member.getNick()).append("\r\n");
        sb.append("加入时间:").append(SF_0.format(new Date(member.getJoinTimestamp() * 1000L))).append("\r\n");
        sb.append("最后发言:").append(SF_0.format(new Date(member.getLastSpeakTimestamp() * 1000L))).append("\r\n");
        sb.append("头衔:").append(member.getSpecialTitle()).append("\r\n");
        sb.append("禁言时长:").append(member.getMuteTimeRemaining()).append("\r\n");
        sb.append("头像链接:").append(member.getAvatarUrl()).append("\r\n");
        return sb.toString();
    }

    public String getPermission(long le) {
        if (le == 0) return "群员";
        if (le == 1) return "管理员";
        if (le == 2) return "群主";
        return "未知";
    }
}
