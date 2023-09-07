package io.github.kloping.kzero.gsuid;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.github.kloping.MySpringTool.h1.impl.LoggerImpl;
import io.github.kloping.MySpringTool.interfaces.Logger;
import io.github.kloping.common.Public;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author github.kloping
 */
public class GsuidClient extends WebSocketClient {
    public final static Logger LOGGER = new LoggerImpl();

    public static GsuidClient INSTANCE;

    static {
        try {
            INSTANCE = new GsuidClient();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public static final String SELF_ID = "bot";

    public GsuidClient() throws URISyntaxException {
        this(new URI("ws://47.100.93.243:8765/ws/" + SELF_ID));
    }

    public GsuidClient(URI serverUri) {
        super(serverUri);
    }

    public void send(MessageReceive receive) {
        send(JSON.toJSONString(receive).getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public void send(byte[] data) {
        super.send(data);
        String json = new String(data, Charset.forName("utf-8"));
        LOGGER.log("send=>" + json);
    }

    private List<GsuidMessageListener> listeners = new ArrayList<>();

    public void addListener(GsuidMessageListener listener) {
        listeners.add(listener);
    }

    @Override
    public void onMessage(String msg) {
        LOGGER.log("gsuid msg size: " + msg.length());
        MessageOut out = JSONObject.parseObject(msg, MessageOut.class);
        Public.EXECUTOR_SERVICE.submit(() -> {
            for (GsuidMessageListener listener : listeners) {
                listener.onMessage(out);
            }
        });
    }

    @Override
    public void onOpen(ServerHandshake handshake) {
        LOGGER.info("=============gsuid_core opened===========");
    }

    @Override
    public void onMessage(ByteBuffer bytes) {
        super.onMessage(bytes);
        String json = new String(bytes.array(), Charset.forName("utf-8"));
        onMessage(json);
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        LOGGER.error(String.format("gsuid_coore close %s => %s", code, reason));
        Public.EXECUTOR_SERVICE.submit(() -> {
            try {
                TimeUnit.SECONDS.sleep(10);
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
}
