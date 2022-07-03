package Project.services.detailServices.tasks;

import Project.broadcast.game.GhostLostBroadcast;
import io.github.kloping.mirai0.commons.task.Task;
import io.github.kloping.mirai0.unitls.Tools.Tool;


/**
 * @author github-kloping
 * @version 1.0
 */
public class Task1000 extends Task {
    private GhostLostBroadcast.KillType needType;

    public Task1000() {
        this.needType = Tool.tool.getRandT(GhostLostBroadcast.KillType.values());
    }

    public GhostLostBroadcast.KillType getNeedType() {
        return needType;
    }

    public void setNeedType(GhostLostBroadcast.KillType needType) {
        this.needType = needType;
    }
}
