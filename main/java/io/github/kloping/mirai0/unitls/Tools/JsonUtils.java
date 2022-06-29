package io.github.kloping.mirai0.unitls.Tools;

import Project.controllers.auto.ControllerSource;
import com.alibaba.fastjson.JSON;

/**
 * @author github-kloping
 */
public class JsonUtils {
    public static String objectToJsonString(Object obj) {
        return ControllerSource.gson0.toJson(obj);
    }

    /**
     * 若想使用此方法,该类必须提供无参构造
     *
     * @param json
     * @param cla
     * @param <T>
     * @return
     */
    public static <T> T jsonStringToObject(String json, Class<T> cla) {
        return JSON.toJavaObject(JSON.parseObject(json), cla);
    }
}
