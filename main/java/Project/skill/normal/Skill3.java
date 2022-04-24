package Project.skill.normal;

import Project.skill.SkillTemplate;
import io.github.kloping.mirai0.commons.Skill;
import io.github.kloping.mirai0.commons.SkillIntro;
import io.github.kloping.mirai0.commons.gameEntitys.SkillInfo;
import io.github.kloping.mirai0.unitls.Tools.Tool;

import java.util.concurrent.CopyOnWriteArrayList;

import static Project.services.detailServices.GameSkillDetailService.*;

/**
 * @author github.kloping
 */
public class Skill3 extends SkillTemplate {


    public Skill3() {
        super(3);
    }

    @Override
    public SkillIntro.Type[] getTypes() {
        return WhTypes.T1;
    }

    @Override
    public String getIntro() {
        return String.format("对指定几个人恢复%s%%的魂力", getAddP(getJid(), getId()));
    }

    @Override
    public Skill create(SkillInfo info, Number who, Number... nums) {
        return new Skill(info, who, new CopyOnWriteArrayList<>(nums), "群体加魂力") {

            @Override
            public void before() {
                for (Long q : nearest(3, who.longValue(), nums)) {
                    addHl(who.longValue(), q, info.getAddPercent());
                    setTips("作用于 " + Tool.at(q));
                }
            }
        };
    }
}
