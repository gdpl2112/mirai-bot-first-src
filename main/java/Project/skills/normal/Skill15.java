package Project.skills.normal;

import Project.aSpring.dao.SkillInfo;
import Project.skills.SkillTemplate;
import io.github.kloping.mirai0.commons.Skill;
import Project.utils.Tools.Tool;

import java.util.concurrent.CopyOnWriteArrayList;

import static Project.commons.rt.CommonSource.percentTo;
import static Project.dataBases.GameDataBase.getInfo;
import static Project.services.detailServices.GameSkillDetailService.addShield;

/**
 * @author github.kloping
 */
public class Skill15 extends SkillTemplate {


    public Skill15() {
        super(15);
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
                setTips("作用于 " + Tool.INSTANCE.at(who.longValue()));
            }
        };
    }
}
