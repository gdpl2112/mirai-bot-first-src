package Project.skill.s8;

import Project.services.detailServices.GameSkillDetailService;
import Project.skill.SkillTemplate;
import io.github.kloping.mirai0.commons.Skill;
import io.github.kloping.mirai0.commons.SkillIntro;
import io.github.kloping.mirai0.commons.game.DamageReductionPack;
import io.github.kloping.mirai0.commons.gameEntitys.SkillInfo;
import io.github.kloping.mirai0.commons.resouce_and_tool.CommonSource;

import java.util.concurrent.CopyOnWriteArrayList;

import static Project.dataBases.GameDataBase.getInfo;
import static Project.services.detailServices.GameSkillDetailService.addShield;
import static Project.services.detailServices.GameSkillDetailService.getAddP;

/**
 * @author github.kloping
 */
public class Skill8120 extends SkillTemplate {

    public Skill8120() {
        super(8120);
    }

    @Override
    public SkillIntro.Type[] getTypes() {
        return new SkillIntro.Type[]{SkillIntro.Type.Att, SkillIntro.Type.ToOne};
    }

    @Override
    public String getIntro() {
        return String.format("大力金刚熊第八魂技,增加%s%%的免伤,并增加%s%%最大生命值得护盾",
                getAddP(getJid(), getId()), getAddP(getJid(), getId()) * F0
        );
    }

    public static final int F0 = 3;

    @Override
    public Skill create(SkillInfo info, Number who, Number... nums) {
        return new Skill(info, who, new CopyOnWriteArrayList<>(nums), "大力金刚熊第八魂技") {
            @Override
            public void before() {
                DamageReductionPack pack = new DamageReductionPack();
                pack.setQ(who.longValue());
                pack.setValue(info.getAddPercent().longValue());
                pack.setEffected(false);
                GameSkillDetailService.addTagPack(pack);
                long v = getInfo(who).getHp();
                int b = info.getAddPercent() * F0;
                long v2 = CommonSource.percentTo(b, v);
                addShield(who.longValue(), v2);
            }

            @Override
            public void run() {
                super.run();
            }
        };
    }
}
