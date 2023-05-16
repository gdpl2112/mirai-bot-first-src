package Project.broadcast.game;

import Project.broadcast.Broadcast;
import Project.commons.broadcast.Receiver;

/**
 * @author github-kloping
 */
public class JoinBroadcast extends Broadcast {
    public static final JoinBroadcast INSTANCE = new JoinBroadcast();

    public JoinBroadcast() {
        super("JsonBroadcast");
    }

    public void broadcast(long who, int type) {
        for (Receiver receiver : RECEIVERS) {
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
