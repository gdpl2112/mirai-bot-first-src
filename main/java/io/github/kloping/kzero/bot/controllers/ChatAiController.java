package io.github.kloping.kzero.bot.controllers;

import io.github.kloping.kzero.bot.database.DataBase;
import io.github.kloping.kzero.hwxb.WxHookStarter;
import io.github.kloping.kzero.main.api.KZeroBot;
import io.github.kloping.kzero.main.api.MessagePack;
import io.github.kloping.kzero.spring.dao.GroupConf;
import io.github.kloping.kzero.utils.ChatAi;
import io.github.kloping.spt.annotations.Action;
import io.github.kloping.spt.annotations.AutoStand;
import io.github.kloping.spt.annotations.Controller;
import io.github.kloping.spt.annotations.DefAction;
import io.github.kloping.spt.exceptions.NoRunException;
import io.github.kloping.spt.interfaces.Logger;
import lombok.Data;

import java.lang.reflect.Method;

/**
 * @author github.kloping
 */
@Controller
public class ChatAiController {
    /*
    @AutoStand
    public DataBase dataBase;

    @DefAction
    public void run(Method method, MessagePack pack, KZeroBot bot) throws NoRunException {
        GroupConf groupConf = dataBase.getConf(pack.getSubjectId());
        if (groupConf != null) {
            if (!groupConf.getOpen()) {
                return;
            }
        }
        hand(bot, pack);
    }

    private String asid = null;

    CodeMatches matches;

    {
        matches = new CodeMatches() {
            @Override
            public boolean matchBefore(String text) {
                String ss = text.replaceAll("\\?|？|\\s", "");
                return ss.equalsIgnoreCase(asid) ? false :
                        text.matches("[\\?|？\\s]+") ? false : true;
            }

            @Override
            public boolean matchTo(String text) {
                return text.startsWith(asid);
            }
        };
        matches.setHead(new String[]{"为什么", "为啥", "什么", "ai", "AI", "问"});
        matches.setEnd(new String[]{"是什么"});
    }

    @AutoStand
    Logger log;

    private void hand(KZeroBot bot, MessagePack pack) {
        String msg = pack.getMsg();
        if (msg == null) return;
        msg = msg.replaceAll("\\s", "");
        if (asid == null) {
            if (bot.getSelf() instanceof WxHookStarter) {
                asid = String.format("@Rfe.");
            } else {
                asid = String.format("<at:%s>", bot.getId());
            }
        }
        String finalMsg = msg;
        if (matches.match(msg)) {
            hdo(bot, pack, msg);
        }
    }

    private void hdo(KZeroBot bot, MessagePack pack, String msg) {
        log.waring("匹配关键词:开始回话");
        if (msg.startsWith("ai")) msg = msg.substring(2);
        if (msg.startsWith("AI")) msg = msg.substring(2);
        if (msg.startsWith("问")) msg = msg.substring(1);
        String out = ChatAi.chat(pack.getSenderId(), msg);
        if (out != null) {
            bot.getAdapter().sendMessage(pack.getType(), pack.getSubjectId(), out);
        }
    }

    @Data
    public abstract static class CodeMatches {
        private String[] head;
        private String[] end;

        public boolean match(String text) {
            if (!matchBefore(text)) return false;
            for (String tt : head) {
                if (text.startsWith(tt)) {
                    return true;
                }
            }
            for (String tt : end) {
                if (text.endsWith(tt)) {
                    return true;
                }
            }
            return matchTo(text);
        }

        public boolean matchBefore(String text) {
            return true;
        }

        public boolean matchTo(String text) {
            return false;
        }
    }*/
}
