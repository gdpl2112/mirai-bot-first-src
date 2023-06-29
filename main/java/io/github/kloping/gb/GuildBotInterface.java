package io.github.kloping.gb;


import io.github.kloping.qqbot.entities.Bot;
import io.github.kloping.qqbot.entities.ex.At;
import io.github.kloping.qqbot.entities.ex.MessageBuilder;
import io.github.kloping.qqbot.entities.ex.MessagePre;
import io.github.kloping.qqbot.entities.qqpd.Channel;
import io.github.kloping.qqbot.entities.qqpd.Guild;
import io.github.kloping.qqbot.entities.qqpd.message.RawMessage;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author github.kloping
 */
public class GuildBotInterface implements BotInterface {
    private Bot bot;

    private Map<String, Channel> id2channel = new HashMap<>();

    public GuildBotInterface(Bot bot) {
        this.bot = bot;
    }

    @Override
    public String getBotId() {
        return bot.getId();
    }

    @Override
    public int onReturn(MessageContext context, String command, Object data) {
        if (data instanceof String) {
            getSender(context).sendEnvReply(data.toString());
        }
        return 0;
    }

    @NotNull
    @Override
    public Sender getSender(@NotNull MessageContext context) {
        return new Sender() {
            @Override
            public void sendEnv(@NotNull List<? extends MessageData> data) {

            }

            @Override
            public void sendEnv(@NotNull String text) {
                GuildBotInterface.this.sendEnv(context.getGid(), new MessageBuilder().append(text).build());
            }

            @Override
            public void sendEnvWithAt(@NotNull String text) {
                MessageBuilder builder = new MessageBuilder();
                builder.append(new At(At.MEMBER_TYPE, context.getSid()));
                builder.append("\n").append(text);
                GuildBotInterface.this.sendEnv(context.getGid(), builder.build());
            }

            @Override
            public void sendEnvReply(@NotNull String text) {
                MessageBuilder builder = new MessageBuilder();
                builder.append(text);
                builder.reply((RawMessage) context.getData());
                GuildBotInterface.this.sendEnv(context.getGid(), builder.build());
            }

            @Override
            public void sendEnvReplyWithAt(@NotNull String text) {
                MessageBuilder builder = new MessageBuilder();
                builder.append(new At(At.MEMBER_TYPE, context.getSid()));
                builder.append("\n").append(text).reply((RawMessage) context.getData());
                GuildBotInterface.this.sendEnv(context.getGid(), builder.build());
            }
        };
    }

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
}
