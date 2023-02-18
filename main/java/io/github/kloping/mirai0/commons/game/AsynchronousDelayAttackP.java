package io.github.kloping.mirai0.commons.game;

import Project.services.detailServices.GameDetailService;
import Project.services.detailServices.roles.DamageType;
import io.github.kloping.mirai0.Main.iutils.MessageUtils;

import java.util.concurrent.ScheduledFuture;

/**
 * @author github.kloping
 */
public class AsynchronousDelayAttackP extends AsynchronousThing {
    private ScheduledFuture<?> future;
    private int i = 0;

    /**
     * @param q1    被攻击者
     * @param q2    攻击者
     * @param value 值
     * @param gid
     * @param t     延时
     */
    public AsynchronousDelayAttackP(long q1, long q2, long value, long gid, long t) {
        super(1, q1, q2, value, t, gid);
        setType(AsynchronousThingType.ATTACK);
    }

    public DamageType type = DamageType.AD;

    @Override
    public void run() {
        MessageUtils.INSTANCE.sendMessageInGroup(GameDetailService.beaten(q1, q2, (int) value, type), gid);
        future.cancel(true);
        over();
    }
}
