package Project.broadcast.game;

import Entitys.gameEntitys.SkillInfo;
import Project.broadcast.Broadcast;
import Project.broadcast.Receiver;

import java.lang.reflect.Method;

public class SkillUseBroadcast extends Broadcast {
    public static final SkillUseBroadcast INSTANCE = new SkillUseBroadcast();

    public void broadcast(long who, int jid, int st, SkillInfo info) {
        for (Receiver receiver : receivers) {
            if (receiver instanceof SkillUseReceiver)
                ((SkillUseReceiver) receiver).onReceive(who, jid, st, info);
        }
    }

    @Override
    public boolean add(Receiver receiver) {
        if (receiver instanceof SkillUseReceiver)
            return super.add(receiver);
        return false;
    }

    public SkillUseBroadcast() {
        super("SkillUseBroadcast");
    }

    public interface SkillUseReceiver extends Receiver {
        void onReceive(long who, int jid, int st, SkillInfo info);
    }
}
