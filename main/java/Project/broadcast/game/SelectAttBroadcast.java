package Project.broadcast.game;

import Project.broadcast.Broadcast;
import Project.commons.broadcast.Receiver;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import static io.github.kloping.mirai0.Main.BootstarpResource.THREADS;

/**
 * @author github-kloping
 */
public class SelectAttBroadcast extends Broadcast {
    public static final SelectAttBroadcast INSTANCE = new SelectAttBroadcast();
    private List<SelectAttReceiver> receivers = new LinkedList<>();

    public SelectAttBroadcast() {
        super("SelectAttBroadcast");
    }

    public void broadcast(long q1, long q2, long v, int type) {
        THREADS.submit(() -> {
            Iterator<SelectAttReceiver> iterator = receivers.iterator();
            while (iterator.hasNext()) {
                SelectAttReceiver receiver = iterator.next();
                if (receiver.onReceive(q1, q2, v, type)) {
                    iterator.remove();
                }
            }
        });
    }

    @Override
    public boolean add(Receiver receiver) {
        if (receiver instanceof SelectAttReceiver) {
            return receivers.add((SelectAttReceiver) receiver);
        }
        return false;
    }

    public interface SelectAttReceiver extends Receiver {
        /**
         * on receive
         *
         * @param q1   攻击者
         * @param q2   被攻击者
         * @param v    值
         * @param type 类型  1 选择攻击 2攻击@
         * @return if return true  next can receive
         */
        boolean onReceive(long q1, long q2, long v, int type);
    }
}
