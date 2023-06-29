package io.github.kloping.gb;

/**
 * @author github.kloping
 */
public class FinalConfig {
    /**
     * 精神力抵消后 剩余倍数 <br/>
     * {@link Project.services.detailServices.GameDetailService#onSpiritAttack}
     */
    public static final int HJ_LOSE_1_X = 9;
    /**
     * 最大精神力消耗血量百分比
     */
    public static final int MAX_SA_LOSE_HJ_B = 75;
    /**
     * 最大精神力抵消百分比
     */
    public static final int MAX_SA_LOSE_HP_B = 45;
    /**
     * 最大批量抢劫次数
     */
    public static final int MAX_ROBBERY_TIMES = 12;
    /**
     * 转让最大数量
     */
    public static final int TRANSFER_ONE_MAX = 20;
    /**
     * 一次出售数量
     */
    public static final int SLE_ONE_MAX = 20;
    /**
     * 每周最大收益
     */
    public static final int MAX_EARNINGS = 120000;
    /**
     * 攻击冷却
     */
    public static final int ATT_CD = 6000;
    /**
     * 攻击前摇
     */
    public static final int ATT_PRE_CD = 4500;
    /**
     * 猜拳赢的几率
     */
    public static final int MORA_WIN = 47;
    /**
     * 猜拳平局几率
     */
    public static final int MORA_P = 4;
    /**
     * 闭关冷却
     */
    public static final long BG_CD = 1000 * 60 * 30L;
    /**
     * 最大等级
     */
    public static final Integer MAX_LEVEL = 150;
    /**
     * 冷却药水  作用值
     */
    public static final Integer OBJ116_VALUE = 1000 * 60 * 8;
}
