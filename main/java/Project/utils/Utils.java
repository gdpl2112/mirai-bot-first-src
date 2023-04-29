package Project.utils;

import io.github.kloping.mirai.BotInstance;

/**
 * @author github.kloping
 */
public class Utils {
    public static Long getAtFromString(String message) {
        int i0 = message.indexOf("[@");
        int ie = message.indexOf("]");
        if (i0 == -1 || ie == -1) return -1L;
        String sid = message.substring(i0 + 2, ie);
        if ("me".equals(sid)) return BotInstance.getInstance().getBotId();
        else return Long.parseLong(sid);
    }
}
