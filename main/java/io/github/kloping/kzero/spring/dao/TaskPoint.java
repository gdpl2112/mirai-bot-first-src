package io.github.kloping.kzero.spring.dao;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.experimental.Accessors;

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

    public void setMasterIndex(Integer masterIndex) {
        this.masterIndex = masterIndex;
    }

    public void setExpertIndex(Integer expertIndex) {
        this.expertIndex = expertIndex;
    }

    public TaskPoint addMasterIndex() {
        this.masterIndex++;
        return this;
    }

    public TaskPoint addMasterIndex(int i) {
        this.masterIndex += i;
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
