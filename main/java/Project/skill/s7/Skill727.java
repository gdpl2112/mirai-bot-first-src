package Project.skill.s7;

import Project.skill.SkillTemplate;
import io.github.kloping.mirai0.commons.PersonInfo;
import io.github.kloping.mirai0.commons.Skill;
import io.github.kloping.mirai0.commons.SkillIntro;
import io.github.kloping.mirai0.commons.gameEntitys.SkillInfo;

import java.util.concurrent.CopyOnWriteArrayList;

import static Project.dataBases.GameDataBase.getInfo;
import static Project.dataBases.GameDataBase.putPerson;
import static Project.dataBases.skill.SkillDataBase.*;
import static Project.services.detailServices.GameSkillDetailService.WhTypes;
import static Project.services.detailServices.GameSkillDetailService.getAddP;

/**
 * @author github.kloping
 */
public class Skill727 extends SkillTemplate {


    public Skill727() {
        super(727);
    }

    @Override
    public SkillIntro.Type[] getTypes() {
        return WhTypes.T72;
    }

    @Override
    public String getIntro() {
        return String.format("渺小的农具,武魂真身,增加%s点攻击", getAddP(getJid(), getId()));
    }

    @Override
    public Skill create(SkillInfo info, Number who, Number... nums) {
        return new Skill(info, who, new CopyOnWriteArrayList<>(nums), "锄头真身") {
            @Override
            public void before() {
                Long q = who.longValue();
                PersonInfo info_ = getInfo(q);
                Long lon = info_.getAtt();
                long v = percentTo(info.getAddPercent(), lon);
                putPerson(info_);
                addAttHasTime(who.longValue(), new HasTimeAdder(System.currentTimeMillis() + t727, who.longValue(), v));
            }
        };
    }
}
