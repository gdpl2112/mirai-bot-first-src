package Project.skill.s8;

import Project.skill.SkillTemplate;
import io.github.kloping.mirai0.commons.PersonInfo;
import io.github.kloping.mirai0.commons.Skill;
import io.github.kloping.mirai0.commons.gameEntitys.SkillInfo;
import io.github.kloping.mirai0.commons.resouce_and_tool.CommonSource;

import java.util.concurrent.CopyOnWriteArrayList;

import static Project.dataBases.GameDataBase.getInfo;
import static Project.services.detailServices.GameDetailServiceUtils.attGhostOrMan;
import static Project.services.detailServices.GameSkillDetailService.getAddP;
import static io.github.kloping.mirai0.commons.resouce_and_tool.CommonSource.toPercent;
import static io.github.kloping.mirai0.commons.resouce_and_tool.ResourceSet.FinalNormalString.NOT_SELECT_STR;

/**
 * @author github.kloping
 */
public class Skill8030 extends SkillTemplate {

    public Skill8030() {
        super(8030);
    }


    @Override
    public String getIntro() {
        return String.format("以炽热的天使圣剑,对敌人造成%s%%的伤害,额外对魂力低于60%%的敌人造成8%%的伤害,对魂兽额外造成15%%的伤害", getAddP(getJid(), getId()));
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
