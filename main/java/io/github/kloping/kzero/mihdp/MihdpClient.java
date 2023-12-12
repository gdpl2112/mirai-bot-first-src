package io.github.kloping.kzero.mihdp;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import io.github.kloping.MySpringTool.h1.impl.LoggerImpl;
import io.github.kloping.MySpringTool.interfaces.Logger;
import io.github.kloping.common.Public;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
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
            JsonDeserializer<GeneralData> deserializer = new DataDeserializer();
            gsonBuilder.registerTypeAdapter(GeneralData.class, deserializer);
            GSON = gsonBuilder.create();
            INSTANCE = new MihdpClient(new URI("ws://localhost:6034"));
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public MihdpClient(URI serverUri) {
        super(serverUri);
    }

    private List<MihdpClientMessageListener> listeners = new LinkedList<>();

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        LOGGER.info("============MihdpClient OPEN===========");
        INSTANCE.send("123456");
    }

    @Override
    public void onMessage(String message) {
        ResDataPack dataPack = GSON.fromJson(message, ResDataPack.class);
        if (dataPack == null || dataPack.getAction() == null) return;
        for (MihdpClientMessageListener listener : listeners) {
            try {
                listener.onMessage(dataPack);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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
