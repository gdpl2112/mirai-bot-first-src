package Project.skill.s7;

import Project.services.detailServices.GameSkillDetailService;
import Project.skill.SkillTemplate;
import io.github.kloping.mirai0.commons.Skill;
import io.github.kloping.mirai0.commons.gameEntitys.SkillInfo;

import java.util.concurrent.CopyOnWriteArrayList;

import static Project.services.detailServices.GameSkillDetailService.getAddP;
import static io.github.kloping.mirai0.Main.ITools.MemberTools.getRecentSpeechesGid;
import static io.github.kloping.mirai0.commons.resouce_and_tool.CommonSource.percentTo;

/**
 * @author github.kloping
 */
public class Skill719 extends SkillTemplate {


    public Skill719() {
        super(719);
    }


    @Override
    public String getIntro() {
        return String.format("蓝银花,每10秒恢复最大生命值得%s%%的生命值,并对指定敌人每10秒造成攻击的%s%%的伤害"
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
