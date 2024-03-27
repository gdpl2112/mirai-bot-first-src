package io.github.kloping.kzero.mirai;

import io.github.kloping.io.ReadUtils;
import io.github.kloping.judge.Judge;
import io.github.kloping.kzero.main.ResourceSet;
import io.github.kloping.kzero.main.api.KZeroBotAdapter;
import io.github.kloping.kzero.main.api.MessagePack;
import io.github.kloping.kzero.main.api.MessageType;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.contact.Friend;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.contact.NormalMember;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.*;

import java.io.ByteArrayInputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

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
    public boolean sendMessage(MessageType type, String targetId, Object obj) {
        if (obj == null) return false;
        long tid = Long.parseLong(targetId);
        Contact contact = null;
        if (type == MessageType.GROUP) {
            if (!bot.getGroups().contains(tid)) return false;
            contact = bot.getGroup(tid);
        } else if (type == MessageType.FRIEND) {
            contact = bot.getFriend(tid);
            if (contact == null) for (Group group : bot.getGroups()) {
                if (group.contains(tid)) {
                    contact = group.get(tid);
                    break;
                }
            }
        }
        try {
            if (obj instanceof Message) {
                Message msg = (Message) obj;
                return contact.sendMessage(msg) != null;
            } else if (obj instanceof String) {
                Message msg = serializer.deserialize(obj.toString());
                return contact.sendMessage(msg) != null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
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
                    if (obj.toString().trim().isEmpty()) continue;
                    Message msg = serializer.deserialize(obj.toString().trim());
                    builder.add(bot.getAsFriend(), msg);
                }
            }
            bot.getGroup(tid).sendMessage(builder.build());
        }
    }

    @Override
    public void onResult(Method method, Object data, MessagePack pack) {
        if (data != null && Judge.isNotEmpty(data.toString())) {
            long sid = Long.parseLong(pack.getSenderId());
            MessageEvent event = (MessageEvent) pack.getRaw();
            if (data.getClass().isArray()) {
                Object[] objects = (Object[]) data;
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
                event.getSubject().sendMessage(builder.build());
            } else {
                Message msg = serializer.deserialize(data.toString());
                if (msg instanceof MusicShare) {
                    event.getSubject().sendMessage(msg);
                } else {
                    msg = new QuoteReply(((MessageEvent) pack.getRaw()).getSource()).plus(msg);
                    event.getSubject().sendMessage(msg);
                }
            }
        }
    }

    @Override
    public String getAvatarUrl(String sid) {
        String icon = KZeroBotAdapter.super.getAvatarUrl(sid);
        if (icon == null) icon = String.format("http://q.qlogo.cn/g?b=qq&nk=%s&s=640", sid);
        ResourceSet.ICON_TEMP_MAP.put(sid, icon);
        return icon;
    }

    @Override
    public String getAvatarUrlConverted(String sid) {
        String icon = String.format("http://q.qlogo.cn/g?b=qq&nk=%s&s=640", sid);
        try {
            Image image = Contact.uploadImage(bot.getAsFriend(), new ByteArrayInputStream(ReadUtils.readAll(new URL(icon).openStream())));
            return Image.queryUrl(image);
        } catch (Exception e) {
            return icon;
        }
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
        if (Judge.isEmpty(nameCard)) {
            return sid;
        } else {
            ResourceSet.NICKNAME_TEMP_MAP.put(sid, nameCard);
            return nameCard;
        }
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
        if (Judge.isEmpty(nameCard)) {
            return sid;
        } else {
            ResourceSet.NICKNAME_TEMP_MAP.put(sid, nameCard);
            return nameCard;
        }
    }

    @Override
    public List<String> getMembers(String tid) {
        Long gid = Long.parseLong(tid);
        Group group = (bot.getGroup(gid));
        List<String> list = new ArrayList();
        for (NormalMember member : group.getMembers())
            list.add(String.valueOf(member.getId()));
        return list;
    }
}
