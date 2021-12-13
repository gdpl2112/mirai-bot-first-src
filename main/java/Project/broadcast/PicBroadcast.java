package Project.broadcast;

public class PicBroadcast extends Broadcast {

    public static interface PicReceiver extends Receiver {
        Object onReceive(long qid, long gid, String pic, Object[] objects);

        default void onReturn(Object o) {
        }
    }

    public static interface PicReceiverOnce extends PicReceiver {
        Object onReceive(long qid, long gid, String pic, Object[] objects);

        default void onReturn(Object o) {
        }
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
        for (Receiver receiver : receivers) {
            if (receiver instanceof PicReceiver) {
                PicReceiver pr = ((PicReceiver) receiver);
                Object o = pr.onReceive(qid, gid, pic, objects);
                pr.onReturn(o);
            } else if (receiver instanceof PicReceiverOnce) {
                PicReceiver pr = ((PicReceiverOnce) receiver);
                Object o = pr.onReceive(qid, gid, pic, objects);
                pr.onReturn(o);
                receivers.remove(receiver);
            }
        }
    }
}
