package Project.skills.s8;

import Project.aSpring.dao.SkillInfo;
import Project.skills.SkillTemplate;
import io.github.kloping.mirai0.commons.Skill;

import java.util.concurrent.CopyOnWriteArrayList;

import static Project.commons.rt.CommonSource.percentTo;
import static Project.services.detailServices.GameSkillDetailService.addAttSchedule;
import static io.github.kloping.mirai0.Main.iutils.MemberUtils.getRecentSpeechesGid;

/**
 * @author github.kloping
 */
public class Skill8280 extends SkillTemplate {

    public Skill8280() {
        super(8280);
    }


    @Override
    public Skill create(SkillInfo info, Number who, Number... nums) {
        return new Skill(info, who, new CopyOnWriteArrayList<>(nums), "") {

            @Override
            public void before() {

            }

            @Override
            public void run() {
                super.run();
                if (nums.length <= 0) return;
                long qid = nums[0].longValue();
                long v = percentTo(info.getAddPercent(), getPersonInfo().att());
                addAttSchedule(3, qid, who.longValue(), v, 100, getRecentSpeechesGid(who.longValue()), "箭矢造成%s%%的伤害");
            }
        };
    }
}
