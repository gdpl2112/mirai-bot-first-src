package io.github.kloping.mirai0.commons.game;

/**
 * @author github.kloping
 */
public enum AsynchronousThingType {
    /**
     * 默认
     */
    NORMAL("default"),
    /**
     * 雷电
     */
    LIGHTNING("light"),
    /**
     * 回复
     */
    HF("addHp"),
    /**
     * 攻击
     */
    ATTACK("attack"),
    /**
     * 火焰
     */
    FIRE("fire");
    String value;

    AsynchronousThingType(String value) {
        this.value = value;
    }
}
