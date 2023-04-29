package Project.services.detailServices.ac.entity;

import Project.commons.gameEntitys.base.BaseInfo;

import static Project.commons.rt.CommonSource.percentTo;
import static Project.commons.rt.CommonSource.toPercent;

/**
 * @author github-kloping
 * @version 1.0
 */
public class Ghost705 extends GhostWithGroup {
    private long cd = 0;

    public Ghost705() {
    }

    public Ghost705(String forWhoStr) {
        super(forWhoStr);
    }

    public Ghost705(long hp, long att, long id, long l) {
        super(hp, att, id, l);
    }

    public Ghost705(long hp, long att, long xp, int id, long l, float bl) {
        super(hp, att, xp, id, l, bl);
    }

    @Override
    public long updateHp(long l, BaseInfo who) {
        setHp(getHp() + l);
        int b = toPercent(getHp(), getHpL());
        if (b <= 25 && cd < System.currentTimeMillis()) {
            cd = System.currentTimeMillis() + 30 * 1000;
            long lv = getHpL() - getHp();
            long v0 = percentTo(18, lv);
            addHp(v0);
            sendMessage("触发" + getName() + "被动,恢复了" + v0 + "点血");
        }
        int bv = toPercent(-l, getMaxHp());
        bv = bv > 100 ? 100 : bv < 1 ? 1 : bv;
        long v = percentTo(bv, getHj());
        addHj(-v);
        return getHp();
    }

    public long getCd() {
        return cd;
    }

    public void setCd(long cd) {
        this.cd = cd;
    }
}
