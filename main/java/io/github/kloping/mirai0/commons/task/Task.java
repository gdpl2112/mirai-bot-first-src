package io.github.kloping.mirai0.commons.task;

import Project.broadcast.Broadcast;
import Project.dataBases.GameTaskDatabase;
import com.alibaba.fastjson.annotation.JSONField;
import io.github.kloping.initialize.FileInitializeValue;
import io.github.kloping.mirai0.Main.iutils.MessageUtils;
import Project.commons.broadcast.Receiver;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.File;
import java.util.*;

import static Project.dataBases.GameTaskDatabase.*;
import static io.github.kloping.mirai0.Main.BootstarpResource.THREADS;
import static Project.commons.resouce_and_tool.ResourceSet.FinalString.TASK_OVER_TIME;

/**
 * @author github-kloping
 */
@Data
@Accessors(chain = true)
public abstract class Task {
    public static final List<Runnable> TASK_RUNNABLE = new LinkedList<>();

    static {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                TASK_RUNNABLE.forEach(r -> THREADS.submit(r));
            }
        }, 15 * 1000, 10 * 60 * 1000);
    }

    /**
     * 任务被动接受者
     */
    private Set<Long> tasker = new LinkedHashSet<>();
    /**
     * 任务主动接受者
     */
    private Long host = -1L;
    /**
     * 群id
     */
    private Long fromG = -1L;
    /**
     * 限时 时间戳
     */
    private Long deadline = System.currentTimeMillis();
    private Type type = Type.NORMAL;
    /**
     * 任务ID
     */
    private Integer taskId = -1;
    /**
     * 任务状态
     */
    private Integer state = -1;
    /**
     * 任务UID
     */
    private String uuid = "";
    /**
     * 任务监听用来在广播中注册服务判断任务是否完成
     */
    @JSONField(serialize = false, deserialize = false)
    private Receiver receiver;
    /**
     * 超时判断
     */
    @JSONField(serialize = false, deserialize = false)
    private Runnable runnable;


    public Task() {
        TASK_RUNNABLE.add(runnable = new Runnable() {
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
                    .setNextCan(System.currentTimeMillis() + (CD1 * 2)).apply();
        }

        TaskPoint.getInstance(getHost().longValue())
                .setNextCan(System.currentTimeMillis() + (CD1 * 2))
                .addPrenticeIndex(-1).apply();

        MessageUtils.INSTANCE.sendMessageInGroupWithAt(TASK_OVER_TIME, getFromG().longValue(), getHost());
        destroy();
    }

    public void destroy() {
        Broadcast.receivers.remove(getReceiver());
        deleteTask(Task.this);
        TASK_RUNNABLE.remove(this.runnable);
    }

    public void save() {
        saveTask(this, true);
    }

    public void update() {
        File file = new File(GameTaskDatabase.path, getUuid());
        FileInitializeValue.putValues(file.getAbsolutePath(), this, true);
    }

    /**
     * 获取任务介绍
     *
     * @return
     */
    public abstract String getIntro();

    /**
     * 获取任务完成tips
     *
     * @return
     */
    public abstract String getFinish();

    /**
     * 获取接收器
     *
     * @return
     */
    public abstract Receiver registrationReceiver();

    public static enum Type {
        WEEK, PRENTICE, NORMAL
    }
}
