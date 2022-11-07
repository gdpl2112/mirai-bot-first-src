package Project.interfaces.http_api;

import com.alibaba.fastjson.JSONObject;
import io.github.kloping.MySpringTool.annotations.http.GetPath;
import io.github.kloping.MySpringTool.annotations.http.HttpClient;
import io.github.kloping.MySpringTool.annotations.http.ParamName;

/**
 * @author github.kloping
 */
@HttpClient("https://xiaoapi.cn/")
public interface XiaoaPi {
    /**
     * url
     *
     * @param url
     * @return
     */
    @GetPath("API/zs_dspjx.php")
    JSONObject parseV(@ParamName("url") String url);
}
