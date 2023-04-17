package Project.skills.s8;

import Project.skills.SkillTemplate;
import io.github.kloping.mirai0.commons.Skill;
import Project.commons.gameEntitys.SkillInfo;

import java.util.concurrent.CopyOnWriteArrayList;

import static Project.dataBases.skill.SkillDataBase.TAG_DAMAGE_REDUCTION;
import static Project.services.detailServices.GameSkillDetailService.*;
import static Project.commons.resouce_and_tool.CommonSource.percentTo;
import static Project.commons.resouce_and_tool.CommonSource.toPercent;

/**
 * @author github.kloping
 */
public class Skill8200 extends SkillTemplate {

    public Skill8200() {
        super(8200);
    }

    @Override
    public Skill create(SkillInfo info, Number who, Number... nums) {
        return new Skill(info, who, new CopyOnWriteArrayList<>(nums), "") {

            @Override
            public void before() {
            }

            @Override
            public void run() {
                super.run();
                if (nums.length <= 0) return;
                long qid = nums[0].longValue();
                long v = info.getAddPercent().longValue();
                if (toPercent(getPersonInfo().getHp(), getPersonInfo().getHpL()) <= 30) {
                    v *= 2;
                }
                getPersonInfo().addTag(TAG_DAMAGE_REDUCTION, v, 90L, getDuration(getJid()));
                addShield(who.longValue(), percentTo(info.getAddPercent(), getPersonInfo().getHpL()));
            }
        };
    }
}
