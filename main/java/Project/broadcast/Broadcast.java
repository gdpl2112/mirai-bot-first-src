package Project.broadcast;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public abstract class Broadcast {
    protected String id;
    public static final Map<Class<? extends Broadcast>, Broadcast> cls2Broadcast = new ConcurrentHashMap<>();
    public static final Map<String, Broadcast> id2Broadcasts = new ConcurrentHashMap<>();
    protected Set<Receiver> receivers = new LinkedHashSet<>();

    public Broadcast(String id) {
        this.id = id;
        cls2Broadcast.put(this.getClass(), this);
        id2Broadcasts.put(id, this);
    }

    public <T extends Receiver> void add(T receiver) {
        receivers.add(receiver);
    }

    public void remove(Receiver receiver) {
        receivers.remove(receiver);
    }

    protected abstract void broadcast(Object... objects);
}
