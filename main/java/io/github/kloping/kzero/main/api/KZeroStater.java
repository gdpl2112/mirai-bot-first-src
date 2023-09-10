package io.github.kloping.kzero.main.api;

/**
 * @author github.kloping
 */
public interface KZeroStater extends Runnable {
    void setCreated(BotCreated listener);

    void setHandler(KZeroBot bot, BotMessageHandler handler);
}
