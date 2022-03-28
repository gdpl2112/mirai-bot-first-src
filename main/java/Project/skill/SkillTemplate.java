package Project.skill;

import io.github.kloping.mirai0.commons.Skill;
import io.github.kloping.mirai0.commons.SkillIntro;
import io.github.kloping.mirai0.commons.gameEntitys.SkillInfo;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import static Project.services.detailServices.GameSkillDetailService.getCoolTime;
import static Project.services.detailServices.GameSkillDetailService.getUserPercent;
import static io.github.kloping.mirai0.unitls.Tools.Tool.trans;

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
    private SkillIntro.Type[] types;
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
    public abstract String getIntro();

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
        sb.append(String.format("第%s魂技", trans(getSt()))).append("\r\n");
        sb.append("魂技类型:");
        for (SkillIntro.Type type : getTypes()) {
            sb.append(type.getContent()).append(",");
        }
        sb.append("技能\r\n");
        sb.append("冷却时间:").append(getCoolTime(getId(), getJid(), getWh(), getSt())).append("分钟").append("\r\n");
        sb.append("魂力消耗:").append(getUserPercent(getSt(), getJid())).append("%\r\n");
        if (getHasTime() > 0)
            sb.append("持续时间:").append(String.format("%s分钟", ((double) (getHasTime() / 60000f)))).append("\r\n");
        sb.append("内容:").append(getIntro());
        return sb.toString();
    }

}
