package io.github.kloping.mirai0.commons.game;


import io.github.kloping.mirai0.commons.gameEntitys.TagPack;

/**
 * @author github.kloping
 */
public class NormalTagPack extends TagPack {
    private long time;

    /**
     * @param tag
     * @param t   current + t
     */
    public NormalTagPack(String tag, long t) {
        super(tag);
        this.time = System.currentTimeMillis() + t;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    @Override
    public void eddValue(long v) {
        if (v >= getValue()) {
            setValue(0L);
        } else {
            setValue(getValue() - v);
        }
    }

    @Override
    public boolean over() {
        return System.currentTimeMillis() >= time || getValue() <= 0;
    }
}
