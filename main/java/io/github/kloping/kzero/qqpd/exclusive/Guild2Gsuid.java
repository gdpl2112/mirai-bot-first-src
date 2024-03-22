package io.github.kloping.kzero.qqpd.exclusive;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.github.kloping.judge.Judge;
import io.github.kloping.kzero.gsuid.*;
import io.github.kloping.kzero.main.api.MessagePack;
import io.github.kloping.qqbot.api.message.MessageChannelReceiveEvent;
import io.github.kloping.qqbot.api.message.MessageEvent;
import io.github.kloping.qqbot.api.v2.GroupMessageEvent;
import io.github.kloping.qqbot.entities.ex.*;
import io.github.kloping.qqbot.entities.ex.msg.MessageChain;
import io.github.kloping.qqbot.impl.message.BaseMessageChannelReceiveEvent;
import io.github.kloping.url.UrlUtils;
import org.jetbrains.annotations.NotNull;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.*;

/**
 * @author github.kloping
 */
public class Guild2Gsuid implements GsuidMessageListener {

    public static final Guild2Gsuid INSTANCE = new Guild2Gsuid();

    private static final Integer MAX_E = 150;

    private Deque<MessageEvent> QUEUE = new LinkedList<>();

    public void offer(MessageEvent msg) {
        if (QUEUE.contains(msg)) return;
        if (QUEUE.size() >= MAX_E) QUEUE.pollLast();
        QUEUE.offerFirst(msg);
    }

    public MessageEvent getMessage(String id) {
        if (temp0 != null && temp0.getRawMessage().getId().equals(id)) return temp0;
        for (MessageEvent event : QUEUE) {
            if (event.getRawMessage().getId().equals(id)) return temp0 = event;
        }
        return null;
    }

    private MessageEvent temp0 = null;

    //=============消息记录end
    public void sendToGsuid(MessageEvent event) {
        sendToGsuid(null, event);
    }

    public void sendToGsuid(MessagePack pack, MessageEvent event) {
        List<MessageData> list = getMessageData(event.getMessage(), event.getBot().getId());
        if (pack == null) {
            pack = new MessagePack();
            pack.setSenderId(event.getSender().getId()).setSubjectId(event.getSubject().getId());
        }
        if (!list.isEmpty()) {
            MessageReceive receive = new MessageReceive();
            receive.setBot_id("qqguild");
            receive.setBot_self_id(event.getBot().getId());
            receive.setUser_id(pack.getSenderId());
            receive.setMsg_id(event.getRawMessage().getId());
            receive.setUser_type("direct");
            receive.setGroup_id("");
            if (event instanceof BaseMessageChannelReceiveEvent || event instanceof GroupMessageEvent) {
                if (event instanceof GroupMessageEvent) receive.setBot_id("qqgroup");
                receive.setUser_type("group");
                receive.setGroup_id(pack.getSubjectId());
            }
            if ("7749068863541459083".equals(pack.getSenderId())) receive.setUser_pm(0);
            else if ("90724F608275850FD6169155FAAEC172".equals(pack.getSenderId())) receive.setUser_pm(0);
            else if ("3474006766".equals(pack.getSenderId())) receive.setUser_pm(0);
            else receive.setUser_pm(2);
            receive.setContent(list.toArray(new MessageData[0]));
            GsuidClient.INSTANCE.send(receive);
        }
    }

    @NotNull
    public static List<MessageData> getMessageData(MessageChain chain, String bid) {
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
                    message.setType("at");
                    message.setData(at.getTargetId().toString());
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
        Markdown markdown = null;
        for (MessageData d0 : out.getContent()) {
            if (d0.getType().equals("node")) {
                try {
                    JSONArray array = (JSONArray) d0.getData();
                    for (MessageData d1 : array.toJavaList(MessageData.class)) {
                        if (d0.getType().equals("text")) {
                            builder.append(new PlainText(d0.getData().toString().trim()));
                        } else if (d0.getType().equals("image")) {
                            byte[] bytes;
                            if (d0.getData().toString().startsWith("base64://")) {
                                bytes = Base64.getDecoder().decode(d0.getData().toString().substring("base64://".length()));
                            } else {
                                bytes = Base64.getDecoder().decode(d0.getData().toString());
                            }
                            builder.append(new Image(bytes));
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                if (d0.getType().equals("text")) {
                    builder.append(new PlainText(d0.getData().toString().trim()));
                } else if (d0.getType().equals("image")) {
                    byte[] bytes;
                    if (d0.getData().toString().startsWith("base64://")) {
                        bytes = Base64.getDecoder().decode(d0.getData().toString().substring("base64://".length()));
                    } else {
                        bytes = Base64.getDecoder().decode(d0.getData().toString());
                    }
                    builder.append(new Image(bytes));
                } else if (d0.getType().equals("buttons")) {
                    JSONArray arr = (JSONArray) d0.getData();
                    Keyboard.KeyboardBuilder b0 = new Keyboard.KeyboardBuilder();
                    Keyboard.RowBuilder r0 = b0.addRow();
                    int i = 0;
                    for (Object o : arr) {
                        i++;
                        JSONObject o1 = (JSONObject) o;
                        r0.addButton()
                                .setLabel(o1.getString("text"))
                                .setVisitedLabel(o1.getString("text"))
                                .setStyle(o1.getInteger("style"))
                                .setActionData(o1.getString("data"))
                                .setActionEnter(false)
                                .setActionReply(true)
                                .setActionType(2).build();
                        if (i >= 2) {
                            r0 = r0.build().addRow();
                            i = 0;
                        }
                    }
                    if (markdown != null) {
                        markdown.setKeyboard(b0.build());
                        raw.send(markdown);
                    } else {
                        raw.send(b0.build());
                    }
                } else if (d0.getType().equals("markdown")) {
                    String data = d0.getData().toString();
                    try {
                        Integer l1 = data.indexOf("(");
                        String url0 = data.substring(l1 + 1, data.lastIndexOf(")"));
                        byte[] bytes = UrlUtils.getBytesFromHttpUrl(url0);
                        try {
                            String url = Jsoup.connect(String.format("https://p.xiaofankj.com.cn/upimg.php"))
                                    .ignoreContentType(true)
                                    .ignoreContentType(true)
                                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/114.0.0.0 Safari/537.36 Edg/114.0.1823.67")
                                    .data("file", "temp.jpg", new ByteArrayInputStream(bytes)).method(Connection.Method.POST).execute().body();
                            url = JSONObject.parseObject(url).getString("msg");
                            markdown = new Markdown("102032364_1710924543")
                                    .addParam("title", "提示")
                                    .addParam("size", data.substring(0, l1))
                                    .addParam("url", String.format("(%s)", url));
                        } catch (IOException e) {
                            e.printStackTrace();
                            raw.send(new Image(bytes));
                        }
                    } catch (Exception e) {
                        System.err.println(data);
                        e.printStackTrace();
                    }
                }
            }
        }
        raw.send(builder.build());
    }
}
