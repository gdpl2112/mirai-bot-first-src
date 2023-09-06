package io.github.kloping.kzero.mirai;

import io.github.kloping.judge.Judge;
import io.github.kloping.kzero.main.api.KZeroBotAdapter;
import io.github.kloping.kzero.main.api.MessagePack;
import io.github.kloping.kzero.main.api.MessageType;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.Message;

import java.lang.reflect.Method;

/**
 * @author github.kloping
 */
public class MiraiBotAdapter implements KZeroBotAdapter {
    private Bot bot;

    private MiraiSerializer serializer;

    public MiraiBotAdapter(Bot bot, MiraiSerializer serializer) {
        this.bot = bot;
        this.serializer = serializer;
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

    @Override
    public void onResult(Method method, Object data, MessagePack pack) {
        if (data != null && Judge.isNotEmpty(data.toString())) {
            Message msg = serializer.deserialize(data.toString());
            long sid = Long.parseLong(pack.getSenderId());
            msg = new At(sid).plus("\n").plus(msg);
            if (pack.getType() == MessageType.GROUP) {
                long gid = Long.parseLong(pack.getSubjectId());
                bot.getGroup(gid).sendMessage(msg);
            } else if (pack.getType() == MessageType.FRIEND) {
                bot.getFriend(sid).sendMessage(msg);
            }
        }
    }
}
