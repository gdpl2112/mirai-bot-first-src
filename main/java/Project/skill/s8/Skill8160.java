package Project.skill.s8;

import Project.skill.SkillTemplate;
import io.github.kloping.mirai0.commons.Skill;
import io.github.kloping.mirai0.commons.gameEntitys.SkillInfo;

import java.util.concurrent.CopyOnWriteArrayList;

import static Project.controllers.auto.ControllerSource.playerBehavioralManager;
import static Project.services.detailServices.GameDetailService.addHp;
import static Project.services.detailServices.GameDetailServiceUtils.attGhostOrMan;
import static Project.services.detailServices.GameSkillDetailService.getAddP;
import static io.github.kloping.mirai0.commons.resouce_and_tool.CommonSource.percentTo;
import static io.github.kloping.mirai0.commons.resouce_and_tool.CommonSource.toPercent;
import static io.github.kloping.mirai0.commons.resouce_and_tool.ResourceSet.FinalNormalString.ATTACK_BREAK;

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
        return String.format("蛇矛八魂技,向指定敌人扔出矛,0.5倍前摇后,对指定敌人造成%s%%的伤害," +
                        "该此伤害附带%s%%的吸血效果,且自身血量越低吸血效果越强,最多%s%%",
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
