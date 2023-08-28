package io.github.kloping.mirai;

import Project.utils.Tools.Tool;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.contact.NormalMember;

import static Project.controllers.normalController.OtherController.getPermission;

/**
 * @author github.kloping
 */
public class MiraiRunnable {

    public static Object getMemberInfo(Long gid, long qq) {
        Bot bot = Bot.getInstances().iterator().next();
        NormalMember member = bot.getGroup(gid).get(qq);
        StringBuilder sb = new StringBuilder();
        sb.append("QQ:").append(qq).append("\r\n");
        sb.append("身份:").append(getPermission(member.getPermission().getLevel())).append("\r\n");
        sb.append("群内名:").append(member.getNameCard()).append("\r\n");
        sb.append("QQ名:").append(member.getNick()).append("\r\n");
        sb.append("加入时间:").append(Tool.INSTANCE.getTimeYMdhms(member.getJoinTimestamp() * 1000L)).append("\r\n");
        sb.append("最后发言:").append(Tool.INSTANCE.getTimeYMdhms(member.getLastSpeakTimestamp() * 1000L)).append("\r\n");
        sb.append("头衔:").append(member.getSpecialTitle()).append("\r\n");
        sb.append("禁言时长:").append(member.getMuteTimeRemaining()).append("\r\n");
        sb.append("头像链接:").append(member.getAvatarUrl()).append("\r\n");
        return sb.toString();
    }

    public static boolean isAdmin(Long gid, Long qid) {
        Bot bot = Bot.getInstances().iterator().next();
        NormalMember member = bot.getGroup(gid).get(qid);
        return member.getPermission().getLevel() >= 1;
    }
}
