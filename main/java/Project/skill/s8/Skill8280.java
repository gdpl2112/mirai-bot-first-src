package Project.skill.s8;

import Project.skill.SkillTemplate;
import io.github.kloping.mirai0.commons.Skill;
import io.github.kloping.mirai0.commons.gameEntitys.SkillInfo;

import java.util.concurrent.CopyOnWriteArrayList;

import static Project.services.detailServices.GameSkillDetailService.addAttSchedule;
import static Project.services.detailServices.GameSkillDetailService.getAddP;
import static io.github.kloping.mirai0.Main.ITools.MemberTools.getRecentSpeechesGid;
import static io.github.kloping.mirai0.commons.resouce_and_tool.CommonSource.percentTo;

/**
 * @author github.kloping
 */
public class Skill8280 extends SkillTemplate {

    public Skill8280() {
        super(8280);
    }


    @Override
    public String getIntro() {
        return String.format("落日神弓第八魂技,连续射出3支箭矢,每支对指定敌人造成%s%%的伤害",
                getAddP(getJid(), getId())
        );
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
