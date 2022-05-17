package Project.skill.s7;

import Project.services.player.Growth;
import Project.services.player.PlayerBehavioralManager;
import Project.skill.SkillTemplate;
import io.github.kloping.mirai0.commons.PersonInfo;
import io.github.kloping.mirai0.commons.Skill;
import io.github.kloping.mirai0.commons.SkillIntro;
import io.github.kloping.mirai0.commons.gameEntitys.SkillInfo;

import java.util.concurrent.CopyOnWriteArrayList;

import static Project.controllers.auto.ControllerSource.playerBehavioralManager;
import static Project.dataBases.GameDataBase.getInfo;
import static Project.dataBases.GameDataBase.putPerson;
import static Project.dataBases.skill.SkillDataBase.HasTimeAdder;
import static Project.dataBases.skill.SkillDataBase.addAttHasTime;
import static Project.services.detailServices.GameSkillDetailService.*;
import static io.github.kloping.mirai0.commons.resouce_and_tool.CommonSource.percentTo;

/**
 * @author github.kloping
 */
public class Skill728 extends SkillTemplate {

    public Skill728() {
        super(728);
    }

    @Override
    public SkillIntro.Type[] getTypes() {
        return WhTypes.T72;
    }

    @Override
    public String getIntro() {
        return String.format("落日神弓,武魂真身,增加%s%%攻击,清空并缩减一半前后摇CD", getAddP(getJid(), getId()));
    }

    @Override
    public Skill create(SkillInfo info, Number who, Number... nums) {
        return new Skill(info, who, new CopyOnWriteArrayList<>(nums), "落日神弓") {
            @Override
            public void before() {
                Long q = who.longValue();
                PersonInfo pInfo = getInfo(q);
                Long lon = pInfo.att();
                long v = percentTo(info.getAddPercent(), lon);
                pInfo.setAk1(2L);
                pInfo.setJak1(2L);
                putPerson(pInfo);
                addAttHasTime(who.longValue(), new HasTimeAdder(System.currentTimeMillis() + getDuration(getJid()), who.longValue(), v));

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
