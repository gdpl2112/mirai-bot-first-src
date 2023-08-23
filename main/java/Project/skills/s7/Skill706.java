package Project.skills.s7;

import Project.aSpring.dao.SkillInfo;
import Project.skills.SkillTemplate;
import io.github.kloping.mirai0.commons.Skill;

import java.util.concurrent.CopyOnWriteArrayList;

import static Project.dataBases.GameDataBase.getInfo;
import static Project.dataBases.skill.SkillDataBase.TAG_WD;
import static Project.services.detailServices.GameSkillDetailService.getDuration;

/**
 * @author github.kloping
 */
public class Skill706 extends SkillTemplate {


    public Skill706() {
        super(706);
    }


    @Override
    public Skill create(SkillInfo info, Number who, Number... nums) {
        return new Skill(info, who, new CopyOnWriteArrayList<>(nums), "柔骨兔真身") {
            @Override
            public void before() {
                (getInfo(who).addTag(TAG_WD, 1, getDuration(getJid()))).apply();
            }
        };
    }
}
