package Project.skills.s8;

import Project.aSpring.dao.SkillInfo;
import Project.commons.rt.CommonSource;
import Project.skills.SkillTemplate;
import io.github.kloping.mirai0.commons.Skill;
import Project.utils.Tools.Tool;

import java.util.concurrent.CopyOnWriteArrayList;

import static Project.commons.rt.ResourceSet.FinalNormalString.NOT_SELECT_STR;
import static Project.dataBases.GameDataBase.getInfo;

/**
 * @author github.kloping
 */
public class Skill8031 extends SkillTemplate {

    public Skill8031() {
        super(8031);
    }

    @Override
    public Skill create(SkillInfo info, Number who, Number... nums) {
        return new Skill(info, who, new CopyOnWriteArrayList<>(nums), "天使第八魂技") {
            @Override
            public void before() {
                long v = CommonSource.percentTo(info.getAddPercent(), getPersonInfo().getHll());
                getPersonInfo().addHl(v).apply();
                if (nums.length <= 0) {
                    setTips(NOT_SELECT_STR);
                    return;
                }
                long id = nums[0].longValue();
                if (id >= 0) {
                    getInfo(id).addHl(v).apply();
                    setTips("作用于" + Tool.INSTANCE.at(id));
                }
            }
        };
    }
}
