package Project.skill.s8;

import Project.skill.SkillTemplate;
import io.github.kloping.mirai0.commons.Skill;
import io.github.kloping.mirai0.commons.SkillIntro;
import io.github.kloping.mirai0.commons.gameEntitys.SkillInfo;

import java.util.concurrent.CopyOnWriteArrayList;

import static Project.services.detailServices.GameSkillDetailService.addHFSchedule;
import static Project.services.detailServices.GameSkillDetailService.getAddP;
import static io.github.kloping.mirai0.Main.ITools.MemberTools.getRecentSpeechesGid;

/**
 * @author github.kloping
 */
public class Skill8050 extends SkillTemplate {

    public Skill8050() {
        super(8050);
        setHasTime(60000L);
    }

    @Override
    public SkillIntro.Type[] getTypes() {
        return new SkillIntro.Type[]{SkillIntro.Type.Control, SkillIntro.Type.ToOne};
    }

    @Override
    public String getIntro() {
        return String.format("蓝银皇,令自己每秒回复%s%%的生命", getAddP(getJid(), getId()));
    }

    @Override
    public Skill create(SkillInfo info, Number who, Number... nums) {
        return new Skill(info, who, new CopyOnWriteArrayList<>(nums), "蓝银皇第八魂技") {
            @Override
            public void before() {
                addHFSchedule(60, who.longValue(), info.getAddPercent(), 1000L, getRecentSpeechesGid(who.longValue()));
            }
        };
    }
}
