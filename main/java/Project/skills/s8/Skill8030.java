package Project.skills.s8;

import Project.aSpring.dao.SkillInfo;
import Project.commons.rt.CommonSource;
import Project.skills.SkillTemplate;
import Project.aSpring.dao.PersonInfo;
import io.github.kloping.mirai0.commons.Skill;

import java.util.concurrent.CopyOnWriteArrayList;

import static Project.commons.rt.CommonSource.toPercent;
import static Project.commons.rt.ResourceSet.FinalNormalString.NOT_SELECT_STR;
import static Project.dataBases.GameDataBase.getInfo;
import static Project.services.detailServices.GameDetailServiceUtils.attGhostOrMan;

/**
 * @author github.kloping
 */
public class Skill8030 extends SkillTemplate {

    public Skill8030() {
        super(8030);
    }

    @Override
    public Skill create(SkillInfo info, Number who, Number... nums) {
        return new Skill(info, who, new CopyOnWriteArrayList<>(nums), "天使第八魂技") {
            @Override
            public void before() {
                if (nums.length <= 0) {
                    setTips(NOT_SELECT_STR);
                    return;
                }
                long id = nums[0].longValue();
                StringBuilder sb = new StringBuilder();
                long v = CommonSource.percentTo(info.getAddPercent(), getPersonInfo().att());
                if (id > 0) {
                    PersonInfo p2 = getInfo(id);
                    int b = toPercent(p2.getHl(), p2.getHll());
                    if (b <= 60) {
                        v = CommonSource.percentTo(108, v);
                    }
                } else {
                    v = CommonSource.percentTo(115, v);
                }
                sb.append("将造成").append(v).append("伤害");
                attGhostOrMan(sb, who, id, v);
                setTips(sb.toString());
            }
        };
    }
}
