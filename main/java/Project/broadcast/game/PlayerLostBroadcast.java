package Project.broadcast.game;

import Project.broadcast.Broadcast;
import Project.commons.broadcast.Receiver;

import java.util.Iterator;

/**
 * @author github-kloping
 */
public class PlayerLostBroadcast extends Broadcast {
    public static final PlayerLostBroadcast INSTANCE = new PlayerLostBroadcast();

    public PlayerLostBroadcast() {
        super("PlayerLostBroadcast");
    }

    public void broadcast(long who, long from, PlayerLostReceiver.LostType type) {
        Iterator<Receiver> receiverIterator = RECEIVERS.iterator();
        while (receiverIterator.hasNext()) {
            Receiver receiver = receiverIterator.next();
            if (receiver instanceof PlayerLostBroadcast.PlayerLostReceiver) {
                ((PlayerLostReceiver) receiver).onReceive(who, from, type);
            } else if (receiver instanceof OncePlayerLostReceiver) {
                if (((OncePlayerLostReceiver) receiver).onReceive(who, from)) {
                    receiverIterator.remove();
                }
            }
        }
    }

    @Override
    public boolean add(Receiver receiver) {
        if (receiver instanceof PlayerLostReceiver || receiver instanceof OncePlayerLostReceiver) {
            return super.add(receiver);
        }
        return false;
    }

    public interface PlayerLostReceiver extends Receiver {
        /**
         * receive
         *
         * @param who
         * @param from
         * @param type
         */
        void onReceive(long who, long from, LostType type);

        enum LostType {
            att, un, fake
        }
    }

    public interface OncePlayerLostReceiver extends Receiver {
        /**
         * receive0
         *
         * @param who
         * @param from
         * @return remove will if true
         */
        boolean onReceive(long who, long from);
    }
}