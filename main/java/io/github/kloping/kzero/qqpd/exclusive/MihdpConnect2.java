package io.github.kloping.kzero.qqpd.exclusive;

import com.alibaba.fastjson.JSON;
import io.github.kloping.kzero.main.api.KZeroBot;
import io.github.kloping.kzero.main.api.MessagePack;
import io.github.kloping.kzero.mihdp.GeneralData;
import io.github.kloping.kzero.mihdp.MihdpClient;
import io.github.kloping.kzero.mihdp.ReqDataPack;
import io.github.kloping.kzero.mihdp.ResDataPack;
import io.github.kloping.kzero.qqpd.GuildStater;
import io.github.kloping.qqbot.api.SendAble;
import io.github.kloping.qqbot.api.event.ConnectedEvent;
import io.github.kloping.qqbot.api.message.MessageChannelReceiveEvent;
import io.github.kloping.qqbot.api.message.MessageEvent;
import io.github.kloping.qqbot.api.v2.GroupMessageEvent;
import io.github.kloping.qqbot.entities.ex.*;
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

    public void sendToMihdp(MessagePack pack, MessageEvent event, KZeroBot bot) {
        ReqDataPack req = new ReqDataPack();
        if (pack == null) {
            pack = new MessagePack();
            pack.setSenderId(event.getSender().getId()).setSubjectId(event.getSubject().getId());
        } else {
            req.getArgs().put("icon", bot.getAdapter().getAvatarUrl(pack.getSenderId()));
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
            req.setContent(JSON.toJSONString(asGeneralData(event.getMessage().get(0))));
        } else {
            List<GeneralData> data = new LinkedList<>();
            for (SendAble sendAble : event.getMessage()) {
                data.add(asGeneralData(sendAble));
            }
            req.setContent(JSON.toJSONString(new GeneralData.ResDataChain(data)));
        }
        MihdpClient.INSTANCE.send(req.toString());
    }

    public static GeneralData asGeneralData(SendAble sendAble) {
        if (sendAble instanceof At) {
            return new GeneralData.ResDataAt(((At) sendAble).getTargetId());
        } else if (sendAble instanceof Image) {
            Image image = (Image) sendAble;
            if (image.getUrl() != null)
                return new GeneralData.ResDataImage(image.getUrl(), "http", 1, 1);
            else return new GeneralData.ResDataImage(image.getBytes(), 1, 1);
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
        } else if (data instanceof GeneralData.ResDataText) {
            return new PlainText(data.toString());
        } else if (data instanceof GeneralData.ResDataChain) {
            MessageAsyncBuilder builder = new MessageAsyncBuilder();
            for (GeneralData generalData : ((GeneralData.ResDataChain) data).getList()) {
                SendAble able = asSendAble(generalData);
                if (able != null) builder.append(able);
            }
            return builder.build();
        }
        return null;
    }

    @Override
    public void onMessage(ResDataPack pack) {
        MessageEvent event = Guild2Gsuid.INSTANCE.getMessage(pack.getId());
        if (pack.getData() == null) return;
        if (event instanceof GroupMessageEvent) {
            if (pack.getData().getType().equals("chain")) {
                GeneralData.ResDataChain chain = (GeneralData.ResDataChain) pack.getData();
                Object o0 = chain.find(GeneralData.ResDataButton.class);
                Object o1 = chain.find(GeneralData.ResDataSelect.class);
                if (o0 != null || o1 != null) {
                    MessageAsyncBuilder builder = null;
                    Markdown markdown = null;
                    Keyboard.KeyboardBuilder keyboardBuilder = null;
                    Keyboard.RowBuilder r0 = null;
                    int kindex = 0;
                    for (GeneralData data : chain.getList()) {
                        if (data.getType().equals("text")) {
                            GeneralData.ResDataText text = (GeneralData.ResDataText) data;
                            if (builder == null) builder = new MessageAsyncBuilder();
                            builder.append(text.getContent());
                        } else if (data.getType().equals("image")) {
                            GeneralData.ResDataImage image = (GeneralData.ResDataImage) data;
                            markdown = new Markdown("102032364_1710924543");
                            markdown.addParam("title", "TIPS");
                            markdown.addParam("size", String.format("![img #%s #%s]", image.getW(), image.getH()));
                            String url;
                            if (image.getP().equals("http")) {
                                url = image.getData();
                            } else {
                                url = GuildStater.upload(Base64.getDecoder().decode(image.getData()));
                            }
                            markdown.addParam("url", String.format("(%s)", url));
                        } else if (data.getType().equals("select")) {
                            GeneralData.ResDataSelect select = (GeneralData.ResDataSelect) data;
                            if (keyboardBuilder == null) {
                                keyboardBuilder = Keyboard.KeyboardBuilder.create();
                                r0 = keyboardBuilder.addRow();
                            }
                            kindex++;
                            r0.addButton().setLabel(select.getContent()).setVisitedLabel(select.getContent()).setStyle(1).setActionData(select.getS().toString()).setActionEnter(false).setActionReply(true).setActionType(2).build();
                            if (kindex >= 2) {
                                r0 = r0.build().addRow();
                                kindex = 0;
                            }
                        } else if (data.getType().equals("button")) {
                            GeneralData.ResDataButton button = (GeneralData.ResDataButton) data;
                            if (keyboardBuilder == null) {
                                keyboardBuilder = Keyboard.KeyboardBuilder.create();
                                r0 = keyboardBuilder.addRow();
                            }
                            kindex++;
                            r0.addButton().setLabel(button.getText()).setVisitedLabel(button.getText()).setStyle(1).setActionData(button.getContent()).setActionEnter(false).setActionReply(true).setActionType(2).build();
                            if (kindex >= 2) {
                                r0 = r0.build().addRow();
                                kindex = 0;
                            }
                        }
                    }
                    if (r0 != null) {
                        if (markdown != null) {
                            markdown.setKeyboard(keyboardBuilder.build());
                            event.send(markdown);
                        } else {
                            event.send(keyboardBuilder.build());
                        }
                    }
                    if (builder != null) event.send(builder.build());
                    return;
                }
            }
        }
        event.send(asSendAble(pack.getData()));
    }
}
