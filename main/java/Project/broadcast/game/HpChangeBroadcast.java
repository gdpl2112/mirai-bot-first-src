package Project.broadcast.game;

import Project.broadcast.Broadcast;
import Project.commons.broadcast.Receiver;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author github-kloping
 */
public class HpChangeBroadcast extends Broadcast {
    public static final HpChangeBroadcast INSTANCE = new HpChangeBroadcast();
    public static final List<Receiver> RECEIVERS = new ArrayList<>();

    public HpChangeBroadcast() {
        super("HpChangeBroadcast");
    }

    @Override
    public boolean add(Receiver receiver) {
        if (receiver instanceof HpChangeReceiver || receiver instanceof OnCeHpChangeReceiver) {
            return RECEIVERS.add(receiver);
        }
        return false;
    }

    public void broadcast(long who, long hpFrom, long hpTo, long value, long from, HpChangeReceiver.type type) {
        Iterator<Receiver> iterator = RECEIVERS.iterator();
        while (iterator.hasNext()) {
            Receiver receiver = iterator.next();
            if (receiver instanceof HpChangeReceiver) {
                ((HpChangeReceiver) receiver).onReceive(who, hpFrom, hpTo, value, from, type);
            } else if (receiver instanceof OnCeHpChangeReceiver) {
                OnCeHpChangeReceiver r0 = (OnCeHpChangeReceiver) receiver;
                if (r0.onReceive(who, hpFrom, hpTo, value, from)) {
                    iterator.remove();
                }
            }
        }
    }

    public interface HpChangeReceiver extends Receiver {
        /**
         * on receive
         *
         * @param who    血量变化者id
         * @param hpFrom
         * @param hpTo
         * @param value
         * @param from   主动者id
         * @param type
         */
        void onReceive(long who, long hpFrom, long hpTo, long value, long from, type type);


        public static enum type {
            //人
            FROM_Q,
            //怪
            FROM_G,
            //其他
            FROM_OTHER
        }
    }

    public interface OnCeHpChangeReceiver extends Receiver {
        /**
         * on receive
         *
         * @param who
         * @param hpFrom
         * @param hpTo
         * @param value
         * @param from
         * @return
         */
        boolean onReceive(long who, long hpFrom, long hpTo, long value, long from);
    }
}
