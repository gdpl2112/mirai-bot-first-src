package io.github.kloping.mirai0.commons;

import io.github.kloping.mirai0.commons.gameEntitys.TagPack;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import static Project.dataBases.GameDataBase.getInfo;
import static Project.dataBases.skill.SkillDataBase.TAG_SHIELD;

/**
 * @author github.kloping
 */
@Accessors(chain = true)
@Getter
@Setter
public class ShieldPack extends TagPack {
    public ShieldPack() {
        super(TAG_SHIELD);
    }

    private Long time;
    private Long max;

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
