package Project.skills.s7;

import Project.commons.gameEntitys.SkillInfo;
import Project.commons.rt.CommonSource;
import Project.skills.SkillTemplate;
import Project.utils.VelocityUtils;
import io.github.kloping.mirai0.commons.PersonInfo;
import io.github.kloping.mirai0.commons.Skill;

import java.util.concurrent.CopyOnWriteArrayList;

import static Project.dataBases.GameDataBase.getInfo;
import static Project.dataBases.skill.SkillDataBase.*;
import static Project.services.detailServices.GameSkillDetailService.getAddP;
import static Project.services.detailServices.GameSkillDetailService.getDuration;

/**
 * @author github.kloping
 */
public class Skill724 extends SkillTemplate {


    public Skill724() {
        super(724);
    }

    @Override
    public String getIntro() {
        return VelocityUtils.getTemplateToString(String.format("skill/%s.intro", getJid()),
                getAddP(getJid(), getId()), getAddP(getJid(), getId()) / 3);
    }

    @Override
    public Skill create(SkillInfo info, Number who, Number... nums) {
        return new Skill(info, who, new CopyOnWriteArrayList<>(nums), "修罗神剑") {
            @Override
            public void before() {
                Long q = who.longValue();
                PersonInfo pInfo = getInfo(q);
                Long lon = pInfo.att();
                long v = CommonSource.percentTo(info.getAddPercent(), lon);
                pInfo.addHl(CommonSource.percentTo(info.getAddPercent() / 3, pInfo.getHll()));
                (pInfo.addTag(TAG_TRUE, 1, getDuration(getJid()))).apply();
                addAttHasTime(who.longValue(), new HasTimeAdder(System.currentTimeMillis() + getDuration(getJid()), who.longValue(), v, getJid()));
            }

            @Override
            public void run() {
                super.run();
                try {
                    Thread.sleep(getDuration(getJid()));
                    (getInfo(who).eddTag(TAG_TRUE, 1)).apply();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }
}
