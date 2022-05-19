package Project.skill.s8;

import Project.services.detailServices.GameSkillDetailService;
import Project.skill.SkillTemplate;
import io.github.kloping.mirai0.commons.Skill;
import io.github.kloping.mirai0.commons.SkillIntro;
import io.github.kloping.mirai0.commons.game.DamageReductionPack;
import io.github.kloping.mirai0.commons.gameEntitys.SkillInfo;

import java.util.concurrent.CopyOnWriteArrayList;

import static Project.services.detailServices.GameSkillDetailService.addShield;
import static Project.services.detailServices.GameSkillDetailService.getAddP;
import static io.github.kloping.mirai0.commons.resouce_and_tool.CommonSource.percentTo;
import static io.github.kloping.mirai0.commons.resouce_and_tool.CommonSource.toPercent;

/**
 * @author github.kloping
 */
public class Skill8200 extends SkillTemplate {

    public Skill8200() {
        super(8200);
    }

    @Override
    public SkillIntro.Type[] getTypes() {
        return new SkillIntro.Type[]{SkillIntro.Type.Att, SkillIntro.Type.ToOne};
    }

    @Override
    public String getIntro() {
        return String.format("玄龟第八魂技,使用后立即获得%s%%的免伤与%s%%的护盾,血量低于30%%时额外获得%s%%免伤",
                getAddP(getJid(), getId()),
                getAddP(getJid(), getId()),
                getAddP(getJid(), getId())
        );
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
                DamageReductionPack pack = new DamageReductionPack();
                pack.setQ(who.longValue());
                long v = info.getAddPercent().longValue();
                if (toPercent(getPersonInfo().getHp(), getPersonInfo().getHpL()) <= 30)
                    v *= 2;
                pack.setValue(v);
                pack.setEffected(false);
                GameSkillDetailService.addTagPack(pack);
                addShield(who.longValue(), percentTo(info.getAddPercent(), getPersonInfo().getHpL()));

            }
        };
    }
}
