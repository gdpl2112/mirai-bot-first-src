package Project.broadcast;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class Broadcast {
    protected String id;
    public static final Map<Class<? extends Broadcast>, Broadcast> cls2Broadcast = new ConcurrentHashMap<>();
    public static final Map<String, Broadcast> id2Broadcasts = new ConcurrentHashMap<>();
    protected LinkedHashSet<Receiver> receivers = new LinkedHashSet<>();

    public Broadcast(String id) {
        this.id = id;
        cls2Broadcast.put(this.getClass(), this);
        id2Broadcasts.put(id, this);
    }

    public boolean add(Receiver receiver) {
        return receivers.add(receiver);
    }

    public boolean remove(Receiver receiver) {
        return receivers.remove(receiver);
    }

    protected abstract void broadcast(Object... objects);

    public static void ifIsRunElseJump(Object INSTANCE, Method method, Object[] objects) {
        if (!method.isAccessible()) method.setAccessible(true);
        if (objects.length == method.getParameterCount()) {
            Class<?>[] cls = method.getParameterTypes();
            for (int i = 0; i < cls.length; i++) {
                if (cls[i] != objects[i].getClass())
                    return;
            }
            try {
                method.invoke(INSTANCE, objects);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }
}
