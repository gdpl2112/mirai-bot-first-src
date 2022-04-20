package io.github.kloping.mirai0.commons.game;

import java.util.List;

/**
 * 世界boss
 *
 * @author github.kloping
 */
public abstract class ChallengeGhost implements Runnable {
    public String name;
    public List<Integer> loseItems;
    public long att;
    public long hp;
    public long maxHp;
    public long hj;
    public long maxHj;
    public String tag;
    public List<Long> meets;

    public ChallengeGhost(String name, List<Integer> loseItems, long att, long hp, long maxHp, long hj, long maxHj, String tag, List<Long> meets) {
        this.name = name;
        this.loseItems = loseItems;
        this.att = att;
        this.hp = hp;
        this.maxHp = maxHp;
        this.hj = hj;
        this.maxHj = maxHj;
        this.tag = tag;
        this.meets = meets;
    }
}
