package io.github.kloping.kzero.main.api;

/**
 * @author github.kloping
 */
public interface BotMessageHandler {
    /**
     * 消息触发时
     *
     * @param type      消息类型
     * @param senderId  发送者id
     * @param subjectId 发送环境id
     * @param msg       格式化 msg
     */
    void onMessage(MessageType type, String senderId, String subjectId, String msg);
}
