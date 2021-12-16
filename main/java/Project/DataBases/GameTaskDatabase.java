package Project.DataBases;

import Entitys.gameEntitys.task.Task;
import Project.DataBases.task.TaskCreator;
import Project.broadcast.Receiver;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.github.kloping.file.FileUtils;
import io.github.kloping.initialize.FileInitializeValue;
import io.github.kloping.map.MapUtils;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static Project.DataBases.task.TaskCreator.getTask;

public class GameTaskDatabase {
    public static String path;

    public GameTaskDatabase(String mainPath) {
        path = mainPath + "/dates/games/tasks";
        init();
    }

    public static final Map<Long, List<Task>> tasks = new ConcurrentHashMap<>();

    private static void init() {
        try {
            new File(path).mkdirs();
            for (File file : new File(path).listFiles()) {
                try {
                    JSONObject jo = JSON.parseObject(FileUtils.getStringFromFile(file.getAbsolutePath()));
                    int id = jo.getInteger("taskId");
                    Task task = FileInitializeValue.getValue(file.getAbsolutePath(), getTask(id), false);
                    saveTask(task, false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static final long cd_ = 24 * 60 * 60 * 1000;

    public static Receiver createTask(Task task) {
        return TaskCreator.create(task);
    }

    public static void deleteTask(Task task) {
        for (Long aLong : task.getTasker()) {
            tasks.get(aLong.longValue()).remove(task);
        }
        tasks.get(task.getHost()).remove(task);
        File file = new File(path, task.getUuid());
        file.delete();
    }

    public static Receiver saveTask(Task task, boolean input) {
        if (task.getHost().longValue() == -1) return null;
        if (task.getTaskId().intValue() == -1) return null;
        if (task.getState().intValue() == -1) return null;
        if (task.getFromG().longValue() == -1) return null;
        if (task.getUuid().isEmpty()) return null;
        if (input) {
            File file = new File(path, task.getUuid());
            FileInitializeValue.putValues(file.getAbsolutePath(), task, true);
        }
        Receiver receiver = createTask(task);
        task.setReceiver(receiver);
        for (Long aLong : task.getTasker())
            MapUtils.append(tasks, aLong.longValue(), task);
        MapUtils.append(tasks, task.getHost().longValue(), task);
        return receiver;
    }
}
