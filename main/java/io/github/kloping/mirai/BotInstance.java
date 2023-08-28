package io.github.kloping.mirai;

import io.github.kloping.BotInterface;
import net.mamoe.mirai.Bot;

/**
 * @author github.kloping
 */
public class BotInstance implements BotInterface {
    public static BotInterface instance;
    private Bot bot;

    public BotInstance(Bot bot) {
        this.bot = bot;
        instance = this;
    }

    public static BotInterface getInstance() {
        return instance;
    }

    public Bot getBot() {
        return bot;
    }

    @Override
    public Long getBotId() {
        return bot.getId();
    }
}
