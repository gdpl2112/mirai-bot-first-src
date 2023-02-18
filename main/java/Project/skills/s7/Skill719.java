package Project.skills.s7;

import Project.utils.VelocityUtils;
import Project.services.detailServices.GameSkillDetailService;
import Project.skills.SkillTemplate;
import io.github.kloping.mirai0.commons.Skill;
import io.github.kloping.mirai0.commons.gameEntitys.SkillInfo;

import java.util.concurrent.CopyOnWriteArrayList;

import static Project.services.detailServices.GameSkillDetailService.getAddP;
import static io.github.kloping.mirai0.Main.iutils.MemberUtils.getRecentSpeechesGid;
import static io.github.kloping.mirai0.commons.resouce_and_tool.CommonSource.percentTo;

/**
 * @author github.kloping
 */
public class Skill719 extends SkillTemplate {


    public Skill719() {
        super(719);
    }

    public String getIntro() {
        return VelocityUtils.getTemplateToString(String.format("skill/%s.intro", getJid())
                , getAddP(getJid(), getId())
                , getAddP(getJid(), getId())
        );
    }

    @Override
    public Skill create(SkillInfo info, Number who, Number... nums) {
        return new Skill(info, who, new CopyOnWriteArrayList<>(nums), "蓝银花真身") {

            @Override
            public void before() {
            }

            @Override
            public void run() {
                super.run();
                long v = percentTo(info.getAddPercent(), getPersonInfo().getHpL());
                GameSkillDetailService.addHFSchedule(12, who.longValue(), v, 10000, getRecentSpeechesGid(who.longValue()));
                if (nums.length <= 0) return;
                long qid = nums[0].longValue();
                long v0 = percentTo(info.getAddPercent(), getPersonInfo().att());
                GameSkillDetailService.addAttSchedule(12, qid, who.longValue(), v0, 10000, getRecentSpeechesGid(who.longValue()), "蓝银花%s的伤害");
            }
        };
    }
}
