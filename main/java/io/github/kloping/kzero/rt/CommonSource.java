package io.github.kloping.kzero.rt;

/**
 * @author github.kloping
 */
public class CommonSource {
    /**
     * 将指定数字转为指定位数字符
     * (2,9)  => 09
     *
     * @param i
     * @param value
     * @return
     */
    public static String toStr(int i, int value) {
        String s0 = Integer.toString(value);
        while (s0.length() < i) {
            s0 = "0" + s0;
        }
        return s0;
    }
}
