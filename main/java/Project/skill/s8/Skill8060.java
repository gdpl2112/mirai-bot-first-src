

package Project.skill.s8;

import Project.services.detailServices.GameBoneDetailService;
import Project.skill.SkillTemplate;
import io.github.kloping.mirai0.commons.Skill;
import io.github.kloping.mirai0.commons.SkillIntro;
import io.github.kloping.mirai0.commons.gameEntitys.SkillInfo;

import java.util.concurrent.CopyOnWriteArrayList;

import static Project.services.detailServices.GameSkillDetailService.getAddP;

/**
 * @author github.kloping
 */
public class Skill8060 extends SkillTemplate {

    public Skill8060() {
        super(8060);
    }

    @Override
    public SkillIntro.Type[] getTypes() {
        return new SkillIntro.Type[]{SkillIntro.Type.Att, SkillIntro.Type.ToOne};
    }

    @Override
    public String getIntro() {
        return String.format("柔骨兔第八魂技,增加%s%%的闪避率", getAddP(getJid(), getId()));
    }

    @Override
    public Skill create(SkillInfo info, Number who, Number... nums) {
        return new Skill(info, who, new CopyOnWriteArrayList<>(nums), "柔骨兔第八魂技") {
            @Override
            public void before() {
            }

            @Override
            public void run() {
                GameBoneDetailService.addForAttr(who.longValue(), info.getAddPercent(), GameBoneDetailService.Type.HIDE_PRO);
            }
        };
    }
}
