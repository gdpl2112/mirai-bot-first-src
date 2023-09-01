package io.github.kloping.kzero.game.interfaces;

/**
 * @author github-kloping
 * @version 1.0
 */
public interface BaseInfo {
    /**
     * get name
     *
     * @return
     */
    String getName();

    /**
     * get attack value
     *
     * @return
     */
    Long getAtt();

    /**
     * set att
     *
     * @param att
     * @param <T>
     * @return
     */
    <T extends BaseInfo> T setAtt(Long att);

    /**
     * get HP value
     *
     * @return
     */
    Long getHp();

    /**
     * get max HP value
     *
     * @return
     */
    Long getHpL();

    /**
     * get Level Value
     *
     * @return
     */
    Integer getLevel();

    /**
     * get spirit value
     *
     * @return
     */
    Long getHj();

    /**
     * get max spirit value
     *
     * @return
     */
    Long getHjL();

    /**
     * get ID
     *
     * @return
     */
    Number getId();

    /**
     * save or apply
     *
     * @param <T>
     * @return
     */
    <T> T apply();

    /**
     * add Hj value
     *
     * @param v
     * @return
     */
    BaseInfo addHj(Long v);

    /**
     * add Hp value
     *
     * @param v
     * @return
     */
    BaseInfo addHp(Long v);

    /**
     * 是否被眩晕
     *
     * @return
     */
    boolean isVertigo();

    /**
     * cancel Vertigo
     *
     * @param <T>
     * @return
     */
    <T extends BaseInfo> T cancelVertigo();

    /**
     * set Vertigo and t ms after over
     *
     * @param t
     * @param <T>
     * @return
     */
    <T extends BaseInfo> T letVertigo(long t);

    /**
     * get tips
     *
     * @return
     */
    <T> T getTips();

    /**
     * add a tag
     *
     * @param myTag   tag name
     * @param percent value
     * @param t       time
     * @param <T>
     * @return
     */
    <T extends BaseInfo> T addTag(String myTag, Number percent, long t);

    /**
     * add a tag with max value
     *
     * @param myTag   tag name
     * @param percent value
     * @param max     max
     * @param t       time
     * @param <T>
     * @return
     */
    <T extends BaseInfo> T addTag(String myTag, Number percent, Number max, long t);

    /**
     * remove a tag
     *
     * @param myTag   tag
     * @param percent value
     * @param <T>
     * @return
     */
    <T extends BaseInfo> T eddTag(String myTag, Number percent);


    /**
     * reduce a tag No matter how many
     *
     * @param myTag tag name
     * @param <T>
     * @return
     */
    <T extends BaseInfo> T eddTag(String myTag);

    /**
     * contain a tag
     *
     * @param tag tag name
     * @return
     */
    boolean containsTag(String tag);

    /**
     * get tag value
     *
     * @param tag tag name
     * @return
     */
    Number getTagValue(String tag);
}
