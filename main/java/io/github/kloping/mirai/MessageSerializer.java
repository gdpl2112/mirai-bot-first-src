package io.github.kloping.mirai;

import io.github.kloping.mirai0.Main.BootstarpResource;
import net.mamoe.mirai.message.action.MemberNudge;
import net.mamoe.mirai.message.data.*;

/**
 * @author github.kloping
 */
public class MessageSerializer {
    public static String messageChain2String(MessageChain chain) {
        StringBuilder sb = new StringBuilder();
        for (Object o : chain) {
            if (o instanceof OnlineMessageSource)
                continue;
            if (o instanceof PlainText) {
                sb.append(((PlainText) o).getContent());
            } else if (o instanceof At) {
                At at = (At) o;
                if (at.getTarget() == BootstarpResource.BOT.getId()) sb.append("[@me]");
                else sb.append("[@").append(at.getTarget()).append("]");
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
}
