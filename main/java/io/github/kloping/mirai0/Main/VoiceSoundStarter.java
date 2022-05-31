package io.github.kloping.mirai0.Main;

import net.mamoe.mirai.Bot;
import net.mamoe.mirai.BotFactory;
import net.mamoe.mirai.utils.BotConfiguration;

/**
 * @author github.kloping
 */
public class VoiceSoundStarter {
    public static String VOICE_BASE;

    public static void main(String[] args) throws Exception {
        String qid = args[0];
        String pwd = args[1];
        BotConfiguration.MiraiProtocol protocol = BotConfiguration.MiraiProtocol.valueOf(args[2]);
        VOICE_BASE = args[3];
        BotConfiguration configuration = new BotConfiguration();
        configuration.setProtocol(protocol);
        Bot bot = BotFactory.INSTANCE.newBot(Long.parseLong(qid), pwd, configuration);
        bot.login();

    }
}
