package io.github.kloping.gb;

import net.mamoe.mirai.Bot;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.message.data.QuoteReply;

/**
 * @author github.kloping
 */
public class MiraiBotInterface implements BotInterface {
    private Bot bot;

    public MiraiBotInterface(Bot bot) {
        this.bot = bot;
    }

    @Override
    public String getBotId() {
        return String.valueOf(bot.getId());
    }

    @Override
    public void sendEnv(String gid, String text) {
        bot.getGroup(Long.valueOf(gid)).sendMessage(text);
    }

    @Override
    public void sendEnvReply(String gid, String text, MessageContext context) {
        MessageChainBuilder builder = new MessageChainBuilder();
        QuoteReply reply = new QuoteReply((MessageChain) context.getData());
        builder.append(text).append(reply);
        bot.getGroup(Long.valueOf(gid)).sendMessage(builder.build());
    }

    @Override
    public void sendEnvReplyWithAt(String gid, String text, MessageContext context) {
        MessageChainBuilder builder = new MessageChainBuilder();
        QuoteReply reply = new QuoteReply((MessageChain) context.getData());
        builder.add(new At(Long.valueOf(context.getSid())));
        builder.append(text).append(reply);
        bot.getGroup(Long.valueOf(gid)).sendMessage(builder.build());
    }
}
