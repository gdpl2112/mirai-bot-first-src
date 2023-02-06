package Project.skill.s8;

import Project.skill.SkillTemplate;
import io.github.kloping.mirai0.commons.Skill;
import io.github.kloping.mirai0.commons.game.NormalTagPack;
import io.github.kloping.mirai0.commons.gameEntitys.SkillInfo;

import java.util.concurrent.CopyOnWriteArrayList;

import static Project.dataBases.skill.SkillDataBase.TAG_DAMAGE_REDUCTION;
import static Project.services.detailServices.GameSkillDetailService.addTagPack;
import static Project.services.detailServices.GameSkillDetailService.getDuration;

/**
 * @author github.kloping
 */
public class Skill8131 extends SkillTemplate {

    public Skill8131() {
        super(8131);
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
                addTagPack(new NormalTagPack(TAG_DAMAGE_REDUCTION, getDuration(getJid())).setQ(who.longValue())
                        .setValue(Long.valueOf(info.getAddPercent())));
            }

        };
    }
}
