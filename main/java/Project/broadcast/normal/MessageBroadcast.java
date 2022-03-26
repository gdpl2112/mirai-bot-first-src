package Project.broadcast.normal;

import Project.broadcast.Broadcast;
import io.github.kloping.mirai0.commons.broadcast.Receiver;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author github-kloping
 * @version 1.0
 */
public class MessageBroadcast extends Broadcast {
    public static final MessageBroadcast INSTANCE = new MessageBroadcast();
    private List<MessageReceiver> messageReceivers = new CopyOnWriteArrayList<>();

    public MessageBroadcast() {
        super("MessageBroadcast");
    }

    @Override
    public boolean add(Receiver receiver) {
        if (receiver instanceof MessageReceiver) {
            return messageReceivers.add((MessageReceiver) receiver);
        }
        return false;
    }

    public void broadcast(long qid, long gid, String context) {
        messageReceivers.forEach(r -> {
            threads.submit(() -> r.onReceive(qid, gid, context));
        });
    }


    public static interface MessageReceiver extends Receiver {
        /**
         * receive
         *
         * @param qid
         * @param gid
         * @param context
         */
        void onReceive(long qid, long gid, String context);
    }
}
