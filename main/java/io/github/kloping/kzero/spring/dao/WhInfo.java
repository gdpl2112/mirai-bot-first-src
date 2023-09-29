package io.github.kloping.kzero.spring.dao;


import io.github.kloping.number.NumberUtils;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author github-kloping
 */
@Data
@Accessors(chain = true)
public class WhInfo {
    /**
     * 攻击
     */
    public Long att = 10L;
    /**
     * 魂力
     */
    public Long hl = 100L;
    /**
     * 魂力最大值
     */
    public Long hll = 100L;
    /**
     * 血量
     */
    public Long hp = 100L;
    /**
     * 最大血量
     */
    public Long hpl = 100L;
    /**
     * 精神力
     */
    public Long hj = 100L;
    /**
     * 最大精神力
     */
    public Long hjl = 100L;
    /**
     * 等级
     */
    public Integer level = 1;
    /**
     * 武魂
     */
    public Integer wh = 0;
    /**
     * 武魂类型
     */
    public Integer whType = -1;
    /**
     * 经验
     */
    public Long xp = 0L;
    public Long xpl = 100L;
    /**
     * 主人
     */
    public String sid;
    public Integer p;

    public Integer getXpPercent() {
        return NumberUtils.toPercent(getXp(), getXpl());
    }

    public Integer getHpPercent() {
        return NumberUtils.toPercent(getHp(), getHpl());
    }

    public Integer getHlPercent() {
        return NumberUtils.toPercent(getHl(), getHll());
    }

    public Integer getHjPercent() {
        return NumberUtils.toPercent(getHj(), getHjl());
    }

    public WhInfo addXp(long xp) {
        this.xp = this.xp + xp;
        return this;
    }

    public void addHp(long hp) {
        this.hp += hp;
        this.hp = this.hp < 0 ? 0 : this.hp > this.hpl ? this.hpl : this.hp;
    }

    public void addHl(long hl) {
        this.hl += hl;
        this.hl = this.hl < 0 ? 0 : this.hl > this.hll ? this.hll : this.hl;
    }

    public void addHj(long hj) {
        this.hj += hj;
        this.hj = this.hj < 0 ? 0 : this.hj > this.hjl ? this.hjl : this.hj;
    }
}
