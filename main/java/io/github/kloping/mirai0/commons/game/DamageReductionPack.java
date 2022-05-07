package io.github.kloping.mirai0.commons.game;

import io.github.kloping.mirai0.commons.gameEntitys.TagPack;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import static Project.dataBases.GameDataBase.getInfo;
import static Project.dataBases.skill.SkillDataBase.TAG_DAMAGE_REDUCTION;

/**
 * @author github-kloping
 */
@Accessors(chain = true)
@Getter
@Setter
public class DamageReductionPack extends TagPack {

    private Long time = System.currentTimeMillis() + 2 * 60 * 1000L;
    private Long max = 90L;

    public DamageReductionPack() {
        super(TAG_DAMAGE_REDUCTION);
    }

    @Override
    public void effect() {
        setEffected(true);
        getInfo(getQ().longValue()).addTag(getTAG(), getValue(), getMax()).apply();
    }

    @Override
    public boolean over() {
        return System.currentTimeMillis() > getTime().longValue();
    }

    @Override
    public void loseEffect() {
        getInfo(getQ().longValue()).eddTag(getValue(), getTAG()).apply();
    }
}