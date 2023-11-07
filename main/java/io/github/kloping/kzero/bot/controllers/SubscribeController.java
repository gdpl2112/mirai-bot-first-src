package io.github.kloping.kzero.bot.controllers;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.github.kloping.MySpringTool.annotations.*;
import io.github.kloping.kzero.bot.commons.apis.WeatherDetail;
import io.github.kloping.kzero.bot.commons.apis.WeatherM;
import io.github.kloping.kzero.bot.interfaces.httpApi.KlopingWeb;
import io.github.kloping.kzero.main.api.KZeroBot;
import io.github.kloping.kzero.main.api.MessagePack;
import io.github.kloping.kzero.main.api.MessageType;
import io.github.kloping.kzero.spring.dao.SweatherData;
import io.github.kloping.kzero.spring.mapper.SweatherDataMapper;

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

    @CronSchedule("8 01 6 * * ? *")
    public void dayMorning() {
        for (SweatherData data : sweatherDataMapper.selectList(null)) {
            try {
                WeatherDetail wd = klopingWeb.weatherDetail(data.getAddress());
                if (wd == null) continue;
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
                    bot.getAdapter().sendMessage(MessageType.valueOf(data.getType()), data.getGid(),
                            String.format("<at:%s>\n%s", data.getSid(), sb.toString().trim())
                    );
                    sweatherDataMapper.updateById(data);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
