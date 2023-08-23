package Project.skills.s8;

import Project.aSpring.dao.SkillInfo;
import Project.skills.SkillTemplate;
import io.github.kloping.mirai0.commons.Skill;

import java.util.concurrent.CopyOnWriteArrayList;

import static Project.commons.rt.CommonSource.percentTo;
import static Project.controllers.auto.ControllerSource.playerBehavioralManager;
import static Project.services.detailServices.GameDetailServiceUtils.attGhostOrMan;
import static Project.services.detailServices.GameSkillDetailService.getAddP;

/**
 * @author github.kloping
 */
public class Skill8121 extends SkillTemplate {

    public static final int F0 = 3;

    public Skill8121() {
        super(8121);
    }

    @Override
    public String getIntro() {
        return String.format("大力金刚熊第八魂技,向指定敌人,跳跃重击立刻对其造成%s%%的伤害,并落地之后(1个攻击前摇)后使用熊爪对其造成%s%%的伤害",
                getAddP(getJid(), getId()), getAddP(getJid(), getId()) * F0
        );
    }

    @Override
    public Skill create(SkillInfo info, Number who, Number... nums) {
        return new Skill(info, who, new CopyOnWriteArrayList<>(nums), "大力金刚熊第八魂技") {
            @Override
            public void before() {

            }

            @Override
            public void run() {
                super.run();
                if (nums.length == 0) return;
                long qid = nums[0].longValue();
                StringBuilder sb = new StringBuilder();
                long v = percentTo(info.getAddPercent(), getPersonInfo().att());
                attGhostOrMan(sb, who, qid, v);
                setTips(sb.toString());
                try {
                    Thread.sleep(playerBehavioralManager.getAttPre(who.longValue()));
                    sb = new StringBuilder();
                    v = percentTo(info.getAddPercent() * F0, getPersonInfo().att());
                    attGhostOrMan(sb, who, qid, v);
                    setTips(sb.toString());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
    }
}
