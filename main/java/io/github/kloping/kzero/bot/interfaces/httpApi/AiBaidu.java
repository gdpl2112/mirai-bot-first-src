package io.github.kloping.kzero.bot.interfaces.httpApi;

import io.github.kloping.MySpringTool.annotations.http.Headers;
import io.github.kloping.MySpringTool.annotations.http.HttpClient;
import io.github.kloping.MySpringTool.annotations.http.PostPath;
import io.github.kloping.MySpringTool.annotations.http.RequestData;
import io.github.kloping.kzero.bot.commons.apiEntitys.baidu.AiRequest;
import io.github.kloping.kzero.bot.commons.apiEntitys.baidu.AiResponse;

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
