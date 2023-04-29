package Project.skills.s7;

import Project.commons.gameEntitys.SkillInfo;
import Project.services.player.Growth;
import Project.services.player.PlayerBehavioralManager;
import Project.skills.SkillTemplate;
import io.github.kloping.mirai0.commons.Skill;

import java.util.concurrent.CopyOnWriteArrayList;

import static Project.commons.rt.CommonSource.percentTo;
import static Project.controllers.auto.ControllerSource.playerBehavioralManager;
import static Project.dataBases.skill.SkillDataBase.HasTimeAdder;
import static Project.dataBases.skill.SkillDataBase.addAttHasTime;
import static Project.services.detailServices.GameSkillDetailService.getDuration;

/**
 * @author github.kloping
 */
public class Skill728 extends SkillTemplate {

    public Skill728() {
        super(728);
    }


    @Override
    public Skill create(SkillInfo info, Number who, Number... nums) {
        return new Skill(info, who, new CopyOnWriteArrayList<>(nums), "落日神弓") {
            @Override
            public void before() {
                Long q = who.longValue();
                Long lon = getPersonInfo().att();
                long v = percentTo(info.getAddPercent(), lon);
                getPersonInfo().setAk1(2L);
                getPersonInfo().setJak1(2L).apply();
                addAttHasTime(who.longValue(), new HasTimeAdder(System.currentTimeMillis() + getDuration(getJid()), who.longValue(), v, getJid()));
                playerBehavioralManager.add(
                        new Growth().setQid(q).setTime(System.currentTimeMillis() + getDuration(getJid()))
                                .setType(PlayerBehavioralManager.ATTACK_AFTER).setValue(-playerBehavioralManager.getAttPost(who.longValue()) / 2));
                playerBehavioralManager.add(
                        new Growth().setQid(q).setTime(System.currentTimeMillis() + getDuration(getJid()))
                                .setType(PlayerBehavioralManager.ATTACK_PRE).setValue(-playerBehavioralManager.getAttPre(who.longValue()) / 2));

            }
        };
    }
}
