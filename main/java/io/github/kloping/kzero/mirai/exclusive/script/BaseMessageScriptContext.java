package io.github.kloping.kzero.mirai.exclusive.script;

import io.github.kloping.spt.impls.PackageScannerImpl;
import io.github.kloping.spt.interfaces.component.PackageScanner;
import io.github.kloping.kzero.main.api.MessageSerializer;
import io.github.kloping.url.UrlUtils;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.contact.User;
import net.mamoe.mirai.event.events.FriendMessageEvent;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.event.events.GroupMessageSyncEvent;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.ForwardMessageBuilder;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.Message;
import net.mamoe.mirai.message.data.MessageChain;

import javax.script.ScriptEngine;
import java.io.ByteArrayInputStream;


public class BaseMessageScriptContext implements ScriptContext {
    private MessageEvent event;
    private MessageSerializer<Message> serializer;

    public BaseMessageScriptContext(MessageEvent event, MessageSerializer<Message> serializer) {
        this.event = event;
        this.serializer = serializer;
    }

    @Override
    public Bot getBot() {
        return event.getBot();
    }

    @Override
    public MessageChain getRaw() {
        return event.getMessage();
    }

    @Override
    public Message deSerialize(String msg) {
        return serializer.deserialize(msg);
    }

    @Override
    public void send(String str) {
        Message msg = serializer.deserialize(str);
        event.getSubject().sendMessage(msg);
    }

    @Override
    public void send(Message message) {
        event.getSubject().sendMessage(message);
    }

    @Override
    public ForwardMessageBuilder forwardBuilder() {
        return new ForwardMessageBuilder(getSubject());
    }

    @Override
    public User getSender() {
        return event.getSender();
    }

    @Override
    public Contact getSubject() {
        return event.getSubject();
    }

    @Override
    public Image uploadImage(String url) {
        try {
            byte[] bytes = UrlUtils.getBytesFromHttpUrl(url);
            Image image = Contact.uploadImage(event.getBot().getAsFriend(), new ByteArrayInputStream(bytes));
            return image;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String getType() {
        return event instanceof GroupMessageEvent || event instanceof GroupMessageSyncEvent ?
                "group" : event instanceof FriendMessageEvent ? "friend" : "Unknown";
    }
}
