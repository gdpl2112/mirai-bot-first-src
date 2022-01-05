package Project.interfaces;

import Entitys.apiEntitys.WeatherDetail;
import Entitys.apiEntitys.WeatherM;
import io.github.kloping.MySpringTool.annotations.Param;
import io.github.kloping.MySpringTool.annotations.http.GetPath;
import io.github.kloping.MySpringTool.annotations.http.HttpClient;
import io.github.kloping.MySpringTool.annotations.http.ParamName;

import static Project.Plugins.NetMain.ROOT_PATH;

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
