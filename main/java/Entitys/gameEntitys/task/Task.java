package Entitys.gameEntitys.task;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.LinkedHashSet;
import java.util.Set;

@Data
@Accessors(chain = true)
public class Task {
    public static enum Type {
        old, normal, expert, ultimate
    }

    private Set<Long> tasker = new LinkedHashSet<>();
    private Long host = -1L;
    private Long fromG = -1L;
    private Long deadline = System.currentTimeMillis();
    private Type type = Type.normal;
    private Integer taskId = -1;
    private Integer state = -1;
    private String uuid = "";

}
