package io.github.kloping.kzero.guilds;

import io.github.kloping.judge.Judge;
import io.github.kloping.kzero.main.api.KZeroBotAdapter;
import io.github.kloping.kzero.main.api.MessagePack;
import io.github.kloping.kzero.main.api.MessageType;
import io.github.kloping.qqbot.api.SendAble;
import io.github.kloping.qqbot.api.message.MessageEvent;
import io.github.kloping.qqbot.entities.Bot;
import io.github.kloping.qqbot.entities.ex.MessageAsyncBuilder;
import io.github.kloping.qqbot.entities.qqpd.Channel;
import io.github.kloping.qqbot.entities.qqpd.Guild;
import io.github.kloping.qqbot.entities.qqpd.Member;

import java.lang.reflect.Method;

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
    public void sendMessage(MessageType type, String targetId, Object msg) {
        Channel channel = null;
        for (Guild guild : bot.guilds()) {
            if (guild.channelMap().containsKey(targetId)) {
                channel = guild.channelMap().get(targetId);
            }
        }
        if (channel == null) return;
        MessageAsyncBuilder builder = new MessageAsyncBuilder();
        for (SendAble sendAble : serializer.ARR_DE_SERIALIZER.deserializer(msg.toString())) {
            if (sendAble != null) builder.append(sendAble);
        }
        SendAble sendAble = builder.build();
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
            } else {
                MessageAsyncBuilder builder = new MessageAsyncBuilder();
                for (SendAble sendAble : serializer.ARR_DE_SERIALIZER.deserializer(data.toString()))
                    if (sendAble != null) builder.append(sendAble);
                builder.reply(event.getRawMessage());
                event.send(builder.build());
            }
        }
    }

    @Override
    public String getAvatarUrl(String sid) {
        for (Guild guild : bot.guilds()) {
            Member member = guild.getMember(sid);
            if (member != null) {
                return member.getUser().getAvatar();
            }
        }
        return null;
    }

    @Override
    public String getNameCard(String sid) {
        for (Guild guild : bot.guilds()) {
            Member member = guild.getMember(sid);
            if (member != null) {
                return member.getUser().getUsername();
            }
        }
        return null;
    }

    @Override
    public String getNameCard(String sid, String tid) {
        for (Guild guild : bot.guilds()) {
            if (guild.channelMap().containsKey(tid)) {
                Member member = guild.getMember(sid);
                if (member != null) {
                    return member.getUser().getUsername();
                }
            }
        }
        return null;
    }
}
