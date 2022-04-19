package Project.services.detailServices.ac.entity;

import io.github.kloping.mirai0.commons.gameEntitys.base.BaseInfo;

/**
 * @author github-kloping
 * @version 1.0
 */
public class Ghost704 extends GhostWithGroup {
    private boolean k = true;

    public Ghost704() {
    }

    public Ghost704(String forWhoStr) {
        super(forWhoStr);
    }

    public Ghost704(long hp, long att, long xp, long id, long l) {
        super(hp, att, xp, id, l);
    }

    public Ghost704(long hp, long att, long xp, int id, long l, boolean rand, float bl) {
        super(hp, att, xp, id, l, rand, bl);
    }

    public boolean isK() {
        return k;
    }

    public void setK(boolean k) {
        this.k = k;
    }

    @Override
    public long updateHp(long l, BaseInfo who) {
        if (k && (-l) > getHp()) {
            sendMessage("来自地狱魔的力量免疫该次死亡", who.getId().longValue());
            k = false;
            return getHp();
        }
        return super.updateHp(l, who);
    }
}
