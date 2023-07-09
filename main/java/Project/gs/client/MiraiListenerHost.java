package Project.gs.client;

import com.alibaba.fastjson.JSON;
import io.github.kloping.common.Public;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.event.events.FriendMessageEvent;
import net.mamoe.mirai.event.events.GroupMessageEvent;


/**
 * @author github.kloping
 */
public class MiraiListenerHost extends SimpleListenerHost {
    private GsClient client;

    public MiraiListenerHost(GsClient client) {
        this.client = client;
    }

    @EventHandler
    public void gs(GroupMessageEvent event) {
        Public.EXECUTOR_SERVICE.submit(() -> {
            MessageReceive receive = Utils.getMessageReceive(client, event);
            if (receive != null) {
                receive.setGroup_id(String.valueOf(event.getGroup().getId()));
                receive.setUser_type("group");
                client.send(JSON.toJSONString(receive).getBytes());
            }
        });
    }

    @EventHandler
    public void gss(FriendMessageEvent event) {
        MessageReceive receive = Utils.getMessageReceive(client, event);
        if (receive != null) {
            receive.setGroup_id("");
            receive.setUser_type("direct");
            client.send(JSON.toJSONString(receive).getBytes());
        }
    }
}
