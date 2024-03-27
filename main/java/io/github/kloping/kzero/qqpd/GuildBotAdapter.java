package io.github.kloping.kzero.qqpd;

import io.github.kloping.judge.Judge;
import io.github.kloping.kzero.main.ResourceSet;
import io.github.kloping.kzero.main.api.KZeroBotAdapter;
import io.github.kloping.kzero.main.api.MessagePack;
import io.github.kloping.kzero.main.api.MessageType;
import io.github.kloping.kzero.utils.Utils;
import io.github.kloping.qqbot.api.SendAble;
import io.github.kloping.qqbot.api.SenderAndCidMidGetter;
import io.github.kloping.qqbot.api.message.MessageEvent;
import io.github.kloping.qqbot.entities.Bot;
import io.github.kloping.qqbot.entities.ex.MessageAsyncBuilder;
import io.github.kloping.qqbot.entities.qqpd.Guild;
import io.github.kloping.qqbot.entities.qqpd.Member;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author github.kloping
 */
public class GuildBotAdapter implements KZeroBotAdapter {
    private Bot bot;
    private GuildSerializer serializer;

    public GuildBotAdapter(Bot bot, GuildSerializer serializer) {
        this.bot = bot;
        this.serializer = serializer;
    }

    @Override
    public boolean sendMessage(MessageType type, String targetId, Object msg) {
        SenderAndCidMidGetter sender = null;
        if (type == MessageType.GROUP) {
            for (Guild guild : bot.guilds()) {
                if (guild.channelMap().containsKey(targetId)) {
                    sender = guild.channelMap().get(targetId);
                    break;
                }
            }
        } else if (type == MessageType.FRIEND) {
            for (Guild guild : bot.guilds()) {
                Member member = guild.getMember(targetId);
                if (member != null) sender = guild.create(targetId);
            }
        }
        if (sender == null) return false;
        MessageAsyncBuilder builder = new MessageAsyncBuilder();
        for (SendAble sendAble : serializer.ARR_DE_SERIALIZER.deserializer(msg.toString())) {
            if (sendAble != null) builder.append(sendAble);
        }
        SendAble sendAble = builder.build();
        return sender.send(sendAble) != null;
    }

    @Override
    public void sendMessageByForward(MessageType type, String targetId, Object... objects) {
        if (objects != null) {
            for (Object msg : objects) {
                MessageAsyncBuilder builder = new MessageAsyncBuilder();
                for (SendAble sendAble : serializer.ARR_DE_SERIALIZER.deserializer(msg.toString())) {
                    if (sendAble != null) builder.append(sendAble);
                }
                SendAble sendAble = builder.build();
                for (Guild guild : bot.guilds()) {
                    if (guild.channelMap().containsKey(targetId)) {
                        sendAble.send(guild.channelMap().get(targetId));
                    }
                }
            }
        }
    }

    @Override
    public void onResult(Method method, Object data, MessagePack pack) {
        if (data != null && Judge.isNotEmpty(data.toString())) {
            MessageEvent event = (MessageEvent) pack.getRaw();
            if (data.getClass().isArray()) {
                String targetId = pack.getSubjectId();
                Object[] objects = (Object[]) data;
                for (Object msg : objects) {
                    MessageAsyncBuilder builder = new MessageAsyncBuilder();
                    for (SendAble sendAble : serializer.ARR_DE_SERIALIZER.deserializer(msg.toString())) {
                        if (sendAble != null) builder.append(sendAble);
                    }
                    event.send(builder.build());
                }
            } else if (data instanceof SendAble) {
                event.send((SendAble) data);
            } else {
                MessageAsyncBuilder builder = new MessageAsyncBuilder();
                for (SendAble sendAble : serializer.ARR_DE_SERIALIZER.deserializer(data.toString()))
                    if (sendAble != null) builder.append(sendAble);
                event.send(builder.build());
            }
        }
    }

    @Override
    public String getAvatarUrl(String sid) {
        String icon = KZeroBotAdapter.super.getAvatarUrl(sid);
        if (Judge.isNotEmpty(icon)) return icon;
        if (Utils.isAllNumber(sid)) {
            for (Guild guild : bot.guilds()) {
                Member member = guild.getMember(sid);
                if (member != null) {
                    return member.getUser().getAvatar();
                }
            }
            icon = String.format("http://q1.qlogo.cn/g?b=qq&nk=%s&s=640", sid);
            ResourceSet.ICON_TEMP_MAP.put(sid, icon);
            return icon;
        }
        return DEFAULT_ICON;
    }

    public static final String DEFAULT_ICON = "http://kloping.top/icon.jpg";
    public static final String DEFAULT_NAME = "默认昵称";

    @Override
    public String getNameCard(String sid) {
        String nick = KZeroBotAdapter.super.getNameCard(sid);
        if (nick != null) return nick;
        if (Utils.isAllNumber(sid)) {
            for (Guild guild : bot.guilds()) {
                Member member = guild.getMember(sid);
                if (member != null) {
                    return member.getUser().getUsername();
                }
            }
        }
        return DEFAULT_NAME;
    }

    @Override
    public String getNameCard(String sid, String tid) {
        String nick = KZeroBotAdapter.super.getNameCard(sid);
        if (nick != null) return nick;
        if (Utils.isAllNumber(sid)) {
            for (Guild guild : bot.guilds()) {
                if (guild.channelMap().containsKey(tid)) {
                    Member member = guild.getMember(sid);
                    if (member != null) {
                        return member.getUser().getUsername();
                    }
                }
            }
        }
        return DEFAULT_NAME;
    }

    @Override
    public List<String> getMembers(String tid) {
        if (Utils.isAllNumber(tid)) {
            List<String> cs = new LinkedList<>();
            for (Guild guild : bot.guilds()) {
                if (guild.channelMap().containsKey(tid)) {

                }
            }
            return cs;
        }
        return new ArrayList<>();
    }
}
