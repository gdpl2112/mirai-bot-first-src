package Project.skills;

import Project.commons.gameEntitys.SkillInfo;
import Project.utils.VelocityUtils;
import io.github.kloping.mirai0.commons.Skill;
import io.github.kloping.mirai0.unitls.Tools.Tool;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import static Project.services.detailServices.GameSkillDetailService.*;

/**
 * @author github.kloping
 */
@Accessors(chain = true)
@Getter
@Setter
public abstract class SkillTemplate {
    private final Integer jid;

    private Integer id;
    private Integer st;
    private String name;
    private Long hasTime;
    private Integer wh;

    public SkillTemplate(Integer jid) {
        this.jid = jid;
    }

    /**
     * 获取技能介绍
     *
     * @return
     */
    public String getIntro() {
        return VelocityUtils.getTemplateToString(String.format("skill/%s.intro", getJid()), getAddP(getJid(), getId()));
    }

    /**
     * create a skill
     *
     * @param info The Info
     * @param who  The user
     * @param nums Passive user
     * @return
     */
    public abstract Skill create(SkillInfo info, Number who, Number... nums);

    public String getContent() {
        StringBuilder sb = new StringBuilder();
        if (getSt() > 0)
            sb.append(String.format("第%s魂技", Tool.INSTANCE.trans(getSt()))).append("\r\n");
        sb.append("冷却时间:").append(getCoolTime(getId(), getJid(), getWh(), getSt())).append("分钟").append("\r\n");
        sb.append("魂力消耗:").append(getUserPercent(getSt(), getJid())).append("%\r\n");
        if (getHasTime() > 0)
            sb.append("持续时间:").append(String.format("%s分钟", ((double) (getHasTime() / 60000f)))).append("\r\n");
        sb.append("内容:").append(getIntro());
        return sb.toString();
    }

    public Integer getJid() {
        return jid;
    }

    public Integer getId() {
        return id;
    }

    public Integer getSt() {
        return st;
    }

    public String getName() {
        return name;
    }

    public Long getHasTime() {
        return hasTime;
    }

    public Integer getWh() {
        return wh;
    }
}
