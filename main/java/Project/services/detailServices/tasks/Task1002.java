package Project.services.detailServices.tasks;

import Project.broadcast.game.GhostLostBroadcast;
import io.github.kloping.mirai0.commons.task.Task;

import static io.github.kloping.mirai0.unitls.Tools.Tool.getRandT;

/**
 * @author github-kloping
 * @version 1.0
 */
public class Task1002 extends Task {
    private GhostLostBroadcast.KillType needType;

    public Task1002() {
        this.needType = getRandT(GhostLostBroadcast.KillType.values());
    }

    public GhostLostBroadcast.KillType getNeedType() {
        return needType;
    }

    public void setNeedType(GhostLostBroadcast.KillType needType) {
        this.needType = needType;
    }
}
