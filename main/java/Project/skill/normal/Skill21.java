package Project.skill.normal;

import Project.services.detailServices.GameSkillDetailService;
import Project.skill.SkillTemplate;
import io.github.kloping.mirai0.commons.Skill;
import io.github.kloping.mirai0.commons.game.DamageReductionPack;
import io.github.kloping.mirai0.commons.gameEntitys.SkillInfo;

import java.util.concurrent.CopyOnWriteArrayList;

import static Project.services.detailServices.GameSkillDetailService.getAddP;

/**
 * @author github.kloping
 */
public class Skill21 extends SkillTemplate {
    public Skill21() {
        super(21);
    }


    @Override
    public String getIntro() {
        return String.format("令自身增加%s%%的免伤", getAddP(getJid(), getId()));
    }

    @Override
    public Skill create(SkillInfo info, Number who, Number... nums) {
        return new Skill(info, who, new CopyOnWriteArrayList<>(nums), "免伤") {
            @Override
            public void before() {

            }

            @Override
            public void run() {
                DamageReductionPack pack = new DamageReductionPack();
                pack.setQ(who.longValue());
                pack.setValue(info.getAddPercent().longValue());
                pack.setEffected(false);
                GameSkillDetailService.addTagPack(pack);
            }
        };
    }

}
