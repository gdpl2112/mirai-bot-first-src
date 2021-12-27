package Project.broadcast;

import Entitys.TradingRecord;

import java.util.LinkedHashSet;

public class RecordBroadcast extends Broadcast {
    public RecordBroadcast() {
        super("RecordBroadcast");
    }

    public static final RecordBroadcast INSTANCE = new RecordBroadcast();

    public final LinkedHashSet<RecordReceiver> receivers = new LinkedHashSet<>();

    @Override
    public boolean add(Receiver receiver) {
        if (receiver instanceof RecordReceiver)
            return receivers.add((RecordReceiver) receiver);
        return false;
    }

    public void broadcast(long who, TradingRecord record) {
        for (RecordReceiver receiver : receivers)
            threads.submit(() -> receiver.onReceiver(who, record));
    }

    public static interface RecordReceiver extends Receiver {
        void onReceiver(long who, TradingRecord record);
    }
}
