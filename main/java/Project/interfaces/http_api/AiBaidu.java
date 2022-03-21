package Project.interfaces.http_api;

import io.github.kloping.MySpringTool.annotations.http.*;
import io.github.kloping.mirai0.commons.apiEntitys.baidu.AiRequest;
import io.github.kloping.mirai0.commons.apiEntitys.baidu.AiResponse;

import java.util.Map;

/**
 * @author github.kloping
 */
@HttpClient("https://ai.baidu.com/")
public interface AiBaidu {
    /**
     * 语音
     *
     * @param request
     * @param headers
     * @return
     */
    @PostPath("aidemo")
    AiResponse get(@RequestData AiRequest request
            , @Headers Map<String, String> headers
    );
}
