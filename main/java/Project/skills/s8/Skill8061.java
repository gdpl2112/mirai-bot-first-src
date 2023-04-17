package Project.skills.s8;

import Project.skills.SkillTemplate;
import io.github.kloping.mirai0.commons.Skill;
import Project.commons.gameEntitys.SkillInfo;
import Project.commons.resouce_and_tool.CommonSource;
import Project.commons.resouce_and_tool.ResourceSet;

import java.util.concurrent.CopyOnWriteArrayList;

import static Project.services.detailServices.GameDetailServiceUtils.attGhostOrMan;

/**
 * @author github.kloping
 */
public class Skill8061 extends SkillTemplate {

    public Skill8061() {
        super(8061);
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
                    attGhostOrMan(sb, who, nums[0].longValue(), CommonSource.percentTo(info.getAddPercent(), getPersonInfo().att()));
                    Thread.sleep(500);
                    setTips(sb.toString());
                    //2
                    sb = new StringBuilder();
                    attGhostOrMan(sb, who, nums[0].longValue(), CommonSource.percentTo(info.getAddPercent(), getPersonInfo().att()));
                    Thread.sleep(500);
                    setTips(sb.toString());
                    //3
                    sb = new StringBuilder();
                    attGhostOrMan(sb, who, nums[0].longValue(), CommonSource.percentTo(info.getAddPercent(), getPersonInfo().att()));
                    Thread.sleep(500);
                    setTips(sb.toString());
                    //4
                    sb = new StringBuilder();
                    attGhostOrMan(sb, who, nums[0].longValue(), CommonSource.percentTo(info.getAddPercent(), getPersonInfo().att()));
                    Thread.sleep(500);
                    setTips(sb.toString());
                    //5
                    sb = new StringBuilder();
                    attGhostOrMan(sb, who, nums[0].longValue(), CommonSource.percentTo(info.getAddPercent(), getPersonInfo().att()));
                    Thread.sleep(500);
                    setTips(sb.toString());
                    //6
                    sb = new StringBuilder();
                    attGhostOrMan(sb, who, nums[0].longValue(), CommonSource.percentTo(info.getAddPercent(), getPersonInfo().att()));
                    Thread.sleep(500);
                    setTips(sb.toString());
                    //7
                    sb = new StringBuilder();
                    attGhostOrMan(sb, who, nums[0].longValue(), CommonSource.percentTo(info.getAddPercent(), getPersonInfo().att()));
                    Thread.sleep(500);
                    setTips(sb.toString());
                    //8
                    sb = new StringBuilder();
                    attGhostOrMan(sb, who, nums[0].longValue(), CommonSource.percentTo(info.getAddPercent(), getPersonInfo().att()));
                    Thread.sleep(500);
                    setTips(sb.toString());
                } catch (Exception e) {
                    setTips(ResourceSet.FinalNormalString.ATTACK_BREAK);
                }
            }
        };
    }
}
