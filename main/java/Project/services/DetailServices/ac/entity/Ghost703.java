package Project.services.DetailServices.ac.entity;

import Project.services.DetailServices.GameDetailService;
import io.github.kloping.mirai0.Entitys.gameEntitys.base.BaseInfo;

import static io.github.kloping.mirai0.unitls.Tools.Tool.percentTo;
import static io.github.kloping.mirai0.unitls.Tools.Tool.rand;

/**
 * @author github-kloping
 * @version 1.0
 */
public class Ghost703 extends GhostWithGroup {
    public Ghost703() {
    }

    public Ghost703(String forWhoStr) {
        super(forWhoStr);
    }

    public Ghost703(long hp, long att, long xp, long id, long l) {
        super(hp, att, xp, id, l);
    }

    public Ghost703(long hp, long att, long xp, int idMin, int idMax, long l, boolean rand, float bl) {
        super(hp, att, xp, idMin, idMax, l, rand, bl);
    }

    @Override
    public long updateHp(long l, BaseInfo who) {
        l = l > getHp() ? getHp() : l;
        if (rand.nextInt(2) == 0) {
            long v1 = percentTo(Math.toIntExact(l), 15);
            sendMessage("受到反甲效果:\n" + GameDetailService.beaten(who.getId(), -1, v1), who.getId().longValue());
        }
        return super.updateHp(l, who);
    }
}
