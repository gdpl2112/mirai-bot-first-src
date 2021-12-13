package Project.DataBases;

import Entitys.gameEntitys.task.Task;
import Entitys.gameEntitys.task.TaskPoint;
import Project.DataBases.task.TaskCreator;
import io.github.kloping.Mirai.Main.Handlers.OwnerHandler;
import io.github.kloping.Mirai.Main.ITools.MessageTools;
import io.github.kloping.file.FileUtils;
import io.github.kloping.initialize.FileInitializeValue;
import io.github.kloping.map.MapUtils;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GameTaskDatabase {
    private static String path;

    public GameTaskDatabase(String mainPath) {
        path = mainPath + "/dates/games/tasks";
        init();
    }

    public static final Map<Long, List<Task>> tasks = new ConcurrentHashMap<>();

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
        } catch (Exception e) {
            e.printStackTrace();
        }
        after();
    }

    public static final long cd_ = 24 * 60 * 60 * 1000;
    public static final int maxPrenticeIndex = 1;

    private static void after() {
        OwnerHandler.tenEveRunnable.add(() -> {
            tasks.values().forEach((v) -> {
                v.forEach(e -> {
                    if (System.currentTimeMillis() > e.getDeadline()) {
                        for (Long aLong : e.getTasker())
                            TaskPoint.getInstance(aLong.longValue()).setNextCan(System.currentTimeMillis() + (cd_ * 2)).apply();
                        TaskPoint.getInstance(e.getHost().longValue()).setNextCan(System.currentTimeMillis() + (cd_ * 2)).apply();
                    }
                    deleteTask(e);
                    MessageTools.sendMessageInGroupWithAt("任务过期,未完成", e.getFromG().longValue(), e.getHost());
                });
            });
        });
    }

    public static void createTask(Task task) {
        TaskCreator.create(task);
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
