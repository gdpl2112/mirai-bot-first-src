package io.github.kloping.gb;

import io.github.kloping.judge.Judge;
import io.github.kloping.number.NumberUtils;

import java.util.Random;

/**
 * @author github.kloping
 */
public class Utils {
    public static final Random RANDOM = new Random();

    public static final Integer getInteger(String str, String delete, Integer def) {
        String end = str;
        if (Judge.isNotEmpty(delete)) {
            end = str.replace(delete, "");
        }
        try {
            return Integer.valueOf(NumberUtils.findNumberFromString(end));
        } catch (NumberFormatException e) {
            return def;
        }
    }

    public static final Long getLong(String str, String delete, Long def) {
        String end = str;
        if (Judge.isNotEmpty(delete)) {
            end = str.replace(delete, "");
        }
        try {
            return Long.valueOf(NumberUtils.findNumberFromString(end));
        } catch (NumberFormatException e) {
            return def;
        }
    }
}
