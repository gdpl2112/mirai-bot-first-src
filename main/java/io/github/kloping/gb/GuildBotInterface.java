package io.github.kloping.gb;


import io.github.kloping.qqbot.entities.Bot;
import io.github.kloping.qqbot.entities.ex.At;
import io.github.kloping.qqbot.entities.ex.MessageBuilder;
import io.github.kloping.qqbot.entities.ex.MessagePre;
import io.github.kloping.qqbot.entities.qqpd.Channel;
import io.github.kloping.qqbot.entities.qqpd.Guild;
import io.github.kloping.qqbot.entities.qqpd.message.RawMessage;

import java.util.HashMap;
import java.util.Map;

/**
 * @author github.kloping
 */
public class GuildBotInterface implements BotInterface {
    private Bot bot;

    public GuildBotInterface(Bot bot) {
        this.bot = bot;
    }

    @Override
    public String getBotId() {
        return bot.getId();
    }

    private Map<String, Channel> id2channel = new HashMap<>();

    public void sendEnv(String gid, MessagePre pre) {
        if (!id2channel.containsKey(gid)) {
            for (Guild guild : bot.guilds()) {
                for (Channel channel : guild.channels()) {
                    id2channel.put(channel.getId(), channel);
                }
            }
        }
        Channel channel = id2channel.get(gid);
        pre.send(channel);
    }

    @Override
    public void sendEnvReply(String gid, String text, MessageContext context) {
        MessageBuilder builder = new MessageBuilder();
        builder.append(text);
        builder.reply((RawMessage) context.getData());
        sendEnv(gid, builder.build());
    }

    @Override
    public void sendEnvReplyWithAt(String gid, String text, MessageContext context) {
        MessageBuilder builder = new MessageBuilder();
        builder.append(new At(At.MEMBER_TYPE, context.getSid()));
        builder.append("\n").append(text).reply((RawMessage) context.getData());
        sendEnv(gid, builder.build());
    }

    @Override
    public void sendEnv(String gid, String text) {
        sendEnv(gid, new MessageBuilder().append(text).build());
    }

    @Override
    public void sendEnvWithAt(String gid, String text, MessageContext context) {
        MessageBuilder builder = new MessageBuilder();
        builder.append(new At(At.MEMBER_TYPE, context.getSid()));
        builder.append("\n").append(text);
        sendEnv(gid, builder.build());
    }
}
