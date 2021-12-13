package Project.DataBases.task;

import Entitys.gameEntitys.GhostObj;
import Entitys.gameEntitys.task.Task;
import Project.Services.DetailServices.TaskDetailService;
import Project.broadcast.game.GhostLostBroadcast;
import io.github.kloping.Mirai.Main.ITools.MessageTools;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static Project.DataBases.GameTaskDatabase.deleteTask;
import static io.github.kloping.Mirai.Main.Resource.threads;

public class TaskCreator {
    public static void create(Task task) {
        try {
            int id = task.getTaskId();
            Method method = TaskCreator.class.getDeclaredMethod("task" + id, Task.class);
            method.setAccessible(true);
            method.invoke(null, task);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public static void task0(Task task) {
        GhostLostBroadcast.INSTANCE.add(new GhostLostBroadcast.GhostLostReceiver() {
            @Override
            public void onReceive(long who, Long with, GhostObj ghostObj) {
                if (who == task.getHost().longValue()) {
                    if (task.getTasker().contains(with.longValue())) {
                        deleteTask(task);
                        MessageTools.sendMessageInGroupWithAt(TaskDetailService.getFinish(task)
                                , task.getFromG().longValue(), task.getHost());
                        threads.submit(() -> GhostLostBroadcast.INSTANCE.remove(this));
                    }
                }
            }
        });

    }
}
