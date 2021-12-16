package Project.DataBases.task;

import Entitys.gameEntitys.task.Task;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TaskEntityDetail {
    public static class Task1000 extends Task {
        public Map<Integer, Boolean> m1 = new ConcurrentHashMap<>();

        public boolean isFinish() {
            if (m1.containsKey(1))
                if (m1.get(1))
                    if (m1.containsKey(2))
                        if (m1.get(2))
                            if (m1.containsKey(3))
                                if (m1.get(3))
                                    return true;
            return false;
        }

        public Map<Integer, Boolean> getM1() {
            return m1;
        }

        public void setM1(Map<Integer, Boolean> m1) {
            this.m1 = m1;
        }

        @Override
        public void save() {
            super.save();
        }
    }
}
