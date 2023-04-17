package io.github.kloping.mirai;

import Project.aSpring.SpringBootResource;
import Project.listeners.SaveHandler;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import io.github.kloping.mirai0.Main.iutils.EventUtils;
import io.github.kloping.mirai0.unitls.Tools.Tool;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.contact.NormalMember;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageSource;
import net.mamoe.mirai.message.data.MessageSourceBuilder;
import net.mamoe.mirai.message.data.MessageSourceKind;

import java.util.List;

import static Project.commons.resouce_and_tool.ResourceSet.FinalString.RECALL_FAIL;
import static Project.controllers.normalController.OtherController.getPermission;

/**
 * @author github.kloping
 */
public class MiraiRunnable {
    public static String recallMessage(String tips, List<SaveHandler.AllMessage> messages) {
        for (SaveHandler.AllMessage message : messages) {
            try {
                MessageSourceBuilder builder = new MessageSourceBuilder();
                builder.setInternalIds(new int[]{message.getInternalId()});
                builder.setIds(new int[]{message.getId()});
                builder.setFromId(message.getSenderId());
                builder.setTime(message.getIntTime());
                builder.setTargetId(message.getFromId());
                MessageSource source = builder.build(message.getBotId(), MessageSourceKind.GROUP);
                MessageSource.recall(source);
                UpdateWrapper<SaveHandler.AllMessage> updateWrapper = new UpdateWrapper<>();
                updateWrapper.eq("internal_id", message.getInternalId());
                message.setRecalled(1);
                SpringBootResource.getSaveMapper().update(message, updateWrapper);
            } catch (Exception e) {
                e.printStackTrace();
                tips = RECALL_FAIL;
            }
        }
        return tips;
    }

    public static Object getMemberInfo(Long id, long qq) {
        Bot bot = Bot.getInstances().iterator().next();
        NormalMember member = bot.getGroup(id).get(qq);
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

    public static String miraiJsonToText(String s0) {
        return EventUtils.messageChain2String(MessageChain.deserializeFromJsonString(s0));
    }
}
