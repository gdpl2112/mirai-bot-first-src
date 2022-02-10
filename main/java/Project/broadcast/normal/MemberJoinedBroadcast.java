package Project.broadcast.normal;

import Project.broadcast.Broadcast;
import Project.broadcast.Receiver;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author github-kloping
 * @version 1.0
 */
public class MemberJoinedBroadcast extends Broadcast {
    public MemberJoinedBroadcast() {
        super("MemberJoinedBroadcast");
    }

    public static final MemberJoinedBroadcast INSTANCE = new MemberJoinedBroadcast();

    private List<MemberJoinedReceiver> receiver = new CopyOnWriteArrayList<>();

    @Override
    public boolean add(Receiver receiver) {
        if (receiver instanceof MemberJoinedReceiver) {
            return this.receiver.add((MemberJoinedReceiver) receiver);
        }
        return false;
    }

    public void broadcast(long qid, long gid) {
        receiver.forEach(r -> {
            threads.submit(() -> r.onReceive(qid, gid));
        });
    }

    public static interface MemberJoinedReceiver extends Receiver {
        /**
         * on
         *
         * @param q
         * @param g
         */
        void onReceive(long q, long g);
    }
}
