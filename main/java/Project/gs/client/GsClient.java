package Project.gs.client;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.github.kloping.MySpringTool.StarterApplication;
import io.github.kloping.common.Public;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.event.events.FriendMessageEvent;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.message.data.QuoteReply;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.io.ByteArrayInputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Base64;
import java.util.Deque;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

/**
 * <a href="https://github.com/Genshin-bots/gsuid_core">gsuid_core</a>
 * 临时连接 <br>
 * 启动方式
 *
 * @author github.kloping
 */
public class GsClient extends WebSocketClient {

    public static final String SELF_ID = "mirai-client";
    public static final GsClient INSTANCE;

    static {
        try {
            INSTANCE = new GsClient(new URI("ws://47.100.93.243:8765/ws/" + SELF_ID));
            INSTANCE.connect();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public GsClient(URI serverUri) {
        super(serverUri);
    }

    public static MessageChainBuilder factory(MessageEvent event, MessageOut out) {
        MessageChainBuilder builder = new MessageChainBuilder();
        builder.append(new QuoteReply(event.getMessage()));
        if (!(event instanceof FriendMessageEvent)) builder.append(new At(event.getSender().getId())).append("\n");
        for (MessageData data : out.getContent()) {
            if (data.getType().equals("node")) {
                try {
                    JSONArray array = (JSONArray) data.getData();
                    for (MessageData d0 : array.toJavaList(MessageData.class)) {
                        append(builder, d0, event);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            append(builder, data, event);
        }
        return builder;
    }

    public static void append(MessageChainBuilder builder, MessageData d0, MessageEvent event) {
        if (d0.getType().equals("text")) {
            builder.append(d0.getData().toString().trim());
        } else if (d0.getType().equals("image")) {
            byte[] bytes = Base64.getDecoder().decode(d0.getData().toString().substring("base64://".length()));
            builder.append(Contact.uploadImage(event.getSubject(), new ByteArrayInputStream(bytes)));
        }
    }

    @Override
    public void send(byte[] data) {
        super.send(data);
        String json = new String(data, Charset.forName("utf-8"));
        StarterApplication.logger.log("send=>" + json);
    }

    @Override
    public void onOpen(ServerHandshake handshake) {
        StarterApplication.logger.info("opened");
    }

    @Override
    public void onMessage(ByteBuffer bytes) {
        super.onMessage(bytes);
        String json = new String(bytes.array(), Charset.forName("utf-8"));
        StarterApplication.logger.info("rec=>" + json);
        MessageOut out = JSONObject.parseObject(json, MessageOut.class);
        MessageEvent sender = getMessage(out.getMsg_id());
        if (out == null || sender == null || out.getBot_id() == null) return;
        MessageChainBuilder builder = factory(sender, out);
        sender.getSubject().sendMessage(builder.build());
    }

    @Override
    public void onMessage(String message) {
        System.out.println(message);
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        StarterApplication.logger.error(String.format("close %s => %s", code, reason));
        Public.EXECUTOR_SERVICE.submit(() -> {
            try {
                TimeUnit.SECONDS.sleep(3);
                reconnect();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public void onError(Exception ex) {
        ex.printStackTrace();
    }


    public static final Integer MAX_E = 20;

    private Deque<MessageEvent> QUEUE = new LinkedList<>();

    public void offer(MessageEvent msg) {
        if (QUEUE.contains(msg)) return;
        if (QUEUE.size() >= MAX_E) QUEUE.pollLast();
        QUEUE.offerFirst(msg);
    }

    private MessageEvent temp0 = null;

    public MessageEvent getMessage(String id) {
        if (temp0 != null && String.valueOf(temp0.getTime()).equals(id)) return temp0;
        for (MessageEvent event : QUEUE) {
            if (String.valueOf(event.getTime()).equals(id)) return event;
        }
        return null;
    }
}
