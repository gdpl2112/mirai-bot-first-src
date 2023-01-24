package Project.services.detailServices.ac.entity;

import io.github.kloping.mirai0.commons.game.NormalWithWhoTagPack;
import io.github.kloping.mirai0.commons.gameEntitys.base.BaseInfo;
import io.github.kloping.number.NumberUtils;

import static Project.dataBases.skill.SkillDataBase.TAG_MS;
import static Project.dataBases.skill.SkillDataBase.TAG_SHIELD;
import static io.github.kloping.mirai0.commons.resouce_and_tool.CommonSource.percentTo;
import static io.github.kloping.mirai0.commons.resouce_and_tool.CommonSource.toPercent;

/**
 * @author github-kloping
 * @version 1.0
 */
public class Ghost710 extends GhostWithGroup {
    private boolean k = false;

    public Ghost710() {
        if (!k) init();
    }

    public Ghost710(String forWhoStr) {
        super(forWhoStr);
        if (!k) init();
    }

    public Ghost710(long hp, long att, long id, long l) {
        super(hp, att, id, l);
        if (!k) init();
    }

    public Ghost710(long hp, long att, long xp, int id, long l, float bl) {
        super(hp, att, xp, id, l, bl);
        if (!k) init();
    }

    private synchronized void init() {
        k = true;
        this.addTag(TAG_MS, 15, 1000 * 60 * 300);
    }
}
