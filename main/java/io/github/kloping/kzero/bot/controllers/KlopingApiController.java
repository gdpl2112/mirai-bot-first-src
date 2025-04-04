package io.github.kloping.kzero.bot.controllers;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.github.kloping.date.DateUtils;
import io.github.kloping.judge.Judge;
import io.github.kloping.kzero.bot.KlopingWeb;
import io.github.kloping.kzero.bot.commons.apis.BottleMessage;
import io.github.kloping.kzero.bot.commons.apis.WeatherM;
import io.github.kloping.kzero.main.ResourceSet;
import io.github.kloping.kzero.main.api.KZeroBot;
import io.github.kloping.kzero.main.api.MessagePack;
import io.github.kloping.kzero.utils.Utils;
import io.github.kloping.spt.annotations.*;
import io.github.kloping.url.UrlUtils;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * @author github.kloping
 */
@Controller
public class KlopingApiController {
    public static final String BASE_URL_CLOUD = "http://img.nsmc.org.cn/CLOUDIMAGE/FY4A/MTCC/FY4A_CHINA.JPG";
    public static final String BASE_URL_CLOUD0 = "http://img.nsmc.org.cn/CLOUDIMAGE/FY4A/MTCC/FY4A_DISK.JPG";

    @AutoStand
    KlopingWeb klopingWeb;

    RestTemplate template = new RestTemplate();
/*
    @Action("天气<.+=>name>")
    public Object weather(@Param("name") String name) {
        try {
            WeatherDetail wd = klopingWeb.weatherDetail(name.trim());
            if (wd == null) return "天气获取失败!";
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
            return e.getMessage();
        }
     }*/

    @Action("天气<.+=>name>")
    public String weather0(@Param("name") String name) {
        String type = "zgtq";
        JSONObject jo = getWeatherData(name, type);
        return jo.getString("data") + "\n" + jo.getString("shzs");
    }

    @Action("中央天气<.+=>name>")
    public String weather1(@Param("name") String name) {
        String type = "zytq";
        JSONObject jo = getWeatherData(name, type);
        return jo.getString("data");
    }

    @Action("彩云天气<.+=>name>")
    public String weather2(@Param("name") String name) {
        String type = "cytq";
        JSONObject jo = getWeatherData(name, type);
        return jo.getString("data");
    }

    private JSONObject getWeatherData(String name, String type) {
        String url = String.format("https://www.hhlqilongzhu.cn/api/juhe_tianqi.php?msg=%s&n=1&type=%s", name, type);
        String data = template.getForObject(url, String.class);
        return JSON.parseObject(data);
    }

    @Action("短时预报<.+=>name>")
    public String shortWeather(@Param("name") String name) {
        try {
            WeatherM wm = klopingWeb.weatherM(name.trim());
            if (wm == null) return "短时获取失败!";
            StringBuilder sb = new StringBuilder();
            sb.append(wm.getName()).append(" 的短时预报:\n======\n");
            sb.append(wm.getIntro()).append("\n=======\n\t");
            sb.append("城市等级:").append(wm.getLevel()).append("\n\t");
            sb.append("经度:").append(wm.getLng()).append("\n\t");
            sb.append("纬度:").append(wm.getLat());
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    private static final SimpleDateFormat SF_0 = new SimpleDateFormat("yyyy-MM-dd:HH:mm:ss");

    @Action(value = "捡漂流瓶", otherName = {"捡瓶子"})
    public String getBottle() {
        BottleMessage pab = null;
        pab = klopingWeb.pickUpBottle();
        StringBuilder sb = new StringBuilder();
        sb.append("你捡到一个瓶子\n它来自群:").append(pab.getGid()).append("\n的:")
                .append(pab.getSid()).append("(").append(pab.getName()).append(")")
                .append("\n在:").append(SF_0.format(new Date(pab.getTime()))).append("\n写的:").append(pab.getMessage());
        return sb.toString();
    }

    @Action(value = "扔漂流瓶<.+=>str>", otherName = {"扔瓶子<.+=>str>"})
    public String setBottle(String sid, @Param("str") String str, MessagePack pack, KZeroBot bot) {
        if (str == null || str.trim().isEmpty()) return "请携带内容~";
        String name = bot.getAdapter().getNameCard(sid, pack.getSubjectId());
        name = name.replaceAll("\\s", "").isEmpty() ? "默认昵称" : name;
        return klopingWeb.throwBottle(pack.getSubjectId(), sid, str, name);
    }

    @Action("卫星云图")
    public String mn() {
        StringBuilder sb = new StringBuilder();
        sb.append("当前时间:" + DateUtils.getFormat());
        sb.append("\n");
        sb.append(String.format(ResourceSet.FinalFormat.PIC_FORMAT0, BASE_URL_CLOUD));
        return sb.toString();
    }

    @Action("全球卫星云图")
    public String m1() {
        StringBuilder sb = new StringBuilder();
        sb.append("当前时间:" + DateUtils.getFormat());
        sb.append("\n");
        sb.append(String.format(ResourceSet.FinalFormat.PIC_FORMAT0, BASE_URL_CLOUD0));
        return sb.toString();
    }

    @AutoStand
    InterceptController interceptController;

    @Action("搜图.*?")
    public Object searchPic(@AllMess String msg, String sid, KZeroBot bot, MessagePack pack) {
        String url = Utils.getFormat(msg, "pic");
        if (url == null) {
            String aid = Utils.getAtFormat(msg);
            if (aid != null) {
                url = bot.getAdapter().getAvatarUrlConverted(aid);
                return searchPicNow(url, bot);
            } else interceptController.register(sid, (p, b) -> {
                if (p.getSenderId().equals(sid)) {
                    String url0 = Utils.getFormat(p.getMsg(), "pic");
                    if (url0 == null) {
                        String aid0 = Utils.getAtFormat(p.getMsg());
                        if (aid0 != null) {
                            url0 = b.getAdapter().getAvatarUrlConverted(aid0);
                            return searchPicNow(url0, b);
                        } else return "未发现图片!";
                    } else {
                        return searchPicNow(url0, b);
                    }
                }
                return null;
            });
            return "请发送图片!";
        } else return searchPicNow(url, bot);
    }


    private Object searchPicNow(String url, KZeroBot bot) {
        List<String> list = new ArrayList();
        url = URLEncoder.encode(url, StandardCharsets.UTF_8);
        String jsonData = klopingWeb.searchPics(url);
        try {
            JSONArray array = JSON.parseArray(jsonData);
            for (Object o : array) {
                JSONObject e = (JSONObject) o;
                if (e.containsKey("website")) {
                    list.add(String.format("<pic:%s>\nsource:%s\ntitle:%s", e.getString("image_src"), e.getString("url"), e.getJSONArray("title")));
                } else {
                    list.add(String.format("<pic:%s>\nsource:%s", e.getString("thumbUrl"), e.getString("fromUrl")));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "搜索失败,请稍等重试!";
        }
        return list.toArray(new String[0]);
    }


//    @Action("<.+=>name>海报")
//    public Object posters(@Param("name") String name) {
//        String data = klopingWeb.posters(name);
//        if (Judge.isEmpty(data)) return "获取失败";
//        JSONArray arr = null;
//        try {
//            arr = JSON.parseArray(data);
//        } catch (Exception e) {
//            return "获取失败\n" + e.getMessage();
//        }
//        List<String> list = new LinkedList<>();
//        for (Object o : arr) {
//            JSONObject e = (JSONObject) o;
//            String u0 = e.getString("pic");
//            list.add(u0);
//            String e0 = String.format("%s\n<pic:%s>", e.getString("name"), u0);
//            list.add(e0);
//        }
//        return list.toArray(new String[0]);
//    }


    @AutoStand
    SubscribeController subscribeController;

    @Action("未来天气<.+=>name>")
    public String wea0(@Param("name") String addr) {
        try {
            return String.format("%s", subscribeController.futureWeaNow(addr));
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @Action("小蛇糕")
    public String wea1(@Param("name") String addr) {
        String json = UrlUtils.getStringFromHttpUrl("https://api.k452b.cn/api/posts");
        JSONObject jo = JSON.parseObject(json);
        JSONArray arr = jo.getJSONArray("posts");
        StringBuilder sb = new StringBuilder("\n");
        for (Object o : arr) {
            JSONObject e = (JSONObject) o;
            sb.append(e.getString("created_at")).append(" ").append(e.getString("text")).append("\n");
        }
        return sb.toString();
    }
}
