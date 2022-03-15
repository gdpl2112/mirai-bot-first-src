package io.github.kloping.mirai0.Entitys.gameEntitys.task;

import Project.aSpring.SpringBootResource;
import Project.dataBases.GameDataBase;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.github.kloping.initialize.FileInitializeValue;
import lombok.Data;
import lombok.experimental.Accessors;

import static Project.dataBases.task.TaskCreator.*;

/**
 * @author github-kloping
 */
@Data
@Accessors(chain = true)
public class TaskPoint {
    @TableId
    @TableField("`q`")
    private Long q = -1L;

    @TableField("`prentice_index`")
    private Integer prenticeIndex = 0;

    @TableField("`master_index`")
    private Integer masterIndex = 0;

    @TableField("`normal_index`")
    private Integer normalIndex = 1000;

    @TableField("`expert_index`")
    private Integer expertIndex = 0;

    @TableField("`next_can`")
    private Long nextCan = 0L;

    public static TaskPoint getInstanceFromFile(long q) {
        String d = GameDataBase.path + "/dates/users/" + q + "/taskPoint.json";
        return FileInitializeValue.getValue(d, new TaskPoint().setQ(q), true);
    }

    public static TaskPoint getInstance(long q) {
        TaskPoint taskPoint = SpringBootResource.getTaskPointMapper().selectById(q);
        if (taskPoint == null) {
            taskPoint = new TaskPoint().setQ(q);
            SpringBootResource.getTaskPointMapper().insert(taskPoint);
        }
        return taskPoint;
    }

    public void apply() {
        SpringBootResource.getTaskPointMapper().updateById(this);
    }

    public TaskPoint setNormalIndex(Integer normalIndex) {
        this.normalIndex = normalIndex;
        if (this.normalIndex < MIN_INDEX) {
            this.normalIndex = MIN_INDEX;
        } else if (this.normalIndex > MAX_INDEX) {
            this.normalIndex = MAX_INDEX;
        }
        return this;
    }

    public void setPrenticeIndex(Integer prenticeIndex) {
        this.prenticeIndex = prenticeIndex;
        if (this.prenticeIndex < MIN_PRENTICE_INDEX) {
            this.prenticeIndex = MIN_PRENTICE_INDEX;
        } else if (this.prenticeIndex > MAX_PRENTICE_INDEX) {
            this.prenticeIndex = MAX_PRENTICE_INDEX;
        }
    }

    public void setMasterIndex(Integer masterIndex) {
        this.masterIndex = masterIndex;
    }

    public void setExpertIndex(Integer expertIndex) {
        this.expertIndex = expertIndex;
    }

    public TaskPoint addPrenticeIndex() {
        this.prenticeIndex++;
        if (this.prenticeIndex < MIN_PRENTICE_INDEX) {
            this.prenticeIndex = MIN_PRENTICE_INDEX;
        } else if (this.prenticeIndex > MAX_PRENTICE_INDEX) {
            this.prenticeIndex = MAX_PRENTICE_INDEX;
        }
        return this;
    }

    public TaskPoint addPrenticeIndex(int i) {
        this.prenticeIndex += i;
        if (this.prenticeIndex < MIN_PRENTICE_INDEX) {
            this.prenticeIndex = MIN_PRENTICE_INDEX;
        } else if (this.prenticeIndex > MAX_PRENTICE_INDEX) {
            this.prenticeIndex = MAX_PRENTICE_INDEX;
        }
        return this;
    }

    public TaskPoint addNormalIndex() {
        this.normalIndex++;
        if (this.normalIndex < MIN_INDEX) {
            this.normalIndex = MIN_INDEX;
        } else if (this.normalIndex > MAX_INDEX) {
            this.normalIndex = MAX_INDEX;
        }
        return this;
    }

    public TaskPoint addMasterIndex() {
        this.masterIndex++;
        return this;
    }

    public TaskPoint addMasterIndex(int i) {
        this.masterIndex += i;
        return this;
    }

    public TaskPoint addNormalIndex(int i) {
        this.normalIndex += i;
        if (this.normalIndex < MIN_INDEX) {
            this.normalIndex = MIN_INDEX;
        } else if (this.normalIndex > MAX_INDEX) {
            this.normalIndex = MAX_INDEX;
        }
        return this;
    }

    public TaskPoint addExpertIndex() {
        this.expertIndex++;
        return this;
    }

    public TaskPoint addExpertIndex(int i) {
        this.expertIndex += i;
        return this;
    }

    public TaskPoint addNextCan() {
        this.nextCan++;
        return this;
    }

    public TaskPoint addNextCan(int i) {
        this.nextCan += i;
        return this;
    }
}
