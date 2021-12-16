package Project.broadcast.game;

import Entitys.gameEntitys.GhostObj;
import Project.broadcast.Broadcast;
import Project.broadcast.Receiver;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

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

    public Set<Runnable> AfterRunnable = new LinkedHashSet<>();

    @Override
    public boolean add(Receiver receiver) {
        if (receiver instanceof GhostLostReceiver)
            return super.add(receiver);
        return false;
    }

    public synchronized void addAfter() {
        Iterator<Runnable> runnableIterator = AfterRunnable.iterator();
        while (runnableIterator.hasNext()) {
            runnableIterator.next().run();
            runnableIterator.remove();
        }
    }

    public static interface GhostLostReceiver extends Receiver {
        void onReceive(long who, Long with, GhostObj ghostObj);
    }
}
