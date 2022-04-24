package Project.interfaces.http_api;

import io.github.kloping.MySpringTool.annotations.http.GetPath;
import io.github.kloping.MySpringTool.annotations.http.HttpClient;
import io.github.kloping.MySpringTool.annotations.http.ParamName;
import io.github.kloping.mirai0.commons.apiEntitys.jiuli.tianqi.Weather;

/**
 * @author github kloping
 * @version 1.0
 */
@HttpClient("http://jiuli.xiaoapi.cn/i/web")
public interface JuiLi {
    /**
     * 获取五日天气
     *
     * @param city
     * @return
     */
    @GetPath("tianqi.php")
    Weather weather(@ParamName("city") String city);
}
