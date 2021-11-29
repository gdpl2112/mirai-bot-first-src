package Project.broadcast;

public class PicBroadcast extends Broadcast {

    public static interface PicReceiver extends Receiver {
        Object receiver(long qid, long gid, String pic, Object[] objects);

        default void onReturn(Object o) {
        }
    }

    public static interface PicReceiverOnce extends PicReceiver {
        Object receiver(long qid, long gid, String pic, Object[] objects);

        default void onReturn(Object o) {
        }
    }

    public static final PicBroadcast INSTANCE = new PicBroadcast();

    public PicBroadcast() {
        super("PicBroadcast");
    }

    public void broadcast(long qid, long gid, String pic, Object[] objects) {
        broadcast(gid, qid, pic, (Object) objects);
    }

    @Override
    protected void broadcast(Object... objects) {
        long qid = Long.parseLong(objects[0].toString());
        long gid = Long.parseLong(objects[1].toString());
        String pic = objects[2].toString();
        Object[] os = (Object[]) objects[3];
        for (Receiver receiver : receivers) {
            if (receiver instanceof PicReceiver) {
                PicReceiver pr = ((PicReceiverOnce) receiver);
                Object o = pr.receiver(qid, gid, pic, os);
                pr.onReturn(o);
            } else if (receiver instanceof PicReceiverOnce) {
                PicReceiver pr = ((PicReceiverOnce) receiver);
                Object o = pr.receiver(qid, gid, pic, os);
                pr.onReturn(o);
                receivers.remove(receiver);
            }
        }
    }
}
