package Project.skill.normal;

import Project.controllers.auto.ControllerSource;
import Project.services.player.Growth;
import Project.services.player.PlayerBehavioralManager;
import Project.skill.SkillTemplate;
import io.github.kloping.mirai0.commons.Skill;
import io.github.kloping.mirai0.commons.gameEntitys.SkillInfo;

import java.util.concurrent.CopyOnWriteArrayList;

import static Project.services.detailServices.GameSkillDetailService.*;
import static io.github.kloping.mirai0.unitls.Tools.Tool.device;

/**
 * @author github.kloping
 */
public class Skill25 extends SkillTemplate {

    public Skill25() {
        super(25);
    }


    @Override
    public String getIntro() {
        return String.format("缩减指定人自身%s秒的攻击后摇,后摇最小1s", device(getAddP(getJid(), getId()), 1000, 1));
    }

    @Override
    public Skill create(SkillInfo info, Number who, Number... nums) {
        return new Skill(info, who, new CopyOnWriteArrayList<>(nums), "减cd") {
            @Override
            public void before() {
                long q = nearest(1, who.longValue(), nums)[0];
                ControllerSource.playerBehavioralManager.add(
                        new Growth().setQid(q).setTime(System.currentTimeMillis() + getDuration(getJid()))
                                .setType(PlayerBehavioralManager.ATTACK_AFTER).setValue(-info.getAddPercent()));
                setTips("作用于:" + q);
            }
        };
    }
}
