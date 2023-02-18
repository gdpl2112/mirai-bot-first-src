package Project.plugins;

import Project.interfaces.httpApi.AiBaidu;
import io.github.kloping.MySpringTool.annotations.AutoStand;
import io.github.kloping.MySpringTool.annotations.Entity;
import io.github.kloping.mirai0.commons.apiEntitys.baidu.AiRequest;
import io.github.kloping.mirai0.commons.apiEntitys.baidu.AiResponse;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * @author github.kloping
 */
@Entity
public class AiBaiduDetail {
    private static final Map<String, String> MAP = new HashMap<>();

    static {
        MAP.put("Host", "ai.baidu.com");
        MAP.put("Origin", "https://ai.baidu.com");
        MAP.put("Referer", "https://ai.baidu.com/tech/speech/tts_online");
    }

    @AutoStand
    AiBaidu aiBaidu;

    public byte[] getBytes(String text) {
        AiRequest request = new AiRequest();
        request.setTex(text);
        AiResponse res = aiBaidu.get(request, MAP);
        String base64Str = res.getData().substring("data:audio/x-mpeg;base64,".length());
        byte[] bytes = Base64.getDecoder().decode(base64Str);
        return bytes;
    }
}
