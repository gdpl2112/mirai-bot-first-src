package Project.broadcast;

import Entitys.gameEntitys.SkillInfo;

import java.lang.reflect.Method;

public class SkillUseBroadcast extends Broadcast {
    public static final SkillUseBroadcast INSTANCE = new SkillUseBroadcast();

    @Override
    protected void broadcast(Object... objects) {
        if (method == null) {
            try {
                method = this.getClass().getDeclaredMethod("broadcast",
                        long.class, int.class, int.class, SkillInfo.class);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        ifIsRunElseJump(INSTANCE, method, objects);
    }

    private static Method method;

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
