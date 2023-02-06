package Project.skill.ghost;

import Project.services.player.Growth;
import Project.services.player.PlayerBehavioralManager;
import Project.skill.SkillTemplate;
import io.github.kloping.mirai0.commons.Skill;
import io.github.kloping.mirai0.commons.gameEntitys.SkillInfo;

import java.util.concurrent.CopyOnWriteArrayList;

import static Project.controllers.auto.ControllerSource.playerBehavioralManager;
import static Project.services.detailServices.GameSkillDetailService.getAddP;
import static Project.services.detailServices.GameSkillDetailService.getDuration;

/**
 * @author github.kloping
 */
public class Skill1101 extends SkillTemplate {

    public Skill1101() {
        super(1101);
        setName("冷域迟缓");
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
                Integer b = getAddP(getJid(), getId()).intValue();
                long q = -who.longValue();
                playerBehavioralManager.add(
                        new Growth().setQid(q).setTime(System.currentTimeMillis() + getDuration(getJid()))
                                .setType(PlayerBehavioralManager.ATTACK_AFTER).setValue(playerBehavioralManager.getAttPost(who.longValue()) * b));
                playerBehavioralManager.add(
                        new Growth().setQid(q).setTime(System.currentTimeMillis() + getDuration(getJid()))
                                .setType(PlayerBehavioralManager.ATTACK_PRE).setValue(playerBehavioralManager.getAttPre(who.longValue()) * b));

            }
        };
    }
}
