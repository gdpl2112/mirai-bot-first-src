package Project.dataBases;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.github.kloping.file.FileUtils;
import io.github.kloping.initialize.FileInitializeValue;
import io.github.kloping.map.MapUtils;
import io.github.kloping.mirai0.commons.broadcast.Receiver;
import io.github.kloping.mirai0.commons.task.Task;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static Project.dataBases.task.TaskCreator.getTask;

/**
 * @author github-kloping
 */
public class GameTaskDatabase {
    public static final Map<Long, List<Task>> TASKS = new ConcurrentHashMap<>();
    public static final long CD0 = 24 * 60 * 60 * 1000;
    public static final long CD1 = 30 * 60 * 1000;
    public static String path;

    public GameTaskDatabase(String mainPath) {
        path = mainPath + "/dates/games/tasks";
        init();
    }

    private static void init() {
        try {
            new File(path).mkdirs();
            for (File file : new File(path).listFiles(f -> f.getName().endsWith(".json"))) {
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

    public static Receiver createTask(Task task) {
        saveActivity(task.getHost());
        return task.registrationReceiver();
    }

    private static void saveActivity(Long host) {
        File file = new File(path, "list");
        try {
            PrintWriter pw = new PrintWriter(new FileOutputStream(file, true));
            pw.println(host.toString());
            pw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static long[] getActivities(boolean k) {
        File file = new File(path, "list");
        String[] sss = FileUtils.getStringsFromFile(file.getAbsolutePath());
        long[] longs = new long[sss.length];
        for (int i = 0; i < sss.length; i++) longs[i] = Long.parseLong(sss[i].trim());
        if (k) file.delete();
        return longs;
    }

    public static void deleteTask(Task task) {
        for (Long aLong : task.getTasker()) {
            TASKS.get(aLong.longValue()).remove(task);
        }
        TASKS.get(task.getHost()).remove(task);
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
        for (Long aLong : task.getTasker()) {
            MapUtils.append(TASKS, aLong.longValue(), task);
        }
        MapUtils.append(TASKS, task.getHost().longValue(), task);
        return receiver;
    }
}
