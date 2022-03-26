package Project.broadcast;

import io.github.kloping.mirai0.commons.broadcast.Receiver;
import io.github.kloping.object.ObjectUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author github-kloping
 */
public abstract class Broadcast {
    public static final ExecutorService threads = Executors.newFixedThreadPool(10);
    public static final Map<Class<? extends Broadcast>, Broadcast> cls2Broadcast = new ConcurrentHashMap<>();
    public static final Map<String, Broadcast> id2Broadcasts = new ConcurrentHashMap<>();
    public static final LinkedHashSet<Receiver> receivers = new LinkedHashSet<>();
    protected String id;
    private Method method;

    public Broadcast(String id) {
        this.id = id;
        cls2Broadcast.put(this.getClass(), this);
        id2Broadcasts.put(id, this);
        for (Method declaredMethod : this.getClass().getDeclaredMethods()) {
            if ("broadcast".equals(declaredMethod.getName())) {
                if (declaredMethod.getParameterCount() >= 1) {
                    if (declaredMethod.getParameterTypes()[0] != Object[].class) {
                        method = declaredMethod;
                        break;
                    }
                }
            }
        }
    }

    public static void ifIsRunElseJump(Object INSTANCE, Method method, Object[] objects) {
        if (!method.isAccessible()) method.setAccessible(true);
        if (objects.length == method.getParameterCount()) {
            Class<?>[] cls = method.getParameterTypes();
            cls = ObjectUtils.baseToPack(cls);
            objects = ObjectUtils.baseToPack(objects);
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

    public boolean add(Receiver receiver) {
        return receivers.add(receiver);
    }

    public boolean remove(Receiver receiver) {
        return receivers.remove(receiver);
    }

    protected final void broadcast(Object... objects) {
        ifIsRunElseJump(this, method, objects);
    }

    protected void setId(String id) {
        this.id = id;
    }
}
