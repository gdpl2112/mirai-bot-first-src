package Project.skill.s8;

import Project.skill.SkillTemplate;
import io.github.kloping.mirai0.commons.Skill;
import io.github.kloping.mirai0.commons.gameEntitys.SkillInfo;
import io.github.kloping.mirai0.commons.resouce_and_tool.CommonSource;
import io.github.kloping.mirai0.unitls.Tools.Tool;

import java.util.concurrent.CopyOnWriteArrayList;

import static Project.dataBases.GameDataBase.getInfo;
import static io.github.kloping.mirai0.commons.resouce_and_tool.ResourceSet.FinalNormalString.NOT_SELECT_STR;

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
                    setTips("作用于" + Tool.tool.at(id));
                }
            }
        };
    }
}
