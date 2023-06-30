package io.github.kloping.gb;

import net.mamoe.mirai.Bot;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.contact.Friend;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.contact.Member;
import net.mamoe.mirai.message.data.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @author github.kloping
 */
public class MiraiBotInterface implements BotInterface{
    private Bot bot;

    public MiraiBotInterface(Bot bot) {
        this.bot = bot;
    }

    @Override
    public String getBotId() {
        return String.valueOf(bot.getId());
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

    @Nullable
    @Override
    public Sender getSender(MessageContext context) {
        return new Sender() {
            @Override
            public void sendEnv(@NotNull List<? extends MessageData> data) {
                MessageChainBuilder builder = new MessageChainBuilder();
                for (MessageData datum : data) {
                    try {
                        if (datum instanceof DataText) {
                            DataText text = (DataText) datum;
                            builder.append(text.getText());
                        } else if (datum instanceof DataImage) {
                            DataImage dataImage = (DataImage) datum;
                            Image image = null;
                            if (dataImage.getStream() != null) {
                                image = Contact.uploadImage(bot.getAsFriend(), dataImage.getStream());
                            } else if (dataImage.getUrl() != null) {
                                image = Contact.uploadImage(bot.getAsFriend(), new URL(dataImage.getUrl()).openStream());
                            }
                            builder.append(image);
                        } else if (datum instanceof DataAt) {
                            DataAt daa = (DataAt) datum;
                            builder.append(new At(Long.valueOf(daa.getId())));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                bot.getGroup(Long.valueOf(context.getGid())).sendMessage(builder.build());
            }

            @Override
            public void sendEnv(@NotNull String text) {
                bot.getGroup(Long.valueOf(context.getGid())).sendMessage(text);
            }

            @Override
            public void sendEnvWithAt(@NotNull String text) {
                MessageChainBuilder builder = new MessageChainBuilder();
                builder.append(new At(Long.valueOf(context.getSid())));
                builder.append("\n");
                builder.append(text);
                bot.getGroup(Long.valueOf(context.getGid())).sendMessage(builder.build());
            }

            @Override
            public void sendEnvReply(@NotNull String text) {
                MessageChainBuilder builder = new MessageChainBuilder();
                QuoteReply reply = new QuoteReply((MessageChain) context.getData());
                builder.append(text).append(reply);
                bot.getGroup(Long.valueOf(context.getGid())).sendMessage(builder.build());
            }

            @Override
            public void sendEnvReplyWithAt(@NotNull String text) {
                MessageChainBuilder builder = new MessageChainBuilder();
                QuoteReply reply = new QuoteReply((MessageChain) context.getData());
                builder.append(new At(Long.valueOf(context.getSid())));
                builder.append("\n");
                builder.append(text).append(reply);
                bot.getGroup(Long.valueOf(context.getGid())).sendMessage(builder.build());
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
                Friend friend = bot.getFriend(Long.valueOf(id));
                if (friend != null) return friend.getNick();
                return "";
            }

            @NotNull
            @Override
            public String getNameFromEnv(@NotNull String id) {
                Group group = bot.getGroup(Long.valueOf(context.getGid()));
                if (group != null) {
                    Member member = group.get(Long.valueOf(id));
                    if (member != null) return member.getNameCard();
                }
                return getName(id);
            }
        };
    }
}
