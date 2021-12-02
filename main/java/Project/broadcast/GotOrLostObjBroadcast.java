package Project.broadcast;

import Project.broadcast.enums.ObjType;

import java.lang.reflect.Method;

public class GotOrLostObjBroadcast extends Broadcast {
    public static final GotOrLostObjBroadcast INSTANCE = new GotOrLostObjBroadcast();

    public GotOrLostObjBroadcast() {
        super("GotOrLostObjBroadcast");
    }


    @Override
    protected void broadcast(Object... objects) {
        if (method == null) {
            try {
                method = this.getClass().getDeclaredMethod("broadcast"
                        , long.class, int.class, int.class, ObjType.class);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        ifIsRunElseJump(INSTANCE, method, objects);
    }

    public void broadcast(long who, int id, int num, ObjType type) {
        for (Receiver receiver : receivers) {
            if (receiver instanceof GotOrLostReceiver)
                ((GotOrLostReceiver) receiver).onReceive(who, id, num, type);
        }
    }

    @Override
    public boolean add(Receiver receiver) {
        if (receiver instanceof GotOrLostReceiver)
            return super.add(receiver);
        return false;
    }

    private static Method method;

    public static interface GotOrLostReceiver extends Receiver {
        void onReceive(long who, int id, int num, ObjType type);
    }
}
