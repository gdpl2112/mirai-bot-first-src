package io.github.kzero.spring.dao;


import io.github.kzero.game.interfaces.BaseInfo;

/**
 * @author github-kloping
 */
public class WhInfo implements BaseInfo {
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
    public Long hjL = 100L;
    /**
     * 等级
     */
    public Integer Level = 1;
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
    public Long xpL = 100L;
    /**
     * 主人
     */
    public Long qid;

    public Integer p;

    @Override
    public String getName() {
        return null;
    }

    @Override
    public Long getAtt() {
        return null;
    }

    @Override
    public <T extends BaseInfo> T setAtt(Long att) {
        return null;
    }

    @Override
    public Long getHp() {
        return null;
    }

    @Override
    public Long getHpL() {
        return null;
    }

    @Override
    public Integer getLevel() {
        return null;
    }

    @Override
    public Long getHj() {
        return null;
    }

    @Override
    public Long getHjL() {
        return null;
    }

    @Override
    public Number getId() {
        return null;
    }

    @Override
    public <T> T apply() {
        return null;
    }

    @Override
    public BaseInfo addHj(Long v) {
        return null;
    }

    @Override
    public BaseInfo addHp(Long v) {
        return null;
    }

    @Override
    public boolean isVertigo() {
        return false;
    }

    @Override
    public <T extends BaseInfo> T cancelVertigo() {
        return null;
    }

    @Override
    public <T extends BaseInfo> T letVertigo(long t) {
        return null;
    }

    @Override
    public <T> T getTips() {
        return null;
    }

    @Override
    public <T extends BaseInfo> T addTag(String myTag, Number percent, long t) {
        return null;
    }

    @Override
    public <T extends BaseInfo> T addTag(String myTag, Number percent, Number max, long t) {
        return null;
    }

    @Override
    public <T extends BaseInfo> T eddTag(String myTag, Number percent) {
        return null;
    }

    @Override
    public <T extends BaseInfo> T eddTag(String myTag) {
        return null;
    }

    @Override
    public boolean containsTag(String tag) {
        return false;
    }

    @Override
    public Number getTagValue(String tag) {
        return null;
    }
}