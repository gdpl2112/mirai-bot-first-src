package Project.broadcast.game;

import Project.broadcast.Broadcast;
import io.github.kloping.mirai0.commons.broadcast.Receiver;
import io.github.kloping.mirai0.commons.gameEntitys.SkillInfo;

import java.util.LinkedList;
import java.util.List;

/**
 * @author github-kloping
 */
public class SkillUseBroadcast extends Broadcast {
    public static final SkillUseBroadcast INSTANCE = new SkillUseBroadcast();

    public SkillUseBroadcast() {
        super("SkillUseBroadcast");
    }

    public void broadcast(long who, int jid, int st, SkillInfo info) {
        for (SkillUseReceiver receiver : receivers) {
            receiver.onReceive(who, jid, st, info);
        }
    }

    private List<SkillUseReceiver> receivers = new LinkedList<>();

    @Override
    public boolean add(Receiver receiver) {
        if (receiver instanceof SkillUseReceiver)
            return receivers.add((SkillUseReceiver) receiver);
        return false;
    }

    public interface SkillUseReceiver extends Receiver {
        /**
         * on
         *
         * @param who
         * @param jid
         * @param st
         * @param info
         */
        void onReceive(long who, int jid, int st, SkillInfo info);
    }
}
