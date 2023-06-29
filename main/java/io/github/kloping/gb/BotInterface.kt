package io.github.kloping.gb;

/**
 * @author github.kloping
 */
public interface BotInterface {
    String getBotId();

    void sendEnv(String gid, String text);

    void sendEnvWithAt(String gid, String text, MessageContext context);

    void sendEnvReply(String gid, String text, MessageContext context);

    void sendEnvReplyWithAt(String gid, String text, MessageContext context);
}
