package Project.Controllers;


import Project.DataBases.DataBase;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ControllerTool {

    private final static Map<Long, Boolean> ks = new ConcurrentHashMap<>();

    public static boolean CanGroup(long id) {
        if (!ks.containsKey(id))
            ks.put(id, DataBase.canBack(id));
        return ks.get(id);
    }

    public static synchronized void setLongIs(Long lq,Boolean k){ ks.put(lq,k); }
    public static synchronized void removeGroup(Long lq){ ks.remove(lq); }
}
