package Project.skill.s8;

import Project.services.detailServices.GameSkillDetailService;
import Project.skill.SkillTemplate;
import io.github.kloping.mirai0.commons.Skill;
import io.github.kloping.mirai0.commons.SkillIntro;
import io.github.kloping.mirai0.commons.game.NormalTagPack;
import io.github.kloping.mirai0.commons.gameEntitys.SkillInfo;

import java.util.concurrent.CopyOnWriteArrayList;

import static Project.dataBases.skill.SkillDataBase.TAG_LIGHT_ATT;
import static Project.services.detailServices.GameSkillDetailService.getAddP;
import static Project.services.detailServices.GameSkillDetailService.getDuration;

/**
 * @author github.kloping
 */
public class Skill8010 extends SkillTemplate {

    public Skill8010() {
        super(8010);
    }

    @Override
    public SkillIntro.Type[] getTypes() {
        return new SkillIntro.Type[]{SkillIntro.Type.Att, SkillIntro.Type.HasTime, SkillIntro.Type.ToNum};
    }

    @Override
    public String getIntro() {
        return String.format("使用后,所有攻击都会附带雷电, 造成2次额外的%s%%的攻击的值", getAddP(getJid(), getId()));
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
