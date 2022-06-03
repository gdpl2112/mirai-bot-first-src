package Project.skill.s8;

import Project.broadcast.game.HpChangeBroadcast;
import Project.skill.SkillTemplate;
import io.github.kloping.mirai0.commons.Skill;
import io.github.kloping.mirai0.commons.gameEntitys.SkillInfo;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import static Project.services.detailServices.GameDetailServiceUtils.attGhostOrMan;
import static Project.services.detailServices.GameSkillDetailService.addShield;
import static Project.services.detailServices.GameSkillDetailService.getAddP;
import static io.github.kloping.mirai0.Main.Resource.THREADS;
import static io.github.kloping.mirai0.commons.resouce_and_tool.CommonSource.percentTo;
import static io.github.kloping.mirai0.commons.resouce_and_tool.ResourceSet.FinalNormalString.SKILL_BREAK;

/**
 * @author github.kloping
 */
public class Skill8150 extends SkillTemplate {

    private static final Integer F0 = 2;

    public Skill8150() {
        super(8150);
    }


    @Override
    public String getIntro() {
        return String.format("刺豚第八魂技,10秒内将收到的第一次伤害以%s%%的量返还给攻击者,若10秒未收到攻击则增加%s%%的护盾",
                getAddP(getJid(), getId()),
                getAddP(getJid(), getId()) / 2
        );
    }

    @Override
    public Skill create(SkillInfo info, Number who, Number... nums) {
        return new Skill(info, who, new CopyOnWriteArrayList<>(nums), "刺豚第八魂技") {

            @Override
            public void before() {

            }

            @Override
            public void run() {
                super.run();
                AtomicBoolean ab = new AtomicBoolean(false);
                HpChangeBroadcast.OnCeHpChangeReceiver receiver = null;
                HpChangeBroadcast.INSTANCE.add(receiver = new HpChangeBroadcast.OnCeHpChangeReceiver() {
                    @Override
                    public boolean onReceive(long q0, long hpFrom, long hpTo, long value, long from) {
                        if (value < 0) {
                            if (q0 == who.longValue()) {
                                StringBuilder sb = new StringBuilder();
                                long v = percentTo(info.getAddPercent(), -value);
                                attGhostOrMan(sb, who, from, v);
                                setTips(sb.toString());
                                ab.set(true);
                                return true;
                            }
                        }
                        return false;
                    }
                });
                final HpChangeBroadcast.OnCeHpChangeReceiver finalReceiver = receiver;
                THREADS.submit(() -> {
                    try {
                        Thread.sleep(10000);
                        if (!ab.get()) {
                            addShield(who.longValue(), percentTo(info.getAddPercent() / 2, getPersonInfo().getHpL()));
                        }
                    } catch (InterruptedException e) {
                        setTips(SKILL_BREAK);
                    }
                    HpChangeBroadcast.INSTANCE.remove(finalReceiver);
                });
            }
        };
    }
}
