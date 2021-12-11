package Project.DataBases;

import Entitys.gameEntitys.GhostObj;
import Entitys.gameEntitys.task.Task;
import Project.Services.DetailServices.TaskDetailService;
import Project.broadcast.GhostLostBroadcast;
import io.github.kloping.Mirai.Main.ITools.MessageTools;
import io.github.kloping.file.FileUtils;
import io.github.kloping.initialize.FileInitializeValue;
import io.github.kloping.map.MapUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static io.github.kloping.Mirai.Main.Resource.threads;

public class GameTaskDatabase {
    private static String path;

    public GameTaskDatabase(String mainPath) {
        path = mainPath + "/dates/games/tasks";
        init();
    }

    private static final Map<Long, List<Task>> tasks = new ConcurrentHashMap<>();

    private static void init() {
        try {
            FileUtils.testFile(path + "/m");
            for (File file : new File(path).listFiles()) {
                try {
                    Task task = FileInitializeValue.getValue(file.getAbsolutePath(), new Task(), false);
                    saveTask(task, false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        after();
    }

    private static void after() {

    }

    public static void createTask(Task task) {
        int id = task.getTaskId();
        switch (id) {
            case 0:
                GhostLostBroadcast.INSTANCE.add(new GhostLostBroadcast.GhostLostReceiver() {
                    @Override
                    public void onReceive(long who, Long with, GhostObj ghostObj) {
                        if (who == task.getHost().longValue()) {
                            if (task.getTasker().contains(with.longValue())) {
                                deleteTask(task);
                                MessageTools.sendMessageInGroupWithAt(TaskDetailService.getFinish(task)
                                        , task.getFromG().longValue(), task.getHost());
                                threads.submit(() -> {
                                    try {
                                        Thread.sleep(1 * 1000);
                                        GhostLostBroadcast.INSTANCE.remove(this);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                });
                            }
                        }
                    }
                });
                break;
        }
    }

    public static void deleteTask(Task task) {
        for (Long aLong : task.getTasker()) {
            tasks.get(aLong.longValue()).remove(task);
        }
        tasks.get(task.getHost()).remove(task);

        File file = new File(path, task.getUuid());
        file.delete();
    }

    public static void saveTask(Task task, boolean input) {
        if (task.getHost().longValue() == -1) return;
        if (task.getTaskId().intValue() == -1) return;
        if (task.getState().intValue() == -1) return;
        if (task.getFromG().longValue() == -1) return;
        if (task.getUuid().isEmpty()) return;
        if (task.getTasker().isEmpty()) return;
        if (input) {
            File file = new File(path, task.getUuid());
            FileInitializeValue.putValues(file.getAbsolutePath(), task, true);
        }
        for (Long aLong : task.getTasker())
            MapUtils.append(tasks, aLong.longValue(), task);
        MapUtils.append(tasks, task.getHost().longValue(), task);
        createTask(task);
    }
}
