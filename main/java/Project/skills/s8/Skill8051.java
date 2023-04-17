package Project.skills.s8;

import Project.skills.SkillTemplate;
import io.github.kloping.mirai0.commons.Skill;
import io.github.kloping.mirai0.commons.game.NormalTagPack;
import Project.commons.gameEntitys.SkillInfo;
import Project.commons.gameEntitys.base.BaseInfo;
import Project.commons.resouce_and_tool.ResourceSet;

import java.util.concurrent.CopyOnWriteArrayList;

import static Project.dataBases.skill.SkillDataBase.TAG_CANT_USE;
import static Project.services.detailServices.GameDetailServiceUtils.getBaseInfoFromAny;
import static Project.services.detailServices.GameSkillDetailService.addTagPack;

/**
 * @author github.kloping
 */
public class Skill8051 extends SkillTemplate {

    public Skill8051() {
        super(8051);
    }

    @Override
    public Skill create(SkillInfo info, Number who, Number... nums) {
        return new Skill(info, who, new CopyOnWriteArrayList<>(nums), "蓝银皇第八魂技") {
            @Override
            public void before() {
                if (nums.length <= 0) {
                    setTips(ResourceSet.FinalNormalString.NOT_SELECT_STR);
                    return;
                }
                long q = nums[0].longValue();
                BaseInfo b1 = getBaseInfoFromAny(who, who);
                BaseInfo b2 = null;
                b2 = getBaseInfoFromAny(who, q);
                float i = info.getAddPercent();
                if (b1.getHj() > b2.getHj() && b1.getHjL() > b2.getHjL()) {
                    i *= 2;
                } else if (b2.getHj() > b1.getHj()) {
                    i /= 2;
                }
                long t = i > 0 ? (long) i : 1L;
                addTagPack(new NormalTagPack(TAG_CANT_USE, t).setValue(1L).setQ(q));
                setTips("作用于 " + b2.getName() + t + "秒");
            }
        };
    }
}
