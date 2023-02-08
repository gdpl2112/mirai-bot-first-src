package Project.skill.s7;

import Project.e0.VelocityUtils;
import Project.skill.SkillTemplate;
import io.github.kloping.mirai0.commons.PersonInfo;
import io.github.kloping.mirai0.commons.Skill;
import io.github.kloping.mirai0.commons.gameEntitys.SkillInfo;
import io.github.kloping.mirai0.commons.resouce_and_tool.CommonSource;

import java.util.concurrent.CopyOnWriteArrayList;

import static Project.dataBases.GameDataBase.getInfo;
import static Project.dataBases.GameDataBase.putPerson;
import static Project.dataBases.skill.SkillDataBase.HasTimeAdder;
import static Project.dataBases.skill.SkillDataBase.addAttHasTime;
import static Project.services.detailServices.GameSkillDetailService.*;

/**
 * @author github.kloping
 */
public class Skill731 extends SkillTemplate {


    public Skill731() {
        super(731);
    }

    public String getIntro() {
        return VelocityUtils.getTemplateToString(String.format("skill/%s.intro", getJid()), getAddP(getJid(), getId()), getAddP(getJid(), getId()));
    }

    @Override
    public Skill create(SkillInfo info, Number who, Number... nums) {
        return new Skill(info, who, new CopyOnWriteArrayList<>(nums), "暗金恐爪熊") {
            private long v;

            @Override
            public void before() {
                PersonInfo pInfo = getInfo(who);
                v = CommonSource.percentTo(info.getAddPercent(), pInfo.att());
                long v = CommonSource.percentTo(info.getAddPercent(), pInfo.getHpL());
                addAttHasTime(who.longValue(), new HasTimeAdder(System.currentTimeMillis() + getDuration(getJid()), who.longValue(), v, getJid()));
                addShield(who.longValue(), v, getDuration(getJid()));
                putPerson(pInfo);
            }

            @Override
            public void run() {
                super.run();
            }
        };
    }
}
