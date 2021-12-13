package Project.broadcast.game;

import Project.broadcast.Broadcast;
import Project.broadcast.Receiver;

import java.lang.reflect.Method;

public class PlayerLostBroadcast extends Broadcast {
    public static final PlayerLostBroadcast INSTANCE = new PlayerLostBroadcast();

    public PlayerLostBroadcast() {
        super("PlayerLostBroadcast");
    }

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