package Project.gs.client;

import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.PlainText;
import net.mamoe.mirai.message.data.SingleMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * @author github.kloping
 */
public class Utils {

    public static String toStringFilterAtSelf(MessageEvent event) {
        StringBuilder sb = new StringBuilder();
        event.getMessage().forEach((e) -> {
            if (e instanceof PlainText) {
                sb.append(e.toString().trim());
            } else if (e instanceof At) {
                At at = (At) e;
                if (at.getTarget() == (event.getBot().getId())) return;
                else sb.append(e.toString());
            }
        });
        return sb.toString().trim();
    }


    public static Integer getPm(MessageEvent event) {
        if (event.getSender().getId() == 3474006766L) return 0;
        return 3;
    }

    public static MessageReceive getMessageReceive(GsClient client, MessageEvent event) {
        client.offer(event);
        List<MessageData> list = new ArrayList<>();
        for (SingleMessage singleMessage : event.getMessage()) {
            MessageData message = new MessageData();
            if (singleMessage instanceof PlainText) {
                message.setType("text");
                message.setData(((PlainText) singleMessage).getContent());
            } else if (singleMessage instanceof Image) {
                Image image = (Image) singleMessage;
                message.setType("image");
                message.setData(Image.queryUrl(image));
            } else continue;
            list.add(message);
        }
        if (list.size() > 0) {
            MessageReceive receive = new MessageReceive();
            receive.setBot_id(GsClient.SELF_ID);
            receive.setBot_self_id(String.valueOf(event.getBot().getId()));
            receive.setUser_id(String.valueOf(event.getSender().getId()));
            receive.setMsg_id(String.valueOf(event.getTime()));
            receive.setUser_pm(getPm(event));
            receive.setContent(list.toArray(new MessageData[0]));
            return receive;
        }
        return null;
    }
}
