package Project.skill.s8;

import Project.skill.SkillTemplate;
import io.github.kloping.mirai0.commons.Skill;
import io.github.kloping.mirai0.commons.SkillIntro;
import io.github.kloping.mirai0.commons.gameEntitys.SkillInfo;

import java.util.concurrent.CopyOnWriteArrayList;

import static Project.controllers.auto.ControllerSource.playerBehavioralManager;
import static Project.services.detailServices.GameDetailServiceUtils.attGhostOrMan;
import static Project.services.detailServices.GameSkillDetailService.getAddP;
import static io.github.kloping.mirai0.commons.resouce_and_tool.CommonSource.percentTo;

/**
 * @author github.kloping
 */
public class Skill8121 extends SkillTemplate {

    public Skill8121() {
        super(8121);
    }

    @Override
    public SkillIntro.Type[] getTypes() {
        return new SkillIntro.Type[]{SkillIntro.Type.Att, SkillIntro.Type.ToOne};
    }

    @Override
    public String getIntro() {
        return String.format("大力金刚熊第八魂技,向指定敌人,跳跃重击立刻对其造成%s%%的伤害,并落地之后(1个攻击前摇)后使用熊爪对其造成%s%%的伤害",
                getAddP(getJid(), getId()), getAddP(getJid(), getId()) * F0
        );
    }

    public static final int F0 = 3;

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
