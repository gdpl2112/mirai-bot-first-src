package io.github.kloping.mirai0.Main.Handlers;

import com.alibaba.fastjson.JSON;
import io.github.kloping.file.FileUtils;
import net.mamoe.mirai.event.events.FriendMessageEvent;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.event.events.StrangerMessageEvent;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageSource;

import java.io.File;

/**
 * @author github-kloping
 */
public class AllMessage {
    private int[] internalIds;
    private Number senderId;
    private int[] ids;
    private Number botId;
    private String type;
    private Number fromId;
    private String content;

    public int[] getInternalIds() {
        return internalIds;
    }

    public int[] getIds() {
        return ids;
    }

    public AllMessage setInternalIds(int[] internalIds) {
        this.internalIds = internalIds;
        return this;
    }

    public Number getSenderId() {
        return this.senderId;
    }

    public AllMessage setSenderId(Number senderId) {
        this.senderId = senderId;
        return this;
    }

    public AllMessage setIds(int[] ids) {
        this.ids = ids;
        return this;
    }

    public Number getBotId() {
        return this.botId;
    }

    public AllMessage setBotId(Number botId) {
        this.botId = botId;
        return this;
    }

    public String getType() {
        return this.type;
    }

    public AllMessage setType(String type) {
        this.type = type;
        return this;
    }

    public Number getFromId() {
        return this.fromId;
    }

    public AllMessage setFromId(Number fromId) {
        this.fromId = fromId;
        return this;
    }

    public String getContent() {
        return this.content;
    }

    public AllMessage setContent(String content) {
        this.content = content;
        return this;
    }

    private File file;

    public AllMessage setFile(File file) {
        this.file = file;
        return this;
    }

    public void save() {
        File file = new File(this.file.getAbsolutePath(), getFromId() + "/" + System.currentTimeMillis() + "-" + getSenderId());
        file.getParentFile().mkdirs();
        FileUtils.putStringInFile(JSON.toJSONString(this), file);
    }

    public static AllMessage factory(MessageEvent event, File file) {
        MessageSource messageSource = (MessageSource) event.getMessage().get(0);
        if (event instanceof GroupMessageEvent) {
            GroupMessageEvent gme = (GroupMessageEvent) event;
            return new AllMessage()
                    .setBotId(event.getBot().getId())
                    .setIds(messageSource.getIds())
                    .setContent(getText(gme.getMessage()))
                    .setFromId(gme.getSubject().getId())
                    .setSenderId(gme.getSender().getId())
                    .setInternalIds(messageSource.getInternalIds())
                    .setType("group").setFile(file);
        } else if (event instanceof FriendMessageEvent) {
            FriendMessageEvent gme = (FriendMessageEvent) event;
            return new AllMessage()
                    .setBotId(event.getBot().getId())
                    .setIds(messageSource.getIds())
                    .setContent(getText(gme.getMessage()))
                    .setFromId(gme.getSubject().getId())
                    .setSenderId(gme.getSender().getId())
                    .setInternalIds(messageSource.getInternalIds())
                    .setType("friend").setFile(file);
        } else if (event instanceof StrangerMessageEvent) {
            FriendMessageEvent gme = (FriendMessageEvent) event;
            return new AllMessage()
                    .setBotId(event.getBot().getId())
                    .setIds(messageSource.getIds())
                    .setContent(getText(gme.getMessage()))
                    .setFromId(gme.getSubject().getId())
                    .setSenderId(gme.getSender().getId())
                    .setInternalIds(messageSource.getInternalIds())
                    .setType("stranger").setFile(file);
        }
        return null;
    }


    private static String getText(MessageChain chain) {
        return MessageChain.serializeToJsonString(chain);
    }
}