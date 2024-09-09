package io.github.kloping.kzero.mirai.listeners;

import com.alibaba.fastjson.JSONArray;
import io.github.kloping.kzero.bot.database.DataBase;
import io.github.kloping.kzero.gsuid.*;
import io.github.kloping.kzero.main.api.KZeroBot;
import io.github.kloping.kzero.mihdp.MihdpClient;
import io.github.kloping.kzero.spring.dao.GroupConf;
import io.github.kloping.spt.annotations.AutoStand;
import io.github.kloping.spt.annotations.Entity;
import io.github.kloping.spt.interfaces.Logger;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.GlobalEventChannel;
import net.mamoe.mirai.event.ListenerHost;
import net.mamoe.mirai.event.events.*;
import net.mamoe.mirai.message.data.*;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

/**
 * @author github.kloping
 */
@Entity
public class GenshinUidConnect implements ListenerHost {

    public GenshinUidConnect(KZeroBot kZeroBot) {
        if (!(kZeroBot.getSelf() instanceof Bot)) {
            throw new RuntimeException("=======ERROR=====FOR MIRAI GROUP");
        } else {
            Bot bot = (Bot) kZeroBot.getSelf();
            GlobalEventChannel.INSTANCE.registerListenerHost(this);
        }
    }


    @EventHandler
    public void onMessage(GroupMessageEvent event) {
        messageMer.offer(event);
        sendToGsuid(event);
    }

    @EventHandler
    public void onMessage(FriendMessageEvent event) {
        messageMer.offer(event);
        sendToGsuid(event);
    }

    @EventHandler
    public void onMessage(GroupTempMessageEvent event) {
        messageMer.offer(event);
        sendToGsuid(event);
    }

    @AutoStand
    Logger logger;

    @AutoStand
    DataBase dataBase;

    @AutoStand
    MessageMer messageMer;

    private void sendToGsuid(MessageEvent event) {
        if (MihdpClient.INSTANCE == null) return;
        String gid = String.valueOf(event.getSubject().getId());
        GroupConf groupConf = dataBase.getConf(gid);
        if (groupConf != null) {
            if (!groupConf.getOpen()) {
                logger.waring("未开启 group");
                return;
            }
        }
        List<MessageData> list = getMessageData(event);
        if (!list.isEmpty()) {
            MessageReceive receive = new MessageReceive();
            receive.setBot_id("qqgroup0");
            receive.setBot_self_id(String.valueOf(event.getBot().getId()));
            receive.setUser_id(String.valueOf(event.getSender().getId()));
            receive.setMsg_id(messageMer.getMessageEventId(event));
            receive.setUser_type("direct");
            receive.setGroup_id("");
            if (event instanceof GroupMessageEvent) {
                receive.setUser_type("group");
                receive.setGroup_id(String.valueOf(event.getSubject().getId()));
            }
            if (event.getSender().getId() == 3474006766L) receive.setUser_pm(0);
            else receive.setUser_pm(2);
            receive.setContent(list.toArray(new MessageData[0]));
            if (GsuidClient.INSTANCE != null) GsuidClient.INSTANCE.send(receive);
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
        if (GsuidClient.INSTANCE != null) GsuidClient.INSTANCE.addListener(bid, new GsuidMessageListener() {
            @Override
            public void onMessage(MessageOut out) {
                MessageEvent raw = messageMer.getMessage(out.getMsg_id());
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
}
