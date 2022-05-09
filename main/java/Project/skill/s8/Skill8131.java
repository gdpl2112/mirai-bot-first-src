package Project.skill.s8;

import Project.skill.SkillTemplate;
import io.github.kloping.mirai0.commons.Skill;
import io.github.kloping.mirai0.commons.SkillIntro;
import io.github.kloping.mirai0.commons.game.NormalTagPack;
import io.github.kloping.mirai0.commons.gameEntitys.SkillInfo;

import java.util.concurrent.CopyOnWriteArrayList;

import static Project.dataBases.skill.SkillDataBase.TAG_DAMAGE_REDUCTION;
import static Project.services.detailServices.GameSkillDetailService.*;

/**
 * @author github.kloping
 */
public class Skill8131 extends SkillTemplate {

    public Skill8131() {
        super(8131);
    }

    @Override
    public SkillIntro.Type[] getTypes() {
        return new SkillIntro.Type[]{SkillIntro.Type.Att, SkillIntro.Type.ToOne};
    }

    @Override
    public String getIntro() {
        return String.format("奇茸通天菊第八魂技,金刚不坏之身,立刻增加%s%%的免伤", getAddP(getJid(), getId()));
    }

    @Override
    public Skill create(SkillInfo info, Number who, Number... nums) {
        return new Skill(info, who, new CopyOnWriteArrayList<>(nums), "奇茸通天菊第八魂技") {
            @Override
            public void before() {

            }

            @Override
            public void run() {
                super.run();
                if (nums.length == 0) return;
                long qid = nums[0].longValue();
                StringBuilder sb = new StringBuilder();
                addTagPack(new NormalTagPack(TAG_DAMAGE_REDUCTION, getDuration(getJid())).setQ(who.longValue())
                        .setValue(Long.valueOf(info.getAddPercent())).setEffected(false));
            }

        };
    }
}
