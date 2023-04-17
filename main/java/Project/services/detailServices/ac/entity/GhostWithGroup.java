package Project.services.detailServices.ac.entity;

import io.github.kloping.mirai0.Main.iutils.MessageUtils;
import io.github.kloping.mirai0.commons.GhostObj;
import Project.commons.SpGroup;

/**
 * @author github-kloping
 * @version 1.0
 */
public class GhostWithGroup extends GhostObj {
    protected SpGroup group;

    public GhostWithGroup() {
    }

    public GhostWithGroup(String forWhoStr) {
        super(forWhoStr);
    }

    public GhostWithGroup(long hp, long att, long id, long l) {
        super(hp, att, id, l);
    }

    public GhostWithGroup(long hp, long att, long xp, int id, long l,  float bl) {
        super(hp, att, xp, id, l, bl);
    }

    public SpGroup getGroup() {
        return group;
    }

    public void setGroup(SpGroup group) {
        this.group = group;
    }

    protected void sendMessage(String str) {
        MessageUtils.INSTANCE.sendMessageInGroup(str, group.getId());
    }

    protected void sendMessage(String str, long q) {
        MessageUtils.INSTANCE.sendMessageInGroupWithAt(str, group.getId(), q);
    }
}
