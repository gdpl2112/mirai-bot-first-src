package io.github.kloping.mirai0.commons.game;


import io.github.kloping.mirai0.commons.gameEntitys.TagPack;
import io.github.kloping.mirai0.commons.gameEntitys.base.BaseInfo;

import static Project.services.detailServices.GameDetailServiceUtils.getBaseInfoFromAny;

/**
 * 主动
 *
 * @author github.kloping
 */
public class NormalWithWhoTagPack extends TagPack {
    public long time;
    private long who;

    /**
     * @param tag
     * @param t   current + t
     */
    public NormalWithWhoTagPack(String tag, long t) {
        super(tag);
        time = System.currentTimeMillis() + t;
    }

    @Override
    public void effect() {
        BaseInfo info = getBaseInfoFromAny(who, getQ());
        long v = getValue();
        v = v > info.getHpL() ? info.getHpL() : v;
        if (info != null)
            info.addTag(getTAG(), v).apply();
        setEffected(true);
    }

    @Override
    public boolean over() {
        return System.currentTimeMillis() >= time;
    }

    @Override
    public void loseEffect() {
        BaseInfo info = getBaseInfoFromAny(who, getQ());
        long v = getValue();
        v = v > info.getHpL() ? info.getHpL() : v;
        if (info != null) {
            info.eddTag(getTAG(), v).apply();
        }
    }

    public long getWho() {
        return who;
    }

    public NormalWithWhoTagPack setWho(long who) {
        this.who = who;
        return this;
    }

    @Override
    public NormalWithWhoTagPack setQ(Long q) {
        this.q = q;
        return this;
    }
}
