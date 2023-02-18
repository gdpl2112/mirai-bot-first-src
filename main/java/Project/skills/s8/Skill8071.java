package Project.skills.s8;

import Project.skills.SkillTemplate;
import io.github.kloping.mirai0.commons.Skill;
import io.github.kloping.mirai0.commons.gameEntitys.SkillInfo;

import java.util.concurrent.CopyOnWriteArrayList;

import static Project.services.detailServices.GameDetailServiceUtils.attGhostOrMan;
import static io.github.kloping.mirai0.commons.resouce_and_tool.CommonSource.percentTo;
import static io.github.kloping.mirai0.commons.resouce_and_tool.ResourceSet.FinalNormalString.ATTACK_BREAK;

/**
 * @author github.kloping
 */
public class Skill8071 extends SkillTemplate {

    public Skill8071() {
        super(8071);
    }

    @Override
    public Skill create(SkillInfo info, Number who, Number... nums) {
        return new Skill(info, who, new CopyOnWriteArrayList<>(nums), "白虎第八魂技") {
            @Override
            public void before() {

            }

            @Override
            public void run() {
                if (nums.length == 0) return;
                try {
                    Thread.sleep(1500);
                    StringBuilder sb = new StringBuilder();
                    long v = percentTo(info.getAddPercent(), getPersonInfo().att());
                    attGhostOrMan(sb, who, nums[0].longValue(), v);
                    getPersonInfo().addHp(percentTo(15, v));
                    setTips(sb.toString());
                } catch (InterruptedException e) {
                    setTips(ATTACK_BREAK);
                }
            }
        };
    }
}
