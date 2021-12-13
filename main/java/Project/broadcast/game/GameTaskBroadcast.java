package Project.broadcast.game;

import Entitys.gameEntitys.task.Task;
import Project.broadcast.Broadcast;
import Project.broadcast.Receiver;

import java.lang.reflect.Method;

public class GameTaskBroadcast extends Broadcast {
    public GameTaskBroadcast() {
        super("GameTaskBroadcast");
    }

    public static final GameTaskBroadcast INSTANCE = new GameTaskBroadcast();

    public void broadcast(long who, int taskId, GameTaskReceiver.Type type, Task task) {
        for (Receiver receiver : receivers) {
            if (receiver instanceof GameTaskBroadcast.GameTaskReceiver)
                ((GameTaskBroadcast.GameTaskReceiver) receiver).onReceive(who, taskId, type, task);
        }
    }

    @Override
    public boolean add(Receiver receiver) {
        if (receiver instanceof GameTaskBroadcast.GameTaskReceiver)
            return super.add(receiver);
        return false;
    }

    public static interface GameTaskReceiver extends Receiver {
        public static enum Type {
            join, finish, lost
        }

        void onReceive(long who, int taskId, Type type, Task task);
    }
}
