package Entitys.gameEntitys;

import Project.Tools.JSONUtils;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class SkillInfo {

    /**
     * 第几魂技
     */
    private Integer st;
    /**
     * 魂环ID
     */
    private Integer id;
    /**
     * 魂技ID
     */
    private Integer jid;
    /**
     * 获得百分比
     */
    private Integer addPercent;
    /**
     * 消耗百分比
     */
    private Integer usePercent;
    /**
     * 下次可可使用时间
     */
    private Long time;
    /**
     * 一次冷却
     */
    private Long timeL;
    /**
     * 修改冷却
     */
    private Long md_time = 1L;
    /**
     * 拥有者
     */
    private Number qq;
    /**
     * 魂技名称
     */
    private String name;
    /**
     * UUID
     */
    private String UUID;
    /**
     * 状态
     */
    private Integer state=1;

    public SkillInfo() {
    }
}