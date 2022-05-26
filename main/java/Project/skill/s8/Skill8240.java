package Project.skill.s8;

import Project.skill.SkillTemplate;
import io.github.kloping.mirai0.commons.Skill;
import io.github.kloping.mirai0.commons.SkillIntro;
import io.github.kloping.mirai0.commons.gameEntitys.SkillInfo;
import io.github.kloping.mirai0.commons.gameEntitys.base.BaseInfo;
import io.github.kloping.mirai0.commons.resouce_and_tool.CommonSource;

import java.util.concurrent.CopyOnWriteArrayList;

import static Project.services.detailServices.GameDetailServiceUtils.attGhostOrMan;
import static Project.services.detailServices.GameDetailServiceUtils.getBaseInfoFromAny;
import static Project.services.detailServices.GameSkillDetailService.getAddP;
import static io.github.kloping.mirai0.commons.resouce_and_tool.CommonSource.toPercent;

/**
 * @author github.kloping
 */
public class Skill8240 extends SkillTemplate {

    public Skill8240() {
        super(8240);
    }

    @Override
    public SkillIntro.Type[] getTypes() {
        return new SkillIntro.Type[]{SkillIntro.Type.Att, SkillIntro.Type.ToOne};
    }


    public static final int V0 = 3;

    @Override
    public String getIntro() {
        return String.format("修罗神剑第八魂技,发出致命一击,对指定敌人造成%s%%的伤害,若指定敌人生命值低于50%%则额外造成50%%的伤害",
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
                StringBuilder sb = new StringBuilder();
                long v = CommonSource.percentTo(info.getAddPercent(), getPersonInfo().att());
                BaseInfo baseInfo = getBaseInfoFromAny(who, qid);
                int b = toPercent(baseInfo.getHp(), baseInfo.getHpL());
                if (b <= 50) {
                    v = CommonSource.percentTo(150, v);
                }
                sb.append("将造成").append(v).append("伤害");
                attGhostOrMan(sb, who, nums[0].longValue(), v);
                setTips(sb.toString());

            }
        };
    }
}
