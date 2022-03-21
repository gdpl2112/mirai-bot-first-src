package Project.services.detailServices.ac.entity;

import io.github.kloping.mirai0.commons.Group;
import io.github.kloping.mirai0.commons.GhostObj;
import io.github.kloping.mirai0.Main.ITools.MessageTools;

/**
 * @author github-kloping
 * @version 1.0
 */
public class GhostWithGroup extends GhostObj {
    protected Group group;

    public GhostWithGroup() {
    }

    public GhostWithGroup(String forWhoStr) {
        super(forWhoStr);
    }

    public GhostWithGroup(long hp, long att, long xp, long id, long l) {
        super(hp, att, xp, id, l);
    }

    public GhostWithGroup(long hp, long att, long xp, int idMin, int idMax, long l, boolean rand, float bl) {
        super(hp, att, xp, idMin, idMax, l, rand, bl);
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    protected void sendMessage(String str) {
        MessageTools.sendMessageInGroup(str, group.getId());
    }

    protected void sendMessage(String str, long q) {
        MessageTools.sendMessageInGroupWithAt(str, group.getId(), q);
    }
}
