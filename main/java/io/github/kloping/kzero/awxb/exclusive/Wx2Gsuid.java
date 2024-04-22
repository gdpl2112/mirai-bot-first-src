package io.github.kloping.kzero.awxb.exclusive;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.github.gdpl2112.onebot.v12.ListenerHost;
import io.github.gdpl2112.onebot.v12.action.Action;
import io.github.gdpl2112.onebot.v12.data.*;
import io.github.gdpl2112.onebot.v12.event.EventReceiver;
import io.github.gdpl2112.onebot.v12.event.GroupMessageEvent;
import io.github.gdpl2112.onebot.v12.event.MessageEvent;
import io.github.kloping.kzero.gsuid.MessageData;
import io.github.kloping.kzero.gsuid.*;
import io.github.kloping.kzero.qqpd.GuildStater;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * @author github.kloping
 */
public class Wx2Gsuid extends ListenerHost implements GsuidMessageListener {
    public static final Wx2Gsuid INSTANCE = new Wx2Gsuid();

    @Override
    public void handleException(Throwable e) {

    }

    @EventReceiver
    public void onEvent(GroupMessageEvent event) {
        Wx2Mihdp.offer(event);
        if (GsuidClient.INSTANCE == null) return;
        sendToGsuid(event);
    }

    @Override
    public void onMessage(MessageOut out) {
        MessageEvent event = Wx2Mihdp.getMessage(out.getMsg_id());
        MessageChainBuilder builder = new MessageChainBuilder();
        for (MessageData messageData : out.getContent()) {
            append(builder, messageData, event);
        }
        event.sendMessage(builder.build());
    }

    private void append(MessageChainBuilder builder, MessageData messageData, MessageEvent event) {
        if (messageData.getType().equals("node")) {
            JSONArray array = (JSONArray) messageData.getData();
            for (MessageData d1 : array.toJavaList(MessageData.class)) {
                append(builder, d1, event);
            }
        } else if (messageData.getType().equals("text")) {
            builder.append(messageData.getData().toString());
        } else if (messageData.getType().equals("image")) {
            Image image = null;
            if (messageData.getData().toString().startsWith("base64://")) {
                byte[] bytes = Base64.getDecoder().decode(messageData.getData().toString().substring("base64://".length()));
                image = event.uploadImage(GuildStater.upload(bytes));
            } else {
                image = event.uploadImage(messageData.getData().toString());
            }
            builder.append(image);
        }
    }

    private void sendToGsuid(MessageEvent event) {
        List<MessageData> list = getMessageData(event);
        if (!list.isEmpty()) {
            MessageReceive receive = new MessageReceive();
            receive.setBot_id("wx");
            receive.setBot_self_id(event.getSelf().getUserId());
            receive.setUser_id(event.getSender().getUserId());
            receive.setMsg_id(event.getId());
            receive.setUser_type("direct");
            receive.setGroup_id("");
            if (event instanceof GroupMessageEvent) {
                receive.setUser_type("group");
                receive.setGroup_id(String.valueOf(((GroupMessageEvent) event).getGroup().getGroupId()));
            }
            if ("wxid_ewwmqd4axkd822".equals(event.getSender().getUserId())) receive.setUser_pm(0);
            else receive.setUser_pm(2);
            receive.setContent(list.toArray(new MessageData[0]));
            GsuidClient.INSTANCE.send(receive);
        }
    }


    @NotNull
    private static List<MessageData> getMessageData(MessageEvent event) {
        MessageChain chain = event.getMessage();
        List<MessageData> list = new ArrayList<>();
        for (Message e : chain.getMessages()) {
            MessageData message = new MessageData();
            if (e instanceof PlainText) {
                message.setType("text");
                String data = ((PlainText) e).getText();
                if (data.equalsIgnoreCase("/gs帮助")) data = "gs帮助";
                message.setData(data);
            } else if (e instanceof Image) {
                Image image = (Image) e;
                message.setType("image");
                String url = queryImageUrl(event, image);
                message.setData(url);
            } else if (e instanceof At) {
                At at = (At) e;
                if (at.getTarget().equals(event.getSelf().getUserId())) continue;
                else {
                    message.setType("at");
                    message.setData(at.getTarget());
                }
            } else continue;
            list.add(message);
        }
        return list;
    }

    public static String queryImageUrl(MessageEvent event, Image image) {
        Action action = new Action();
        action.setAction("get_file");
        Map<String, Object> ps = new HashMap();
        ps.put("file_id", image.getId());
        ps.put("type", "path");
        action.setParams(ps);
        JSONObject jo = event.send(action).getData(JSONObject.class);
        return String.format("http://139.196.195.212:60080/get_file?path=%s",
                jo.getString("path").replaceAll("\\\\", "/"));
    }
}
