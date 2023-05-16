package Project.broadcast.normal;

import Project.broadcast.Broadcast;
import Project.commons.broadcast.Receiver;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author github-kloping
 * @version 1.0
 */
public class MemberJoinedBroadcast extends Broadcast<MemberJoinedBroadcast.MemberJoinedReceiver> {
    public static final MemberJoinedBroadcast INSTANCE = new MemberJoinedBroadcast();
    private List<MemberJoinedReceiver> receiver = new CopyOnWriteArrayList<>();

    public MemberJoinedBroadcast() {
        super("MemberJoinedBroadcast");
    }

    @Override
    public boolean add(MemberJoinedReceiver receiver) {
        if (receiver instanceof MemberJoinedReceiver) {
            return this.receiver.add((MemberJoinedReceiver) receiver);
        }
        return false;
    }

    public void broadcast(long qid, long gid, long iq) {
        receiver.forEach(r -> {
            THREADS.submit(() -> r.onReceive(qid, gid, iq));
        });
    }

    public interface MemberJoinedReceiver extends Receiver {
        /**
         * on
         *
         * @param q  加入者id
         * @param g  群id
         * @param iq 邀请者id 若无 则 小于0
         */
        void onReceive(long q, long g, long iq);
    }
}
