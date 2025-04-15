package io.github.kloping.kzero.aigame;

import lombok.Getter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

public class MultiValueMapUtils {
    public static MultiValueMap of(String key, Object value) {
        MultiValueMap map = new LinkedMultiValueMap<>();
        map.add(key, value);
        return map;
    }

    public static MultiValueMap of(String key, Object value, String key1, Object value1) {
        MultiValueMap map = of(key, value);
        map.add(key1, value1);
        return map;
    }

    public static MultiValueMap of(String key, Object value, String key1, Object value1, String key2, Object value2) {
        MultiValueMap map = of(key, value, key1, value1);
        map.add(key2, value2);
        return map;
    }

    public static MultiValueMap of(String key, Object value, String key1, Object value1, String key2, Object value2, String key3, Object value3) {
        MultiValueMap map = of(key, value, key1, value1, key2, value2);
        map.add(key3, value3);
        return map;
    }

    public static MultiValueMap of(String key, Object value, String key1, Object value1, String key2, Object value2, String key3, Object value3, String key4, Object value4) {
        MultiValueMap map = of(key, value, key1, value1, key2, value2, key3, value3);
        map.add(key4, value4);
        return map;
    }
}