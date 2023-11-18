package io.github.kloping.kzero.guilds;

import com.alibaba.fastjson.JSONArray;
import io.github.kloping.judge.Judge;
import io.github.kloping.kzero.gsuid.*;
import io.github.kloping.qqbot.api.message.MessageChannelReceiveEvent;
import io.github.kloping.qqbot.api.message.MessageEvent;
import io.github.kloping.qqbot.api.v2.GroupMessageEvent;
import io.github.kloping.qqbot.entities.ex.At;
import io.github.kloping.qqbot.entities.ex.Image;
import io.github.kloping.qqbot.entities.ex.MessageAsyncBuilder;
import io.github.kloping.qqbot.entities.ex.PlainText;
import io.github.kloping.qqbot.entities.ex.msg.MessageChain;
import io.github.kloping.qqbot.impl.message.BaseMessageChannelReceiveEvent;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * @author github.kloping
 */
public class Guild2Gsuid implements GsuidMessageListener {

    //=============消息记录start
    private static final Integer MAX_E = 150;

    private Deque<MessageEvent> QUEUE = new LinkedList<>();

    public void offer(MessageEvent msg) {
        if (QUEUE.contains(msg)) return;
        if (QUEUE.size() >= MAX_E) QUEUE.pollLast();
        QUEUE.offerFirst(msg);
    }

    private MessageEvent temp0 = null;

    public MessageEvent getMessage(String id) {
        if (temp0 != null && temp0.getRawMessage().getId().equals(id)) return temp0;
        for (MessageEvent event : QUEUE) {
            if (event.getRawMessage().getId().equals(id)) return temp0 = event;
        }
        return null;
    }

    //=============消息记录end
    public void sendToGsuid(MessageEvent event) {
        List<MessageData> list = getMessageData(event.getMessage(), event.getBot().getId());
        if (!list.isEmpty()) {
            MessageReceive receive = new MessageReceive();
            receive.setBot_id("pd-qq-c");
            receive.setBot_self_id(event.getBot().getId());
            receive.setUser_id(event.getSender().getId());
            receive.setMsg_id(event.getRawMessage().getId());
            receive.setUser_type("direct");
            receive.setGroup_id("");
            if (event instanceof BaseMessageChannelReceiveEvent || event instanceof GroupMessageEvent) {
                receive.setUser_type("group");
                receive.setGroup_id(event.getSubject().getId());
            }
            if ("7749068863541459083".equals(event.getSender().getId())) receive.setUser_pm(0);
            else if ("90724F608275850FD6169155FAAEC172".equals(event.getSender().getId())) receive.setUser_pm(0);
            else receive.setUser_pm(2);
            receive.setContent(list.toArray(new MessageData[0]));
            GsuidClient.INSTANCE.send(receive);
        }
    }

    @NotNull
    private static List<MessageData> getMessageData(MessageChain chain, String bid) {
        List<MessageData> list = new ArrayList<>();
        chain.forEach(e -> {
            MessageData message = new MessageData();
            if (e instanceof PlainText) {
                message.setType("text");
                String data = e.toString().trim();
                if (data.startsWith("/") && data.length() > 1) data = data.substring(1);
                message.setData(data);
            } else if (e instanceof Image) {
                Image image = (Image) e;
                message.setType("image");
                message.setData(image.getUrl().startsWith("http") ? image.getUrl() : "https://" + image.getUrl());
            } else if (e instanceof At) {
                At at = (At) e;
                if (bid.equals(at.getTargetId())) {
                    return;
                } else {
                    message.setType("text");
                    message.setData("[CQ:at,qq=" + at.getTargetId() + "]");
                }
            } else return;
            list.add(message);
        });
        return list;
    }

    @Override
    public void onMessage(MessageOut out) {
        if (Judge.isEmpty(out.getMsg_id())) return;
        MessageEvent raw = null;
        raw = getMessage(out.getMsg_id());
        MessageAsyncBuilder builder = new MessageAsyncBuilder();
        if (raw instanceof MessageChannelReceiveEvent) {
            builder.append(new At(At.MEMBER_TYPE, raw.getSender().getId()));
            builder.append(new PlainText("\n"));
        }
        for (MessageData d0 : out.getContent()) {
            if (d0.getType().equals("node")) {
                try {
                    JSONArray array = (JSONArray) d0.getData();
                    for (MessageData d1 : array.toJavaList(MessageData.class)) {
                        builderAppend(builder, d1);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else builderAppend(builder, d0);
        }
        builder.reply(raw.getRawMessage());
        raw.send(builder.build());
    }

    private void builderAppend(MessageAsyncBuilder builder, MessageData d0) {
        if (d0.getType().equals("text")) {
            builder.append(new PlainText(d0.getData().toString().trim()));
        } else if (d0.getType().equals("image")) {
            byte[] bytes = Base64.getDecoder().decode(d0.getData().toString().substring("base64://".length()));
            builder.append(new Image(bytes));
        }
    }
}
