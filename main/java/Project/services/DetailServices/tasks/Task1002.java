package Project.services.DetailServices.tasks;

import Entitys.gameEntitys.task.Task;
import Entitys.gameEntitys.task.TaskPoint;
import Project.broadcast.game.GhostLostBroadcast;
import io.github.kloping.Mirai.Main.ITools.MessageTools;

import static Project.DataBases.GameTaskDatabase.cd_0;
import static Project.Tools.Tool.getRandT;

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
