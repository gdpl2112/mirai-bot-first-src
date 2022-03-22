package io.github.kloping.mirai0.commons;


import io.github.kloping.mirai0.Main.ITools.MessageTools;
import io.github.kloping.mirai0.commons.gameEntitys.SkillInfo;

import java.util.ArrayList;
import java.util.List;

import static Project.dataBases.GameDataBase.getInfo;

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
    private Object[] args = null;

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object... args) {
        this.args = args;
    }

    private PersonInfo personInfo = null;

    public PersonInfo getPersonInfo() {
        return personInfo == null ? personInfo = getInfo(qq.longValue()) : personInfo;
    }

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