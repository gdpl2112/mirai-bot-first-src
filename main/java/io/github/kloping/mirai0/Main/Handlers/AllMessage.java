package io.github.kloping.mirai0.Main.Handlers;

import Project.ASpring.SpringStarter0;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import net.mamoe.mirai.event.events.FriendMessageEvent;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.event.events.StrangerMessageEvent;
import net.mamoe.mirai.message.action.MemberNudge;
import net.mamoe.mirai.message.data.*;

/**
 * @author github-kloping
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class AllMessage {
    private Long time;
    private Integer id;
    private Integer internalId;
    private Long senderId;
    private Long botId;
    private String type;
    private Long fromId;
    private String content;

    public void save() {
        SpringStarter0.saveMapper.insert(this);
    }

    public static AllMessage factory(MessageEvent event) {
        MessageSource messageSource = (MessageSource) event.getMessage().get(0);
        if (event instanceof GroupMessageEvent) {
            GroupMessageEvent gme = (GroupMessageEvent) event;
            return new AllMessage()
                    .setBotId(event.getBot().getId())
                    .setId(messageSource.getIds()[messageSource.getIds().length - 1])
                    .setContent(getText(gme.getMessage()))
                    .setFromId(gme.getSubject().getId())
                    .setSenderId(gme.getSender().getId())
                    .setInternalId(messageSource.getInternalIds()[messageSource.getInternalIds().length - 1])
                    .setType("group").setTime(System.currentTimeMillis())
                    ;
        } else if (event instanceof FriendMessageEvent) {
            FriendMessageEvent gme = (FriendMessageEvent) event;
            return new AllMessage()
                    .setBotId(event.getBot().getId())
                    .setId(messageSource.getIds()[messageSource.getIds().length - 1])
                    .setContent(getText(gme.getMessage()))
                    .setFromId(gme.getSubject().getId())
                    .setSenderId(gme.getSender().getId())
                    .setInternalId(messageSource.getInternalIds()[messageSource.getInternalIds().length - 1])
                    .setType("friend").setTime(System.currentTimeMillis());
        } else if (event instanceof StrangerMessageEvent) {
            FriendMessageEvent gme = (FriendMessageEvent) event;
            return new AllMessage()
                    .setBotId(event.getBot().getId())
                    .setId(messageSource.getIds()[messageSource.getIds().length - 1])
                    .setContent(getText(gme.getMessage()))
                    .setFromId(gme.getSubject().getId())
                    .setSenderId(gme.getSender().getId())
                    .setInternalId(messageSource.getInternalIds()[messageSource.getInternalIds().length - 1])
                    .setType("stranger").setTime(System.currentTimeMillis());
        }
        return null;
    }

    private static String getText(MessageChain chain) {
        if (chain.size() == 2) {
            if (chain.get(1) instanceof PlainText) {
                return ((PlainText) chain.get(1)).getContent();
            } else if (chain.get(1) instanceof Audio) {
                return MessageChain.serializeToJsonString(chain);
            } else {
                return MessageChain.serializeToJsonString(chain);
            }
        } else {
            return getStringFromMessageChain(chain);
        }
    }

    public static String getStringFromMessageChain(MessageChain chain) {
        StringBuilder sb = new StringBuilder();
        for (Object o : chain) {
            if (o instanceof OnlineMessageSource) {
                continue;
            }
            if (o instanceof PlainText) {
                sb.append(((PlainText) o).getContent());
            } else if (o instanceof At) {
                At at = (At) o;
                sb.append("[@").append(at.getTarget()).append("]");
            } else if (o instanceof FlashImage) {
                FlashImage flashImage = (FlashImage) o;
                sb.append("[闪照:").append(Image.queryUrl(flashImage.getImage())).append(":]");
            } else if (o instanceof Face) {
                Face face = (Face) o;
                sb.append("[Face:").append(face.getId()).append("(").append(face.getName()).append(")").append("]");
            } else if (o instanceof Image) {
                Image image = (Image) o;
                sb.append("[Pic:").append(image.getImageId()).append("]");
            } else if (o instanceof MemberNudge) {
                MemberNudge mn = (MemberNudge) o;
                long qid = mn.getTarget().getId();
                sb.append("[戳一戳:").append(qid).append("]");
            } else {
                continue;
            }
        }
        return sb.toString();
    }
}