package Project.skills.s8;

import Project.commons.gameEntitys.SkillInfo;
import Project.services.detailServices.GameBoneDetailService;
import Project.skills.SkillTemplate;
import io.github.kloping.mirai0.commons.Skill;

import java.util.concurrent.CopyOnWriteArrayList;

import static Project.services.detailServices.GameSkillDetailService.getDuration;

/**
 * @author github.kloping
 */
public class Skill8060 extends SkillTemplate {

    public Skill8060() {
        super(8060);
    }

    @Override
    public Skill create(SkillInfo info, Number who, Number... nums) {
        return new Skill(info, who, new CopyOnWriteArrayList<>(nums), "柔骨兔第八魂技") {
            @Override
            public void before() {
            }

            @Override
            public void run() {
                GameBoneDetailService.addForAttr(who.longValue(), info.getAddPercent(), GameBoneDetailService.Type.HIDE_PRO, getDuration(getJid()));
            }
        };
    }
}
