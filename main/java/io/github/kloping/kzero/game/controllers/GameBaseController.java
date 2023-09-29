package io.github.kloping.kzero.game.controllers;

import io.github.kloping.MySpringTool.annotations.Action;
import io.github.kloping.MySpringTool.annotations.AutoStand;
import io.github.kloping.MySpringTool.annotations.Controller;
import io.github.kloping.kzero.bot.database.SourceDataBase;
import io.github.kloping.kzero.game.commons.GameUserInfo;
import io.github.kloping.kzero.game.database.GameDataBase;
import io.github.kloping.kzero.game.services.GameBaseService;
import io.github.kloping.kzero.main.ResourceSet;
import io.github.kloping.kzero.main.api.KZeroBot;
import io.github.kloping.kzero.main.api.MessagePack;
import io.github.kloping.rand.RandomUtils;

import java.awt.image.BufferedImage;

/**
 * @author github.kloping
 */
@Controller
public class GameBaseController {
    @AutoStand
    GameDataBase gameDataBase;
    @AutoStand
    GameBaseService service;
    @AutoStand
    SourceDataBase sourceDataBase;

    @Action("信息")
    public String info(MessagePack pack, KZeroBot bot, String sid) {
        GameUserInfo info = gameDataBase.getGameUserInfo(sid);
        BufferedImage image = service.drawGameUserInfo(info);
        return "<pic:" + sourceDataBase.save(image) + ">";
    }

    @Action("修炼")
    public String xl(String sid) {
        return service.xl(sid);
    }

    @Action("升级")
    public String upgrade(String sid) {
        return service.upgrade(sid);
    }

    @Action(value = "觉醒",otherName = {"武魂觉醒","觉醒武魂"})
    public String arousal(String sid) {
        return gameDataBase.operate(sid, info -> {
            if (info.getWhInfo().getWh() != 0) {
                return ResourceSet.FinalString.AWAKENED_WH;
            }
            long level = info.getWhInfo().getLevel();
            if (level < 2) {
                return ResourceSet.FinalString.LEVEL2_AWAKENING_WH_TIPS;
            }
            int r = RandomUtils.RANDOM.nextInt(31) + 1;
            info.getWhInfo().setWh(r);
            return ResourceSet.FinalString.AWAKENING_WH_SUCCEED;
        });
    }
}
