package io.github.kloping.kzero.hwxb;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * @author github.kloping
 */
@Component
@ConditionalOnProperty(name = "wxbot.token")
@Data
public class WxAuth {
    @Value("${wxbot.token}")
    String token;

    @Value("${wxbot.url}")
    String url;

    @Value("${wxbot.ip}")
    String ip;

    @Value("${wxbot.port}")
    Integer port0;

    @Value("${server.port}")
    Integer port;

    @Value("${server.self}")
    String self;
}
