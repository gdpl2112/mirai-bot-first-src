package Project.skills.normal;

import Project.aSpring.dao.SkillInfo;
import Project.skills.SkillTemplate;
import Project.aSpring.dao.PersonInfo;
import io.github.kloping.mirai0.commons.Skill;

import java.util.concurrent.CopyOnWriteArrayList;

import static Project.dataBases.GameDataBase.getInfo;
import static Project.dataBases.skill.SkillDataBase.TAG_REF_ATT;
import static Project.services.detailServices.GameSkillDetailService.*;

/**
 * @author github.kloping
 */
public class Skill26 extends SkillTemplate {

    public Skill26() {
        super(26);
    }


    @Override
    public Skill create(SkillInfo info, Number who, Number... nums) {
        return new Skill(info, who, new CopyOnWriteArrayList<>(nums), "减cd") {
            @Override
            public void before() {
                long t0 = info.getAddPercent();
                for (Long q : nearest(2, who.longValue(), nums)) {
                    PersonInfo pInfo = getInfo(q);
                    pInfo.setJak1(1L).setAk1(1L).addTag(TAG_REF_ATT, getAddP(getJid(), getId()), getDuration(getJid())).apply();
                    setTips("作用于:" + q);
                }
            }
        };
    }
}
