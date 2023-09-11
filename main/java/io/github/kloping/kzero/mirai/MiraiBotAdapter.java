package io.github.kloping.kzero.mirai;

import io.github.kloping.judge.Judge;
import io.github.kloping.kzero.main.api.KZeroBotAdapter;
import io.github.kloping.kzero.main.api.MessagePack;
import io.github.kloping.kzero.main.api.MessageType;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.contact.Friend;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.contact.NormalMember;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.ForwardMessageBuilder;
import net.mamoe.mirai.message.data.Message;
import net.mamoe.mirai.message.data.QuoteReply;

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
        long tid = Long.parseLong(targetId);
        if (type == MessageType.GROUP) {
            if (!bot.getGroups().contains(tid)) return;
            if (obj instanceof Message) {
                Message msg = (Message) obj;
                bot.getGroup(tid).sendMessage(msg);
            } else if (obj instanceof String) {
                Message msg = serializer.deserialize(obj.toString());
                bot.getGroup(tid).sendMessage(msg);
            }
        }
    }

    @Override
    public void sendMessageByForward(MessageType type, String targetId, Object... objects) {
        if (type == MessageType.GROUP) {
            Long tid = Long.valueOf(targetId);
            ForwardMessageBuilder builder = new ForwardMessageBuilder(bot.getAsFriend());
            for (Object obj : objects) {
                if (obj instanceof Message) {
                    Message msg = (Message) obj;
                    builder.add(bot.getAsFriend(), msg);
                } else if (obj instanceof String) {
                    Message msg = serializer.deserialize(obj.toString());
                    builder.add(bot.getAsFriend(), msg);
                }
            }
            bot.getGroup(tid).sendMessage(builder.build());
        }
    }

    @Override
    public void onResult(Method method, Object data, MessagePack pack) {
        if (data != null && Judge.isNotEmpty(data.toString())) {
            Message msg = serializer.deserialize(data.toString());
            long sid = Long.parseLong(pack.getSenderId());
            MessageEvent event = (MessageEvent) pack.getRaw();
            msg = new QuoteReply(((MessageEvent) pack.getRaw()).getSource()).plus(msg);
            event.getSubject().sendMessage(msg);
        }
    }

    @Override
    public String getAvatarUrl(String sid) {
        return String.format("http://q.qlogo.cn/g?b=qq&nk=%s&s=640", sid);
    }

    @Override
    public String getNameCard(String sid) {
        Long qid = Long.parseLong(sid);
        String nameCard = "";
        for (Group group : bot.getGroups()) {
            if (group.contains(qid)) {
                NormalMember normalMember = group.get(qid);
                nameCard = normalMember.getNick();
            }
        }
        if (Judge.isEmpty(nameCard)) {
            Friend friend = bot.getFriend(qid);
            if (friend != null) nameCard = friend.getNick();
        }
        return Judge.isEmpty(nameCard) ? sid : nameCard;
    }

    @Override
    public String getNameCard(String sid, String tid) {
        Long qid = Long.parseLong(sid);
        Long gid = Long.parseLong(tid);
        String nameCard = "";
        Group group = (bot.getGroup(gid));
        NormalMember normalMember = group.get(qid);
        nameCard = normalMember.getNameCard();
        if (nameCard.isEmpty()) nameCard = normalMember.getNick();
        if (Judge.isEmpty(nameCard)) {
            Friend friend = bot.getFriend(qid);
            if (friend != null) nameCard = friend.getNick();
        }
        return Judge.isEmpty(nameCard) ? sid : nameCard;
    }

}
