package io.github.kloping.mirai0.Main.iutils;

import io.github.kloping.mirai0.Main.BootstarpResource;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.action.MemberNudge;
import net.mamoe.mirai.message.data.*;

import static io.github.kloping.mirai0.Main.BootstarpResource.isSuperQ;

/**
 * @author github-kloping
 */
public class EventUtils {
    public static String messageEvent2String(MessageEvent event) {
        StringBuilder sb = new StringBuilder();
        sb.append(messageChain2String(event.getMessage()));
        String text = sb.toString();
        return text;
    }

    public static String messageEvent2String(GroupMessageEvent event, boolean k) {
        StringBuilder sb = new StringBuilder();
        sb.append(messageChain2String(event.getMessage()));
        String text = sb.toString();
        if (k)
            text = text.trim().replaceAll("\n| |\r", "");
        return text;
    }

    public static String messageEvent2String(GroupMessageEvent event, boolean k, long q) {
        StringBuilder sb = new StringBuilder();
        sb.append(messageChain2String(event.getMessage(), q));
        String text = sb.toString();
        if (k)
            text = text.trim().replaceAll("\n| |\r", "");
        return text;
    }

    public static String messageChain2String(MessageChain event) {
        StringBuilder sb = new StringBuilder();
        for (Object o : event) {
            if (o instanceof OnlineMessageSource)
                continue;
            if (o instanceof PlainText) {
                if (!isIllegal(o.toString(), -1L))
                    sb.append(((PlainText) o).getContent());
            } else if (o instanceof At) {
                At at = (At) o;
                if (at.getTarget() == BootstarpResource.BOT.getId())
                    sb.append("[@me]");
                else
                    sb.append("[@").append(at.getTarget()).append("]");
            } else if (o instanceof FlashImage) {
                FlashImage flashImage = (FlashImage) o;
                sb.append("[闪照:").append(Image.queryUrl(flashImage.getImage())).append(":]");
            } else if (o instanceof Audio) {
                sb.append("[语音").append(((Audio) o).getFilename()).append("]");
            } else if (o instanceof Face) {
                Face face = (Face) o;
                sb.append("[Face:" + face.getId() + "]");
            } else if (o instanceof Image) {
                Image image = (Image) o;
                sb.append("[Pic:" + image.getImageId() + "]");
            } else if (o instanceof MemberNudge) {
                MemberNudge mn = (MemberNudge) o;
                long qid = mn.getTarget().getId();
                sb.append("[戳一戳:").append(qid == BootstarpResource.BOT.getId() ? "me" : qid).append("]");
            } else continue;
        }
        return sb.toString();
    }

    public static String messageChain2String(MessageChain event, long q) {
        StringBuilder sb = new StringBuilder();
        for (Object o : event) {
            if (o instanceof OnlineMessageSource)
                continue;
            if (o instanceof PlainText) {
                if (!isIllegal(o.toString(), q))
                    sb.append(((PlainText) o).getContent());
            } else if (o instanceof At) {
                At at = (At) o;
                if (at.getTarget() == BootstarpResource.BOT.getId())
                    sb.append("[@me]");
                else
                    sb.append("[@").append(at.getTarget()).append("]");
            } else if (o instanceof FlashImage) {
                FlashImage flashImage = (FlashImage) o;
                sb.append("[闪照:").append(Image.queryUrl(flashImage.getImage())).append(":]");
            } else if (o instanceof Audio) {
                sb.append("[语音").append(((Audio) o).getFilename()).append("]");
            } else if (o instanceof Face) {
                Face face = (Face) o;
                sb.append("[Face:" + face.getId() + "]");
            } else if (o instanceof Image) {
                Image image = (Image) o;
                sb.append("[Pic:" + image.getImageId() + "]");
            } else if (o instanceof MemberNudge) {
                MemberNudge mn = (MemberNudge) o;
                long qid = mn.getTarget().getId();
                sb.append("[戳一戳:").append(qid).append("]");
            } else continue;
        }
        return sb.toString();
    }


    private static boolean isIllegal(String str, Long qid) {
        if (qid > 0 && isSuperQ(qid)) return false;
        if (str.matches(".*\\[@\\d+].*")) {
            return true;
        } else return str.matches(".*\\[(图片|语音)].*");
    }
}
