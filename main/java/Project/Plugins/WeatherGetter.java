package Project.Plugins;

import Entitys.apiEntitys.WeatherDetail;
import Entitys.apiEntitys.WeatherM;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

import static Project.Plugins.NetMain.*;

/**
 * @author github-kloping
 */
public class WeatherGetter {
    private static final String AD1 = ROOT_PATH + getWeatherShort;
    private static final String AD2 = ROOT_PATH + getWeatherDetail;

    public static String get(String address) {
        try {
            String urlStr = String.format(AD1, address);
            Document doc = Jsoup.connect(urlStr).ignoreContentType(true).timeout(7000).get();
            String m1 = doc.body().text();
            JSONObject jsonObject = JSON.parseObject(m1);
            System.out.println(jsonObject);
            WeatherM wm = jsonObject.toJavaObject(WeatherM.class);
            StringBuilder sb = new StringBuilder();
            sb.append(wm.getName()).append(" 的短时预报:\n======\n");
            sb.append(wm.getIntro()).append("\n=======\n\t");
            sb.append("城市等级:").append(wm.getLevel()).append("\n\t");
            sb.append("经度:").append(wm.getLng()).append("\n\t");
            sb.append("纬度:").append(wm.getLat());
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "获取异常";
    }

    public static String detail(String address) {
        try {
            String urlStr = String.format(AD2, address);
            Document doc = Jsoup.connect(urlStr).ignoreContentType(true).timeout(7000).get();
            String m1 = doc.body().text();
            JSONObject jsonObject = JSON.parseObject(m1);
            System.out.println(jsonObject);
            WeatherDetail wd = jsonObject.toJavaObject(WeatherDetail.class);
            StringBuilder sb = new StringBuilder();
            sb.append(wd.getTime()).append("\n");
            sb.append(wd.getAddress()).append(": ").append(wd.getDescribed()).append("\n\t");
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
