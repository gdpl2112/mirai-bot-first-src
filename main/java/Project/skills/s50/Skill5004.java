package Project.skills.s50;

import Project.commons.gameEntitys.SkillInfo;
import Project.skills.SkillTemplate;
import io.github.kloping.mirai0.commons.Skill;

import java.util.concurrent.CopyOnWriteArrayList;

import static Project.dataBases.GameDataBase.getInfo;
import static Project.dataBases.skill.SkillDataBase.TAG_DAMAGE_REDUCTION;
import static Project.services.detailServices.GameSkillDetailService.getDuration;
import static Project.services.detailServices.GameSkillDetailService.nearest;

/**
 * @author github.kloping
 */
public class Skill5004 extends SkillTemplate {
    public Skill5004() {
        super(5004);
    }

    @Override
    public Skill create(SkillInfo info, Number who, Number... nums) {
        return new Skill(info, who, new CopyOnWriteArrayList<>(nums), "四曰御") {

            Long[] qs;

            @Override
            public void before() {
                qs = nearest(2, who.longValue(), nums);
                for (Long q : qs) {
                    getInfo(q).addTag(TAG_DAMAGE_REDUCTION, info.getAddPercent().longValue(), 90L, getDuration(getJid()));
                }
            }

        };
    }
}
