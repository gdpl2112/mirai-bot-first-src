package io.github.kloping.kzero.main.api;

import java.util.regex.Pattern;

/**
 * @author github.kloping
 */
public interface MessageSerializer<T> {
    Pattern PATTER_FACE = Pattern.compile("<face:\\d+>");
    Pattern PATTER_PIC = Pattern.compile("<pic:[^>^]+?>");
    Pattern PATTER_AT = Pattern.compile("<at:[\\d+|?]+>");
    Pattern PATTER_MUSIC = Pattern.compile("<music:\\d+>");
    Pattern PATTER_VOICE = Pattern.compile("<audio:.+>");

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
