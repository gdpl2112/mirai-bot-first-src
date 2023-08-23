package Project.skills.s9;

import Project.aSpring.dao.SkillInfo;
import Project.skills.SkillTemplate;
import io.github.kloping.mirai0.commons.Skill;

import java.util.concurrent.CopyOnWriteArrayList;

import static Project.services.detailServices.GameSkillDetailService.getAddP;

/**
 * @author github.kloping
 */
public class Skill9010 extends SkillTemplate {

    public Skill9010() {
        super(9010);
    }

    @Override
    public String getIntro() {
        return String.format("使用后,对指定敌人造成[雷]元素,造成%s的攻击加成", getAddP(getJid(), getId()));
    }

    @Override
    public Skill create(SkillInfo info, Number who, Number... nums) {
        return new Skill(info, who, new CopyOnWriteArrayList<>(nums), "蓝电霸王龙第九魂技") {
            @Override
            public void before() {

            }
        };
    }
}
