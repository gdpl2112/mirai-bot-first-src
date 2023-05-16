package Project.broadcast.game;

import Project.broadcast.Broadcast;
import Project.commons.TradingRecord;
import Project.commons.broadcast.Receiver;

import java.util.LinkedHashSet;

/**
 * @author github-kloping
 */
public class RecordBroadcast extends Broadcast {
    public static final RecordBroadcast INSTANCE = new RecordBroadcast();
    public final LinkedHashSet<RecordReceiver> receivers = new LinkedHashSet<>();

    public RecordBroadcast() {
        super("RecordBroadcast");
    }

    @Override
    public boolean add(Receiver receiver) {
        if (receiver instanceof RecordReceiver) {
            return receivers.add((RecordReceiver) receiver);
        }
        return false;
    }

    public void broadcast(long who, TradingRecord record) {
        for (RecordReceiver receiver : receivers) {
            THREADS.submit(() -> receiver.onReceiver(who, record));
        }
    }

    public static interface RecordReceiver extends Receiver {
        /**
         * receive
         *
         * @param who
         * @param record
         */
        void onReceiver(long who, TradingRecord record);
    }
}
