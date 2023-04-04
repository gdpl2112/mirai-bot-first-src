package Project.skills.e;

import io.github.kloping.mirai0.commons.AttackPackE;

import static Project.dataBases.skill.SkillDataBase.TAG_E_A;

/**
 * @author github.kloping
 */
public class EaAttackPack extends AttackPackE {
    public EaAttackPack(Long initiator, Long target, Long value) {
        super(initiator, target, value);
    }

    /**
     * 抛出攻击
     *
     * @param <T>
     * @return
     */
    @Override
    public <T> T thrown() {
        if (getTargetInfo().containsTag(TAG_E_A)) {

        }
        return null;
    }
}
