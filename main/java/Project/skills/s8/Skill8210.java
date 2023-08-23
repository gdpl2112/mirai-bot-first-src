package Project.skills.s8;

import Project.broadcast.game.GhostLostBroadcast;
import Project.broadcast.game.SelectAttBroadcast;
import Project.commons.SpGroup;
import Project.aSpring.dao.SkillInfo;
import Project.controllers.auto.ControllerSource;
import Project.services.detailServices.ac.GameJoinDetailService;
import Project.services.detailServices.roles.DamageType;
import Project.skills.SkillTemplate;
import io.github.kloping.mirai0.Main.iutils.MemberUtils;
import io.github.kloping.mirai0.commons.Skill;

import java.util.concurrent.CopyOnWriteArrayList;

import static Project.commons.rt.CommonSource.percentTo;
import static Project.services.detailServices.GameSkillDetailService.getDuration;

/**
 * @author github.kloping
 */
public class Skill8210 extends SkillTemplate {

    public Skill8210() {
        super(8210);
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
                SelectAttBroadcast.INSTANCE.add(new SelectAttBroadcast.SelectAttReceiver() {
                    private long cd = System.currentTimeMillis() + getDuration(getJid());
                    private int by = info.getAddPercent();
                    private long att = percentTo(by, getPersonInfo().getAtt());

                    @Override
                    public boolean onReceive(long q1, long q2, long v, int type) {
                        if (q1 == who.longValue()) {
                            if (type == 1) {
                                ControllerSource.gameService.attNow(who.longValue(), q2, SpGroup.get(MemberUtils.getRecentSpeechesGid(who.longValue())), 0, by,
                                        false, false);
                            } else {
                                setTips(GameJoinDetailService.attGho(
                                        who.longValue(), att, DamageType.AP, true, GhostLostBroadcast.KillType.SKILL_ATT,
                                        true));
                            }
                        }
                        return System.currentTimeMillis() >= cd;
                    }
                });
            }
        };
    }
}
