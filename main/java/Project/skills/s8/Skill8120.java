package Project.skills.s8;

import Project.utils.VelocityUtils;
import Project.skills.SkillTemplate;
import io.github.kloping.mirai0.commons.Skill;
import Project.commons.gameEntitys.SkillInfo;
import Project.commons.resouce_and_tool.CommonSource;

import java.util.concurrent.CopyOnWriteArrayList;

import static Project.dataBases.GameDataBase.getInfo;
import static Project.dataBases.skill.SkillDataBase.TAG_DAMAGE_REDUCTION;
import static Project.services.detailServices.GameSkillDetailService.*;

/**
 * @author github.kloping
 */
public class Skill8120 extends SkillTemplate {

    public static final int F0 = 3;

    public Skill8120() {
        super(8120);
    }

    public String getIntro() {
        return VelocityUtils.getTemplateToString(String.format("skill/%s.intro", getJid()),
                getAddP(getJid(), getId()), getAddP(getJid(), getId()) * F0);
    }

    @Override
    public Skill create(SkillInfo info, Number who, Number... nums) {
        return new Skill(info, who, new CopyOnWriteArrayList<>(nums), "大力金刚熊第八魂技") {
            @Override
            public void before() {
                getPersonInfo().addTag(TAG_DAMAGE_REDUCTION, info.getAddPercent().longValue(), 90L, getDuration(getJid()));
                long v = getInfo(who).getHp();
                int b = info.getAddPercent() * F0;
                long v2 = CommonSource.percentTo(b, v);
                addShield(who.longValue(), v2);
            }

            @Override
            public void run() {
                super.run();
            }
        };
    }
}
