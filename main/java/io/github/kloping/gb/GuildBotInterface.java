package io.github.kloping.gb;


import io.github.kloping.io.ReadUtils;
import io.github.kloping.qqbot.entities.Bot;
import io.github.kloping.qqbot.entities.ex.At;
import io.github.kloping.qqbot.entities.ex.Image;
import io.github.kloping.qqbot.entities.ex.MessageBuilder;
import io.github.kloping.qqbot.entities.ex.MessagePre;
import io.github.kloping.qqbot.entities.qqpd.Channel;
import io.github.kloping.qqbot.entities.qqpd.Guild;
import io.github.kloping.qqbot.entities.qqpd.Member;
import io.github.kloping.qqbot.entities.qqpd.message.RawMessage;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
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
        } else if (data instanceof List) {
            List list = (List) data;
            if (list.get(0) instanceof MessageData) {
                List<MessageData> e = new ArrayList<>();
                e.add(new DataAt(context.getSid()));
                e.add(new DataText("\n"));
                e.addAll(list);
                getSender(context).sendEnv(e);
            }
        }
        return 0;
    }

    @NotNull
    @Override
    public Sender getSender(@NotNull MessageContext context) {
        return new Sender() {
            @Override
            public void sendEnv(@NotNull List<? extends MessageData> data) {
                MessageBuilder builder = new MessageBuilder();
                for (MessageData datum : data) {
                    try {
                        if (datum instanceof DataText) {
                            DataText text = (DataText) datum;
                            builder.append(text.getText());
                        } else if (datum instanceof DataImage) {
                            DataImage dataImage = (DataImage) datum;
                            Image image = null;
                            if (dataImage.getStream() != null) {
                                image = new Image(ReadUtils.readAll(dataImage.getStream()));
                            } else if (dataImage.getUrl() != null) {
                                image = new Image(dataImage.getUrl());
                            }
                            builder.append(image);
                        } else if (datum instanceof DataAt) {
                            DataAt daa = (DataAt) datum;
                            builder.append(new At(At.MEMBER_TYPE, daa.getId()));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                GuildBotInterface.this.sendEnv(context.getGid(), builder.build());
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

    @NotNull
    @Override
    public InfoGetter getInfoGetter(@NotNull MessageContext context) {
        return new InfoGetter() {
            @NotNull
            @Override
            public String getName(@NotNull String id) {
                for (Guild guild : bot.guilds()) {
                    Member member = guild.getMember(id);
                    if (member != null) {
                        return member.getNick();
                    }
                }
                return "";
            }

            @NotNull
            @Override
            public String getNameFromEnv(@NotNull String id) {
                Guild guild = bot.getGuild(context.getOid());
                if (guild != null) {
                    Member member = guild.getMember(id);
                    if (member != null) return member.getNick();
                }
                return getName(id);
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
