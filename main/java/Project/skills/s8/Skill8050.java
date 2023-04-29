package Project.skills.s8;

import Project.commons.gameEntitys.SkillInfo;
import Project.skills.SkillTemplate;
import io.github.kloping.mirai0.commons.Skill;

import java.util.concurrent.CopyOnWriteArrayList;

import static Project.services.detailServices.GameSkillDetailService.addHFSchedule;
import static io.github.kloping.mirai0.Main.iutils.MemberUtils.getRecentSpeechesGid;

/**
 * @author github.kloping
 */
public class Skill8050 extends SkillTemplate {

    public Skill8050() {
        super(8050);
        setHasTime(60000L);
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
