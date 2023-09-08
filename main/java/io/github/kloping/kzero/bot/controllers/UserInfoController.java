package io.github.kloping.kzero.bot.controllers;


import io.github.kloping.MySpringTool.annotations.Action;
import io.github.kloping.MySpringTool.annotations.AllMess;
import io.github.kloping.MySpringTool.annotations.AutoStand;
import io.github.kloping.MySpringTool.annotations.Controller;
import io.github.kloping.date.DateUtils;
import io.github.kloping.judge.Judge;
import io.github.kloping.kzero.bot.database.DataBase;
import io.github.kloping.kzero.bot.database.SourceDataBase;
import io.github.kloping.kzero.bot.services.UserService;
import io.github.kloping.kzero.game.ResourceSet;
import io.github.kloping.kzero.main.api.KZeroBot;
import io.github.kloping.kzero.spring.dao.UserScore;
import io.github.kloping.kzero.utils.ImageDrawerUtils;
import io.github.kloping.kzero.utils.Utils;
import io.github.kloping.number.NumberUtils;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.MalformedURLException;
import java.net.URL;


/**
 * @author github-kloping
 */
@Controller
public class UserInfoController {
    @AutoStand
    UserService scoreService;

    @Action("取积分.+")
    public String getScore(String sid, @AllMess String str) {
        Integer sc = NumberUtils.getIntegerFromString(str, 1);
        return scoreService.getScore(sid, sc);
    }

    @Action("存积分.+")
    public String putScore(String sid, @AllMess String str) {
        Integer sc = NumberUtils.getIntegerFromString(str, 1);
        return scoreService.putScore(sid, sc);
    }

    @AutoStand
    DataBase dataBase;

    @Action(value = "积分转让.+", otherName = {"转让积分.+"})
    public String transfer(String sid, @AllMess String str) {
        Integer num = null;
        String tid = Utils.getAtFromString(str);
        if (Judge.isEmpty(tid)) return ResourceSet.FinalString.NOT_FOUND_AT;
        if (!dataBase.exists(sid)) return ResourceSet.FinalString.PLAYER_NOT_REGISTERED;
        str = str.replaceFirst(tid, "");
        num = NumberUtils.getIntegerFromString(str, 0);
        return scoreService.getScoreTo(sid, tid, num);
    }

    @Action("积分排行.*?")
    public String scorePh(@AllMess String s) {
        Integer s0 = NumberUtils.getIntegerFromString(s);
        s0 = s0 == null ? 10 : s0;
        s0 = s0 > 20 ? 20 : s0;
        return scoreService.scorePh(s0);
    }

    @AutoStand
    SourceDataBase sourceDataBase;

    @Action("个人信息")
    public String info(String sid, KZeroBot bot) throws Exception {
        String icon = bot.getAdapter().getAvatarUrl(sid);
        String name = bot.getAdapter().getNameCard(sid);
        UserScore user = dataBase.getUserInfo(sid);
        BufferedImage image = getInfoImage(icon, name, user, "个人信息获取成功!", true);
        return "<pic:" + sourceDataBase.save(image) + ">";
    }

    @Action("签到")
    public String sign(String sid, KZeroBot bot) throws Exception {
        String icon = bot.getAdapter().getAvatarUrl(sid);
        String name = bot.getAdapter().getNameCard(sid);
        UserScore user = dataBase.getUserInfo(sid);
        BufferedImage image;
        if (user.getDay() == DateUtils.getDay()) {
            image = getInfoImage(icon, name, user, "今日已签到!签到失败", false);
        } else {
            user.setScore0(user.getScore0() + 300);
            user.setDay(DateUtils.getDay());
            user.setDays(user.getDays() + 1);
            dataBase.putInfo(user);
            image = getInfoImage(icon, name, user, "签到成功! 积分+300", true);
        }
        return "<pic:" + sourceDataBase.save(image) + ">";
    }

    /**
     *
     * @param icon
     * @param name
     * @param user
     * @param tips
     * @param t
     * @return
     * @throws MalformedURLException
     */
    @NotNull
    private BufferedImage getInfoImage(String icon, String name, UserScore user, String tips, boolean t) throws MalformedURLException {
        BufferedImage image = ImageDrawerUtils.readImage(sourceDataBase.getImgPathById("info_bg"), 800, 1000);
        BufferedImage icon0 = ImageDrawerUtils.readImage(new URL(icon), 180, 180);
        icon0 = ImageDrawerUtils.roundImage(icon0, 999);
        image = ImageDrawerUtils.putImage(image, icon0, 310, 50);
        Graphics graphics = image.getGraphics();

        graphics.setColor(ImageDrawerUtils.BLACK_A45);
        graphics.drawRoundRect(300, 250, 200, 40, 40, 20);

        graphics.setFont(ImageDrawerUtils.SMALL_FONT22);
        graphics.drawString(name, 330, 280);

        //=
        graphics.setFont(ImageDrawerUtils.SMALL_FONT24);

        graphics.setColor(ImageDrawerUtils.GREEN_A75);
        graphics.drawString("当前积分: " + user.getScore(), 30, 380);

        graphics.setColor(ImageDrawerUtils.BLUE_A75);
        graphics.drawString("存储积分: " + user.getScore0(), 300, 380);

        graphics.setColor(ImageDrawerUtils.BLACK_A45);
        graphics.drawString("预计利息:", 560, 380);
        int r1 = (user.getScore0() >= 10000 ? (int) (user.getScore0() / 10000 * 4) : 0);
        if (r1 == 0) graphics.setColor(ImageDrawerUtils.YELLOW_A75);
        else if (r1 < 100) graphics.setColor(ImageDrawerUtils.BLUE_A75);
        else graphics.setColor(ImageDrawerUtils.GREEN_A85);
        graphics.drawString(String.valueOf(r1), 700, 380);
        //=
        graphics.setColor(ImageDrawerUtils.BLACK_A75);
        graphics.drawString("签到状态: ", 30, 480);
        graphics.drawString("签到次数: ", 300, 480);

        boolean k1 = user.getDay() == DateUtils.getDay();
        if (k1) {
            graphics.setColor(ImageDrawerUtils.GREEN_A75);
            graphics.drawString("√", 150, 480);
        } else {
            graphics.setColor(ImageDrawerUtils.RED_A75);
            graphics.drawString("×", 150, 480);
        }

        int r2 = user.getDays();
        if (r2 < 100) graphics.setColor(ImageDrawerUtils.GREEN_A85);
        else if (r2 < 300) graphics.setColor(ImageDrawerUtils.BLUE_A75);
        else graphics.setColor(ImageDrawerUtils.RED_A75);
        graphics.drawString(String.valueOf(r2), 420, 480);


        graphics.setFont(ImageDrawerUtils.SMALL_FONT28);
        graphics.setColor(ImageDrawerUtils.BLACK_A75);
        graphics.drawRoundRect(180, 720, 440, 60, 30, 30);
        if (t) graphics.setColor(ImageDrawerUtils.GREEN_A85);
        else graphics.setColor(Color.RED);
        graphics.drawString(tips, 220, 760);

        graphics.dispose();
        return image;
    }
}
