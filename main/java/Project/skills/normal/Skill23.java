package Project.skills.normal;

import Project.commons.gameEntitys.SkillInfo;
import Project.controllers.auto.ControllerSource;
import Project.services.player.Growth;
import Project.services.player.PlayerBehavioralManager;
import Project.skills.SkillTemplate;
import Project.utils.VelocityUtils;
import io.github.kloping.mirai0.commons.Skill;
import io.github.kloping.mirai0.unitls.Tools.Tool;

import java.util.concurrent.CopyOnWriteArrayList;

import static Project.services.detailServices.GameSkillDetailService.*;

/**
 * @author github.kloping
 */
public class Skill23 extends SkillTemplate {

    public Skill23() {
        super(23);
    }


    @Override
    public String getIntro() {
        return VelocityUtils.getTemplateToString(String.format("skill/%s.intro", getJid()),
                Tool.INSTANCE.device(getAddP(getJid(), getId()), 1000, 1));
    }

    @Override
    public Skill create(SkillInfo info, Number who, Number... nums) {
        return new Skill(info, who, new CopyOnWriteArrayList<>(nums), "减cd") {
            @Override
            public void before() {
                long q = nearest(1, who.longValue(), nums)[0];
                ControllerSource.playerBehavioralManager.add(new Growth().setQid(q).setTime(System.currentTimeMillis() + getDuration(getJid())).setType(PlayerBehavioralManager.ATTACK_PRE).setValue(-info.getAddPercent()));
                setTips("作用于:" + q);
            }
        };
    }
}
