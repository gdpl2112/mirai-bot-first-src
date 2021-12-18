package Project.DataBases.task;

import Entitys.gameEntitys.GhostObj;
import Entitys.gameEntitys.task.Task;
import Project.Services.DetailServices.TaskDetailService;
import Project.broadcast.Receiver;
import Project.broadcast.enums.ObjType;
import Project.broadcast.game.GhostLostBroadcast;
import io.github.kloping.Mirai.Main.ITools.MessageTools;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static Project.DataBases.GameDataBase.addToBgs;
import static Project.DataBases.GameDataBase.getImgById;
import static Project.DataBases.GameTaskDatabase.deleteTask;
import static Project.Tools.Tool.getRandT;

public class TaskCreator {
    public static final int maxPrenticeIndex = 0;
    public static final int maxIndex = 1000;

    public static <T extends Task> T getTask(int id) {
        if (id < 10) return (T) new Task();
        else if (id < 1100) return (T) new TaskEntityDetail.Task1000();
        return null;
    }

    public static Receiver create(Task task) {
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
        GhostLostBroadcast.INSTANCE.add(receiver = new GhostLostBroadcast.GhostLostReceiverWith<Task>(taskN) {
            @Override
            public void onReceive(long who, Long with, GhostObj ghostObj) {
                Task task = getT();
                if (who == task.getHost().longValue()) {
                    if (task.getTasker().contains(with.longValue())) {
                        deleteTask(task);
                        MessageTools.sendMessageInGroupWithAt(TaskDetailService.getFinish(task)
                                , task.getFromG().longValue(), task.getHost());
                        addToBgs(who, 1601, ObjType.got);
                        addToBgs(with.longValue(), 1601, ObjType.got);
                        GhostLostBroadcast.INSTANCE.AfterRunnable.add(() -> task.destroy());
                    }
                }
            }
        });
        return receiver;
    }

    public static final Integer[] t1000objs = new Integer[]{201, 202, 203, 101, 102, 103, 104, 105, 106, 107, 109, 110, 112, 113, 1000};

    public static int getRandObj1000() {
        return getRandT(t1000objs);
    }

    public static synchronized Receiver task1000(Task task1000) {
        if (!(task1000 instanceof TaskEntityDetail.Task1000)) return null;
        Receiver receiver = null;
        GhostLostBroadcast.INSTANCE.add(receiver = new GhostLostBroadcast.GhostLostReceiverWith
                <TaskEntityDetail.Task1000>((TaskEntityDetail.Task1000) task1000) {

            @Override
            public void onReceive(long who, Long with, GhostObj ghostObj) {
                TaskEntityDetail.Task1000 task = getT();
                if (ghostObj.getId() < 600)
                    task.m1.put(1, true);
                else if (ghostObj.getId() < 700)
                    task.m1.put(2, true);
                else if (ghostObj.getId() < 800)
                    task.m1.put(3, true);

                task.update();

                if (task.isFinish()) {
                    deleteTask(task);
                    int id = getRandObj1000();

                    MessageTools.sendMessageInGroupWithAt(TaskDetailService.getFinish(task) + getImgById(id)
                            , task.getFromG().longValue(), task.getHost());

                    addToBgs(who, id, ObjType.got);
                    GhostLostBroadcast.INSTANCE.AfterRunnable.add(() -> {
                        task.destroy();
                    });
                }
            }
        });
        return receiver;
    }
}
