package io.github.kloping.kzero.game.controllers;

import io.github.kloping.MySpringTool.annotations.Action;
import io.github.kloping.MySpringTool.annotations.AutoStand;
import io.github.kloping.MySpringTool.annotations.Controller;
import io.github.kloping.kzero.bot.database.SourceDataBase;
import io.github.kloping.kzero.game.commons.GameUserInfo;
import io.github.kloping.kzero.game.database.GameDataBase;
import io.github.kloping.kzero.game.services.GameBaseService;
import io.github.kloping.kzero.main.api.KZeroBot;
import io.github.kloping.kzero.main.api.MessagePack;

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
}
