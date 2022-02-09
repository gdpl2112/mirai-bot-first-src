package Project.interfaces;

import io.github.kloping.MySpringTool.annotations.http.Headers;
import io.github.kloping.MySpringTool.annotations.http.HttpClient;
import io.github.kloping.MySpringTool.annotations.http.PostPath;
import io.github.kloping.MySpringTool.annotations.http.RequestBody;

import java.util.Map;

/**
 * @author github kloping
 * @version 1.0
 */
@HttpClient("https://miao.baidu.com")
public interface BaiduShitu0 {
    /**
     * get
     * sign data
     *
     * @param header
     * @param body
     * @return
     */
    @PostPath("/abdr")
    String getDataSign(
            @Headers
                    Map<String, String> header,
            @RequestBody
                    String body
    );
}
