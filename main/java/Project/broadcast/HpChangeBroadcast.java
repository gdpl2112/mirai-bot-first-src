package Project.broadcast;

import java.lang.reflect.Method;

public class HpChangeBroadcast extends Broadcast {
    public static final HpChangeBroadcast INSTANCE = new HpChangeBroadcast();

    public HpChangeBroadcast() {
        super("HpChangeBroadcast");
    }

    @Override
    public boolean add(Receiver receiver) {
        if (receiver instanceof HpChangeReceiver)
            return super.add(receiver);
        return false;
    }

    public void broadcast(long who, long hpFrom, long hpTo, long value, long from, HpChangeReceiver.type type) {
        for (Receiver receiver : receivers) {
            if (receiver instanceof HpChangeReceiver)
                ((HpChangeReceiver) receiver).onReceive(who, hpFrom, hpTo, value, from, type);
        }
    }

    private static Method method;

    @Override
    protected void broadcast(Object... objects) {
        if (method == null) {
            try {
                method = HpChangeBroadcast.class.getDeclaredMethod("broadcast", long.class, long.class, long.class, long.class, long.class, HpChangeReceiver.type.class);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        ifIsRunElseJump(INSTANCE, method, objects);
    }

    public interface HpChangeReceiver {
        public static enum type {
            //人
            fromQ,
            //怪
            fromG,
            //其他
            fromOther
        }

        void onReceive(long who, long hpFrom, long hpTo, long value, long from, type type);
    }
}
