package io.github.kloping.mirai0.commons.game;


import io.github.kloping.mirai0.commons.gameEntitys.TagPack;

import static Project.dataBases.GameDataBase.getInfo;

/**
 * @author github.kloping
 */
public class NormalTagPack extends TagPack {
    public long time;

    /**
     * @param tag
     * @param t   current + t
     */
    public NormalTagPack(String tag, long t) {
        super(tag);
        time = System.currentTimeMillis() + t;
    }

    @Override
    public void effect() {
        getInfo(getQ()).addTag(getTAG(), getValue()).apply();
        setEffected(true);
    }

    @Override
    public boolean over() {
        return System.currentTimeMillis() >= time;
    }

    @Override
    public void loseEffect() {
        getInfo(getQ()).eddTag(getTAG(), getValue()).apply();
    }
}
