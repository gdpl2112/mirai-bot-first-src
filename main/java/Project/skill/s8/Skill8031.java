package Project.skill.s8;

import Project.skill.SkillTemplate;
import io.github.kloping.mirai0.commons.Skill;
import io.github.kloping.mirai0.commons.SkillIntro;
import io.github.kloping.mirai0.commons.gameEntitys.SkillInfo;
import io.github.kloping.mirai0.commons.resouce_and_tool.CommonSource;

import java.util.concurrent.CopyOnWriteArrayList;

import static Project.dataBases.GameDataBase.getInfo;
import static Project.services.detailServices.GameSkillDetailService.getAddP;
import static io.github.kloping.mirai0.commons.resouce_and_tool.ResourceSet.FinalNormalString.NOT_SELECT_STR;
import static io.github.kloping.mirai0.unitls.Tools.Tool.at;

/**
 * @author github.kloping
 */
public class Skill8031 extends SkillTemplate {

    public Skill8031() {
        super(8031);
    }

    @Override
    public SkillIntro.Type[] getTypes() {
        return new SkillIntro.Type[]{SkillIntro.Type.Att, SkillIntro.Type.ToOne};
    }


    @Override
    public String getIntro() {
        return String.format("以炽热的天使圣剑,吸收太阳真火在短时间内为自己和指定一人回复%s%%魂力", getAddP(getJid(), getId()));
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
                    setTips("作用于" + at(id));
                }
            }
        };
    }
}
