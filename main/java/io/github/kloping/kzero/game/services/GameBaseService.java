package io.github.kloping.kzero.game.services;

import io.github.kloping.MySpringTool.annotations.AutoStand;
import io.github.kloping.MySpringTool.annotations.Entity;
import io.github.kloping.date.DateUtils;
import io.github.kloping.judge.Judge;
import io.github.kloping.kzero.bot.database.SourceDataBase;
import io.github.kloping.kzero.game.commons.GameUserInfo;
import io.github.kloping.kzero.game.database.GameDataBase;
import io.github.kloping.kzero.game.database.GameFinalSource;
import io.github.kloping.kzero.main.ResourceSet;
import io.github.kloping.kzero.spring.dao.PersonInfo;
import io.github.kloping.kzero.spring.dao.WhInfo;
import io.github.kloping.kzero.utils.ImageDrawerUtils;
import io.github.kloping.kzero.utils.Utils;
import io.github.kloping.rand.RandomUtils;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * @author github.kloping
 */
@Entity
public class GameBaseService {
    @AutoStand
    GameDataBase gameDataBase;

    public String xl(String sid) {
        return gameDataBase.operate(sid, (i) -> {
            if (i.getWhInfo().getWh() == 0 && i.getWhInfo().getLevel() >= 2) {
                return ResourceSet.FinalString.PLEASE_AWAKENING_WH;
            } else {
                long now = System.currentTimeMillis();
                if (now >= i.getPersonInfo().getK1()) {

                    int tr = RandomUtils.RANDOM.nextInt(6) + 9;
                    i.getPersonInfo().setK1(now + (tr * 1000 * 60));

                    int c = (GameFinalSource.getRandXl(i.getWhInfo().getLevel()));
                    long mx = i.getWhInfo().getXpl();
                    long xr = mx / c;
                    i.getWhInfo().addXp(xr);

                    int c1 = c;
                    if (c1 > 30) c1 = 30;
                    if (c1 < 4) c1 = 4;
                    long l5 = i.getWhInfo().getHpl() / c1;
                    i.getWhInfo().addHp(l5);

                    if (c1 > 24) c1 = 24;
                    if (c1 < 4) c1 = 4;
                    l5 = i.getWhInfo().getHll() / c1;
                    i.getWhInfo().addHl(l5);

                    if (c1 > 20) c1 = 20;
                    if (c1 < 8) c1 = 8;
                    l5 = i.getWhInfo().getHjl() / c1;
                    i.getWhInfo().addHj(l5);

                    return String.format("修炼成功!\n当前状态:\n经验:%s%%\n血量:%s%%\n魂力:%s%%\n精神力:%s%%",
                            i.getWhInfo().getXpPercent(),
                            i.getWhInfo().getHpPercent(),
                            i.getWhInfo().getHlPercent(),
                            i.getWhInfo().getHjPercent());
                } else {
                    return String.format(ResourceSet.FinalFormat.XL_WAIT_TIPS, Utils.getTimeTips(i.getPersonInfo().getK1()));
                }
            }
        });
    }

    @AutoStand
    SourceDataBase sourceDataBase;

    public BufferedImage drawGameUserInfo(GameUserInfo userInfo) {
        WhInfo whInfo = userInfo.getWhInfo();
        PersonInfo is = userInfo.getPersonInfo();
        BufferedImage image = ImageDrawerUtils.readImage(sourceDataBase.getImgPathById("info_bg" + (RandomUtils.RANDOM.nextInt(2) + 1)), 800, 1000);
        int whid = userInfo.getWhInfo().getWh();
        whid = whid > 0 ? whid : 0;
        BufferedImage icon = ImageDrawerUtils.readImage(sourceDataBase.getImgPathById(whid, false), 300, 300);

        int y = 40;
        int x = 15;

        icon = ImageDrawerUtils.roundImage(icon, 999);
        image = ImageDrawerUtils.putImage(image, icon, x, 10);
        Graphics graphics = image.getGraphics();

        //===============xp-start===============
        x += 315;
        int r0 = whInfo.getXpPercent();
        graphics.setColor(ImageDrawerUtils.BLACK_A75);
        graphics.setFont(ImageDrawerUtils.SMALL_FONT18);
        ImageDrawerUtils.drawStringContinuousDiscoloration(graphics, x, y - 5, "经验: ", ImageDrawerUtils.BLACK_A75, String.valueOf(whInfo.getXp()), ImageDrawerUtils.BLACK_A45, "/", ImageDrawerUtils.BLACK_A75, String.valueOf(whInfo.getXpl()), ImageDrawerUtils.RED_A75, String.format("(%s%%)", String.valueOf(r0)), Color.BLACK);

        graphics.setColor(ImageDrawerUtils.WHITE_A80);
        graphics.drawRoundRect(x, y, 400, 20, 5, 5);

        if (r0 < 30) graphics.setColor(ImageDrawerUtils.YELLOW_A75);
        else if (r0 < 60) graphics.setColor(Color.GREEN);
        else if (r0 < 90) graphics.setColor(ImageDrawerUtils.ORIGIN_A75);
        else graphics.setColor(ImageDrawerUtils.RED_A75);
        graphics.fillRoundRect(x, y, r0 * 4, 20, 5, 5);
        //===============xp-end===============
        //===============hp-start=============
        y += 50;
        r0 = whInfo.getHpPercent();
        graphics.setColor(ImageDrawerUtils.BLACK_A75);
        graphics.setFont(ImageDrawerUtils.SMALL_FONT22);
        ImageDrawerUtils.drawStringContinuousDiscoloration(graphics, x, y - 5, "血量: ", ImageDrawerUtils.BLACK_A75, String.valueOf(whInfo.getHp()), ImageDrawerUtils.GREEN_A85, "/", ImageDrawerUtils.BLACK_A75, String.valueOf(whInfo.getHpl()), ImageDrawerUtils.RED_A75, String.format("(%s%%)", String.valueOf(r0)), Color.BLACK);
        graphics.setColor(ImageDrawerUtils.WHITE_A80);
        graphics.drawRoundRect(x, y, 400, 20, 5, 5);
        if (r0 < 20) graphics.setColor(ImageDrawerUtils.RED_A75);
        else if (r0 < 40) graphics.setColor(ImageDrawerUtils.YELLOW_A75);
        else graphics.setColor(ImageDrawerUtils.GREEN_A85);
        graphics.fillRoundRect(x, y, r0 * 4, 20, 5, 5);
        //===============hp-end=============
        //===============hl-start=============
        y += 50;
        r0 = whInfo.getHlPercent();
        graphics.setColor(ImageDrawerUtils.BLACK_A75);
        graphics.setFont(ImageDrawerUtils.SMALL_FONT22);
        ImageDrawerUtils.drawStringContinuousDiscoloration(graphics, x, y - 5, "魂力: ", ImageDrawerUtils.BLACK_A75, String.valueOf(whInfo.getHl()), ImageDrawerUtils.YELLOW_A85, "/", ImageDrawerUtils.BLACK_A75, String.valueOf(whInfo.getHll()), ImageDrawerUtils.RED_A75, String.format("(%s%%)", String.valueOf(r0)), Color.BLACK);

        graphics.setColor(ImageDrawerUtils.WHITE_A80);
        graphics.drawRoundRect(x, y, 400, 20, 5, 5);
        graphics.setColor(ImageDrawerUtils.YELLOW_A75);
        graphics.fillRoundRect(x, y, r0 * 4, 20, 5, 5);
        //===============hl-end=============
        //===============hj-start=============
        y += 50;
        r0 = whInfo.getHjPercent();
        graphics.setColor(ImageDrawerUtils.BLACK_A75);
        graphics.setFont(ImageDrawerUtils.SMALL_FONT22);
        ImageDrawerUtils.drawStringContinuousDiscoloration(graphics, x, y - 5, "精神力:", ImageDrawerUtils.BLACK_A75, String.valueOf(whInfo.getHj()), ImageDrawerUtils.BLUE2_A75, "/", ImageDrawerUtils.BLACK_A75, String.valueOf(whInfo.getHjl()), ImageDrawerUtils.RED_A75, String.format("(%s%%)", String.valueOf(r0)), Color.BLACK);
        graphics.setColor(ImageDrawerUtils.WHITE_A80);
        graphics.drawRoundRect(x, y, 400, 20, 5, 5);
        graphics.setColor(ImageDrawerUtils.BLUE2_A75);
        graphics.fillRoundRect(x, y, r0 * 4, 20, 5, 5);
        //===============hj-end=============

        //===============det1-start=============
        y += 60;
        graphics.setFont(ImageDrawerUtils.SMALL_FONT22);

        graphics.setColor(ImageDrawerUtils.BLACK_A75);
        graphics.drawString("武魂: ", x, y - 5);
        graphics.setColor(ImageDrawerUtils.RED_A75);
        graphics.drawString(whid == 0 ? "未觉醒" : GameFinalSource.ID_2_NAME_MAPS.get(whid), x + 60, y - 5);

        graphics.setColor(ImageDrawerUtils.BLACK_A75);
        graphics.drawString("攻击力:", x + 180, y - 5);
        graphics.setColor(ImageDrawerUtils.RED_A75);
        graphics.drawString(String.valueOf(whInfo.getAtt()), x + 260, y - 5);

        y += 40;
        graphics.setColor(ImageDrawerUtils.BLACK_A75);
        graphics.drawString("等级: ", x, y - 5);
        graphics.setColor(ImageDrawerUtils.RED_A75);
        graphics.drawString(String.valueOf(whInfo.getLevel()), x + 60, y - 5);

        graphics.setColor(ImageDrawerUtils.BLACK_A75);
        graphics.drawString("称号: ", x + 180, y - 5);
        graphics.setColor(ImageDrawerUtils.RED_A75);
        graphics.drawString(GameFinalSource.getFhName(whInfo.getLevel()), x + 260, y - 5);
        //===============det1-end=============
        if (Judge.isNotEmpty(is.getName())) {
            graphics.setColor(ImageDrawerUtils.BLACK_A85);
            graphics.setFont(ImageDrawerUtils.SMALL_FONT28);
            graphics.drawString(is.getName() + GameFinalSource.getFhName(whInfo.getLevel()), graphics.getFontMetrics().stringWidth(is.getName()) / 2 + 150, 360);
        }
        graphics.setFont(ImageDrawerUtils.SMALL_FONT24);
        ImageDrawerUtils.drawStringContinuousDiscoloration(graphics, x + 175, 340, "金魂币: ", ImageDrawerUtils.BLUE_A75, String.valueOf(is.getGold()), ImageDrawerUtils.ORIGIN_A75);

        //end=================
        y = image.getHeight() - graphics.getFontMetrics().getHeight();
        graphics.setColor(ImageDrawerUtils.WHITE_A80);
        graphics.fillRoundRect(2, y - 50, image.getWidth() - 4, 80, 20, 20);
        graphics.setColor(Color.BLACK);
        graphics.setFont(ImageDrawerUtils.SMALL_FONT22_TYPE0);

        graphics.drawString(userInfo.getWhInfo().getSid(), 5, y);
        String dt = DateUtils.getFormat();
        graphics.drawString(dt, (image.getWidth() - graphics.getFontMetrics().stringWidth(dt)) / 2, y);

        dt = "create by github@kloping";
        graphics.drawString(dt, image.getWidth() - graphics.getFontMetrics().stringWidth(dt) - 5, y);


        graphics.dispose();
        return image;
    }

    public String upgrade(String sid) {
        return gameDataBase.operate(sid, info -> {
            WhInfo is = info.getWhInfo();
            long xp = is.getXp();
            long xpL = is.getXpl();
            int L = is.getLevel();
            if (L != 2 || is.getWh() != 0) {
                if (xp >= xpL) {
                    if (L > ResourceSet.FinalValue.MAX_LEVEL) return "等级最大限制..";
                    if (GameFinalSource.isJTop(is)) return "无法升级,因为到达等级瓶颈,吸收魂环后继续升级";
                    StringBuilder sb = new StringBuilder("升级成功");
                    is.setLevel(is.getLevel() + 1).addXp(-xpL);
                    long basePp;
                    basePp = GameFinalSource.getPromoteProperties(L);
                    is.setXpl(is.getXpl() + basePp * 10);

                    basePp = GameFinalSource.getPromoteProperties(L);
                    is.setHpl(is.getHpl() + basePp).addHp(basePp);
                    sb.append("\r\n增加了:").append(basePp).append("最大血量");

                    basePp = GameFinalSource.getPromoteProperties(L);
                    is.setHll(is.getHll() + basePp).addHl(basePp);
                    sb.append("\r\n增加了:").append(basePp).append("最大魂力");


                    basePp = GameFinalSource.getPromoteProperties(L);
                    is.setAtt(is.getAtt() + basePp);
                    sb.append("\r\n增加了:").append(basePp).append("攻击");

                    basePp = GameFinalSource.getPromoteProperties(L) / 10;
                    is.setHjl(is.getHjl() + basePp).addHj(basePp);
                    sb.append("\r\n增加了:").append(basePp).append("最大精神力");
                    return sb.toString();
                } else {
                    return "经验不足,无法升级!";
                }
            } else {
                return ResourceSet.FinalString.PLEASE_AWAKENING_WH;
            }
        });
    }
}
