package Project.services.detailServices.roles;

/**
 * @author github.kloping
 */
public enum DamageType {
    /**
     * 物理伤害
     */
    AD("物理伤害"),
    AP("魔法伤害"),
    AB("真实伤害"),
    ;
    public final String name;

    private DamageType(String name) {
        this.name = name;
    }
}
