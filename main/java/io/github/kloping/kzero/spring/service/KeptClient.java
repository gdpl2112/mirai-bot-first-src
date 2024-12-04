package io.github.kloping.kzero.spring.service;

import io.github.kloping.common.Public;
import lombok.extern.slf4j.Slf4j;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * @author github kloping
 * @date 2024/12/4-13:22
 */
@Service
@Slf4j
@ConditionalOnProperty(prefix = "kpet", name = "enable", havingValue = "true")
public class KeptClient extends WebSocketClient {
    public KeptClient(@Value("${kpet.url}") String url) throws URISyntaxException {
        super(new URI(url));
        Public.EXECUTOR_SERVICE.submit(this);
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
       log.info("kpet client opened");
    }

    @Override
    public void onMessage(String message) {

    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
    }

    @Override
    public void onError(Exception ex) {

    }

}
