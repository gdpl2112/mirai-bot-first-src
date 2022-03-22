package Project.skill.normal;

import Project.skill.SkillTemplate;
import io.github.kloping.mirai0.commons.Skill;
import io.github.kloping.mirai0.commons.SkillIntro;
import io.github.kloping.mirai0.commons.gameEntitys.SkillInfo;

import java.util.concurrent.CopyOnWriteArrayList;

import static Project.services.detailServices.GameSkillDetailService.getAddP;
import static Project.services.detailServices.GameSkillDetailService.getArgFromSkillArgs;

/**
 * @author github.kloping
 */
public class Skill22 extends SkillTemplate {
    public Skill22() {
        super(21);
    }

    @Override
    public SkillIntro.Type[] getTypes() {
        return new SkillIntro.Type[]{SkillIntro.Type.Special, SkillIntro.Type.Add};
    }

    @Override
    public String getIntro() {
        return String.format("令自身向前移动指定距离,最大5格", getAddP(getJid(), getId()));
    }

    @Override
    public Skill create(SkillInfo info, Number who, Number... nums) {
        return new Skill(info, who, new CopyOnWriteArrayList<>(nums), "位移") {
            @Override
            public void before() {

            }

            @Override
            public void run() {
                Integer i0 = getArgFromSkillArgs(this, Integer.class, 1);
                i0 = i0 > 5 ? 5 : i0;

            }
        };
    }
}
