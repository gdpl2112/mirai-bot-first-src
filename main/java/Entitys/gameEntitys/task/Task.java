package Entitys.gameEntitys.task;

import Project.DataBases.GameTaskDatabase;
import Project.broadcast.Broadcast;
import Project.broadcast.Receiver;
import com.alibaba.fastjson.annotation.JSONField;
import io.github.kloping.Mirai.Main.ITools.MessageTools;
import io.github.kloping.initialize.FileInitializeValue;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.File;
import java.util.*;

import static Project.DataBases.GameTaskDatabase.*;
import static io.github.kloping.Mirai.Main.Resource.THREADS;

@Data
@Accessors(chain = true)
public class Task {
    public static enum Type {
        week, prentice, normal
    }

    private Set<Long> tasker = new LinkedHashSet<>();
    private Long host = -1L;
    private Long fromG = -1L;
    private Long deadline = System.currentTimeMillis();
    private Type type = Type.normal;
    private Integer taskId = -1;
    private Integer state = -1;
    private String uuid = "";

    @JSONField(serialize = false, deserialize = false)
    private Receiver receiver;
    @JSONField(serialize = false, deserialize = false)
    private Runnable runnable;

    public Task() {
        taskRunnable.add(runnable = new Runnable() {
            @Override
            public void run() {
                if (System.currentTimeMillis() > getDeadline()) {
                    over();
                }
            }
        });
    }

    public void over() {
        for (Long aLong : getTasker()) {
            TaskPoint.getInstance(aLong.longValue())
                    .setNextCan(System.currentTimeMillis() + (cd_ * 2)).apply();
        }

        TaskPoint.getInstance(getHost().longValue())
                .setNextCan(System.currentTimeMillis() + (cd_ * 2))
                .addPrenticeIndex(-1).apply();

        MessageTools.sendMessageInGroupWithAt("任务过期,未完成", getFromG().longValue(), getHost());
        destroy();
    }

    public void destroy() {
        Broadcast.receivers.remove(getReceiver());
        deleteTask(Task.this);
        taskRunnable.remove(this.runnable);
    }

    public void save() {
        saveTask(this, true);
    }

    public void update() {
        File file = new File(GameTaskDatabase.path, getUuid());
        FileInitializeValue.putValues(file.getAbsolutePath(), this, true);
    }

    public static final List<Runnable> taskRunnable = new LinkedList<>();

    static {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                taskRunnable.forEach(r -> THREADS.submit(r));
            }
        }, 15 * 1000, 10 * 60 * 1000);
    }
}
