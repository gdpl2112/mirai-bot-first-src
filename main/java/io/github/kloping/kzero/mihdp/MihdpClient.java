package io.github.kloping.kzero.mihdp;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.github.kloping.MySpringTool.h1.impl.LoggerImpl;
import io.github.kloping.MySpringTool.interfaces.Logger;
import io.github.kloping.common.Public;
import io.github.kloping.judge.Judge;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author github.kloping
 */
public class MihdpClient extends WebSocketClient {
    public static final Logger LOGGER = new LoggerImpl();
    public static final MihdpClient INSTANCE;
    public static Gson GSON;

    static {
        try {
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.registerTypeAdapter(GeneralData.class, new GeneralData.GeneralDataDeserializer());
            GSON = gsonBuilder.create();
            INSTANCE = new MihdpClient(new URI("ws://localhost:6034"));
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public MihdpClient(URI serverUri) {
        super(serverUri);
    }

    public Map<String, MihdpClientMessageListener> listeners = new HashMap<>();

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        LOGGER.info("============MihdpClient OPEN===========");
        INSTANCE.send("123456");
    }

    @Override
    public void onMessage(String message) {
        ResDataPack dataPack = GSON.fromJson(message, ResDataPack.class);
        if (dataPack == null || dataPack.getAction() == null) return;
        String bid = dataPack.getBot_id();
        if (Judge.isEmpty(bid)) {
            listeners.forEach((k, v) -> v.onMessage(dataPack));
        } else if (listeners.containsKey(bid)) {
            listeners.get(bid).onMessage(dataPack);
        }
    }

    @Override
    public void send(String text) {
        if (!this.isOpen()) return;
        super.send(text);
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        Public.EXECUTOR_SERVICE.submit(() -> {
            try {
                LOGGER.error("=========MihdpClient ==reconnect=====");
                TimeUnit.SECONDS.sleep(8);
                reconnect();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public void onError(Exception ex) {

    }

    public interface MihdpClientMessageListener {
        void onMessage(ResDataPack pack);
    }
}
