package io.github.kloping.kzero.bot.commons.apiEntitys;

/**
 * @author github-kloping
 * @version 1.0
 */
public interface RunnableWithOver extends Runnable {
    /**
     * 是否结束
     *
     * @return
     */
    boolean over();
}
