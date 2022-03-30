package io.github.kloping.mirai0.commons.game;

import Project.services.detailServices.GameDetailService;
import io.github.kloping.date.FrameUtils;
import io.github.kloping.mirai0.Main.ITools.MessageTools;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * @author github.kloping
 */
public class AsynchronousAttack implements Runnable {
    private ScheduledFuture<?> future;
    public int n;
    public long q1;
    public long q2;
    public long value;
    public long eve;
    public long gid;

    public AsynchronousAttack(int n, long q1, long q2, long value, long eve, long gid) {
        this.n = n;
        this.q1 = q1;
        this.q2 = q2;
        this.value = value;
        this.eve = eve;
        this.gid = gid;
    }

    private int i = 0;

    @Override
    public void run() {
        if (i++ >= n) {
            future.cancel(true);
        } else {
            String s0 = sFormat == null ? "" : String.format(sFormat, value) + GameDetailService.beaten(q1, q2, value);
            MessageTools.sendMessageInGroup(s0, gid);
        }
    }

    private String sFormat;

    public AsynchronousAttack setFormatStr(String s) {
        this.sFormat = s;
        return this;
    }

    public void start() {
        future = FrameUtils.SERVICE.scheduleWithFixedDelay(this, eve, eve, TimeUnit.MILLISECONDS);
    }
}
