package Project.dataBases.task;

import Project.broadcast.game.GhostLostBroadcast;
import Project.services.detailServices.tasks.Task1000;
import Project.services.detailServices.tasks.Task1001;
import Project.services.detailServices.tasks.Task1002;
import Project.services.detailServices.tasks.reciver.GhostLostReceiverWithTask0;
import Project.services.detailServices.tasks.reciver.GhostLostReceiverWithTask1000;
import Project.services.detailServices.tasks.reciver.GhostLostReceiverWithTask1001;
import Project.services.detailServices.tasks.reciver.GhostLostReceiverWithTask1002;
import io.github.kloping.mirai0.commons.broadcast.Receiver;
import io.github.kloping.mirai0.commons.task.Task;

import java.lang.reflect.Method;

import static io.github.kloping.mirai0.unitls.Tools.Tool.getRandT;

/**
 * 任务创建
 *
 * @author github-kloping
 */
public class TaskCreator {
    public static final int MAX_PRENTICE_INDEX = 1;
    public static final int MIN_PRENTICE_INDEX = 0;
    public static final int MAX_INDEX = 1003;
    public static final int MIN_INDEX = 1000;

    public static final Integer[] T_1000_OBJS = new Integer[]{
            201, 202, 203, 101, 102, 103, 104, 105, 106, 107, 109, 110, 112, 113, 114, 115, 116, 1000};

    public static <T extends Task> T getTask(int id) {
        if (id == 0) {
            return (T) new Task();
        } else if (id == 1000) {
            return (T) new Task1000();
        } else if (id == 1001) {
            return (T) new Task1001();
        } else if (id == 1002) {
            return (T) new Task1002();
        }
        return null;
    }

    public static Receiver create(final Task task) {
        try {
            int id = task.getTaskId();
            Method method = TaskCreator.class.getDeclaredMethod("task" + id, Task.class);
            method.setAccessible(true);
            return (Receiver) method.invoke(null, task);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static synchronized Receiver task0(final Task taskN) {
        Receiver receiver = null;
        GhostLostBroadcast.INSTANCE.add(receiver = new GhostLostReceiverWithTask0(taskN));
        return receiver;
    }

    public static int getRandObj1000() {
        return getRandT(T_1000_OBJS);
    }

    public static Receiver task1000(Task task) {
        if (!(task instanceof Task1000)) return null;
        Receiver receiver = null;
        GhostLostBroadcast.INSTANCE.add(receiver
                = new GhostLostReceiverWithTask1000((Task1000) task));
        return receiver;
    }

    public static synchronized Receiver task1001(Task task) {
        if (!(task instanceof Task1001)) return null;
        Receiver receiver = null;
        GhostLostBroadcast.INSTANCE.add(receiver
                = new GhostLostReceiverWithTask1001((Task1001) task));
        return receiver;
    }

    public static synchronized Receiver task1002(Task task) {
        if (!(task instanceof Task1002)) return null;
        Receiver receiver = null;
        GhostLostBroadcast.INSTANCE.add(receiver
                = new GhostLostReceiverWithTask1002((Task1002) task));
        return receiver;
    }

}
