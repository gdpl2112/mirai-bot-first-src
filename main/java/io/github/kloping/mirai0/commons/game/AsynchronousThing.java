package io.github.kloping.mirai0.commons.game;

import io.github.kloping.date.FrameUtils;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * @author github.kloping
 */
public abstract class AsynchronousThing implements Runnable {
    private ScheduledFuture<?> future;
    public int n;
    public long q1;
    public long q2;
    public long value;
    public long eve;
    public long gid;

    public AsynchronousThing(int n, long q1, long q2, long value, long eve, long gid) {
        this.n = n;
        this.q1 = q1;
        this.q2 = q2;
        this.value = value;
        this.eve = eve;
        this.gid = gid;
    }

    protected String sFormat;

    public AsynchronousThing setFormatStr(String s) {
        this.sFormat = s;
        return this;
    }

    public void start() {
        future = FrameUtils.SERVICE.scheduleWithFixedDelay(this, eve, eve, TimeUnit.MILLISECONDS);
    }
}
