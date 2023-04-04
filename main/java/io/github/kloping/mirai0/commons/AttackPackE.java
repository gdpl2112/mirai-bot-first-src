package io.github.kloping.mirai0.commons;

import Project.dataBases.GameDataBase;
import Project.interfaces.entities.AttackPack;

/**
 * @author github.kloping
 */
public abstract class AttackPackE implements AttackPack {
    private Long initiator;
    private Long target;
    private Integer state = -1;

    protected Long value = 0L;
    protected Long fValue = value;

    public AttackPackE(Long initiator, Long target, Long value) {
        this.initiator = initiator;
        this.target = target;
        this.value = value;
    }

    public Long getValue() {
        return value;
    }

    public void setValue(Long value) {
        this.value = value;
    }

    public Long getValueF() {
        return fValue;
    }

    @Override
    public Long getInitiator() {
        return initiator;
    }

    @Override
    public Long getTarget() {
        return target;
    }

    @Override
    public Integer getState() {
        return state;
    }

    protected PersonInfo getInfo() {
        return GameDataBase.getInfo(initiator);
    }

    protected PersonInfo getTargetInfo() {
        return GameDataBase.getInfo(target);
    }
}
