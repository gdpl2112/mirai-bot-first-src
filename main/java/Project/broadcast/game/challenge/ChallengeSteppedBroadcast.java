package Project.broadcast.game.challenge;

import Project.broadcast.Broadcast;
import io.github.kloping.mirai0.commons.Group;
import io.github.kloping.mirai0.commons.broadcast.Receiver;
import io.github.kloping.mirai0.commons.game.ChallengeField;

import java.util.LinkedList;
import java.util.List;

/**
 * @author github.kloping
 */
public class ChallengeSteppedBroadcast extends Broadcast {
    public static final ChallengeSteppedBroadcast INSTANCE = new ChallengeSteppedBroadcast();
    private List<ChallengeSteppedReceiver> receivers = new LinkedList<>();
    public ChallengeSteppedBroadcast() {
        super("challengeStepped");
    }

    public void broadcast(ChallengeField field, Group group, String side) {
        for (ChallengeSteppedReceiver receiver : receivers) {
            receiver.onReceive(field, side);
        }
    }

    @Override
    public boolean add(Receiver receiver) {
        if (receiver instanceof ChallengeSteppedReceiver) {
            receivers.add((ChallengeSteppedReceiver) receiver);
        }
        return false;
    }

    @Override
    public boolean remove(Receiver receiver) {
        return receivers.remove(receiver);
    }

    public static interface ChallengeSteppedReceiver extends Receiver {
        /**
         * over
         *
         * @param o
         * @deprecated
         */
        @Override
        default void onReceive(Object o) {
            Receiver.super.onReceive(o);
        }

        /**
         * 一方结束行动
         *
         * @param field
         * @param side
         */
        void onReceive(ChallengeField field, String side);
    }
}
