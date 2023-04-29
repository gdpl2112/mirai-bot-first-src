package io.github.kloping.mirai0.commons;

import Project.commons.gameEntitys.TagPack;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import static Project.dataBases.skill.SkillDataBase.TAG_SHIELD;

/**
 * @author github.kloping
 */
@Accessors(chain = true)
@Getter
@Setter
public class ShieldPack extends TagPack {
    private Long time;
    private Long max;

    public ShieldPack() {
        super(TAG_SHIELD);
    }

    @Override
    public void eddValue(long v) {
        if (v >= getValue()) {
            setValue(0L);
        } else {
            setValue(getValue() - v);
        }
    }

    @Override
    public boolean over() {
        return System.currentTimeMillis() >= time || getValue() == 0;
    }
}
