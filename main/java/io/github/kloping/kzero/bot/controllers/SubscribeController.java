package io.github.kloping.kzero.bot.controllers;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.github.kloping.MySpringTool.annotations.*;
import io.github.kloping.kzero.bot.commons.apis.WeatherDetail;
import io.github.kloping.kzero.bot.commons.apis.WeatherM;
import io.github.kloping.kzero.bot.database.SourceDataBase;
import io.github.kloping.kzero.bot.interfaces.httpApi.KlopingWeb;
import io.github.kloping.kzero.main.api.KZeroBot;
import io.github.kloping.kzero.main.api.MessagePack;
import io.github.kloping.kzero.main.api.MessageType;
import io.github.kloping.kzero.spring.dao.SweatherData;
import io.github.kloping.kzero.spring.mapper.SweatherDataMapper;
import io.github.kloping.kzero.utils.ImageDrawerUtils;
import io.github.kloping.rand.RandomUtils;
import io.github.kloping.url.UrlUtils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URLEncoder;
import java.util.AbstractMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author github.kloping
 */
@Controller
public class SubscribeController {

    @Action("天气订阅<.+=>ad>")
    public Object sub0(@Param("ad") String ad, MessagePack pack, KZeroBot bot) {
        SweatherData data = new SweatherData();
        data.setGid(pack.getSubjectId()).setSid(pack.getSenderId())
                .setAddress(ad.trim()).setBid(bot.getId()).setType(pack.getType().name()).setD0("");
        sweatherDataMapper.insert(data);
        return "订阅成功!\n可使用'取消天气订阅'";
    }

    @Action("取消天气订阅")
    public Object cancelSub0(MessagePack pack, KZeroBot bot) {
        QueryWrapper<SweatherData> qw = new QueryWrapper<>();
        qw.eq("sid", pack.getSenderId());
        qw.eq("gid", pack.getSubjectId());
        return sweatherDataMapper.delete(qw) > 0 ? "取消成功!" : "取消异常";
    }

    @AutoStand
    SweatherDataMapper sweatherDataMapper;

    @AutoStand
    KlopingWeb klopingWeb;

    @AutoStand
    KZeroBot bot;

    @AutoStand
    AllController allController;

    @CronSchedule("5 6,36 6,7,8,9,10,11,12,13,14,15,16,17,18,19 * * ? *")
    public void hourEve() {
        for (SweatherData data : sweatherDataMapper.selectList(null)) {
            try {
                if (!allController.isWakeUp(data.getSid())) continue;
                WeatherM weatherM = klopingWeb.weatherM(data.getAddress());
                if (weatherM == null) continue;
                if (weatherM.getIntro().equals(data.getD0())) continue;
                else {
                    data.setD0(weatherM.getIntro());
                    bot.getAdapter().sendMessage(MessageType.valueOf(data.getType()), data.getGid(),
                            String.format("<at:%s>\n%s\n\t%s", data.getSid(), weatherM.getName(), weatherM.getIntro()));
                    sweatherDataMapper.updateById(data);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @CronSchedule("12 15 7 * * ? *")
    public void dayMorning() {
        for (SweatherData data : sweatherDataMapper.selectList(null)) {
            todayWeaNow(data);
            try {
                String msg = futureWeaNow(data.getAddress());
                bot.getAdapter().sendMessage(MessageType.GROUP, data.getSid(), String.format("<at:%s>\n%s", data.getSid(), msg));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @AutoStand
    SourceDataBase sourceDataBase;

    protected String futureWeaNow(String addr) throws Exception {
        int r0 = RandomUtils.RANDOM.nextInt(3);
        BufferedImage image = ImageDrawerUtils.readImage(sourceDataBase.getImgPathById("info_bg"), 825, 900);
        String json = UrlUtils.getStringFromHttpUrl("https://v2.api-m.com/api/weather?city=" + URLEncoder.encode(addr));
        JSONObject jsono = JSON.parseObject(json);
        JSONObject data = jsono.getJSONObject("data");
        JSONArray dataArr = jsono.getJSONObject("data").getJSONArray("data");

        Graphics graphics = image.getGraphics();
        graphics.setColor(ImageDrawerUtils.BLACK_A35);

        graphics.fillRoundRect(10, 10, 265, 430, 30, 30);
        graphics.fillRoundRect(285, 10, 530, 430, 30, 30);

        graphics.setColor(ImageDrawerUtils.BLACK_A75);
        graphics.drawLine(285, 225, 815, 225);

        graphics.setColor(ImageDrawerUtils.ORIGIN_A80);
        graphics.setFont(ImageDrawerUtils.BIG_FONT80_TYPE0);
        graphics.drawString(data.getString("city"), 60, 150);

        graphics.setColor(ImageDrawerUtils.RED_A75);
        graphics.setFont(ImageDrawerUtils.SMALL_FONT38_TYPE0);
        graphics.drawString("昨/今/未来", 45, 250);

        graphics.setColor(ImageDrawerUtils.BLUE_A75);
        graphics.setFont(ImageDrawerUtils.SMALL_FONT46);
        graphics.drawString("天气", 95, 340);

        int x = 285;
        int y = 5;

        List<Map.Entry<Integer, Integer>> tempList = new LinkedList<>();
        for (int i = 0; i < 2; i++) {
            JSONObject dr0 = dataArr.getJSONObject(i);
            graphics.setFont(ImageDrawerUtils.SMALL_FONT46);
            ImageDrawerUtils.drawStringContinuousDiscoloration(
                    graphics, x + 20, y + 60
                    , i == 0 ? "昨: " : "今: ", ImageDrawerUtils.BLACK_A60
                    , dr0.getString("date"), ImageDrawerUtils.RED_A75
                    , "/", ImageDrawerUtils.BLACK_A85
                    , "天气: ", ImageDrawerUtils.WHITE_A60
                    , dr0.getString("weather"), ImageDrawerUtils.BLUE2_A75
            );

            String temp0 = dr0.getString("temperature");
            int i0 = temp0.indexOf("-");
            if (i0 == 0) i0 = temp0.substring(1).indexOf("-") + 1;
            String lowest0 = temp0.substring(0, i0);
            String highest0 = temp0.substring(i0 + 1, temp0.length() - 1);
            Integer lowest = Integer.valueOf(lowest0);
            Integer highest = Integer.valueOf(highest0);
            tempList.add(new AbstractMap.SimpleEntry<>(lowest, highest));
            ImageDrawerUtils.drawStringContinuousDiscoloration(
                    graphics, x + 20, y + 120
                    , "温度: ", ImageDrawerUtils.ORIGIN_A75
                    , lowest0, getColorByTemperature(lowest)
                    , "-", ImageDrawerUtils.BLACK_A75
                    , highest0, getColorByTemperature(highest)
                    , "℃", ImageDrawerUtils.BLACK_A75
            );


            graphics.setFont(ImageDrawerUtils.SMALL_FONT38);
            ImageDrawerUtils.drawStringContinuousDiscoloration(
                    graphics, x + 20, y + 180
                    , "空气质量:", ImageDrawerUtils.WHITE_A80
                    , dr0.getString("air_quality"), ImageDrawerUtils.BLUE_A75
                    , "/", ImageDrawerUtils.BLACK_A85
                    , dr0.getString("wind"), ImageDrawerUtils.BLUE2_A75
            );
            y += 210;
        }

        x = 5;
        y = 455;
        for (int i = 0; i < 4; i++) {
            JSONObject dr0 = dataArr.getJSONObject(i + 2);
            graphics.setColor(ImageDrawerUtils.BLACK_A35);
            graphics.fillRoundRect(x, y, 200, 235, 30, 30);

            graphics.setFont(ImageDrawerUtils.SMALL_FONT38);
            graphics.setColor(ImageDrawerUtils.BLACK_A75);

            graphics.drawString(dr0.getString("date"), x + 60, y + 40);

            graphics.setColor(ImageDrawerUtils.BLACK_A90);
            graphics.drawLine(x, y + 50, x + 200, y + 50);

            graphics.setFont(ImageDrawerUtils.SMALL_FONT26);
            String temp0 = dr0.getString("temperature");
            int i0 = temp0.indexOf("-");
            if (i0 == 0) i0 = temp0.substring(1).indexOf("-") + 1;
            String lowest0 = temp0.substring(0, i0);
            String highest0 = temp0.substring(i0 + 1, temp0.length() - 1);
            Integer lowest = Integer.valueOf(lowest0);
            Integer highest = Integer.valueOf(highest0);
            tempList.add(new AbstractMap.SimpleEntry<>(lowest, highest));
            ImageDrawerUtils.drawStringContinuousDiscoloration(
                    graphics, x + 10, y + 90
                    , "温度: ", ImageDrawerUtils.ORIGIN_A75
                    , lowest0, getColorByTemperature(lowest)
                    , "-", ImageDrawerUtils.BLACK_A75
                    , highest0, getColorByTemperature(highest)
                    , "℃", ImageDrawerUtils.BLACK_A75
            );

            ImageDrawerUtils.drawStringContinuousDiscoloration(
                    graphics, x + 10, y + 130
                    , "天气: ", ImageDrawerUtils.WHITE_A60
                    , dr0.getString("weather"), ImageDrawerUtils.BLUE2_A75
            );

            ImageDrawerUtils.drawStringContinuousDiscoloration(
                    graphics, x + 10, y + 170
                    , "空气质量:", ImageDrawerUtils.WHITE_A80
                    , dr0.getString("air_quality"), ImageDrawerUtils.BLUE_A75
            );

            graphics.setColor(ImageDrawerUtils.BLUE2_A75);
            graphics.drawString(dr0.getString("wind"), x + 10, y + 210);

            x += 205;
        }

        graphics.setFont(ImageDrawerUtils.SMALL_FONT18);
        graphics.setColor(ImageDrawerUtils.BLACK_A45);
        y = 695;
        x = 12;
        graphics.fillRoundRect(x, y, 800, 200, 6, 6);
        graphics.drawLine(x, y + 50, x + 800, y + 50);
        graphics.drawString("10℃", x, y + 50);
        graphics.drawLine(x, y + 100, x + 800, y + 100);
        graphics.drawString("0℃", x, y + 100);
        graphics.drawLine(x, y + 150, x + 800, y + 150);
        graphics.drawString("-10℃", x, y + 150);
        x = 162;
        for (int i = 0; i < 6; i++) {
            graphics.drawLine(x, y, x, y + 200);
            graphics.drawString(WS[i], x, y + 190);
            x += 100;
        }

        ImageDrawerUtils.drawStringContinuousDiscoloration(graphics, 20, 710
                , "--", ImageDrawerUtils.RED_A90
                , "最高温", ImageDrawerUtils.RED_A90
        );
        ImageDrawerUtils.drawStringContinuousDiscoloration(graphics, 20, 725
                , "--", ImageDrawerUtils.BLUE3_A90
                , "最低温", ImageDrawerUtils.BLUE3_A90
        );

        graphics.setFont(ImageDrawerUtils.SMALL_FONT46);
        x = 162;
        y = 795;
        int eve0 = 100;
        Map.Entry<Integer, Integer> uekv = null;
        for (Map.Entry<Integer, Integer> ekv : tempList) {
            if (uekv != null) {
                //0 y+100
                //+1 y+5
                int l1 = uekv.getKey();
                int l2 = ekv.getKey();
                graphics.setColor(ImageDrawerUtils.BLUE3_A90);
                graphics.drawLine(x, y - l1 * 5, x + eve0, y - l2 * 5);

                int h1 = uekv.getValue();
                int h2 = ekv.getValue();
                graphics.setColor(ImageDrawerUtils.RED_A90);
                graphics.drawLine(x, y - h1 * 5, x + eve0, y - h2 * 5);
                x += eve0;
            }
            uekv = ekv;
        }
        return "<pic:" + sourceDataBase.save(image) + ">";
    }

    private final String[] WS = {"昨", "今", "+1", "+2", "+3", "+4"};

    private Color getColorByTemperature(Integer c) {
        if (c >= 30) return ImageDrawerUtils.ORIGIN_A80;
        else if (c >= 20) return ImageDrawerUtils.YELLOW_A75;
        else if (c >= 10) return ImageDrawerUtils.GREEN_A75;
        else if (c >= 0) return ImageDrawerUtils.BLUE3_A75;
        else if (c >= -10) return ImageDrawerUtils.BLUE4_A75;
        else return ImageDrawerUtils.BLUE5_A75;
    }

    private void todayWeaNow(SweatherData data) {
        try {
            WeatherDetail wd = klopingWeb.weatherDetail(data.getAddress());
            if (wd == null) return;
            else {
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
                bot.getAdapter().sendMessage(MessageType.FRIEND, data.getSid(), sb.toString().trim());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
