package Project.skill.ghost;

import Project.skill.SkillTemplate;
import io.github.kloping.mirai0.commons.Skill;
import io.github.kloping.mirai0.commons.gameEntitys.SkillInfo;

import java.util.concurrent.CopyOnWriteArrayList;

import static Project.services.detailServices.GameSkillDetailService.getAddP;

/**
 * @author github.kloping
 */
public class Skill102 extends SkillTemplate {

    public Skill102() {
        super(102);
    }

    @Override
    public String getIntro() {
        return String.format("魂兽普通技能,蓄力重击,蓄力倒计时结束后对玩家造成%s%%的伤害", getAddP(getJid(), getId()));
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
