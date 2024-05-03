package io.github.kloping.kzero.hwxb;

import io.github.kloping.kzero.main.api.*;

/**
 * @author github.kloping
 */
public class WxHookStarter implements KZeroStater {
    public static final String ID = "wxh";

    private BotCreated botCreated;

    @Override
    public void setCreated(BotCreated listener) {
        this.botCreated = listener;
    }

    private BotMessageHandler handler;

    @Override
    public void setHandler(KZeroBot bot, BotMessageHandler handler) {
        this.handler = handler;
    }

    @Override
    public void run() {
        botCreated.created(this, new KZeroBot() {
            @Override
            public String getId() {
                return ID;
            }

            @Override
            public KZeroBotAdapter getAdapter() {
                return null;
            }

            @Override
            public MessageSerializer getSerializer() {
                return null;
            }

            @Override
            public Object getSelf() {
                return null;
            }
        });
    }
}
