package io.github.kloping.kzero.gsuid;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.github.kloping.spt.impls.LoggerImpl;
import io.github.kloping.spt.interfaces.Logger;
import io.github.kloping.common.Public;
import io.github.kloping.judge.Judge;
import io.github.kloping.kzero.main.DevPluginConfig;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author github.kloping
 */
public class GsuidClient extends WebSocketClient {
    public Logger LOGGER = new LoggerImpl();

    public static GsuidClient INSTANCE;

    public GsuidClient() throws URISyntaxException {
        this(new URI(DevPluginConfig.CONFIG.contextManager.getContextEntity(String.class, "gsuid.uri")));
        INSTANCE = this;
        LOGGER = DevPluginConfig.CONFIG.logger;
    }

    public GsuidClient(URI serverUri) {
        super(serverUri);
    }

    public void send(MessageReceive receive) {
        if (!this.isOpen()) return;
        send(JSON.toJSONString(receive).getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public void send(byte[] data) {
        super.send(data);
        String json = new String(data, Charset.forName("utf-8"));
        LOGGER.info("send=>" + json);
    }

    public Map<String, GsuidMessageListener> gsuidMessageListenerMap = new HashMap<>();

    public void addListener(String id, GsuidMessageListener listener) {
        gsuidMessageListenerMap.put(id, listener);
    }

    @Override
    public void onMessage(String msg) {
        MessageOut out = JSONObject.parseObject(msg, MessageOut.class);
        String bsid = out.getBot_self_id();
        LOGGER.info(String.format("gsuid msg bot(%s) to size: %s", bsid, msg.length()));
        if (Judge.isEmpty(bsid)) {
            Public.EXECUTOR_SERVICE.submit(() -> {
                gsuidMessageListenerMap.values().forEach(e -> {
                    e.onMessage(out);
                });
            });
        } else if (gsuidMessageListenerMap.containsKey(bsid))
            Public.EXECUTOR_SERVICE.submit(() -> {
                gsuidMessageListenerMap.get(bsid).onMessage(out);
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
        LOGGER.log(json);
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        LOGGER.error(String.format("gsuid_coore close %s => %s", code, reason));
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
}
