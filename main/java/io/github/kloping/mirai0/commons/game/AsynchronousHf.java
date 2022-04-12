package io.github.kloping.mirai0.commons.game;

import Project.services.detailServices.GameDetailService;

import java.util.concurrent.ScheduledFuture;

/**
 * @author github.kloping
 */
public class AsynchronousHf extends AsynchronousThing {
    private ScheduledFuture<?> future;

    public AsynchronousHf(int n, long q1, long q2, long value, long eve, long gid) {
        super(n, q1, q2, value, eve, gid);
        setType(AsynchronousThingType.HF);
    }

    private int i = 0;

    @Override
    public void run() {
        if (i++ >= n) {
            future.cancel(true);
            over();
        } else {
            GameDetailService.addHp(q1, (int) value);
        }
    }
}
