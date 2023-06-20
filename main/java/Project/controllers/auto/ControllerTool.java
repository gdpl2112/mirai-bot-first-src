package Project.controllers.auto;


import Project.dataBases.DataBase;
import io.github.kloping.MySpringTool.exceptions.NoRunException;
import io.github.kloping.mirai0.Main.BotStarter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static io.github.kloping.mirai0.Main.BootstarpResource.Switch.AllK;

/**
 * @author github-kloping
 */
public class ControllerTool {

    private final static Map<Long, Boolean> KS = new ConcurrentHashMap<>();

    public static boolean opened(long gid, Class<?> cla) {
        if (BotStarter.test) {
            return gid == 759590727L || gid == 836258851L;
        } else if (!AllK) {
            throw new NoRunException("总关闭");
        } else {
            if (canGroup(gid)) {
                return true;
            } else {
                return false;
            }
        }
    }

    public static boolean canGroup(long id) {
        if (!KS.containsKey(id)) KS.put(id, DataBase.canBack(id));
        return KS.get(id);
    }

    public static synchronized void setLongIs(Long lq, Boolean k) {
        KS.put(lq, k);
    }

    public static synchronized void removeGroup(Long lq) {
        KS.remove(lq);
    }
}
