package Project.skills.s8;

import Project.services.detailServices.GameSkillDetailService;
import Project.skills.SkillTemplate;
import io.github.kloping.mirai0.commons.Skill;
import io.github.kloping.mirai0.commons.game.NormalTagPack;
import io.github.kloping.mirai0.commons.gameEntitys.SkillInfo;

import java.util.concurrent.CopyOnWriteArrayList;

import static Project.dataBases.skill.SkillDataBase.TAG_LIGHT_F;
import static Project.services.detailServices.GameSkillDetailService.getDuration;

/**
 * @author github.kloping
 */
public class Skill8011 extends SkillTemplate {

    public Skill8011() {
        super(8011);
    }

    @Override
    public Skill create(SkillInfo info, Number who, Number... nums) {
        return new Skill(info, who, new CopyOnWriteArrayList<>(nums), "蓝电霸王龙第八魂技") {
            @Override
            public void before() {
                GameSkillDetailService.addTagPack(new NormalTagPack(TAG_LIGHT_F, getDuration(getJid()))
                        .setQ(who.longValue()).setValue(info.getAddPercent().longValue()));
            }
        };
    }
}
