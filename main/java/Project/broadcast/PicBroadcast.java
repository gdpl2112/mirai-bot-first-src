package Project.broadcast;

import java.util.Iterator;

public class PicBroadcast extends Broadcast {

    public static interface PicReceiver extends Receiver {
        Object onReceive(long qid, long gid, String pic, Object[] objects);

    }

    public static interface PicReceiverOnce extends PicReceiver {
        @Override
        Object onReceive(long qid, long gid, String pic, Object[] objects);
    }

    public static final PicBroadcast INSTANCE = new PicBroadcast();

    public PicBroadcast() {
        super("PicBroadcast");
    }

    @Override
    public boolean add(Receiver receiver) {
        if (receiver instanceof PicReceiver)
            return super.add(receiver);
        return false;
    }

    public void broadcast(long qid, long gid, String pic, Object[] objects) {
        Iterator iterator = receivers.iterator();
        while (iterator.hasNext()) {
            Object receiver = iterator.next();
            if (receiver instanceof PicReceiverOnce) {
                PicReceiver pr = ((PicReceiverOnce) receiver);
                Object o = pr.onReceive(qid, gid, pic, objects);
                if (o != null) {
                    iterator.remove();
                }
            } else if (receiver instanceof PicReceiver) {
                PicReceiver pr = ((PicReceiver) receiver);
                pr.onReceive(qid, gid, pic, objects);
            }
        }
    }
}
