package io.github.kloping.kzero.mirai.listeners;

import com.alibaba.fastjson.JSONArray;
import io.github.kloping.judge.Judge;
import io.github.kloping.kzero.gsuid.*;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.ListenerHost;
import net.mamoe.mirai.event.events.*;
import net.mamoe.mirai.message.data.*;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayInputStream;
import java.util.*;

/**
 * @author github.kloping
 */
public class GenshinUidConnect implements ListenerHost {

    public static final GenshinUidConnect INSTANCE = new GenshinUidConnect();

    @EventHandler
    public void onMessage(GroupMessageEvent event) {
        offer(event);
        sendToGsuid(event);
    }

    @EventHandler
    public void onMessage(FriendMessageEvent event) {
        offer(event);
        sendToGsuid(event);
    }

    @EventHandler
    public void onMessage(GroupTempMessageEvent event) {
        offer(event);
        sendToGsuid(event);
    }


    private void sendToGsuid(MessageEvent event) {
        List<MessageData> list = getMessageData(event);
        if (!list.isEmpty()) {
            MessageReceive receive = new MessageReceive();
            receive.setBot_id("qqgroup");
            receive.setBot_self_id(String.valueOf(event.getBot().getId()));
            receive.setUser_id(String.valueOf(event.getSender().getId()));
            receive.setMsg_id(getMessageEventId(event));
            receive.setUser_type("direct");
            receive.setGroup_id("");
            if (event instanceof GroupMessageEvent) {
                receive.setUser_type("group");
                receive.setGroup_id(String.valueOf(event.getSubject().getId()));
            }
            if (event.getSender().getId() == 3474006766L) receive.setUser_pm(0);
            else receive.setUser_pm(2);
            receive.setContent(list.toArray(new MessageData[0]));
            GsuidClient.INSTANCE.send(receive);
        }
    }

    @NotNull
    private static List<MessageData> getMessageData(MessageEvent event) {
        MessageChain chain = event.getMessage();
        List<MessageData> list = new ArrayList<>();
        chain.forEach(e -> {
            MessageData message = new MessageData();
            if (e instanceof PlainText) {
                message.setType("text");
                String data = e.toString().trim();
                if (data.equalsIgnoreCase("/gs帮助")) data = "gs帮助";
                message.setData(data);
            } else if (e instanceof Image) {
                Image image = (Image) e;
                message.setType("image");
                message.setData(Image.queryUrl(image));
            } else if (e instanceof At) {
                At at = (At) e;
                if (at.getTarget() == event.getBot().getId())
                    return;
                else {
                    message.setType("at");
                    message.setData(at.getTarget());
                }
            } else return;
            list.add(message);
        });
        return list;
    }

    @EventHandler
    public void onBotOnline(BotOnlineEvent event) {
        String bid = String.valueOf(event.getBot().getId());
        GsuidClient.INSTANCE.addListener(bid, new GsuidMessageListener() {

            @Override
            public void onMessage(MessageOut out) {
                MessageEvent raw = getMessage(out.getMsg_id());
                MessageChainBuilder builder = new MessageChainBuilder();
                if (raw != null) builder.append(new QuoteReply(raw.getSource()));
                for (MessageData d0 : out.getContent()) {
                    if (d0.getType().equals("node")) {
                        try {
                            JSONArray array = (JSONArray) d0.getData();
                            for (MessageData d1 : array.toJavaList(MessageData.class))
                                builderAppend(builder, d1, event);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else builderAppend(builder, d0, event);
                }
                Contact contact;
                if (raw != null) contact = raw.getSubject();
                else
                    contact = out.getTarget_type().equals("direct") ? event.getBot().getFriend(Long.parseLong(out.getTarget_id())) : event.getBot().getGroup(Long.parseLong(out.getTarget_id()));
                contact.sendMessage(builder.build());
            }

            private void builderAppend(MessageChainBuilder builder, MessageData d0, BotOnlineEvent event) {
                if (d0.getType().equals("text")) {
                    builder.append(new PlainText(d0.getData().toString().trim())).append("\n");
                } else if (d0.getType().equals("image")) {
                    byte[] bytes;
                    if (d0.getData().toString().startsWith("base64://")) {
                        bytes = Base64.getDecoder().decode(d0.getData().toString().substring("base64://".length()));
                    } else {
                        bytes = Base64.getDecoder().decode(d0.getData().toString());
                    }
                    Image image = Contact.uploadImage(event.getBot().getAsFriend(), new ByteArrayInputStream(bytes));
                    builder.append(image);
                }
            }
        });
    }

    //=============消息记录start
    public static final Integer MAX_E = 50;
    private MessageEvent temp0 = null;
    private Deque<MessageEvent> QUEUE = new LinkedList<>();


    public void offer(MessageEvent msg) {
        if (QUEUE.contains(msg)) return;
        if (QUEUE.size() >= MAX_E) QUEUE.pollLast();
        QUEUE.offerFirst(msg);
    }

    public MessageEvent getMessage(String id) {
        if (Judge.isEmpty(id)) return null;
        if (temp0 != null && getMessageEventId(temp0).equals(id)) return temp0;
        for (MessageEvent event : QUEUE) {
            if (getMessageEventId(event).equals(id)) return temp0 = event;
        }
        return null;
    }

    public String getMessageEventId(MessageEvent event) {
        if (event.getSource().getIds().length == 0) return "";
        else return String.valueOf(event.getSource().getIds()[0]);
    }
    //=============消息记录end

}
