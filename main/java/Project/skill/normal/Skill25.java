package Project.skill.normal;

import Project.dataBases.skill.SkillDataBase;
import Project.skill.SkillTemplate;
import io.github.kloping.mirai0.commons.Skill;
import io.github.kloping.mirai0.commons.game.NormalTagPack;
import io.github.kloping.mirai0.commons.gameEntitys.SkillInfo;

import java.util.concurrent.CopyOnWriteArrayList;

import static Project.services.detailServices.GameSkillDetailService.*;

/**
 * @author github.kloping
 */
public class Skill25 extends SkillTemplate {

    public Skill25() {
        super(25);
    }


    @Override
    public String getIntro() {
        return String.format("强化下次选择攻击伤害,伤害为原本的+%s%%", getAddP(getJid(), getId()));
    }

    @Override
    public Skill create(SkillInfo info, Number who, Number... nums) {
        return new Skill(info, who, new CopyOnWriteArrayList<>(nums), "强普") {
            @Override
            public void before() {
                long v = 100;
                v += info.getAddPercent();
                NormalTagPack tagPack = new NormalTagPack(SkillDataBase.TAG_STRENGTHEN_ATT, getDuration(getJid()));
                tagPack.setQ(who.longValue()).setValue(v).setTime(getDuration(getJid()));
                addTagPack(tagPack);
            }
        };
    }
}
