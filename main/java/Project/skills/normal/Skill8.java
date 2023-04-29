package Project.skills.normal;

import Project.commons.gameEntitys.SkillInfo;
import Project.commons.rt.CommonSource;
import Project.skills.SkillTemplate;
import io.github.kloping.mirai0.commons.Skill;

import java.util.concurrent.CopyOnWriteArrayList;

import static Project.dataBases.GameDataBase.getInfo;
import static Project.services.detailServices.GameDetailServiceUtils.attGhostOrMan;

/**
 * @author github.kloping
 */
public class Skill8 extends SkillTemplate {


    public Skill8() {
        super(8);
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
                long v = CommonSource.percentTo(info.getAddPercent(), getInfo(who).att());
                attGhostOrMan(sb, who, nums[0], v);
                setTips(sb.toString());
            }
        };
    }
}
