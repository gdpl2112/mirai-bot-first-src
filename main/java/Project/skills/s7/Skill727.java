package Project.skills.s7;

import Project.skills.SkillTemplate;
import io.github.kloping.mirai0.commons.Skill;
import Project.commons.gameEntitys.SkillInfo;
import io.github.kloping.mirai0.unitls.Tools.Tool;

import java.util.concurrent.CopyOnWriteArrayList;

import static Project.services.detailServices.GameSkillDetailService.*;

/**
 * @author github.kloping
 */
public class Skill727 extends SkillTemplate {


    public Skill727() {
        super(727);
    }




    @Override
    public Skill create(SkillInfo info, Number who, Number... nums) {
        return new Skill(info, who, new CopyOnWriteArrayList<>(nums), "九心海棠真身") {
            @Override
            public void before() {
                for (Long aLong : nearest(2, who.longValue(), nums)) {
                    addHp(who, aLong, info.getAddPercent());
                    setTips("作用于:" + Tool.INSTANCE.at(aLong));
                }
            }
        };
    }
}
