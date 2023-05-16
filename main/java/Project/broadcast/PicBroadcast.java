package Project.broadcast;

import Project.commons.broadcast.Receiver;

import java.util.Iterator;

/**
 * @author github-kloping
 */
public class PicBroadcast extends Broadcast {

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
        Iterator iterator = RECEIVERS.iterator();
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

    public static interface PicReceiver extends Receiver {
        /**
         * on received call method
         *
         * @param qid
         * @param gid
         * @param pic
         * @param objects
         * @return
         */
        Object onReceive(long qid, long gid, String pic, Object[] objects);
    }

    public static interface PicReceiverOnce extends PicReceiver {
        /**
         * will remove on first call return not is null
         *
         * @param qid
         * @param gid
         * @param pic
         * @param objects
         * @return
         */
        @Override
        Object onReceive(long qid, long gid, String pic, Object[] objects);
    }
}
