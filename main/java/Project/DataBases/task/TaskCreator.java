package Project.DataBases.task;

import Entitys.gameEntitys.task.Task;
import Project.broadcast.Receiver;
import Project.broadcast.game.GhostLostBroadcast;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static Project.Tools.Tool.getRandT;

public class TaskCreator {
    public static final int maxPrenticeIndex = 0;
    public static final int maxIndex = 1000;

    public static <T extends Task> T getTask(int id) {
        if (id < 10) return (T) new Task();
        else if (id < 1100) return (T) new TaskEntityDetail.Task1000();
        return null;
    }

    public static Receiver create(final Task task) {
        try {
            int id = task.getTaskId();
            Method method = TaskCreator.class.getDeclaredMethod("task" + id, Task.class);
            method.setAccessible(true);
            return (Receiver) method.invoke(null, task);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static synchronized Receiver task0(final Task taskN) {
        Receiver receiver = null;
        GhostLostBroadcast.INSTANCE.add(receiver = new TaskEntityDetail.GhostLostReceiverWithTask0(taskN));
        return receiver;
    }

    public static final Integer[] t1000objs = new Integer[]{201, 202, 203, 101, 102, 103, 104, 105, 106, 107, 109, 110, 112, 113, 1000};

    public static int getRandObj1000() {
        return getRandT(t1000objs);
    }

    public static synchronized Receiver task1000(Task task1000) {
        if (!(task1000 instanceof TaskEntityDetail.Task1000)) return null;
        Receiver receiver = null;
        GhostLostBroadcast.INSTANCE.add(receiver = new TaskEntityDetail.GhostLostReceiverWithTask1000((TaskEntityDetail.Task1000) task1000));
        return receiver;
    }
}
