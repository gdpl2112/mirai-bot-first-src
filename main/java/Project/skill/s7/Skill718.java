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
import static Project.dataBases.skill.SkillDataBase.*;
import static Project.services.detailServices.GameSkillDetailService.*;

/**
 * @author github.kloping
 */
public class Skill718 extends SkillTemplate {


    public Skill718() {
        super(718);
    }

    public String getIntro() {
        return VelocityUtils.getTemplateToString(String.format("skill/%s.intro", getJid()),
                getAddP(getJid(), getId()), getAddP(getJid(), getId()) / 8);
    }

    @Override
    public Skill create(SkillInfo info, Number who, Number... nums) {
        return new Skill(info, who, new CopyOnWriteArrayList<>(nums), "蛇杖真身") {
            @Override
            public void before() {
                Long q = who.longValue();
                PersonInfo pInfo = getInfo(q);
                Long lon = pInfo.att();
                int p = info.getAddPercent();
                long v = CommonSource.percentTo(p, lon);
                long o = CommonSource.percentTo(p, v);
                addShield(who.longValue(), o);
                addAttHasTime(who.longValue(), new HasTimeAdder(System.currentTimeMillis() + getDuration(getJid()), who.longValue(), v, getJid()));
                putPerson(pInfo.addTag(TAG_XX, info.getAddPercent() / 8, getDuration(getJid())));
            }
        };
    }
}
