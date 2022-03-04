package io.github.kloping.mirai0.Entitys.gameEntitys;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import static Project.aSpring.SpringBootResource.getZonMapper;

/**
 * @author github-kloping
 */
@Accessors(chain = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Zong implements Serializable {

    /**
     * 宗门名称
     */
    private String name;

    /**
     * 宗门Id
     */
    @TableId
    private Integer id;

    /**
     * 宗门状态
     */
    private Integer state;

    /**
     * 总嗯宗主
     */
    private Long main;

    /**
     * 宗门人数
     */
    private Integer members;

    /**
     * 长老数
     */
    private Integer elders;

    /**
     * 成员列表
     */
    @TableField(exist = false)
    private Set<Number> member;

    /**
     * 长老列表
     */
    @TableField(exist = false)
    private Set<Number> elder;

    /**
     * 宗门经验
     */
    private Long xp = 0L;

    /**
     * 宗门需要的升级经验
     */
    private Long xpMax = 300L;

    /**
     * 宗门等级
     */
    private Integer Level = 1;

    /**
     * 宗门图标
     */
    private String icon = "";

    /**
     * 宗门长老最大数
     */
    private Integer elderNum = 2;

    /**
     * 宗门最大人数
     */
    private Integer maxP = 8;

    /**
     * 宗门金币
     */
    private Long gold = 200L;

    /**
     * 宗门修改信息时间
     */
    private Long mk = 1L;

    /**
     * 共用救援次数
     */
    private Integer pub = 0;

    public Integer getMembers() {
        return getMember().size();
    }

    public Integer getElders() {
        return getElder().size();
    }
/*
    public Set<Number> getMember() {
        Set<Number> set = new HashSet<>();
        for (Zon zon : getZonMapper().selectByZongId(getId().intValue())) {
            set.add(zon.getQq());
        }
        return set;
    }

    public Set<Number> getElder() {
        Set<Number> set = new HashSet<>();
        for (Zon zon : getZonMapper().selectEldersByZongId(getId().intValue())) {
            set.add(zon.getQq());
        }
        return set;
    }*/
}
