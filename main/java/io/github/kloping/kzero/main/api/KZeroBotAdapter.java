package io.github.kloping.kzero.main.api;

import io.github.kloping.kzero.main.ResourceSet;

import java.lang.reflect.Method;
import java.util.List;

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

    /**
     * 获取指定id人的头像链接
     *
     * @param sid
     * @return
     */
    default String getAvatarUrl(String sid) {
        return ResourceSet.ICON_TEMP_MAP.get(sid);
    }

    /**
     * 获取指定id人的头像链接
     *
     * @param sid
     * @return
     */
    default String getAvatarUrlConverted(String sid) {
       return getAvatarUrl(sid);
    }

    /**
     * 获取指定id人名片
     *
     * @param sid
     * @return
     */
    default String getNameCard(String sid) {
        return ResourceSet.NICKNAME_TEMP_MAP.get(sid);
    }

    /**
     * 获取指定id人名片
     *
     * @param sid
     * @return
     */
    String getNameCard(String sid, String tid);

    /**
     * 获取指定环境群员数量
     *
     * @param tid
     * @return
     */
    List<String> getMembers(String tid);
}
