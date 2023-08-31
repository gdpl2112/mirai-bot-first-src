package io.github.kzero.mirai;

import io.github.kzero.main.api.KZeroBotAdapter;
import io.github.kzero.main.api.MessageType;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.message.data.Message;

/**
 * @author github.kloping
 */
public class MiraiBotAdapter implements KZeroBotAdapter {
    private Bot bot;

    public MiraiBotAdapter(Bot bot) {
        this.bot = bot;
    }

    @Override
    public void sendMessage(MessageType type, String targetId, Object obj) {
        if (type == MessageType.GROUP) {
            if (obj instanceof Message) {
                Message msg = (Message) obj;
                Long tid = Long.valueOf(targetId);
                bot.getGroup(tid).sendMessage(msg);
            }
        }
    }

    @Override
    public void sendMessageByForward(MessageType type, String targetId, Object... objects) {

    }
}
