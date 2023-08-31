package io.github.kzero.main.api;

/**
 * @author github.kloping
 */
public interface KZeroStater extends Runnable {
    void setCreated(BotCreated listener);

    void setHandler(BotMessageHandler handler);
}
