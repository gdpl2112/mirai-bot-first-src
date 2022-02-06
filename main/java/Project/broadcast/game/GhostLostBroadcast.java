package Project.broadcast.game;

import io.github.kloping.mirai0.Entitys.gameEntitys.GhostObj;
import Project.broadcast.Broadcast;
import Project.broadcast.Receiver;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import static Project.dataBases.GameDataBase.addToBgs;
import static Project.dataBases.GameDataBase.getImgById;

/**
 * @author github-kloping
 */
public class GhostLostBroadcast extends Broadcast {
    public static final GhostLostBroadcast INSTANCE = new GhostLostBroadcast();

    public GhostLostBroadcast() {
        super("GhostLostBroadcast");
    }

    public void broadcast(long who, GhostObj ghostObj, KillType type) {
        for (Receiver receiver : receivers) {
            if (receiver instanceof GhostLostReceiver)
                ((GhostLostReceiver) receiver).onReceive(who, ghostObj.getWith(), ghostObj, type);
        }
        threads.submit(this::after);
    }

    public Set<Runnable> AfterRunnable = new LinkedHashSet<>();

    @Override
    public boolean add(Receiver receiver) {
        if (receiver instanceof GhostLostReceiver)
            return super.add(receiver);
        return false;
    }

    public synchronized void after() {
        if (AfterRunnable.isEmpty())
            return;
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

    public static enum KillType {
        NORMAL_ATT("普通攻击"),
        SPIRIT_ATT("精神攻击"),
        SKILL_ATT("魂技攻击"),
        ANQ_ATT("暗器攻击");
        String name;

        KillType(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    public static interface GhostLostReceiver extends Receiver {
        /**
         * receive
         *
         * @param who
         * @param with
         * @param ghostObj
         */
        void onReceive(long who, Long with, GhostObj ghostObj, KillType killType);
    }

    public static abstract class AbstractGhostLostReceiverWith<T> implements GhostLostReceiver {
        private T t;

        public AbstractGhostLostReceiverWith(T t) {
            this.t = t;
        }

        public T getT() {
            return t;
        }
    }

}
