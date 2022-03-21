package Project.broadcast.game;

import Project.broadcast.Broadcast;
import io.github.kloping.mirai0.commons.broadcast.Receiver;

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
    public boolean add(Receiver receiver) {
        if (receiver instanceof JoinReceiver)
            return super.add(receiver);
        return false;
    }

    public static interface JoinReceiver extends Receiver {
        void onReceive(long who, int type);
    }
}
