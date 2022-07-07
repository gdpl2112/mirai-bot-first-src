package Project.skill.ghost;

import Project.skill.SkillTemplate;
import io.github.kloping.mirai0.commons.Skill;
import io.github.kloping.mirai0.commons.gameEntitys.SkillInfo;

import java.util.concurrent.CopyOnWriteArrayList;

import static Project.services.detailServices.GameSkillDetailService.getAddP;

/**
 * @author github.kloping
 */
public class Skill103 extends SkillTemplate {

    public Skill103() {
        super(103);
    }

    @Override
    public String getIntro() {
        return String.format("魂兽普通技能,锁定魂技,下一次攻击无法躲避,锁定时间%s", getAddP(getJid(), getId()));
    }

    @Override
    public Skill create(SkillInfo info, Number who, Number... nums) {
        return new Skill(info, who, new CopyOnWriteArrayList<>(nums), " ") {
            @Override
            public void before() {

            }

            @Override
            public void run() {

            }
        };
    }
}
