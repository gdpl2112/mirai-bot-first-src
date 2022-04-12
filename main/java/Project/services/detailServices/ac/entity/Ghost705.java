package Project.services.detailServices.ac.entity;

import Project.dataBases.skill.SkillDataBase;
import io.github.kloping.mirai0.commons.gameEntitys.base.BaseInfo;

import static Project.dataBases.skill.SkillDataBase.percentTo;
import static io.github.kloping.mirai0.unitls.Tools.Tool.toPercent;

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

    public Ghost705(long hp, long att, long xp, long id, long l) {
        super(hp, att, xp, id, l);
    }

    public Ghost705(long hp, long att, long xp, int idMin, int idMax, long l, boolean rand, float bl) {
        super(hp, att, xp, idMin, idMax, l, rand, bl);
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
        int bv = SkillDataBase.toPercent(-l, getMaxHp());
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
