package io.github.kloping.kzero.main.api;

/**
 * @author github.kloping
 */
public interface MessageSerializer<T> {
    /**
     * 将指定类型msg转为格式string
     *
     * @param msg
     * @return
     */
    String serialize(T msg);

    /**
     * 将格式string转为指定类型msg
     *
     * @param msg
     * @return
     */
    T deserialize(String msg);
}
