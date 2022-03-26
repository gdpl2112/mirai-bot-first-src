package Project.skill.normal;

import Project.services.detailServices.GameSkillDetailService;
import Project.skill.SkillTemplate;
import io.github.kloping.mirai0.commons.Skill;
import io.github.kloping.mirai0.commons.SkillIntro;
import io.github.kloping.mirai0.commons.gameEntitys.SkillInfo;
import io.github.kloping.mirai0.commons.gameEntitys.TagPack;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.concurrent.CopyOnWriteArrayList;

import static Project.dataBases.GameDataBase.getInfo;
import static Project.dataBases.skill.SkillDataBase.TAG_DAMAGE_REDUCTION;
import static Project.services.detailServices.GameSkillDetailService.getAddP;

/**
 * @author github.kloping
 */
public class Skill21 extends SkillTemplate {
    public Skill21() {
        super(21);
    }

    @Override
    public SkillIntro.Type[] getTypes() {
        return new SkillIntro.Type[]{SkillIntro.Type.Special, SkillIntro.Type.Add};
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

    @Accessors(chain = true)
    @Getter
    @Setter
    public static class DamageReductionPack extends TagPack {

        private Long time = System.currentTimeMillis() + 2 * 60 * 1000L;
        private Long max = 90L;

        public DamageReductionPack() {
            super(TAG_DAMAGE_REDUCTION);
        }

        @Override
        public void effect() {
            setEffected(true);
            getInfo(getQ().longValue()).addTag(getTAG(), getValue(), getMax()).apply();
        }

        @Override
        public boolean over() {
            return System.currentTimeMillis() > getTime().longValue();
        }

        @Override
        public void loseEffect() {
            getInfo(getQ().longValue()).eddTag(getValue(), getTAG()).apply();
        }
    }
}
