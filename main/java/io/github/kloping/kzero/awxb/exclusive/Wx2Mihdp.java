package io.github.kloping.kzero.awxb.exclusive;

import com.alibaba.fastjson.JSON;
import io.github.gdpl2112.onebot.v12.ListenerHost;
import io.github.gdpl2112.onebot.v12.data.*;
import io.github.gdpl2112.onebot.v12.event.EventReceiver;
import io.github.gdpl2112.onebot.v12.event.GroupMessageEvent;
import io.github.gdpl2112.onebot.v12.event.MessageEvent;
import io.github.gdpl2112.onebot.v12.utils.ConfigurationUtils;
import io.github.kloping.judge.Judge;
import io.github.kloping.kzero.mihdp.GeneralData;
import io.github.kloping.kzero.mihdp.MihdpClient;
import io.github.kloping.kzero.mihdp.ReqDataPack;
import io.github.kloping.kzero.mihdp.ResDataPack;
import io.github.kloping.kzero.qqpd.GuildStater;
import org.apache.tomcat.util.codec.binary.Base64;

import java.util.Deque;
import java.util.LinkedList;

/**
 * @author github.kloping
 */
public class Wx2Mihdp extends ListenerHost implements MihdpClient.MihdpClientMessageListener {
    public static final Wx2Mihdp INSTANCE = new Wx2Mihdp();

    @Override
    public void handleException(Throwable e) {

    }

    @EventReceiver
    public void onEvent(GroupMessageEvent event) {
        offer(event);
        if (MihdpClient.INSTANCE == null) return;
        GeneralData.ResDataChain chain = new GeneralData.ResDataChain(new LinkedList<>());
        for (Message singleMessage : event.getMessage().getMessages()) {
            if (singleMessage instanceof PlainText) {
                chain.getList().add(new GeneralData.ResDataText(((PlainText) singleMessage).getText()));
            } else if (singleMessage instanceof Image) {
                chain.getList().add(new GeneralData.ResDataImage(((Image) singleMessage).getId(), "http", 1, 1));
            } else if (singleMessage instanceof At) {
                chain.getList().add(new GeneralData.ResDataAt(String.valueOf(((At) singleMessage).getTarget())));
            }
        }
        ReqDataPack req = new ReqDataPack();
        req.setAction("msg").setContent(JSON.toJSONString(chain)).setId(event.getId())
                .setBot_id(String.valueOf(event.getSelf().getUserId()))
                .setTime(System.currentTimeMillis()).setEnv_type(event instanceof GroupMessageEvent ? "group" : "friend")
                .setSender_id(String.valueOf(event.getSender().getUserId())).setEnv_id(String.valueOf(event.getGroupId()));
        req.getArgs().put("icon", event.getSender().getAvatar());
        req.getArgs().put("name", event.getSender().getUserName());
        req.getArgs().put("draw", "true");
        MihdpClient.INSTANCE.send(req.toString());
    }

    @Override
    public void onMessage(ResDataPack pack) {
        MessageEvent event = getMessage(pack.getId());
        MessageChainBuilder builder = new MessageChainBuilder();
        append(builder, pack.getData(), event);
        event.sendMessage(builder.build());
    }

    private void append(MessageChainBuilder builder, GeneralData data, MessageEvent event) {
        if (data instanceof GeneralData.ResDataChain) {
            GeneralData.ResDataChain chain = (GeneralData.ResDataChain) data;
            for (GeneralData generalData : chain.getList()) {
                append(builder, generalData, event);
            }
        } else if (data instanceof GeneralData.ResDataText) {
            builder.append(((GeneralData.ResDataText) data).getContent());
        } else if (data instanceof GeneralData.ResDataAt) {
            builder.append(new At(((GeneralData.ResDataAt) data).getId()));
        } else if (data instanceof GeneralData.ResDataImage) {
            GeneralData.ResDataImage image = (GeneralData.ResDataImage) data;
            String url = null;
            if (image.getType().equals("http")) {
                url = ((GeneralData.ResDataImage) data).getData();
            } else {
                url = GuildStater.upload(Base64.decodeBase64(image.getData()));
            }
            builder.append(ConfigurationUtils.INSTANCE.uploadImage(url, event));
        }
    }


    //=============消息记录start
    public static final Integer MAX_E = 40;
    private static MessageEvent temp0 = null;
    private static Deque<MessageEvent> QUEUE = new LinkedList<>();

    public static void offer(MessageEvent msg) {
        if (QUEUE.contains(msg)) return;
        if (QUEUE.size() >= MAX_E) QUEUE.pollLast();
        QUEUE.offerFirst(msg);
    }

    public static MessageEvent getMessage(String id) {
        if (Judge.isEmpty(id)) return null;
        if (temp0 != null && getMessageEventId(temp0).equals(id)) return temp0;
        for (MessageEvent event : QUEUE) {
            if (getMessageEventId(event).equals(id)) return temp0 = event;
        }
        return null;
    }

    public static String getMessageEventId(MessageEvent event) {
        return event.getId();
    }
    //=============消息记录end

}
