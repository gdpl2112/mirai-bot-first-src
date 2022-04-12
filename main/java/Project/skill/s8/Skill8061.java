
package Project.skill.s8;

import Project.skill.SkillTemplate;
import io.github.kloping.mirai0.commons.Skill;
import io.github.kloping.mirai0.commons.SkillIntro;
import io.github.kloping.mirai0.commons.gameEntitys.SkillInfo;
import io.github.kloping.mirai0.commons.resouce_and_tool.ResourceSet;

import java.util.concurrent.CopyOnWriteArrayList;

import static Project.services.detailServices.GameDetailServiceUtils.attGhostOrMan;
import static Project.services.detailServices.GameSkillDetailService.getAddP;
import static io.github.kloping.mirai0.unitls.Tools.Tool.percentTo;

/**
 * @author github.kloping
 */
public class Skill8061 extends SkillTemplate {

    public Skill8061() {
        super(8061);
    }

    @Override
    public SkillIntro.Type[] getTypes() {
        return new SkillIntro.Type[]{SkillIntro.Type.Att, SkillIntro.Type.ToOne};
    }

    @Override
    public String getIntro() {
        return String.format("柔骨兔第八魂技,连续对指定敌人造成8次伤害,每次造成%s%%的攻击", getAddP(getJid(), getId()));
    }

    @Override
    public Skill create(SkillInfo info, Number who, Number... nums) {
        return new Skill(info, who, new CopyOnWriteArrayList<>(nums), "柔骨兔第八魂技") {
            @Override
            public void before() {
            }

            @Override
            public void run() {
                try {
                    StringBuilder sb;
                    //1
                    sb = new StringBuilder();
                    attGhostOrMan(sb, who, nums[0].longValue(), percentTo(info.getAddPercent(), getPersonInfo().getAtt()));
                    Thread.sleep(500);
                    setTips(sb.toString());
                    //2
                    sb = new StringBuilder();
                    attGhostOrMan(sb, who, nums[0].longValue(), percentTo(info.getAddPercent(), getPersonInfo().getAtt()));
                    Thread.sleep(500);
                    setTips(sb.toString());
                    //3
                    sb = new StringBuilder();
                    attGhostOrMan(sb, who, nums[0].longValue(), percentTo(info.getAddPercent(), getPersonInfo().getAtt()));
                    Thread.sleep(500);
                    setTips(sb.toString());
                    //4
                    sb = new StringBuilder();
                    attGhostOrMan(sb, who, nums[0].longValue(), percentTo(info.getAddPercent(), getPersonInfo().getAtt()));
                    Thread.sleep(500);
                    setTips(sb.toString());
                    //5
                    sb = new StringBuilder();
                    attGhostOrMan(sb, who, nums[0].longValue(), percentTo(info.getAddPercent(), getPersonInfo().getAtt()));
                    Thread.sleep(500);
                    setTips(sb.toString());
                    //6
                    sb = new StringBuilder();
                    attGhostOrMan(sb, who, nums[0].longValue(), percentTo(info.getAddPercent(), getPersonInfo().getAtt()));
                    Thread.sleep(500);
                    setTips(sb.toString());
                    //7
                    sb = new StringBuilder();
                    attGhostOrMan(sb, who, nums[0].longValue(), percentTo(info.getAddPercent(), getPersonInfo().getAtt()));
                    Thread.sleep(500);
                    setTips(sb.toString());
                    //8
                    sb = new StringBuilder();
                    attGhostOrMan(sb, who, nums[0].longValue(), percentTo(info.getAddPercent(), getPersonInfo().getAtt()));
                    Thread.sleep(500);
                    setTips(sb.toString());
                } catch (Exception e) {
                    setTips(ResourceSet.FinalNormalString.ATTACK_BREAK);
                }
            }
        };
    }
}
