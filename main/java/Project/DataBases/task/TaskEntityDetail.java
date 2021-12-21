package Project.DataBases.task;

import Entitys.gameEntitys.GhostObj;
import Entitys.gameEntitys.task.Task;
import Entitys.gameEntitys.task.TaskPoint;
import Project.Services.DetailServices.TaskDetailService;
import Project.broadcast.enums.ObjType;
import Project.broadcast.game.GhostLostBroadcast;
import io.github.kloping.Mirai.Main.ITools.MessageTools;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static Project.DataBases.GameDataBase.addToBgs;
import static Project.DataBases.GameDataBase.getImgById;
import static Project.DataBases.GameTaskDatabase.cd_0;
import static Project.DataBases.GameTaskDatabase.deleteTask;
import static Project.DataBases.task.TaskCreator.getRandObj1000;
import static Project.Tools.Tool.getRandT;

public class TaskEntityDetail {
    public static class Task1000 extends Task {
        public Map<Integer, Boolean> m1 = new ConcurrentHashMap<>();

        public boolean isFinish() {
            if (m1.containsKey(1))
                if (m1.get(1))
                    if (m1.containsKey(2))
                        if (m1.get(2))
//                            if (m1.containsKey(3))
//                                if (m1.get(3))
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
        public void over() {
            TaskPoint.getInstance(getHost().longValue())
                    .setNextCan(System.currentTimeMillis() + (cd_0))
                    .addPrenticeIndex(-1).apply();

            MessageTools.sendMessageInGroupWithAt("任务过期,未完成", getFromG().longValue(), getHost());
            destroy();
        }
    }

    public static class Task1001 extends Task {
        public int needId;
        public static Integer[] ids = {501, 502, 503, 504, 505, 506, 507, 508, 509, 510, 511, 512,
                513, 514, 515, 516, 517, 518, 519, 520, 601, 602, 603};

        public int getNeedId() {
            return needId;
        }

        public Task1001() {
            this.needId = getRandT(ids);
        }

        @Override
        public void over() {
            TaskPoint.getInstance(getHost().longValue())
                    .setNextCan(System.currentTimeMillis() + (cd_0))
                    .addNormalIndex(-1).apply();

            MessageTools.sendMessageInGroupWithAt("任务过期,未完成", getFromG().longValue(), getHost());
            destroy();
        }
    }


    public static class GhostLostReceiverWithTask0
            extends GhostLostBroadcast.AbstractGhostLostReceiverWith<Task> {
        public GhostLostReceiverWithTask0(Task task) {
            super(task);
        }

        @Override
        public void onReceive(long who, Long with, GhostObj ghostObj) {
            Task task = getT();
            if (task.getHost().longValue() != who) return;
            if (who == task.getHost().longValue()) {
                if (task.getTasker().contains(with.longValue())) {
                    deleteTask(task);
                    MessageTools.sendMessageInGroupWithAt(TaskDetailService.getFinish(task)
                            , task.getFromG().longValue(), task.getHost());
                    addToBgs(who, 1601, ObjType.got);
                    addToBgs(with.longValue(), 1601, ObjType.got);
                    GhostLostBroadcast.INSTANCE.AfterRunnable.add(() -> task.destroy());
                }
            }
        }
    }

    public static class GhostLostReceiverWithTask1001
            extends GhostLostBroadcast.AbstractGhostLostReceiverWith<Task1001> {
        public GhostLostReceiverWithTask1001(Task1001 task) {
            super(task);
        }

        @Override
        public void onReceive(long who, Long with, GhostObj ghostObj) {
            Task1001 task = getT();
            if (task.getHost().longValue() != who) return;
            if (ghostObj.getId().intValue() == task.needId) {
                deleteTask(task);
                int id = getRandObj1000();
                MessageTools.sendMessageInGroupWithAt(TaskDetailService.getFinish(task) + getImgById(id)
                        , task.getFromG().longValue(), task.getHost());
                addToBgs(who, id, ObjType.got);
                GhostLostBroadcast.INSTANCE.AfterRunnable.add(() -> {
                    task.destroy();
                });
            }
        }
    }

    public static class GhostLostReceiverWithTask1000 extends GhostLostBroadcast.AbstractGhostLostReceiverWith<Task1000> {
        public GhostLostReceiverWithTask1000(TaskEntityDetail.Task1000 task1000) {
            super(task1000);
        }

        @Override
        public void onReceive(long who, Long with, GhostObj ghostObj) {
            TaskEntityDetail.Task1000 task = getT();
            if (task.getHost().longValue() != who) return;
            if (ghostObj.getId() < 600)
                task.m1.put(1, true);
            else if (ghostObj.getId() < 700)
                task.m1.put(2, true);
            else if (ghostObj.getId() < 800)
                task.m1.put(3, true);

            task.update();

            if (task.isFinish()) {
                deleteTask(task);
                int id = getRandObj1000();

                MessageTools.sendMessageInGroupWithAt(TaskDetailService.getFinish(task) + getImgById(id)
                        , task.getFromG().longValue(), task.getHost());

                addToBgs(who, id, ObjType.got);
                GhostLostBroadcast.INSTANCE.AfterRunnable.add(() -> {
                    task.destroy();
                });
            }
        }
    }

}
