package Project.skills.s8;

import Project.aSpring.dao.SkillInfo;
import Project.commons.gameEntitys.base.BaseInfo;
import Project.commons.rt.CommonSource;
import Project.skills.SkillTemplate;
import io.github.kloping.mirai0.commons.Skill;

import java.util.concurrent.CopyOnWriteArrayList;

import static Project.commons.rt.CommonSource.toPercent;
import static Project.commons.rt.ResourceSet.FinalNormalString.NOT_SELECT_STR;
import static Project.services.detailServices.GameDetailServiceUtils.attGhostOrMan;
import static Project.services.detailServices.GameDetailServiceUtils.getBaseInfoFromAny;

/**
 * @author github.kloping
 */
public class Skill8021 extends SkillTemplate {

    public Skill8021() {
        super(8021);
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
                long v = CommonSource.percentTo(info.getAddPercent(), getPersonInfo().att());
                BaseInfo baseInfo = getBaseInfoFromAny(who, nums[0].longValue());
                int b = toPercent(baseInfo.getHp(), baseInfo.getHpL());
                if (b >= 90) {
                    v = CommonSource.percentTo(108, v);
                }
                sb.append("将造成").append(v).append("伤害");
                attGhostOrMan(sb, who, nums[0].longValue(), v);
                setTips(sb.toString());
            }
        };
    }
}
