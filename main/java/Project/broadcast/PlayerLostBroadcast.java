package Project.broadcast;

import java.lang.reflect.Method;

public class PlayerLostBroadcast extends Broadcast {
    public static final PlayerLostBroadcast INSTANCE = new PlayerLostBroadcast();

    public PlayerLostBroadcast() {
        super("PlayerLostBroadcast");
    }

    @Override
    protected void broadcast(Object... objects) {
        if (method == null) {
            try {
                method = this.getClass().getDeclaredMethod("broadcast",
                        long.class, long.class, PlayerLostReceiver.type.class);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        ifIsRunElseJump(INSTANCE, method, objects);
    }

    private static Method method;

    public void broadcast(long who, long from, PlayerLostReceiver.type type) {
        for (Receiver receiver : receivers) {
            if (receiver instanceof PlayerLostBroadcast.PlayerLostReceiver)
                ((PlayerLostBroadcast.PlayerLostReceiver) receiver).onReceive(who, from, type);
        }
    }

    @Override
    public boolean add(Receiver receiver) {
        if (receiver instanceof PlayerLostBroadcast.PlayerLostReceiver)
            return super.add(receiver);
        return false;
    }

    public static interface PlayerLostReceiver extends Receiver {
        public static enum type {
            att,un
        }

        void onReceive(long who, long from, type type);
    }
}