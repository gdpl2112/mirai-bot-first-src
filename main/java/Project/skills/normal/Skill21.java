package Project.skills.normal;

import Project.skills.SkillTemplate;
import io.github.kloping.mirai0.commons.Skill;
import io.github.kloping.mirai0.commons.gameEntitys.SkillInfo;

import java.util.concurrent.CopyOnWriteArrayList;

import static Project.dataBases.skill.SkillDataBase.TAG_DAMAGE_REDUCTION;
import static Project.services.detailServices.GameSkillDetailService.getDuration;

/**
 * @author github.kloping
 */
public class Skill21 extends SkillTemplate {
    public Skill21() {
        super(21);
    }




    @Override
    public Skill create(SkillInfo info, Number who, Number... nums) {
        return new Skill(info, who, new CopyOnWriteArrayList<>(nums), "免伤") {
            @Override
            public void before() {

            }

            @Override
            public void run() {
                getPersonInfo().addTag(TAG_DAMAGE_REDUCTION, info.getAddPercent().longValue(), 90L, getDuration(getJid()));
            }
        };
    }

}
