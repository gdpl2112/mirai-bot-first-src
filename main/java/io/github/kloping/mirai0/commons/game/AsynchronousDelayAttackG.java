package io.github.kloping.mirai0.commons.game;

import Project.broadcast.game.GhostLostBroadcast;
import Project.services.detailServices.roles.DamageType;
import io.github.kloping.mirai0.Main.iutils.MessageUtils;

import java.util.concurrent.ScheduledFuture;

import static Project.services.detailServices.ac.GameJoinDetailService.attGho;

/**
 * @author github.kloping
 */
public class AsynchronousDelayAttackG extends AsynchronousThing {
    public DamageType type = DamageType.AD;
    private ScheduledFuture<?> future;
    private int i = 0;

    /**
     * @param q1    攻击者
     * @param value 值
     * @param gid
     * @param t     延时
     */
    public AsynchronousDelayAttackG(long q1, long value, long gid, long t) {
        super(1, q1, -q1, value, t, gid);
        setType(AsynchronousThingType.ATTACK);
    }

    @Override
    public void run() {
        StringBuilder sb = new StringBuilder();
        sb.append(attGho(q1, value, type, false, GhostLostBroadcast.KillType.SKILL_ATT));
        MessageUtils.INSTANCE.sendMessageInGroup(sb.toString(), gid);
        future.cancel(true);
        over();
    }
}
