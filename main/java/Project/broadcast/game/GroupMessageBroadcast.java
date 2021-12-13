package Project.broadcast.game;

import Project.broadcast.Broadcast;
import Project.broadcast.Receiver;

import java.lang.reflect.Method;

public class GroupMessageBroadcast extends Broadcast {
    public static final GroupMessageBroadcast INSTANCE = new GroupMessageBroadcast();

    public GroupMessageBroadcast() {
        super("GroupMessageBroadcast");
    }

    @Override
    public boolean add(Receiver receiver) {
        if (receiver instanceof GroupMessageBroadcast.GroupMessageReceiver)
            return super.add(receiver);
        return false;
    }

    public void broadcast(long who, long from, String mess) {
        for (Receiver receiver : receivers) {
            if (receiver instanceof GroupMessageBroadcast.GroupMessageReceiver)
                ((GroupMessageBroadcast.GroupMessageReceiver) receiver).onReceive(who, from, mess);
        }
    }

    public interface GroupMessageReceiver extends Receiver{

        void onReceive(long who, long from, String text);
    }
}
