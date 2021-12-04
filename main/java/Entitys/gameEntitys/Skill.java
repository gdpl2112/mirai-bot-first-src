package Entitys.gameEntitys;


import Entitys.Group;
import io.github.kloping.Mirai.Main.ITools.MessageTools;

import java.util.ArrayList;
import java.util.List;

public abstract class Skill implements Runnable {
    private SkillInfo info = null;
    private Number qq = 0;
    public List<Number> numbers = new ArrayList<>();
    private String name = null;
    private String tips = null;
    private Group group = null;

    public Skill() {
    }

    public Skill(SkillInfo info, Number qq, List<Number> numbers, String name) {
        this.info = info;
        this.qq = qq;
        this.numbers = numbers;
        this.name = name;
        before();
    }

    public abstract void before();

    public void setGroup(Group group) {
        this.group = group;
    }

    public Group getGroup() {
        return group;
    }

    @Override
    public void run() {
    }

    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips += "\n" + tips;
        if (group != null) {
            MessageTools.sendMessageInGroupWithAt(tips, group.getId(), qq.longValue());
        }
    }
}