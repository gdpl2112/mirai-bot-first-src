package Project.skills.s50;

import Project.commons.gameEntitys.SkillInfo;
import Project.dataBases.skill.SkillDataBase;
import Project.skills.SkillTemplate;
import io.github.kloping.mirai0.commons.PersonInfo;
import io.github.kloping.mirai0.commons.Skill;
import io.github.kloping.number.NumberUtils;

import java.util.concurrent.CopyOnWriteArrayList;

import static Project.dataBases.GameDataBase.getInfo;
import static Project.dataBases.skill.SkillDataBase.addAttHasTime;
import static Project.services.detailServices.GameSkillDetailService.*;

/**
 * @author github.kloping
 */
public class Skill5001 extends SkillTemplate {
    public Skill5001() {
        super(5001);
    }

    @Override
    public Skill create(SkillInfo info, Number who, Number... nums) {
        return new Skill(info, who, new CopyOnWriteArrayList<>(nums), "一曰力") {
            @Override
            public void before() {
                final long v = NumberUtils.percentTo(getAddP(getJid(), getId()).intValue(), getPersonInfo().getAtt());
                for (Long qid : nearest(2, who.longValue(), nums)) {
                    PersonInfo pInfo = getInfo(qid);
                    long v1 = v > pInfo.att() ? pInfo.att() : v;
                    addAttHasTime(qid, new SkillDataBase.HasTimeAdder(
                            System.currentTimeMillis() + getDuration(getJid()), who.longValue(), v, getJid()));
                }
            }
        };
    }
}
