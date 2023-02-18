package Project.dataBases.task;

import Project.services.detailServices.tasks.*;
import io.github.kloping.mirai0.commons.task.Task;
import io.github.kloping.mirai0.unitls.Tools.Tool;


/**
 * 任务创建
 *
 * @author github-kloping
 */
public class TaskCreator {
    public static final int MAX_PRENTICE_INDEX = 2;
    public static final int MIN_PRENTICE_INDEX = 0;
    public static final int MAX_INDEX = 1003;
    public static final int MIN_INDEX = 1000;

    public static final Integer[] T_1000_OBJS = new Integer[]{
            201, 202, 203, 101, 102, 103, 104, 105, 106, 107, 109, 110, 112, 113, 114, 115, 116, 1000};

    public static <T extends Task> T getTask(int id) {
        if (id == 0) {
            return (T) new Task0();
        } else if (id == 1) {
            return (T) new Task1();
        } else if (id == 1000) {
            return (T) new Task1000();
        } else if (id == 1001) {
            return (T) new Task1001();
        } else if (id == 1002) {
            return (T) new Task1002();
        }
        return null;
    }

    public static int getRandObj1000() {
        return Tool.INSTANCE.getRandT(T_1000_OBJS);
    }
}
