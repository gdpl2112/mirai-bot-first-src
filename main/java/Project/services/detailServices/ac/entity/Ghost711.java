package Project.services.detailServices.ac.entity;

import Project.commons.gameEntitys.base.BaseInfo;

import static Project.dataBases.GameDataBase.getInfo;

/**
 * @author github-kloping
 * @version 1.0
 */
public class Ghost711 extends GhostWithGroup {

    public Ghost711() {
    }

    public Ghost711(String forWhoStr) {
        super(forWhoStr);
    }

    public Ghost711(long hp, long att, long id, long l) {
        super(hp, att, id, l);
    }

    public Ghost711(long hp, long att, long xp, int id, long l, float bl) {
        super(hp, att, xp, id, l, bl);
    }

    @Override
    public synchronized long updateHp(long l, BaseInfo who) {
        for (Long with : getWiths()) {
            getInfo(with).letVertigo(1000L);
        }
        getInfo(getWhoMeet()).letVertigo(1000L);
        return super.updateHp(l, who);
    }
}
