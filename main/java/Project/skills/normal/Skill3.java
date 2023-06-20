package Project.skills.normal;

import Project.commons.gameEntitys.SkillInfo;
import Project.skills.SkillTemplate;
import io.github.kloping.mirai0.commons.Skill;
import Project.utils.Tools.Tool;

import java.util.concurrent.CopyOnWriteArrayList;

import static Project.services.detailServices.GameSkillDetailService.addHl;
import static Project.services.detailServices.GameSkillDetailService.nearest;

/**
 * @author github.kloping
 */
public class Skill3 extends SkillTemplate {


    public Skill3() {
        super(3);
    }

    @Override
    public Skill create(SkillInfo info, Number who, Number... nums) {
        return new Skill(info, who, new CopyOnWriteArrayList<>(nums), "群体加魂力") {

            @Override
            public void before() {
                for (Long q : nearest(3, who.longValue(), nums)) {
                    addHl(who.longValue(), q, info.getAddPercent());
                    setTips("作用于 " + Tool.INSTANCE.at(q));
                }
            }
        };
    }
}
