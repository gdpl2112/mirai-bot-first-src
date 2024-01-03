package io.github.kloping.kzero.mirai.listeners;

import io.github.kloping.kzero.mihdp.GeneralData;
import io.github.kloping.kzero.mihdp.MihdpClient;
import io.github.kloping.kzero.mihdp.ReqDataPack;
import io.github.kloping.kzero.mihdp.ResDataPack;
import io.github.kloping.url.UrlUtils;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.ListenerHost;
import net.mamoe.mirai.event.events.*;
import net.mamoe.mirai.message.data.*;
import net.mamoe.mirai.utils.ExternalResource;
import org.apache.tomcat.util.codec.binary.Base64;

import java.util.LinkedList;

/**
 * @author github.kloping
 */
public class MihdpConnect implements ListenerHost {
    public static final MihdpConnect INSTANCE = new MihdpConnect();

    @EventHandler
    public void onMessage(GroupMessageEvent event) {
        sendToMihdp(event);
    }

    @EventHandler
    public void onMessage(FriendMessageEvent event) {
        sendToMihdp(event);
    }

    @EventHandler
    public void onMessage(GroupTempMessageEvent event) {
        sendToMihdp(event);
    }

    private void sendToMihdp(MessageEvent event) {
        GeneralData.ResDataChain chain = new GeneralData.ResDataChain(new LinkedList<>());
        for (SingleMessage singleMessage : event.getMessage()) {
            if (singleMessage instanceof PlainText) {
                chain.getList().add(new GeneralData.ResDataText(((PlainText) singleMessage).getContent()));
            } else if (singleMessage instanceof Image) {
                chain.getList().add(new GeneralData.ResDataImage(Image.queryUrl((Image) singleMessage), "http"));
            } else if (singleMessage instanceof At) {
                chain.getList().add(new GeneralData.ResDataAt(String.valueOf(((At) singleMessage).getTarget())));
            }
        }
        ReqDataPack req = new ReqDataPack();
        req.setAction("msg")
                .setContent(chain.toString())
                .setId(GenshinUidConnect.INSTANCE.getMessageEventId(event))
                .setBot_id(String.valueOf(event.getBot().getId()))
                .setTime(System.currentTimeMillis())
                .setEnv_type(event instanceof GroupMessageEvent ? "group" : "friend")
                .setSender_id(String.valueOf(event.getSender().getId()))
                .setEnv_id(String.valueOf(event.getSubject().getId()));
        req.getArgs().put("icon", event.getSender().getAvatarUrl());
        req.getArgs().put("name", event.getSender().getNick());
        req.getArgs().put("draw", "true");
        MihdpClient.INSTANCE.send(req.toString());
    }

    @EventHandler
    public void onBotOnline(BotOnlineEvent event) {
        String bid = String.valueOf(event.getBot().getId());
        MihdpClient.INSTANCE.listeners.put(bid, new MihdpClient.MihdpClientMessageListener() {
            @Override
            public void onMessage(ResDataPack pack) {
                MessageEvent raw = GenshinUidConnect.INSTANCE.getMessage(pack.getId());
                MessageChainBuilder builder = new MessageChainBuilder();
                if (raw != null) builder.append(new QuoteReply(raw.getSource()));
                append(pack.getData(), builder, raw.getSubject());
                Contact contact;
                if (raw != null) contact = raw.getSubject();
                else contact = pack.getEnv_type().equals("group") ?
                        event.getBot().getFriend(Long.parseLong(pack.getEnv_id())) :
                        event.getBot().getGroup(Long.parseLong(pack.getEnv_id()));
                contact.sendMessage(builder.build());
            }

            private void append(GeneralData data, MessageChainBuilder builder, Contact contact) {
                if (data instanceof GeneralData.ResDataChain) {
                    GeneralData.ResDataChain chain = (GeneralData.ResDataChain) data;
                    for (GeneralData generalData : chain.getList()) {
                        append(data, builder, contact);
                    }
                } else if (data instanceof GeneralData.ResDataText) {
                    builder.append(((GeneralData.ResDataText) data).getContent());
                } else if (data instanceof GeneralData.ResDataAt) {
                    builder.append(new At(Long.valueOf(((GeneralData.ResDataAt) data).getId())));
                } else if (data instanceof GeneralData.ResDataImage) {
                    GeneralData.ResDataImage image = (GeneralData.ResDataImage) data;
                    if (image.getType().equals("http")) {
                        byte[] bytes = UrlUtils.getBytesFromHttpUrl(image.getData());
                        builder.append(contact.uploadImage(ExternalResource.create(bytes)));
                    } else {
                        builder.append(contact.uploadImage(ExternalResource.create(Base64.decodeBase64(image.getData()))));
                    }
                } else if (data instanceof GeneralData.ResDataSelect) {
                    String d0 = ((GeneralData.ResDataSelect) data).getS() + "." + ((GeneralData.ResDataSelect) data).getContent();
                    builder.append(d0);
                }
            }
        });
    }

}
