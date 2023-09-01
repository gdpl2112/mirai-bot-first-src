package io.github.kloping.kzero.spring.dao;


import com.baomidou.mybatisplus.annotation.TableId;
import io.github.kloping.kzero.game.interfaces.BaseInfo;

/**
 * @author github-kloping
 */
public class PersonInfo implements BaseInfo {
    //==============
    /**
     * 金魂币
     */
    public Long gold = 200L;
    /**
     * 支援次数
     */
    public Integer helpToc = 0;
    /**
     * 请求支援次数
     */
    public Integer helpC = 0;
    /**
     * 购买请求次数
     */
    public Integer BuyHelpC = 0;
    /**
     * 购买支援次数
     */
    public Integer BuyHelpToC = 0;
    /**
     * 修炼冷却
     */
    public Long k1 = 1L;
    /**
     * 进入冷却
     */
    public Long k2 = 1L;
    /**
     * 购买 冷却
     */
    public Long gk1 = 1L;
    /**
     * 使用冷却
     */
    public Long uk1 = 1L;
    /**
     * 加入宗门冷却
     */
    public Long jk1 = 1L;
    /**
     * 宗门贡献冷却
     */
    public Long Cbk1 = 1L;
    /**
     * 封号修改冷却
     */
    public Long mk1 = 1L;
    /**
     * 攻击冷却
     */
    public Long ak1 = 1L;
    /**
     * 选择攻击冷却
     */
    public Long jak1 = 1L;
    /**
     * 使用 物品 标志
     */
    public String usinged = "null";
    /**
     * 名字QQ
     */
    @TableId
    public String name = null;
    /**
     * 下次进入星斗 R
     */
    public Integer nextR1 = -1;
    /**
     * 下次进入 极北
     */
    public Integer nextR2 = -1;
    /**
     * 下次进 落日
     */
    public Integer nextR3 = -1;
    /**
     * 封号名字
     */
    public String Sname = "";
    /**
     * 标记
     */
    public String myTag = "";
    /**
     * 死过
     */
    public boolean died = false;
    /**
     * 降级
     */
    public boolean downed = false;
    /**
     * 下次血量为空生效时
     */
    public Long dt1 = 0L;
    /**
     * 斗魂中...
     */
    private Boolean temp = false;
    /**
     * 勋章数
     */
    private Integer winC = 0;

    private boolean bg = false;
    private Long bgk = 0L;

    /**
     * 武魂信息指向
     */
    private Integer p = 1;

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