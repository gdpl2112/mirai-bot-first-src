package Entitys.gameEntitys.base;

/**
 * @author github-kloping
 * @version 1.0
 */
public interface BaseInfo {
    /**
     * get attack value
     *
     * @return
     */
    Long getAtt();

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
}
