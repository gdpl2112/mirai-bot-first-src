package io.github.kloping.kzero.bot.controllers;


import io.github.kloping.MySpringTool.annotations.*;
import io.github.kloping.date.DateUtils;
import io.github.kloping.extension.ThreeMap;
import io.github.kloping.extension.ThreeMapImpl;
import io.github.kloping.judge.Judge;
import io.github.kloping.kzero.bot.database.DataBase;
import io.github.kloping.kzero.bot.database.SourceDataBase;
import io.github.kloping.kzero.bot.services.UserService;
import io.github.kloping.kzero.game.ResourceSet;
import io.github.kloping.kzero.main.api.KZeroBot;
import io.github.kloping.kzero.main.api.MessagePack;
import io.github.kloping.kzero.main.api.MessageType;
import io.github.kloping.kzero.spring.dao.UserScore;
import io.github.kloping.kzero.spring.mapper.UserScoreMapper;
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

    @AutoStand
    DataBase dataBase;

    @AutoStand
    SourceDataBase sourceDataBase;

    @AutoStand
    UserScoreMapper userScoreMapper;

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

    @Action(value = "抢劫.+", otherName = {"打劫.+"})
    public String robbery(String sid, @AllMess String str) {
        String tid = Utils.getAtFromString(str);
        if (Judge.isEmpty(tid)) return ResourceSet.FinalString.NOT_FOUND_AT;
        if (!dataBase.exists(tid)) return ResourceSet.FinalString.PLAYER_NOT_REGISTERED;
        str = str.replaceFirst(tid, "");
        Integer n = NumberUtils.getIntegerFromString(str, 1);
        if (dataBase.getUserInfo(sid).getFz() > ResourceSet.FinalValue.MAX_ROBBERY_TIMES) {
            return String.format(ResourceSet.FinalFormat.CANT_BIGGER, ResourceSet.FinalValue.MAX_ROBBERY_TIMES);
        } else if (n > 1) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < n; i++) sb.append(scoreService.robbery(sid, tid)).append("\n");
            return sb.toString().trim();
        } else return scoreService.robbery(sid, tid);
    }

    @CronSchedule("21 0 0 * * ? ")
    public void interest() {
        for (UserScore userScore : userScoreMapper.selectAll()) {
            if (userScore.getScore0() <= 10000) {
                continue;
            } else {
                int s = (int) (userScore.getScore0() / 10000 * 4);
                userScore.setScore0(userScore.getScore0() + s);
                userScoreMapper.updateById(userScore);
            }
        }
    }

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
            int r = 300;
            r = (int) (r + (NumberUtils.percentTo(user.getLevel(), r)));
            user.setScore0(user.getScore0() + r);
            user.setDay(DateUtils.getDay());
            user.setDays(user.getDays() + 1);
            user.addXp(1);
            user.setFz(0);
            dataBase.putInfo(user);
            image = getInfoImage(icon, name, user, "签到成功! 积分+" + r, true);
        }
        return "<pic:" + sourceDataBase.save(image) + ">";
    }

    private ThreeMap<String, MessagePack, KZeroBot> workList = new ThreeMapImpl<>();

    @CronSchedule("0/20 * * * * ? ")
    public void work_test() {
        workList.foreach((sid, pack, bot) -> {
            UserScore user = dataBase.getUserInfo(sid);
            if (System.currentTimeMillis() > user.getK()) {
                try {
                    String icon = bot.getAdapter().getAvatarUrl(sid);
                    String name = bot.getAdapter().getNameCard(sid);
                    int r = 300;
                    r = (int) (r + (NumberUtils.percentTo(user.getLevel(), r)));
                    user.addXp(1);
                    user.setScore(r + user.getScore());
                    dataBase.putInfo(user);
                    BufferedImage image = getInfoImage(icon, name, user, "打工完成,经验+1,积分+" + r, true);
                    bot.getAdapter().sendMessage(MessageType.GROUP, pack.getSubjectId(), "<at:" + pack.getSenderId() + ">\n<pic:" + sourceDataBase.save(image) + ">");
                    workList.remove(sid);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Action("打工")
    public String work(String sid, MessagePack pack, KZeroBot bot) throws Exception {
        String icon = bot.getAdapter().getAvatarUrl(sid);
        String name = bot.getAdapter().getNameCard(sid);
        UserScore user = dataBase.getUserInfo(sid);
        long k0 = user.getK();
        BufferedImage image = null;
        if (k0 > System.currentTimeMillis()) {
            image = getInfoImage(icon, name, user, "打工进行中...", false);
        } else {
            user.setK(System.currentTimeMillis() + 20L * 60 * 1000);
            dataBase.putInfo(user);
            workList.put(sid, pack, bot);
            image = getInfoImage(icon, name, user, "开始打工预计花费20分钟", true);
        }
        return "<pic:" + sourceDataBase.save(image) + ">";
    }

    /**
     * @param icon
     * @param name
     * @param user
     * @param tips
     * @param t
     * @return
     * @throws MalformedURLException
     */
    @NotNull
    public BufferedImage getInfoImage(String icon, String name, UserScore user, String tips, boolean t) throws MalformedURLException {
        BufferedImage image = ImageDrawerUtils.readImage(sourceDataBase.getImgPathById("info_bg"), 800, 1000);
        BufferedImage icon0 = ImageDrawerUtils.readImage(new URL(icon), 180, 180);
        icon0 = ImageDrawerUtils.roundImage(icon0, 999);
        image = ImageDrawerUtils.putImage(image, icon0, 310, 50);
        Graphics graphics = image.getGraphics();

        graphics.setColor(ImageDrawerUtils.BLACK_A75);
        graphics.drawString(String.format("%s/%s", user.getXp(), user.getXpl()), 150, 220);
        graphics.setColor(Color.WHITE);
        graphics.drawRoundRect(150, 240, 500, 20, 5, 5);
        int r0 = NumberUtils.toPercent(user.getXp(), user.getXpl());
        if (r0 < 30) graphics.setColor(ImageDrawerUtils.YELLOW_A75);
        else if (r0 < 60) graphics.setColor(Color.GREEN);
        else graphics.setColor(ImageDrawerUtils.ORIGIN_A75);
        graphics.fillRoundRect(150, 240, r0 * 5, 20, 5, 5);


        graphics.setColor(ImageDrawerUtils.BLACK_A45);
        graphics.drawRoundRect(300, 270, 200, 40, 40, 20);

        graphics.setFont(ImageDrawerUtils.SMALL_FONT22);
        graphics.drawString(name, 330, 300);

        //========line-1-start====
        graphics.setFont(ImageDrawerUtils.SMALL_FONT24);

        graphics.setColor(ImageDrawerUtils.GREEN_A75);
        graphics.drawString("当前积分: " + user.getScore(), 30, 380);

        graphics.setColor(ImageDrawerUtils.BLUE_A75);
        graphics.drawString("存储积分: " + user.getScore0(), 300, 380);

        graphics.setColor(ImageDrawerUtils.BLACK_A45);
        graphics.drawString("预计利息:", 560, 380);
        int r1 = (user.getScore0() >= 10000 ? (int) (user.getScore0() / 10000 * 4) : 0);
        if (r1 == 0) graphics.setColor(ImageDrawerUtils.GREEN_A85);
        else if (r1 < 100) graphics.setColor(ImageDrawerUtils.BLUE_A75);
        else graphics.setColor(ImageDrawerUtils.RED_A75);
        graphics.drawString(String.valueOf(r1), 700, 380);
        //========line-1-end=========
        //========line-2-start===========
        graphics.setColor(ImageDrawerUtils.BLACK_A75);
        graphics.drawString("签到状态: ", 30, 480);
        graphics.drawString("签到次数: ", 300, 480);
        graphics.drawString("积分加成: ", 560, 480);

        if (user.getDay() == DateUtils.getDay()) {
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

        int r3 = user.getLevel();
        if (r3 < 3) graphics.setColor(ImageDrawerUtils.GREEN_A85);
        else if (r3 < 7) graphics.setColor(ImageDrawerUtils.BLUE_A75);
        else graphics.setColor(ImageDrawerUtils.RED_A75);
        graphics.drawString(r3 + "%", 700, 480);
        //========line-2-end===========

        //========line-3-start===========
        graphics.setColor(ImageDrawerUtils.BLACK_A75);
        graphics.drawString("打工状态: ", 30, 580);

        if (user.getK() > System.currentTimeMillis()) {
            graphics.setColor(ImageDrawerUtils.GREEN_A75);
            graphics.drawString("进行中.", 150, 580);
        } else {
            graphics.setColor(ImageDrawerUtils.RED_A75);
            graphics.drawString("空闲中.", 150, 580);
        }
        //========line-3-end===========

        //========line-tips-start===========
        graphics.setFont(ImageDrawerUtils.SMALL_FONT28);
        graphics.setColor(ImageDrawerUtils.BLACK_A75);
        graphics.drawRoundRect(180, 720, 440, 60, 30, 30);
        if (t) graphics.setColor(ImageDrawerUtils.GREEN_A85);
        else graphics.setColor(Color.RED);
        graphics.drawString(tips, 220, 760);
        //========line-tips-end===========

        graphics.dispose();
        return image;
    }
}
