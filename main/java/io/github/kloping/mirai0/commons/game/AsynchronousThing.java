package io.github.kloping.mirai0.commons.game;

import Project.services.detailServices.GameSkillDetailService;
import io.github.kloping.date.FrameUtils;
import io.github.kloping.map.MapUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * @author github.kloping
 */
public abstract class AsynchronousThing implements Runnable {
    public static final Map<Long, List<Future>> L_2_FS = new HashMap<>();
    public int n;
    public long q1;
    public long q2;
    public long value;
    public long eve;
    public long gid;
    public AsynchronousThingType type = AsynchronousThingType.NORMAL;
    protected String sFormat;
    private ScheduledFuture<?> future;

    public AsynchronousThing(int n, long q1, long q2, long value, long eve, long gid) {
        this.n = n;
        this.q1 = q1;
        this.q2 = q2;
        this.value = value;
        this.eve = eve;
        this.gid = gid;
    }

    public AsynchronousThingType getType() {
        return type;
    }

    public void setType(AsynchronousThingType type) {
        this.type = type;
    }

    public AsynchronousThing setFormatStr(String s) {
        this.sFormat = s;
        return this;
    }

    public void start() {
        future = FrameUtils.SERVICE.scheduleWithFixedDelay(this, eve, eve, TimeUnit.MILLISECONDS);
        MapUtils.append(L_2_FS, q2, future);
    }

    public void over() {
        future.cancel(true);
        GameSkillDetailService.ASYNCHRONOUS_THING_MAP.get(q1).remove(this);
    }
}
