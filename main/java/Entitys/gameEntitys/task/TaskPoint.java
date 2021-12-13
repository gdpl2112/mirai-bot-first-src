package Entitys.gameEntitys.task;

import Project.DataBases.GameDataBase;
import io.github.kloping.initialize.FileInitializeValue;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class TaskPoint {
    private Long q = -1L;
    private Integer prenticeIndex = 0;
    private Integer masterIndex = 0;
    private Integer normalIndex = 0;
    private Integer expertIndex = 0;
    private Long nextCan = 0L;

    public static TaskPoint getInstance(long q) {
        String d = GameDataBase.path + "/dates/users/" + q + "/taskPoint";
        return FileInitializeValue.getValue(d, new TaskPoint().setQ(q), true);
    }

    public void apply() {
        String d = GameDataBase.path + "/dates/users/" + q + "/taskPoint";
        FileInitializeValue.putValues(d, this, true);
    }

    public TaskPoint addPrenticeIndex() {
        this.prenticeIndex++;
        return this;
    }

    public TaskPoint addPrenticeIndex(int i) {
        this.prenticeIndex += i;
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

    public TaskPoint addNormalIndex() {
        this.normalIndex++;
        return this;
    }

    public TaskPoint addNormalIndex(int i) {
        this.normalIndex += i;
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
