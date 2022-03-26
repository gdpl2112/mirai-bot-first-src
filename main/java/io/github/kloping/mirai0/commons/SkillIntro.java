package io.github.kloping.mirai0.commons;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author github-kloping
 */
@Data
@Accessors(chain = true)
public class SkillIntro {

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