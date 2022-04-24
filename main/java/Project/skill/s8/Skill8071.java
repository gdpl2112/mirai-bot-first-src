
package Project.skill.s8;

import Project.skill.SkillTemplate;
import io.github.kloping.mirai0.commons.Skill;
import io.github.kloping.mirai0.commons.SkillIntro;
import io.github.kloping.mirai0.commons.gameEntitys.SkillInfo;

import java.util.concurrent.CopyOnWriteArrayList;

import static Project.services.detailServices.GameDetailServiceUtils.attGhostOrMan;
import static Project.services.detailServices.GameSkillDetailService.getAddP;
import static io.github.kloping.mirai0.commons.resouce_and_tool.ResourceSet.FinalNormalString.ATTACK_BREAK;
import static io.github.kloping.mirai0.unitls.Tools.Tool.percentTo;

/**
 * @author github.kloping
 */
public class Skill8071 extends SkillTemplate {

    public Skill8071() {
        super(8071);
    }

    @Override
    public SkillIntro.Type[] getTypes() {
        return new SkillIntro.Type[]{SkillIntro.Type.Att, SkillIntro.Type.ToOne};
    }

    @Override
    public String getIntro() {
        return String.format("白虎第八魂技,向指定敌人发起攻击,造成攻击%s%%的伤害,1.5秒后命中,命中回复该攻击值的15%的血量", getAddP(getJid(), getId()));
    }

    @Override
    public Skill create(SkillInfo info, Number who, Number... nums) {
        return new Skill(info, who, new CopyOnWriteArrayList<>(nums), "白虎第八魂技") {
            @Override
            public void before() {

            }

            @Override
            public void run() {
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
