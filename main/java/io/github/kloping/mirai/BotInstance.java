package io.github.kloping.mirai;

import io.github.kloping.BotInterface;
import io.github.kloping.mirai0.Main.iutils.MessageUtils;
import Project.commons.SpGroup;
import net.mamoe.mirai.Bot;

import static Project.controllers.auto.ControllerSource.aiBaiduDetail;

/**
 * @author github.kloping
 */
public class BotInstance implements BotInterface {
    public static BotInterface instance;

    public static BotInterface getInstance() {
        return instance;
    }

    private Bot bot;

    public BotInstance(Bot bot) {
        this.bot = bot;
        instance = this;
    }

    public Bot getBot() {
        return bot;
    }

    @Override
    public Long getBotId() {
        return bot.getId();
    }

    @Override
    public void speak(String line, SpGroup group) {
        try {
            MessageUtils.INSTANCE.sendVoiceMessageInGroup(aiBaiduDetail.getBytes(line), group.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
