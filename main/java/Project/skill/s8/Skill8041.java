package Project.skill.s8;

import Project.skill.SkillTemplate;
import io.github.kloping.mirai0.commons.Skill;
import io.github.kloping.mirai0.commons.gameEntitys.SkillInfo;
import io.github.kloping.mirai0.commons.gameEntitys.base.BaseInfo;
import io.github.kloping.mirai0.commons.resouce_and_tool.CommonSource;
import io.github.kloping.mirai0.commons.resouce_and_tool.ResourceSet;

import java.util.concurrent.CopyOnWriteArrayList;

import static Project.services.detailServices.GameDetailServiceUtils.attGhostOrMan;
import static Project.services.detailServices.GameDetailServiceUtils.getBaseInfoFromAny;
import static io.github.kloping.mirai0.commons.resouce_and_tool.CommonSource.toPercent;

/**
 * @author github.kloping
 */
public class Skill8041 extends SkillTemplate {

    public Skill8041() {
        super(8041);
    }

    @Override
    public Skill create(SkillInfo info, Number who, Number... nums) {
        return new Skill(info, who, new CopyOnWriteArrayList<>(nums), "噬魂蛛皇第八魂技") {
            @Override
            public void before() {
                if (nums.length <= 0) {
                    setTips(ResourceSet.FinalNormalString.NOT_SELECT_STR);
                    return;
                }
                long id = nums[0].longValue();
                long v = CommonSource.percentTo(info.getAddPercent(), getPersonInfo().att());
                BaseInfo baseInfo = getBaseInfoFromAny(who, id);
                int b = 100;
                int b0 = toPercent(baseInfo.getHj(), baseInfo.getHjL());
                if (b0 < 70) {
                    b += 10;
                }
                if (baseInfo.getHj() < getPersonInfo().getHj()) {
                    b += 6;
                }
                v = CommonSource.percentTo(b, v);
                StringBuilder sb = new StringBuilder();
                attGhostOrMan(sb, who, id, v);
                setTips(sb.toString());
            }
        };
    }
}
