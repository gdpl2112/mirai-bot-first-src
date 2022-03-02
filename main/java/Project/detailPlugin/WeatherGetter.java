package Project.detailPlugin;

import io.github.kloping.mirai0.Entitys.apiEntitys.WeatherDetail;
import io.github.kloping.mirai0.Entitys.apiEntitys.WeatherM;
import Project.interfaces.http_api.WeatherI;
import io.github.kloping.MySpringTool.annotations.AutoStand;
import io.github.kloping.MySpringTool.annotations.Entity;

import static Project.detailPlugin.NetMain.*;


/**
 * @author github-kloping
 */
@Entity
public class WeatherGetter {

    @AutoStand
    WeatherI weatherM;

    public String get(String address) {
        try {
            WeatherM wm = weatherM.weatherM(address);
            StringBuilder sb = new StringBuilder();
            sb.append(wm.getName()).append(" 的短时预报:\n======\n");
            sb.append(wm.getIntro()).append("\n=======\n\t");
            sb.append("城市等级:").append(wm.getLevel()).append("\n\t");
            sb.append("经度:").append(wm.getLng()).append("\n\t");
            sb.append("纬度:").append(wm.getLat());
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "获取异常";
    }

    public String detail(String address) {
        try {
            WeatherDetail wd = weatherM.weatherDetail(address);
            StringBuilder sb = new StringBuilder();
            sb.append(wd.getTime()).append("\n");
            sb.append(wd.getAddress()).append(": ").append(wd.getDescribed()).append("\n");
            sb.append(wd.getWind()).append("\n");
            sb.append(wd.getAir()).append("\n");
            sb.append(wd.getHumidity()).append("\n");
            sb.append(wd.getPm()).append("\n");
            sb.append("现在温度:").append(wd.getTemperatureNow()).append("\n");
            sb.append("今日温度:").append(wd.getTemperature()).append("\n");
            sb.append(wd.getUva()).append("\n");
            sb.append(wd.getSunOn()).append("\n");
            sb.append(wd.getSunDown()).append("\n");
            return sb.toString().trim();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "获取失败";
    }
}
