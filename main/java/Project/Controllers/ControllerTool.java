package Project.Controllers;


import Project.DataBases.DataBase;
import io.github.kloping.MySpringTool.exceptions.NoRunException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static io.github.kloping.mirai0.Main.Resource.Switch.AllK;

/**
 * @author github-kloping
 */
public class ControllerTool {

    private final static Map<Long, Boolean> KS = new ConcurrentHashMap<>();

    public static boolean opened(long gid, Class<?> cla) {
        if (!AllK) {
            throw new NoRunException("总开关——关闭");
        } else {
            if (canGroup(gid)) {
                return true;
            } else {
                return false;
            }
        }
    }

    public static boolean canGroup(long id) {
        if (!KS.containsKey(id))
            KS.put(id, DataBase.canBack(id));
        return KS.get(id);
    }

    public static synchronized void setLongIs(Long lq, Boolean k) {
        KS.put(lq, k);
    }

    public static synchronized void removeGroup(Long lq) {
        KS.remove(lq);
    }
}
