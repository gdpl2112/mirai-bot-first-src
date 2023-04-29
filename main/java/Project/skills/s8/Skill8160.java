package Project.skills.s8;

import Project.commons.gameEntitys.SkillInfo;
import Project.skills.SkillTemplate;
import Project.utils.VelocityUtils;
import io.github.kloping.mirai0.commons.Skill;

import java.util.concurrent.CopyOnWriteArrayList;

import static Project.commons.rt.CommonSource.percentTo;
import static Project.commons.rt.CommonSource.toPercent;
import static Project.commons.rt.ResourceSet.FinalNormalString.ATTACK_BREAK;
import static Project.controllers.auto.ControllerSource.playerBehavioralManager;
import static Project.services.detailServices.GameDetailService.addHp;
import static Project.services.detailServices.GameDetailServiceUtils.attGhostOrMan;
import static Project.services.detailServices.GameSkillDetailService.getAddP;

/**
 * @author github.kloping
 */
public class Skill8160 extends SkillTemplate {

    private static final Integer F0 = 2;
    private static final Integer MAX = 180;

    public Skill8160() {
        super(8160);
    }


    @Override
    public String getIntro() {
        return VelocityUtils.getTemplateToString(String.format("skill/%s.intro", getJid()),
                getAddP(getJid(), getId()),
                getAddP(getJid(), getId()) / F0, MAX
        );
    }

    @Override
    public Skill create(SkillInfo info, Number who, Number... nums) {
        return new Skill(info, who, new CopyOnWriteArrayList<>(nums), "蛇矛第八魂技") {

            @Override
            public void before() {

            }

            @Override
            public void run() {
                super.run();
                if (nums.length <= 0) return;
                long qid = nums[0].longValue();
                try {
                    Thread.sleep(playerBehavioralManager.getAttPre(qid));
                    StringBuilder sb = new StringBuilder();
                    long v = percentTo(info.getAddPercent(), getPersonInfo().att());
                    attGhostOrMan(sb, who, qid, v);
                    setTips(sb.toString());
                    long b = info.getAddPercent() / F0;
                    int b0 = toPercent(getPersonInfo().getHp(), getPersonInfo().getHpL());
                    b += (100 - b0);
                    b = b > MAX ? MAX : b;
                    long v0 = percentTo(b0, v);
                    setTips(addHp(who.longValue(), v0));
                } catch (InterruptedException e) {
                    setTips(ATTACK_BREAK);
                }
            }
        };
    }
}
