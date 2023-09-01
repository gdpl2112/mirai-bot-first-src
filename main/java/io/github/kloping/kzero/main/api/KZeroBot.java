package io.github.kloping.kzero.main.api;

/**
 * @author github.kloping
 */
public interface KZeroBot<T, B> {
    String getId();

    KZeroBotAdapter getAdapter();

    MessageSerializer<T> getSerializer();

    B getSelf();
}
