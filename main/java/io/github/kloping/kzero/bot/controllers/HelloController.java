package io.github.kzero.bot.controllers;

import io.github.kloping.MySpringTool.annotations.Action;
import io.github.kloping.MySpringTool.annotations.Controller;
import io.github.kloping.date.DateUtils;
import io.github.kloping.rand.RandomUtils;
import io.github.kzero.main.api.KZeroBot;

/**
 * @author github.kloping
 */
@Controller
public class HelloController {

    @Action("早.*?")
    public String morning() {
        if (RandomUtils.RANDOM.nextInt(10) > 5) return null;
        String tips;
        int hour = DateUtils.getHour();
        if (hour > 0 && hour <= 4) tips = RandomUtils.getRand("深夜了,注意休息", "一点都不早啊,更适合睡觉", "啊?");
        else if (hour <= 5) tips = RandomUtils.getRand("好早哦.", "真早啊!", "很早诶");
        else if (hour <= 7) tips = RandomUtils.getRand("早上好哦", "早呀", "早安");
        else if (hour <= 9) tips = RandomUtils.getRand("早!", "早");
        else if (hour <= 11) tips = RandomUtils.getRand("不早了呢~", "上午好奥");
        else if (hour <= 12) tips = RandomUtils.getRand("已经中午啦", "中午好", "午");
        else if (hour <= 17) tips = RandomUtils.getRand("?下午好", "笨蛋!已经下午啦", "早?");
        else if (hour <= 21) tips = RandomUtils.getRand("可是已经晚上了￣▽￣", "晚上啦", "???不早");
        else if (hour <= 23) tips = RandomUtils.getRand("已经很晚了,注意身体哦", "快睡觉啦", "(╯﹏╰)晚上好");
        else tips = "";
        return tips;
    }

    @Action("晚.*?")
    public String night() {
        if (RandomUtils.RANDOM.nextInt(10) > 5) return null;
        String tips;
        int hour = DateUtils.getHour();
        if (hour > 0 && hour <= 4) tips = RandomUtils.getRand("深夜好", "晚的很");
        else if (hour <= 5) tips = RandomUtils.getRand("?快早上啦", "不晚,很早");
        else if (hour <= 7) tips = RandomUtils.getRand("可是是早上啊", "早早早,重要的事情说三遍", "早");
        else if (hour <= 9) tips = RandomUtils.getRand("晚上?吗.", "您颠倒了嘛");
        else if (hour <= 11) tips = RandomUtils.getRand("?现在是上午哦", "'上午'好奥");
        else if (hour <= 12) tips = RandomUtils.getRand("中午啊,笨", "中午好!!!", "中午啊￣へ￣");
        else if (hour <= 17) tips = RandomUtils.getRand("嗯..确实快晚上了", "晚啊", "晚");
        else if (hour <= 21) tips = RandomUtils.getRand("晚上好!(oﾟ▽ﾟ)o  ", "晚好(～￣▽￣)～ ", "晚~");
        else if (hour <= 23) tips = RandomUtils.getRand("确实很晚了,注意身体哦", "要睡觉了");
        else tips = "";
        return tips;
    }
}
