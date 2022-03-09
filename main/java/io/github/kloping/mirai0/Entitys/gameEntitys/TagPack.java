package io.github.kloping.mirai0.Entitys.gameEntitys;

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
public abstract class TagPack {
    private final String TAG;

    public TagPack(String tag) {
        this.TAG = tag;
    }

    private Long q;
    private Long value;
    private Boolean effected = false;

    /**
     * 生效
     */
    public abstract void effect();

    /**
     * overed
     *
     * @return
     */
    public abstract boolean over();

    /**
     * 失效
     */
    public void loseEffect() {
    }
}
