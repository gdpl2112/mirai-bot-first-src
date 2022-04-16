package io.github.kloping.mirai0.commons.game;

import Project.services.detailServices.GameDetailService;
import io.github.kloping.mirai0.Main.ITools.MessageTools;

import java.util.concurrent.ScheduledFuture;

/**
 * @author github.kloping
 */
public class AsynchronousAttack extends AsynchronousThing {
    private ScheduledFuture<?> future;

    public AsynchronousAttack(int n, long q1, long q2, long value, long eve, long gid) {
        super(n, q1, q2, value, eve, gid);
        setType(AsynchronousThingType.ATTACK);
    }

    private int i = 0;

    @Override
    public void run() {
        if (i++ >= n) {
            future.cancel(true);
            over();
        } else {
            String s0 = sFormat == null ? "" : String.format(sFormat, value) + GameDetailService.addHp(q1, (int) value);
            if (!s0.isEmpty()) MessageTools.sendMessageInGroup(s0, gid);
        }
    }
}
