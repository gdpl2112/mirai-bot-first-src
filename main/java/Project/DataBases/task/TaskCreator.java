package Project.DataBases.task;

import Entitys.gameEntitys.task.Task;
import Project.broadcast.Receiver;
import Project.broadcast.game.GhostLostBroadcast;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static Project.Tools.Tool.getRandT;

/**
 * 任务创建
 *
 * @author github-kloping
 */
public class TaskCreator {
    public static final int MAX_PRENTICE_INDEX = 1;
    public static final int MIN_PRENTICE_INDEX = 0;
    public static final int MAX_INDEX = 1002;
    public static final int MIN_INDEX = 1000;

    public static <T extends Task> T getTask(int id) {
        if (id < 10) return (T) new Task();
        else if (id < 1001) return (T) new TaskEntityDetail.Task1000();
        else if (id < 1002) return (T) new TaskEntityDetail.Task1001();
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

    public static final Integer[] T_1000_OBJS = new Integer[]{201, 202, 203, 101, 102, 103, 104, 105, 106, 107, 109, 110, 112, 113, 1000};

    public static int getRandObj1000() {
        return getRandT(T_1000_OBJS);
    }

    public static Receiver task1000(Task task) {
        if (!(task instanceof TaskEntityDetail.Task1000)) return null;
        Receiver receiver = null;
        GhostLostBroadcast.INSTANCE.add(receiver
                = new TaskEntityDetail.GhostLostReceiverWithTask1000((TaskEntityDetail.Task1000) task));
        return receiver;
    }

    public static synchronized Receiver task1001(Task task) {
        if (!(task instanceof TaskEntityDetail.Task1001)) return null;
        Receiver receiver = null;
        GhostLostBroadcast.INSTANCE.add(receiver
                = new TaskEntityDetail.GhostLostReceiverWithTask1001((TaskEntityDetail.Task1001) task));
        return receiver;
    }

}
