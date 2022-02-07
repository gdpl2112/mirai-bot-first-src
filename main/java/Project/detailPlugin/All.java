package Project.detailPlugin;

import com.alibaba.fastjson.JSON;

/**
 * @author github-kloping
 * @version 1.0
 */
public class All {
    public static Object getTalentDays(String json) {
        return JSON.parseObject(json).getJSONObject("data").getInteger("days");
    }
}
