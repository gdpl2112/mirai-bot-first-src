package Project.gs.client;

import net.mamoe.mirai.contact.Member;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.PlainText;

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
        String content = toStringFilterAtSelf(event);
        if (content != null) {
            MessageData message = new MessageData();
            message.setType("text");
            message.setData(content);
            MessageReceive receive = new MessageReceive();
            receive.setBot_id(GsClient.SELF_ID);
            receive.setBot_self_id(String.valueOf(event.getBot().getId()));
            receive.setUser_id(String.valueOf(event.getSender().getId()));
            receive.setMsg_id(String.valueOf(event.getTime()));
            receive.setUser_pm(getPm(event));
            receive.setContent(new MessageData[]{message});
            return receive;
        }
        return null;
    }
}
