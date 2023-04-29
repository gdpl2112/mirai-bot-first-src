package io.github.kloping.mirai0.commons.game;

import Project.services.detailServices.GameDetailService;
import Project.services.detailServices.roles.DamageType;
import io.github.kloping.mirai0.Main.iutils.MessageUtils;

import java.util.concurrent.ScheduledFuture;

/**
 * @author github.kloping
 */
public class AsynchronousAttack extends AsynchronousThing {
    public DamageType type = DamageType.AD;
    private ScheduledFuture<?> future;
    private int i = 0;

    public AsynchronousAttack(int n, long q1, long q2, long value, long eve, long gid) {
        super(n, q1, q2, value, eve, gid);
        setType(AsynchronousThingType.ATTACK);
    }

    @Override
    public void run() {
        if (i++ >= n) {
            future.cancel(true);
            over();
        } else {
            String s0 = sFormat == null ? "" : String.format(sFormat, value) + GameDetailService.beaten(q1, q2, (int) value, type);
            if (!s0.isEmpty()) MessageUtils.INSTANCE.sendMessageInGroup(s0, gid);
        }
    }
}
