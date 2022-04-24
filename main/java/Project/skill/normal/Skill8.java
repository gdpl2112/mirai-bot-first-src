package Project.skill.normal;

import Project.skill.SkillTemplate;
import io.github.kloping.mirai0.commons.Skill;
import io.github.kloping.mirai0.commons.SkillIntro;
import io.github.kloping.mirai0.commons.gameEntitys.SkillInfo;

import java.util.concurrent.CopyOnWriteArrayList;

import static Project.dataBases.GameDataBase.getInfo;
import static Project.dataBases.skill.SkillDataBase.percentTo;
import static Project.services.detailServices.GameDetailServiceUtils.attGhostOrMan;
import static Project.services.detailServices.GameSkillDetailService.WhTypes;
import static Project.services.detailServices.GameSkillDetailService.getAddP;

/**
 * @author github.kloping
 */
public class Skill8 extends SkillTemplate {


    public Skill8() {
        super(8);
    }

    @Override
    public SkillIntro.Type[] getTypes() {
        return WhTypes.T8;
    }

    @Override
    public String getIntro() {
        return String.format("对指定敌人造成 攻击%s%%的伤害", getAddP(getJid(), getId()));
    }

    @Override
    public Skill create(SkillInfo info, Number who, Number... nums) {
        return new Skill(info, who, new CopyOnWriteArrayList<>(nums), "单体攻击") {
            @Override
            public void before() {
                if (nums.length == 0) {
                    return;
                }
                StringBuilder sb = new StringBuilder();
                long v = percentTo(info.getAddPercent(), getInfo(who).att());
                attGhostOrMan(sb, who, nums[0], v);
                setTips(sb.toString());
            }
        };
    }
}
