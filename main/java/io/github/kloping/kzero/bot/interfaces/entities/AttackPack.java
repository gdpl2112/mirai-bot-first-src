package io.github.kloping.kzero.bot.interfaces.entities;

/**
 * 攻击包
 *
 * @author github-kloping
 * @date 2023-03-29
 */
public interface AttackPack extends Runnable {
    /**
     * 攻击发起者
     *
     * @return
     */
    Long getInitiator();

    /**
     * 攻击目标
     *
     * @return
     */
    Long getTarget();

    /**
     * -1 未抛出 <br>
     * 0 抛出中 <br>
     * 1 已抛出 <br>
     * 其他 异常<br>
     * 状态
     *
     * @return
     */
    Integer getState();

    /**
     * 抛出攻击(执行)
     *
     * @param <T>
     * @return
     */
    <T> T thrown();

    /**
     * 更名
     */
    @Override
    default void run() {
        thrown();
    }
}
