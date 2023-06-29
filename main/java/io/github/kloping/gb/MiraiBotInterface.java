package io.github.kloping.gb;

import net.mamoe.mirai.Bot;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.message.data.QuoteReply;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
        }
        return 0;
    }

    @Nullable
    @Override
    public Sender getSender(MessageContext context) {
        return new Sender() {
            @Override
            public void sendEnv(@NotNull List<? extends MessageData> data) {

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
}
