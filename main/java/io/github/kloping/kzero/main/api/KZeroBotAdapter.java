package io.github.kzero.main.api;

/**
 * @author github.kloping
 */
public interface KZeroBotAdapter {
    /**
     * 发送消息
     *
     * @param type     消息类型
     * @param targetId 发送到id
     * @param msg
     */
    void sendMessage(MessageType type, String targetId, Object msg);

    /**
     * 发送转发消息
     *
     * @param type
     * @param targetId
     * @param objects
     */
    void sendMessageByForward(MessageType type, String targetId, Object... objects);
}
