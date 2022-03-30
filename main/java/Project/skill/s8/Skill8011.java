package Project.skill.s8;

import Project.services.detailServices.GameSkillDetailService;
import Project.skill.SkillTemplate;
import io.github.kloping.mirai0.commons.Skill;
import io.github.kloping.mirai0.commons.SkillIntro;
import io.github.kloping.mirai0.commons.game.NormalTagPack;
import io.github.kloping.mirai0.commons.gameEntitys.SkillInfo;

import java.util.concurrent.CopyOnWriteArrayList;

import static Project.dataBases.skill.SkillDataBase.TAG_LIGHT_F;
import static Project.services.detailServices.GameSkillDetailService.getAddP;
import static Project.services.detailServices.GameSkillDetailService.getDuration;

/**
 * @author github.kloping
 */
public class Skill8011 extends SkillTemplate {

    public Skill8011() {
        super(8011);
    }

    @Override
    public SkillIntro.Type[] getTypes() {
        return new SkillIntro.Type[]{SkillIntro.Type.Att, SkillIntro.Type.HasTime, SkillIntro.Type.ToNum};
    }

    @Override
    public String getIntro() {
        return String.format("使自身带电,效果时间内,被攻击时攻击者受到两次雷电伤害,值为%s%%的攻击的值", getAddP(getJid(), getId()));
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
