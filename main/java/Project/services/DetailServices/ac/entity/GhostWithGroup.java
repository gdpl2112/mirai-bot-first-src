package Project.services.DetailServices.ac.entity;

import io.github.kloping.mirai0.Entitys.Group;
import io.github.kloping.mirai0.Entitys.gameEntitys.GhostObj;

/**
 * @author github-kloping
 * @version 1.0
 */
public class GhostWithGroup extends GhostObj {
    protected Group group;

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }
}
