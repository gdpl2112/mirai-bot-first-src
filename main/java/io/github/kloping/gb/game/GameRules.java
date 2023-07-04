package io.github.kloping.gb.game;

import io.github.kloping.MySpringTool.annotations.Entity;
import io.github.kloping.gb.spring.dao.WhInfo;

/**
 * @author github.kloping
 */
@Entity
public class GameRules {
    public long getDeserveHp(WhInfo whInfo, long v) {
        return v;
    }

    public long getDeserveHl(WhInfo wi, long v) {
        return v;
    }

    public long getDeserveHj(WhInfo wi, long v) {
        return v;
    }


}
