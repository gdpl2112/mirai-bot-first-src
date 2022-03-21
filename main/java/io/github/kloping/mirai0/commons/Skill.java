package io.github.kloping.mirai0.commons;


import io.github.kloping.mirai0.commons.Group;
import io.github.kloping.mirai0.Main.ITools.MessageTools;
import io.github.kloping.mirai0.commons.gameEntitys.SkillInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * @author github-kloping
 */
public abstract class Skill implements Runnable {
    private SkillInfo info = null;
    private Number qq = 0;
    public List<Number> numbers = new ArrayList<>();
    private String name = null;
    private String tips = "";
    private Group group = null;

    public Skill() {
    }

    public Skill(SkillInfo info, Number qq, List<Number> numbers, String name) {
        this.info = info;
        this.qq = qq;
        this.numbers = numbers;
        this.name = name;
    }

    /**
     * run before
     */
    public abstract void before();

    public void setGroup(Group group) {
        this.group = group;
    }

    public Group getGroup() {
        return group;
    }

    @Override
    public void run() {
        before();
    }

    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = (tips == null || tips.trim().isEmpty()) ? tips : this.tips + "\n" + tips;
        if (group != null) {
            MessageTools.sendMessageInGroupWithAt(tips, group.getId(), qq.longValue());
        }
    }
}