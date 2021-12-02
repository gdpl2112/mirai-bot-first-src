package Project.broadcast;

import Project.broadcast.enums.ObjType;

import java.lang.reflect.Method;

public class JoinBroadcast extends Broadcast {
    public JoinBroadcast() {
        super("JsonBroadcast");
    }

    public static final JoinBroadcast INSTANCE = new JoinBroadcast();

    public void broadcast(long who, int type) {
        for (Receiver receiver : receivers) {
            if (receiver instanceof JoinReceiver)
                ((JoinReceiver) receiver).onReceive(who, type);
        }
    }

    @Override
    protected void broadcast(Object... objects) {
        if (method == null) {
            try {
                method = this.getClass().getDeclaredMethod("broadcast", long.class, int.class);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        ifIsRunElseJump(INSTANCE, method, objects);
    }

    private static Method method;

    @Override
    public boolean add(Receiver receiver) {
        if (receiver instanceof JoinReceiver)
            return super.add(receiver);
        return false;
    }

    public static interface JoinReceiver extends Receiver {
        void onReceive(long who, int type);
    }
}
