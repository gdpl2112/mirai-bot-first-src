package Project.broadcast.game;

import Entitys.gameEntitys.GhostObj;
import Project.broadcast.Broadcast;
import Project.broadcast.Receiver;

public class GhostLostBroadcast extends Broadcast {
    public static final GhostLostBroadcast INSTANCE = new GhostLostBroadcast();

    public GhostLostBroadcast() {
        super("GhostLostBroadcast");
    }

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

    public static interface GhostLostReceiver extends Receiver {
        void onReceive(long who, Long with, GhostObj ghostObj);
    }
}
