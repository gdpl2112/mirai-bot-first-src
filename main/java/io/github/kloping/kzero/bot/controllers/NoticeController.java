package io.github.kloping.kzero.bot.controllers;

import io.github.kloping.spt.annotations.Action;
import io.github.kloping.spt.annotations.AllMess;
import io.github.kloping.spt.annotations.Controller;
import io.github.kloping.kzero.main.api.KZeroBot;
import io.github.kloping.kzero.main.api.MessagePack;
import io.github.kloping.kzero.main.api.MessageType;
import io.github.kloping.kzero.utils.VelocityUtils;

/**
 * @author github-kloping
 */
@Controller
public class NoticeController {
    @Action("菜单")
    public void menu(@AllMess String m, KZeroBot bot, MessagePack pack) {
        String menu = VelocityUtils.getTemplateToString("menu");
        String[] ms = menu.split("====");
        bot.getAdapter().sendMessageByForward(MessageType.GROUP, pack.getSubjectId(), ms);
    }
}
