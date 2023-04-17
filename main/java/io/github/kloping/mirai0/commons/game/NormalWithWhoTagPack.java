package io.github.kloping.mirai0.commons.game;


/**
 * 主动
 *
 * @author github.kloping
 */
public class NormalWithWhoTagPack extends NormalTagPack {
    public long time;
    private long who;

    /**
     * @param tag
     * @param t   current + t
     */
    public NormalWithWhoTagPack(String tag, long t) {
        super(tag,t);
    }

    @Override
    public boolean over() {
        return System.currentTimeMillis() >= time;
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
        q = q <= 0 ? -who : q;
        this.q = q;
        return this;
    }
}
