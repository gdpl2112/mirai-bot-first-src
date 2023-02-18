package Project.services.detailServices.ac.entity;

import Project.services.detailServices.GameDetailService;
import Project.services.detailServices.roles.DamageType;
import io.github.kloping.mirai0.commons.gameEntitys.base.BaseInfo;
import io.github.kloping.mirai0.unitls.Tools.Tool;

import static io.github.kloping.mirai0.commons.resouce_and_tool.CommonSource.percentTo;

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

    public Ghost703(long hp, long att, long id, long l) {
        super(hp, att, id, l);
    }

    public Ghost703(long hp, long att, long xp, int id, long l,   float bl) {
        super(hp, att, xp, id, l,  bl);
    }

    @Override
    public long updateHp(long l, BaseInfo who) {
        l = -l;
        l = l > getHp() ? getHp() : l;
        if (Tool.INSTANCE.RANDOM.nextInt(2) == 0) {
            long v1 = percentTo(Math.toIntExact(l), 15);
            sendMessage("受到反甲效果:\n受到" + v1 + "点反弹伤害\n" + GameDetailService.beaten(who.getId(), -1, v1, DamageType.AD), who.getId().longValue());
        }
        return super.updateHp(-l, who);
    }
}
