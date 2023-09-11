package io.github.kloping.kzero.game.services;

import io.github.kloping.MySpringTool.annotations.AutoStand;
import io.github.kloping.MySpringTool.annotations.Entity;
import io.github.kloping.date.DateUtils;
import io.github.kloping.kzero.bot.database.SourceDataBase;
import io.github.kloping.kzero.game.commons.GameUserInfo;
import io.github.kloping.kzero.game.database.GameFinalSource;
import io.github.kloping.kzero.spring.dao.PersonInfo;
import io.github.kloping.kzero.spring.dao.WhInfo;
import io.github.kloping.kzero.utils.ImageDrawerUtils;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * @author github.kloping
 */
@Entity
public class GameBaseService {
    @AutoStand
    SourceDataBase sourceDataBase;

    public BufferedImage drawGameUserInfo(GameUserInfo userInfo) {
        WhInfo whInfo = userInfo.getWhInfo();
        PersonInfo personInfo = userInfo.getPersonInfo();
        BufferedImage image = ImageDrawerUtils.readImage(sourceDataBase.getImgPathById("info_bg2"), 800, 1000);
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
        graphics.setColor(ImageDrawerUtils.BLACK_A75);
        graphics.setFont(ImageDrawerUtils.SMALL_FONT18);
        ImageDrawerUtils.drawStringContinuousDiscoloration(graphics, x, y - 5,
                "经验: ", ImageDrawerUtils.BLACK_A75,
                String.valueOf(whInfo.getXp()), ImageDrawerUtils.BLACK_A45,
                "/", ImageDrawerUtils.BLACK_A75,
                String.valueOf(whInfo.getXpl()), ImageDrawerUtils.RED_A75);

        graphics.setColor(ImageDrawerUtils.WHITE_A80);
        graphics.drawRoundRect(x, y, 400, 20, 5, 5);

        int r0 = whInfo.getXpPercent();
        if (r0 < 30) graphics.setColor(ImageDrawerUtils.YELLOW_A75);
        else if (r0 < 60) graphics.setColor(Color.GREEN);
        else if (r0 < 90) graphics.setColor(ImageDrawerUtils.ORIGIN_A75);
        else graphics.setColor(ImageDrawerUtils.RED_A75);
        graphics.fillRoundRect(x, y, r0 * 4, 20, 5, 5);
        //===============xp-end===============
        //===============hp-start=============
        y += 50;
        graphics.setColor(ImageDrawerUtils.BLACK_A75);
        graphics.setFont(ImageDrawerUtils.SMALL_FONT22);
        ImageDrawerUtils.drawStringContinuousDiscoloration(graphics, x, y - 5,
                "血量: ", ImageDrawerUtils.BLACK_A75,
                String.valueOf(whInfo.getHp()), ImageDrawerUtils.GREEN_A85,
                "/", ImageDrawerUtils.BLACK_A75,
                String.valueOf(whInfo.getHpl()), ImageDrawerUtils.RED_A75);
        graphics.setColor(ImageDrawerUtils.WHITE_A80);
        graphics.drawRoundRect(x, y, 400, 20, 5, 5);
        r0 = whInfo.getHpPercent();
        if (r0 < 20) graphics.setColor(ImageDrawerUtils.RED_A75);
        else if (r0 < 40) graphics.setColor(ImageDrawerUtils.YELLOW_A75);
        else graphics.setColor(ImageDrawerUtils.GREEN_A85);
        graphics.fillRoundRect(x, y, r0 * 4, 20, 5, 5);
        //===============hp-end=============
        //===============hl-start=============
        y += 50;
        graphics.setColor(ImageDrawerUtils.BLACK_A75);
        graphics.setFont(ImageDrawerUtils.SMALL_FONT22);
        ImageDrawerUtils.drawStringContinuousDiscoloration(graphics, x, y - 5,
                "魂力: ", ImageDrawerUtils.BLACK_A75,
                String.valueOf(whInfo.getHl()), ImageDrawerUtils.YELLOW_A85,
                "/", ImageDrawerUtils.BLACK_A75,
                String.valueOf(whInfo.getHll()), ImageDrawerUtils.RED_A75);

        graphics.setColor(ImageDrawerUtils.WHITE_A80);
        graphics.drawRoundRect(x, y, 400, 20, 5, 5);
        r0 = whInfo.getHlPercent();
        graphics.setColor(ImageDrawerUtils.YELLOW_A75);
        graphics.fillRoundRect(x, y, r0 * 4, 20, 5, 5);
        //===============hl-end=============
        //===============hj-start=============
        y += 50;
        graphics.setColor(ImageDrawerUtils.BLACK_A75);
        graphics.setFont(ImageDrawerUtils.SMALL_FONT22);
        ImageDrawerUtils.drawStringContinuousDiscoloration(graphics, x, y - 5,
                "精神力:", ImageDrawerUtils.BLACK_A75,
                String.valueOf(whInfo.getHj()), ImageDrawerUtils.BLUE2_A75,
                "/", ImageDrawerUtils.BLACK_A75,
                String.valueOf(whInfo.getHjl()), ImageDrawerUtils.RED_A75);
        graphics.setColor(ImageDrawerUtils.WHITE_A80);
        graphics.drawRoundRect(x, y, 400, 20, 5, 5);
        r0 = whInfo.getHjPercent();
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


        graphics.setColor(Color.BLACK);
        graphics.setFont(ImageDrawerUtils.SMALL_FONT18_TYPE0);

        y = image.getHeight() - graphics.getFontMetrics().getHeight();

        graphics.drawString(userInfo.getWhInfo().getSid(), 5, y);
        String dt = DateUtils.getFormat();
        graphics.drawString(dt, (image.getWidth() - graphics.getFontMetrics().stringWidth(dt)) / 2, y);

        dt = "create by github@kloping";
        graphics.drawString(dt, image.getWidth() - graphics.getFontMetrics().stringWidth(dt) - 5, y);


        graphics.dispose();
        return image;
    }
}
