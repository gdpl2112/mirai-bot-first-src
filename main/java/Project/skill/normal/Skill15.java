package Project.skill.normal;

import Project.skill.SkillTemplate;
import io.github.kloping.mirai0.commons.Skill;
import io.github.kloping.mirai0.commons.SkillIntro;
import io.github.kloping.mirai0.commons.gameEntitys.SkillInfo;
import io.github.kloping.mirai0.unitls.Tools.Tool;

import java.util.concurrent.CopyOnWriteArrayList;

import static Project.dataBases.GameDataBase.getInfo;
import static Project.dataBases.skill.SkillDataBase.percentTo;
import static Project.services.detailServices.GameSkillDetailService.addShield;
import static Project.services.detailServices.GameSkillDetailService.getAddP;

/**
 * @author github.kloping
 */
public class Skill15 extends SkillTemplate {


    public Skill15() {
        super(15);
    }

    @Override
    public SkillIntro.Type[] getTypes() {
        return new SkillIntro.Type[]{SkillIntro.Type.Mark, SkillIntro.Type.Shd};
    }

    @Override
    public String getIntro() {
        return String.format("为自己增加一个最大生命值的%s%%的长久护盾", getAddP(getJid(), getId()));
    }

    @Override
    public Skill create(SkillInfo info, Number who, Number... nums) {
        return new Skill(info, who, new CopyOnWriteArrayList<>(nums), "长久护盾") {
            @Override
            public void before() {
                long v = getInfo(who).getHp();
                int b = info.getAddPercent();
                long v2 = percentTo(b, v);
                addShield(who.longValue(), v2);
                setTips("作用于 " + Tool.at(who.longValue()));
            }
        };
    }
}
