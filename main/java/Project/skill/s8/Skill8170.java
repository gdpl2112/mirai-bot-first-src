package Project.skill.s8;

import Project.broadcast.game.HpChangeBroadcast;
import Project.skill.SkillTemplate;
import io.github.kloping.mirai0.commons.Skill;
import io.github.kloping.mirai0.commons.gameEntitys.SkillInfo;

import java.util.concurrent.CopyOnWriteArrayList;

import static Project.controllers.auto.ControllerSource.playerBehavioralManager;
import static Project.services.detailServices.GameDetailServiceUtils.attGhostOrMan;
import static Project.services.detailServices.GameSkillDetailService.*;
import static io.github.kloping.mirai0.Main.Resource.THREADS;
import static io.github.kloping.mirai0.commons.resouce_and_tool.CommonSource.percentTo;
import static io.github.kloping.mirai0.commons.resouce_and_tool.ResourceSet.FinalNormalString.ATTACK_BREAK;

/**
 * @author github.kloping
 */
public class Skill8170 extends SkillTemplate {

    public Skill8170() {
        super(8170);
    }


    @Override
    public String getIntro() {
        return String.format("骨龙第八魂技,1倍前摇后,对指定敌人造成%s%%的伤害,并在魂技时间内,将所受到的伤害的%s%%的值转化为护盾",
                getAddP(getJid(), getId()),
                getAddP(getJid(), getId())
        );
    }

    @Override
    public Skill create(SkillInfo info, Number who, Number... nums) {
        return new Skill(info, who, new CopyOnWriteArrayList<>(nums), "骨龙第八魂技") {

            @Override
            public void before() {

            }

            @Override
            public void run() {
                super.run();
                HpChangeBroadcast.HpChangeReceiver receiver = null;
                HpChangeBroadcast.INSTANCE.add(receiver = new HpChangeBroadcast.HpChangeReceiver() {
                    @Override
                    public void onReceive(long q0, long hpFrom, long hpTo, long value, long from, type type) {
                        if (value < 0) {
                            if (q0 == who.longValue()) {
                                addShield(who.longValue(), percentTo(info.getAddPercent(), -value));
                            }
                        }
                    }
                });
                HpChangeBroadcast.HpChangeReceiver finalReceiver = receiver;
                THREADS.submit(() -> {
                    try {
                        Thread.sleep(getDuration(getJid()));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    HpChangeBroadcast.INSTANCE.remove(finalReceiver);
                });
                if (nums.length <= 0) return;
                long qid = nums[0].longValue();
                try {
                    Thread.sleep(playerBehavioralManager.getAttPre(qid));
                    StringBuilder sb = new StringBuilder();
                    long v = percentTo(info.getAddPercent(), getPersonInfo().att());
                    attGhostOrMan(sb, who, qid, v);
                    setTips(sb.toString());
                } catch (InterruptedException e) {
                    setTips(ATTACK_BREAK);
                }
            }
        };
    }
}
