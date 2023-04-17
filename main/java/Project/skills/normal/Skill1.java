package Project.skills.normal;

import Project.skills.SkillTemplate;
import io.github.kloping.mirai0.commons.Skill;
import Project.commons.gameEntitys.SkillInfo;
import io.github.kloping.mirai0.unitls.Tools.Tool;

import java.util.concurrent.CopyOnWriteArrayList;

import static Project.services.detailServices.GameSkillDetailService.*;

/**
 * @author github.kloping
 */
public class Skill1 extends SkillTemplate {
    public Skill1() {
        super(1);
    }

    @Override
    public Skill create(SkillInfo info, Number who, Number... nums) {
        return new Skill(info, who, new CopyOnWriteArrayList<>(nums), "群体加血") {

            @Override
            public void before() {
                for (Long q : nearest(3, who.longValue(), nums)) {
                    addHp(who.longValue(), q, info.getAddPercent());
                    setTips("作用于 " +  Tool.INSTANCE.at(q));
                }
            }
        };
    }
}
