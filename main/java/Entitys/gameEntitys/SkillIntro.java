package Entitys.gameEntitys;

import lombok.Data;
import lombok.experimental.Accessors;

import static Project.services.DetailServices.GameSkillDetailService.*;
import static Project.Tools.Tool.trans;

/**
 * @author github-kloping
 */
@Data
@Accessors(chain = true)
public class SkillIntro {
    private Integer st;
    private Integer id;
    private Integer jid;
    private Type[] types;
    private String name;
    private Long hasTime;
    private Integer wh;

    public String getContent() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("第%s魂技", trans(st))).append("\r\n");
        sb.append("魂技类型:");
        for (Type type : types) {
            sb.append(type.getContent()).append(",");
        }
        sb.append("技能\r\n");
        sb.append("名字:").append(name == null ? "无" : name).append("\r\n");
        sb.append("内容:").append(getIntroContent(id, jid)).append("\r\n");
        sb.append("冷却时间:").append(getCoolTime(id, jid, wh, st)).append("分钟").append("\r\n");
        sb.append("魂力消耗:").append(getUserPercent(st,jid)).append("%\r\n");
        if (hasTime > 0)
            sb.append("持续时间:").append(String.format("%s分钟", ((double) hasTime / 60000f))).append("\r\n");

        return sb.toString();
    }

    public static enum Type {
        Err("未定义"),
        OneTime("一次性"),
        HasTime("持续性"),
        Mark("标记性"),
        Control("控制性"),
        ToOne("单体"),
        ToNum("群体"),
        WHZs("武魂真身"),
        Add("增益型"),
        Edd("减益型"),
        Special("特殊性"),
        Att("攻击型"),
        Shd("护盾型"),
        heT("辅助型"),
        NLonTime("蓄力型"),
        ;
        private String content;

        Type(String content) {
            this.content = content;
        }

        public String getContent() {
            return content;
        }
    }
}