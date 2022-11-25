package Project.skill.ghost;

import Project.services.detailServices.ac.GameJoinDetailService;
import Project.skill.SkillTemplate;
import io.github.kloping.mirai0.commons.GhostObj;
import io.github.kloping.mirai0.commons.Skill;
import io.github.kloping.mirai0.commons.gameEntitys.SkillInfo;

import java.util.concurrent.CopyOnWriteArrayList;

import static Project.services.detailServices.GameSkillDetailService.getAddP;

/**
 * @author github.kloping
 */
public class Skill1010 extends SkillTemplate {

    public Skill1010() {
        super(1010);
        setName("短暂眩晕");
    }

    @Override
    public String getIntro() {
        return String.format("魂兽普通技能,短暂眩晕,对玩家造成%s秒的眩晕", getAddP(getJid(), getId()));
    }

    @Override
    public Skill create(SkillInfo info, Number who, Number... nums) {
        return new Skill(info, who, new CopyOnWriteArrayList<>(nums), "") {
            @Override
            public void before() {
            }

            @Override
            public void run() {
                super.run();
                long t = getAddP(getJid(), getId());
                GhostObj ghostObj = GameJoinDetailService.getGhostObjFrom(-who.longValue());
                getPersonInfo0().letVertigo(t);
            }
        };
    }
}
