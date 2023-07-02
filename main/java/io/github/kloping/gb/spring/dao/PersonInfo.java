package io.github.kloping.gb.spring.dao;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author
 * @description person_info
 * @date 2023-07-02
 */
@Data
@Accessors(chain = true)
public class PersonInfo implements Serializable {
    /**
     * 名字QQ
     */
    @TableId
    public String name = null;
    //==============
    /**
     * 金魂币
     */
    public Long gold = 200L;
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
}