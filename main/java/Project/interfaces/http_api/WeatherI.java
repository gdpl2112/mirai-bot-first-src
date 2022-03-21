package Project.interfaces.http_api;

import io.github.kloping.mirai0.commons.apiEntitys.WeatherDetail;
import io.github.kloping.mirai0.commons.apiEntitys.WeatherM;
import io.github.kloping.MySpringTool.annotations.http.GetPath;
import io.github.kloping.MySpringTool.annotations.http.HttpClient;
import io.github.kloping.MySpringTool.annotations.http.ParamName;

import static Project.detailPlugin.NetMain.ROOT_PATH;

/**
 * @author github kloping
 * @version 1.0
 * @Date 2022/1/5-11
 */
@HttpClient(ROOT_PATH)
public interface WeatherI {
    /**
     * 短时天气
     *
     * @param address
     * @return
     */
    @GetPath("/api/get/shortWeather")
    WeatherM weatherM(@ParamName("address") String address);

    /**
     * 详细天气
     *
     * @param address
     * @return
     */
    @GetPath("/api/get/weather")
    WeatherDetail weatherDetail(@ParamName("address") String address);
}
