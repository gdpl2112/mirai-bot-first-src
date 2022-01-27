package Project.interfaces;

import io.github.kloping.mirai0.Entitys.apiEntitys.WeatherDetail;
import io.github.kloping.mirai0.Entitys.apiEntitys.WeatherM;
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
    @GetPath("/api/shortWeather")
    WeatherM weatherM(@ParamName("address") String address);

    /**
     * 详细天气
     *
     * @param address
     * @return
     */
    @GetPath("/api/weather")
    WeatherDetail weatherDetail(@ParamName("address") String address);
}
