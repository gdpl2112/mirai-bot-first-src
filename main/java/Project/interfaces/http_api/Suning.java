package Project.interfaces.http_api;

import com.alibaba.fastjson.JSONObject;
import io.github.kloping.MySpringTool.annotations.http.GetPath;
import io.github.kloping.MySpringTool.annotations.http.HttpClient;

/**
 * @author github.kloping
 */
@HttpClient("https://f.m.suning.com/")
public interface Suning {
    /**
     * ct
     *
     * @return
     */
    @GetPath("/api/ct.do")
    JSONObject ct();
}
