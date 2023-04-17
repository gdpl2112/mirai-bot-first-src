package Project.broadcast.game.challenge;

import Project.broadcast.Broadcast;
import Project.commons.broadcast.Receiver;

import java.lang.reflect.Method;

/**
 * @author github.kloping
 */
public class TrialChallengeEndBroadcast extends Broadcast {
    public static final TrialChallengeEndBroadcast INSTANCE = new TrialChallengeEndBroadcast();
    private static Method method;

    public TrialChallengeEndBroadcast() {
        super("TrialChallengeEndBroadcast");
    }

    public void broadcast(long q1, long q2, int w) {
        for (Receiver receiver : receivers) {
            if (receiver instanceof TrialChallengeEndBroadcast.TrialChallengeReceiver)
                ((TrialChallengeEndBroadcast.TrialChallengeReceiver) receiver).onReceive(q1, q2, w);
        }
    }

    @Override
    public boolean add(Receiver receiver) {
        if (receiver instanceof TrialChallengeEndBroadcast.TrialChallengeReceiver)
            return super.add(receiver);
        return false;
    }

    public interface TrialChallengeReceiver extends Receiver {
        /**
         * receive
         *
         * @param q1 玩家1
         * @param q2 玩家2
         * @param w  0.玩家1胜,1.玩家2胜
         */
        void onReceive(long q1, long q2, int w);
    }

    public abstract static class AbstractTrialChallengeReceiverWith<T> implements TrialChallengeReceiver {
        private T t;

        public AbstractTrialChallengeReceiverWith(T t) {
            this.t = t;
        }

        public T getT() {
            return t;
        }
    }
}
