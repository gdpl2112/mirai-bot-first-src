package Project.skills.s8;

import Project.services.detailServices.GameSkillDetailService;
import Project.skills.SkillTemplate;
import io.github.kloping.mirai0.commons.Skill;
import io.github.kloping.mirai0.commons.game.NormalTagPack;
import Project.commons.gameEntitys.SkillInfo;

import java.util.concurrent.CopyOnWriteArrayList;

import static Project.dataBases.skill.SkillDataBase.TAG_LIGHT_ATT;
import static Project.services.detailServices.GameSkillDetailService.getDuration;

/**
 * @author github.kloping
 */
public class Skill8010 extends SkillTemplate {

    public Skill8010() {
        super(8010);
    }

    @Override
    public Skill create(SkillInfo info, Number who, Number... nums) {
        return new Skill(info, who, new CopyOnWriteArrayList<>(nums), "蓝电霸王龙第八魂技") {
            @Override
            public void before() {
                GameSkillDetailService.addTagPack(new NormalTagPack(TAG_LIGHT_ATT, getDuration(getJid()))
                        .setQ(who.longValue()).setValue(info.getAddPercent().longValue()));
            }
        };
    }
}
