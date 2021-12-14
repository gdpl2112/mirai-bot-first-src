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
import static Project.DataBases.GameTaskDatabase.deleteTask;
import static io.github.kloping.Mirai.Main.Resource.threads;

public class TaskCreator {
    public static final int maxPrenticeIndex = 1;
    public static final int maxIndex = 1;

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

    public static Receiver task0(Task task) {
        Receiver receiver = null;
        GhostLostBroadcast.INSTANCE.add(receiver = new GhostLostBroadcast.GhostLostReceiver() {
            @Override
            public void onReceive(long who, Long with, GhostObj ghostObj) {
                if (who == task.getHost().longValue()) {
                    if (task.getTasker().contains(with.longValue())) {
                        deleteTask(task);
                        MessageTools.sendMessageInGroupWithAt(TaskDetailService.getFinish(task)
                                , task.getFromG().longValue(), task.getHost());
                        addToBgs(who, 1601, ObjType.got);
                        addToBgs(with.longValue(), 1601, ObjType.got);
                        threads.submit(() -> {
                            task.destroy();
                        });
                    }
                }
            }
        });
        return receiver;
    }
}
