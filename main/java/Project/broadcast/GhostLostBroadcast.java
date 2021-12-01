package Project.broadcast;

import Entitys.gameEntitys.GhostObj;

import java.lang.reflect.Method;

public class GhostLostBroadcast extends Broadcast {
    public GhostLostBroadcast() {
        super("GhostLostBroadcast");
    }

    public static final GhostLostBroadcast INSTANCE = new GhostLostBroadcast();

    @Override
    protected void broadcast(Object... objects) {
        if (method == null) {
            try {
                method = this.getClass().getDeclaredMethod("broadcast", long.class, GhostObj.class);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        ifIsRunElseJump(INSTANCE, method, objects);
    }

    private static Method method;

    public void broadcast(long who, GhostObj ghostObj) {
        for (Receiver receiver : receivers) {
            if (receiver instanceof GhostLostReceiver)
                ((GhostLostReceiver) receiver).onReceive(who, ghostObj.getWith(), ghostObj);
        }
    }

    @Override
    public boolean add(Receiver receiver) {
        if (receiver instanceof GhostLostReceiver)
            return super.add(receiver);
        return false;
    }

    public static interface GhostLostReceiver {
        void onReceive(long who, Long with, GhostObj ghostObj);
    }
}
