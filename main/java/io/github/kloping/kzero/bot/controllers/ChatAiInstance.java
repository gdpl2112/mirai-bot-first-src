package io.github.kloping.kzero.bot.controllers;

import io.github.kloping.kzero.main.api.KZeroBot;
import io.github.kloping.kzero.main.api.MessagePack;
import io.github.kloping.kzero.spring.dao.GroupConf;
import io.github.kloping.kzero.utils.ChatAi;
import io.github.kloping.spt.annotations.Controller;
import io.github.kloping.spt.annotations.DefAction;
import io.github.kloping.spt.exceptions.NoRunException;

import java.lang.reflect.Method;

/**
 * @author github.kloping
 */
@Controller
public class ChatAiInstance {
    @DefAction
    public void run(Method method, MessagePack pack, KZeroBot bot) throws NoRunException {
        GroupConf groupConf = AllController.dataBase.getConf(pack.getSubjectId());
        if (groupConf != null) {
            if (!groupConf.getOpen()) {
                return;
            }
        }
        hand(bot, pack);
    }

    public static final String REGX = "[我想问|为什么|为啥|什么].+|.+\\?|ai:.+|.+是什么";

    private void hand(KZeroBot bot, MessagePack pack) {
        String msg = pack.getMsg();
        if (msg.matches(REGX)) {
            if (msg.startsWith("ai:")) msg = msg.substring(3);
            String out = ChatAi.chat(pack.getSenderId(), msg);
            if (out != null) {
                bot.getAdapter().sendMessage(pack.getType(), pack.getSubjectId(), out);
            }
        }
    }
}
