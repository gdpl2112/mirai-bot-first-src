package Project.services.detailServices.ac.entity;

import io.github.kloping.mirai0.commons.PersonInfo;
import io.github.kloping.mirai0.commons.gameEntitys.base.BaseInfo;

import static Project.dataBases.skill.SkillDataBase.TAG_TRUE;

/**
 * @author github-kloping
 * @version 1.0
 */
public class Ghost702 extends GhostWithGroup {
    private Long shield = null;

    public Ghost702() {
    }

    public Ghost702(String forWhoStr) {
        super(forWhoStr);
    }

    public Ghost702(long hp, long att, long id, long l) {
        super(hp, att, id, l);
    }

    public Ghost702(long hp, long att, long xp, int id, long l,  float bl) {
        super(hp, att, xp, id, l,  bl);
    }

    public Long getShield() {
        return shield;
    }

    public void setShield(Long shield) {
        this.shield = shield;
    }

    @Override
    public long updateHp(long l0, BaseInfo who) {
        if (shield == null) {
            shield = getHpL() / 5;
        }
        long l = -l0;
        if (who instanceof PersonInfo) {
            PersonInfo p1 = (PersonInfo) who;
            if (p1.containsTag(TAG_TRUE)) {
                sendMessage("真实伤害护盾未生效\n护盾剩余:" + shield, who.getId().longValue());
                return super.updateHp(l0, who);
            } else {
                if (shield.longValue() > 0) {
                    if (l > shield.longValue()) {
                        l = l - shield.longValue();
                        shield = 0L;
                        sendMessage("伤害部分护盾抵挡\n伤害剩余:" + l, who.getId().longValue());
                        return super.updateHp(-l, who);
                    } else {
                        shield = l - shield;
                        sendMessage("伤害全部护盾抵挡\n护盾剩余:" + shield);
                        return super.updateHp(0, who);
                    }
                }
            }
        }
        return super.updateHp(-l, who);
    }
}
