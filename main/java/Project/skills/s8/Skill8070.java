package Project.skills.s8;

import Project.dataBases.skill.SkillDataBase;
import Project.skills.SkillTemplate;
import io.github.kloping.mirai0.commons.Skill;
import io.github.kloping.mirai0.commons.game.NormalTagPack;
import Project.commons.gameEntitys.SkillInfo;

import java.util.concurrent.CopyOnWriteArrayList;

import static Project.dataBases.skill.SkillDataBase.TAG_DAMAGE_REDUCTION;
import static Project.dataBases.skill.SkillDataBase.addAttHasTime;
import static Project.services.detailServices.GameSkillDetailService.*;
import static Project.commons.resouce_and_tool.CommonSource.percentTo;

/**
 * @author github.kloping
 */
public class Skill8070 extends SkillTemplate {

    public Skill8070() {
        super(8070);
    }

    @Override
    public Skill create(SkillInfo info, Number who, Number... nums) {
        return new Skill(info, who, new CopyOnWriteArrayList<>(nums), "白虎第八魂技") {
            @Override
            public void before() {
                int v = info.getAddPercent();
                long a = getPersonInfo().att();
                addShield(who.longValue(), percentTo(v, a));
                addTagPack(new NormalTagPack(TAG_DAMAGE_REDUCTION, v).setQ(who.longValue()).setValue((long) v));
                addAttHasTime(who.longValue(), new SkillDataBase.HasTimeAdder(System.currentTimeMillis() + getDuration(getJid()), who.longValue(), percentTo(v, a), getJid()));
            }
        };
    }
}
