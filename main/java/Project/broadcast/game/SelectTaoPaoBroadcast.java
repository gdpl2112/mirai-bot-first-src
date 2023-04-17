package Project.broadcast.game;

import Project.broadcast.Broadcast;
import io.github.kloping.mirai0.commons.GhostObj;
import Project.commons.broadcast.Receiver;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import static io.github.kloping.mirai0.Main.BootstarpResource.THREADS;

/**
 * @author github-kloping
 */
public class SelectTaoPaoBroadcast extends Broadcast {
    public static final SelectTaoPaoBroadcast INSTANCE = new SelectTaoPaoBroadcast();
    private List<SelectTaoPaoReceiver> receivers = new LinkedList<>();

    public SelectTaoPaoBroadcast() {
        super("SelectTaoPaoBroadcast");
    }

    public void broadcast(long q1, GhostObj ghostObj) {
        THREADS.submit(() -> {
            Iterator<SelectTaoPaoReceiver> iterator = receivers.iterator();
            while (iterator.hasNext()) {
                SelectTaoPaoReceiver receiver = iterator.next();
                if (receiver.onReceive(q1, ghostObj)) {
                    iterator.remove();
                }
            }
        });
    }

    @Override
    public boolean add(Receiver receiver) {
        if (receiver instanceof SelectTaoPaoReceiver) {
            return receivers.add((SelectTaoPaoReceiver) receiver);
        }
        return false;
    }

    public interface SelectTaoPaoReceiver extends Receiver {
        /**
         * on receive
         *
         * @param q1       攻击者
         * @param ghostObj
         * @return if return true  next can receive
         */
        boolean onReceive(long q1, GhostObj ghostObj);
    }
}
