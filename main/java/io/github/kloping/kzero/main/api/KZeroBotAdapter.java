package io.github.kloping.kzero.main.api;

import java.lang.reflect.Method;

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

    /**
     * 当消息返回时
     *
     * @param method
     * @param data
     * @param pack
     */
    void onResult(Method method, Object data, MessagePack pack);
}
