package Project.skills.e;

import io.github.kloping.mirai0.commons.AttackPackE;

/**
 * @author github.kloping
 */
public class EaAttackPack extends AttackPackE {
    public EaAttackPack(Long initiator, Long target, Long value) {
        super(initiator, target, value);
    }

    @Override
    public <T> T thrown() {
        return null;
    }
}
