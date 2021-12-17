package Project.broadcast.game;

import Entitys.gameEntitys.GhostObj;
import Project.broadcast.Broadcast;
import Project.broadcast.Receiver;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import static io.github.kloping.Mirai.Main.Resource.threads;

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
        threads.submit(this::After);
    }

    public Set<Runnable> AfterRunnable = new LinkedHashSet<>();

    @Override
    public boolean add(Receiver receiver) {
        if (receiver instanceof GhostLostReceiver)
            return super.add(receiver);
        return false;
    }

    public synchronized void After() {
        Iterator<Runnable> runnableIterator = AfterRunnable.iterator();
        while (runnableIterator.hasNext()) {
            runnableIterator.next().run();
            runnableIterator.remove();
        }
    }

    private static int index = 0;

    public static synchronized int getSerId() {
        return index++;
    }

    public static interface GhostLostReceiver extends Receiver {
        void onReceive(long who, Long with, GhostObj ghostObj);
    }
}
