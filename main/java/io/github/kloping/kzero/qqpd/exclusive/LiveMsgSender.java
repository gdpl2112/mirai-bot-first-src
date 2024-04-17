package io.github.kloping.kzero.qqpd.exclusive;

import io.github.kloping.kzero.main.KlopZeroMainThreads;
import io.github.kloping.kzero.main.api.KZeroBot;
import io.github.kloping.kzero.main.api.MessageType;
import io.github.kloping.qqbot.api.message.MessageChannelReceiveEvent;
import io.github.kloping.qqbot.impl.ListenerHost;

/**
 * @author github.kloping
 */
public class LiveMsgSender extends ListenerHost {
    public static LiveMsgSender INSTANCE = new LiveMsgSender();

    @EventReceiver
    public void onMsg(MessageChannelReceiveEvent event) {
        if (event.getChannel().getType() == 10005) {
            KZeroBot bot = KlopZeroMainThreads.BOT_MAP.get("291841860");
            if (bot != null) {
                String msg = String.format("'%s'在'%s'说: %s", event.getSender().getNick(), event.getChannel().getName(), event.getMessage().toString());
                bot.getAdapter().sendMessage(MessageType.GROUP, "278681553", msg);
            }
        }
    }
}
