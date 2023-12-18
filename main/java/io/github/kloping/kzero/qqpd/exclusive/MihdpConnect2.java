package io.github.kloping.kzero.qqpd.exclusive;

import io.github.kloping.kzero.main.api.MessagePack;
import io.github.kloping.kzero.mihdp.GeneralData;
import io.github.kloping.kzero.mihdp.MihdpClient;
import io.github.kloping.kzero.mihdp.ReqDataPack;
import io.github.kloping.kzero.mihdp.ResDataPack;
import io.github.kloping.qqbot.api.SendAble;
import io.github.kloping.qqbot.api.event.ConnectedEvent;
import io.github.kloping.qqbot.api.message.MessageChannelReceiveEvent;
import io.github.kloping.qqbot.api.message.MessageEvent;
import io.github.kloping.qqbot.api.v2.GroupMessageEvent;
import io.github.kloping.qqbot.entities.ex.At;
import io.github.kloping.qqbot.entities.ex.Image;
import io.github.kloping.qqbot.entities.ex.MessageAsyncBuilder;
import io.github.kloping.qqbot.entities.ex.PlainText;
import io.github.kloping.qqbot.impl.ListenerHost;

import java.util.Base64;
import java.util.LinkedList;
import java.util.List;

/**
 * @author github.kloping
 */
public class MihdpConnect2 extends ListenerHost implements MihdpClient.MihdpClientMessageListener {

    public static final MihdpConnect2 INSTANCE = new MihdpConnect2();

    @Override
    public void handleException(Throwable throwable) {

    }

    @EventReceiver
    public void onConnectedEvent(ConnectedEvent event) {
        String bid = event.getBot().getId();
        MihdpClient.INSTANCE.listeners.put(bid, this);
    }

    public void sendToMihdp(MessagePack pack, MessageEvent event) {
        ReqDataPack req = new ReqDataPack();
        if (pack == null) {
            pack = new MessagePack();
            pack.setSenderId(event.getSender().getId()).setSubjectId(event.getSubject().getId());
        } else {
            req.getArgs().put("icon", String.format("http://q.qlogo.cn/g?b=qq&nk=%s&s=640", pack.getSenderId()));
        }
        req.setId(event.getRawMessage().getId());
        req.setAction("msg");
        req.setSender_id(pack.getSenderId());
        req.setEnv_id(pack.getSubjectId());
        req.setBot_id(event.getBot().getId());
        req.setTime(System.currentTimeMillis());
        req.setEnv_type("friend");
        req.getArgs().put("draw", true);
        if (event instanceof MessageChannelReceiveEvent) {
            MessageChannelReceiveEvent e0 = (MessageChannelReceiveEvent) event;
            req.setEnv_type("group");
            req.getArgs().put("name", e0.getSender().getNick());
            req.getArgs().put("icon", e0.getSender().getUser().getAvatar());
        } else if (event instanceof GroupMessageEvent) {
            GroupMessageEvent e0 = (GroupMessageEvent) event;
            req.setEnv_type("group");
        }
        if (event.getMessage().size() == 1) {
            req.setContent(asGeneralData(event.getMessage().get(0)).toString());
        } else {
            List<GeneralData> data = new LinkedList<>();
            for (SendAble sendAble : event.getMessage()) {
                data.add(asGeneralData(sendAble));
            }
            req.setContent(new GeneralData.ResDataChain(data).toString());
        }
        MihdpClient.INSTANCE.send(req.toString());
    }

    public static GeneralData asGeneralData(SendAble sendAble) {
        if (sendAble instanceof At) {
            return new GeneralData.ResDataAt(((At) sendAble).getTargetId());
        } else if (sendAble instanceof Image) {
            Image image = (Image) sendAble;
            if (image.getUrl() != null)
                return new GeneralData.ResDataImage(image.getUrl(), "http");
            else return new GeneralData.ResDataImage(image.getBytes());
        } else {
            String text = sendAble.toString().trim();
            if (text.length() > 1 && text.startsWith("/")) text = text.substring(1);
            return new GeneralData.ResDataText(text.trim());
        }
    }

    private SendAble asSendAble(GeneralData data) {
        if (data.getType().equals("text")) {
            GeneralData.ResDataText text = (GeneralData.ResDataText) data;
            return new PlainText(text.getContent());
        } else if (data.getType().equals("image")) {
            GeneralData.ResDataImage image = (GeneralData.ResDataImage) data;
            if (image.getType().equals("http")) {
                return new Image(image.getData());
            } else {
                return new Image(Base64.getDecoder().decode(image.getData()));
            }
        }
        return new PlainText(data.toString());
    }

    @Override
    public void onMessage(ResDataPack pack) {
        MessageEvent event = Guild2Gsuid.INSTANCE.getMessage(pack.getId());
        if (pack.getData().getType().equals("chain")) {
            GeneralData.ResDataChain chain = (GeneralData.ResDataChain) pack.getData();
            MessageAsyncBuilder builder = new MessageAsyncBuilder();
            for (GeneralData generalData : chain.getList()) {
                builder.append(asSendAble(generalData));
            }
            event.send(builder.build());
        } else {
            event.send(asSendAble(pack.getData()));
        }
    }
}
