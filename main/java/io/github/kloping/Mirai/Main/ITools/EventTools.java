package io.github.kloping.Mirai.Main.ITools;

import io.github.kloping.Mirai.Main.Resource;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.action.MemberNudge;
import net.mamoe.mirai.message.data.*;

import static io.github.kloping.Mirai.Main.Resource.superQL;

/**
 * @author github-kloping
 */
public class EventTools {
    public static String getStringFromGroupMessageEvent(GroupMessageEvent event) {
        StringBuilder sb = new StringBuilder();
        sb.append(getStringFromMessageChain(event.getMessage()));
        String text = sb.toString();
        return text;
    }

    public static String getStringFromGroupMessageEvent(GroupMessageEvent event, boolean k) {
        StringBuilder sb = new StringBuilder();
        sb.append(getStringFromMessageChain(event.getMessage()));
        String text = sb.toString();
        if (k)
            text = text.trim().replaceAll("\n| |\r", "");
        return text;
    }

    public static String getStringFromGroupMessageEvent(GroupMessageEvent event, boolean k, long who) {
        StringBuilder sb = new StringBuilder();
        sb.append(getStringFromMessageChain(event.getMessage(), who));
        String text = sb.toString();
        if (k)
            text = text.trim().replaceAll("\n| |\r", "");
        return text;
    }

    public static String getStringFromMessageChain(MessageChain event) {
        StringBuilder sb = new StringBuilder();
        for (Object o : event) {
            if (o instanceof OnlineMessageSource)
                continue;
            if (o instanceof PlainText) {
                if (!isIllegal(o.toString()))
                    sb.append(((PlainText) o).getContent());
            } else if (o instanceof At) {
                At at = (At) o;
                if (at.getTarget() == Resource.qq.getQq())
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
                sb.append("[戳一戳:").append(qid == Resource.qq.getQq() ? "me" : qid).append("]");
            } else continue;
        }
        return sb.toString();
    }

    public static String getStringFromMessageChain(MessageChain event, long who) {
        StringBuilder sb = new StringBuilder();
        for (Object o : event) {
            if (o instanceof OnlineMessageSource)
                continue;
            if (o instanceof PlainText) {
                if (!isIllegal(o.toString(), who))
                    sb.append(((PlainText) o).getContent());
            } else if (o instanceof At) {
                At at = (At) o;
                if (at.getTarget() == Resource.qq.getQq())
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
                sb.append("[戳一戳:").append(qid == Resource.qq.getQq() ? "me" : qid).append("]");
            } else continue;
        }
        return sb.toString();
    }

    private static boolean isIllegal(String str) {
        if (str.matches(".*\\[@\\d+].*")) {
            return true;
        } else return str.matches(".*\\[(图片|语音)\\].*");
    }

    private static boolean isIllegal(String str, long who) {
        if (who == superQL) return false;
        if (str.matches(".*\\[@\\d+].*")) {
            return true;
        } else return str.matches(".*\\[(图片|语音)].*");
    }
}
