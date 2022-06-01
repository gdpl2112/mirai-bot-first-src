package Project.skill.s8;

import Project.skill.SkillTemplate;
import io.github.kloping.mirai0.commons.Skill;
import io.github.kloping.mirai0.commons.gameEntitys.SkillInfo;
import io.github.kloping.mirai0.commons.gameEntitys.base.BaseInfo;

import java.util.concurrent.CopyOnWriteArrayList;

import static Project.services.detailServices.GameDetailServiceUtils.getBaseInfoFromAny;
import static Project.services.detailServices.GameSkillDetailService.*;
import static io.github.kloping.mirai0.Main.ITools.MemberTools.getRecentSpeechesGid;
import static io.github.kloping.mirai0.commons.resouce_and_tool.CommonSource.percentTo;

/**
 * @author github.kloping
 */
public class Skill8190 extends SkillTemplate {

    public Skill8190() {
        super(8190);
    }


    @Override
    public String getIntro() {
        return String.format("蓝银花第八魂技,对指定敌人持续 累计造成%s%%的伤害,伤害存在期间,自身持续恢复生命值",
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
                BaseInfo baseInfo = getBaseInfoFromAny(who.longValue(), qid);
                long v = percentTo(info.getAddPercent(), getPersonInfo().att());
                v /= 12;
                addAttSchedule(12, qid, who.longValue(), v, 10000, getRecentSpeechesGid(who.longValue()), null);
                addHFSchedule(12, who.longValue(), v, 10000, getRecentSpeechesGid(who.longValue()));
            }
        };
    }
}
