package Project.skill.s8;

import Project.skill.SkillTemplate;
import io.github.kloping.mirai0.commons.Skill;
import io.github.kloping.mirai0.commons.SkillIntro;
import io.github.kloping.mirai0.commons.gameEntitys.SkillInfo;
import io.github.kloping.mirai0.commons.gameEntitys.base.BaseInfo;

import java.util.concurrent.CopyOnWriteArrayList;

import static Project.services.detailServices.GameDetailServiceUtils.attGhostOrMan;
import static Project.services.detailServices.GameDetailServiceUtils.getBaseInfoFromAny;
import static Project.services.detailServices.GameSkillDetailService.getAddP;
import static io.github.kloping.mirai0.commons.resouce_and_tool.ResourceSet.FinalNormalString.NOT_SELECT_STR;
import static io.github.kloping.mirai0.unitls.Tools.Tool.percentTo;
import static io.github.kloping.mirai0.unitls.Tools.Tool.toPercent;

/**
 * @author github.kloping
 */
public class Skill8021 extends SkillTemplate {

    public Skill8021() {
        super(8021);
    }

    @Override
    public SkillIntro.Type[] getTypes() {
        return new SkillIntro.Type[]{SkillIntro.Type.Att, SkillIntro.Type.ToOne};
    }

    @Override
    public String getIntro() {
        return String.format("对指定敌人发动强力攻击造成%s%%的伤害,对血量高于90%%的敌人额外造成8%%的伤害", getAddP(getJid(), getId()));
    }

    @Override
    public Skill create(SkillInfo info, Number who, Number... nums) {
        return new Skill(info, who, new CopyOnWriteArrayList<>(nums), "昊天锤第八魂技") {
            @Override
            public void before() {
                if (nums.length <= 0) {
                    setTips(NOT_SELECT_STR);
                    return;
                }
                StringBuilder sb = new StringBuilder();
                long v = percentTo(info.getAddPercent(), getPersonInfo().att());
                BaseInfo baseInfo = getBaseInfoFromAny(who, nums[0].longValue());
                int b = toPercent(baseInfo.getHp(), baseInfo.getHpL());
                if (b >= 90) {
                    v = percentTo(108, v);
                }
                sb.append("将造成").append(v).append("伤害");
                attGhostOrMan(sb, who, nums[0].longValue(), v);
                setTips(sb.toString());
            }
        };
    }
}
