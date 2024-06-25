package io.github.kloping.kzero.qqpd;

import io.github.kloping.spt.annotations.Action;
import io.github.kloping.spt.annotations.AllMess;
import io.github.kloping.spt.annotations.Before;
import io.github.kloping.spt.annotations.Controller;
import io.github.kloping.spt.exceptions.NoRunException;
import io.github.kloping.kzero.main.api.KZeroBot;
import io.github.kloping.kzero.main.api.MessagePack;
import io.github.kloping.qqbot.api.message.MessageChannelReceiveEvent;
import io.github.kloping.qqbot.entities.Bot;

/**
 * @author github.kloping
 */
@Controller
public class PluginController {
    @Before
    public void before(@AllMess String msg, KZeroBot kZeroBot, MessagePack pack) {
        if (!(kZeroBot.getSelf() instanceof Bot)) throw new NoRunException("guild-bot专属扩展");
    }

    @Action("创建私信")
    public void create(MessagePack pack) {
        MessageChannelReceiveEvent event = (MessageChannelReceiveEvent) pack.getRaw();
        event.getGuild().create(pack.getSenderId()).send("创建成功!");
    }
}
