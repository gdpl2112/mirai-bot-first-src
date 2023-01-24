package Project.skill.bone;

import Project.skill.SkillTemplate;
import io.github.kloping.mirai0.commons.Skill;
import io.github.kloping.mirai0.commons.gameEntitys.SkillInfo;
import io.github.kloping.mirai0.commons.gameEntitys.base.BaseInfo;
import io.github.kloping.mirai0.commons.resouce_and_tool.CommonSource;
import io.github.kloping.mirai0.unitls.Tools.Tool;

import java.util.concurrent.CopyOnWriteArrayList;

import static Project.dataBases.GameDataBase.getInfo;
import static Project.services.detailServices.GameDetailServiceUtils.attGhostOrMan;
import static Project.services.detailServices.GameDetailServiceUtils.getBaseInfoFromAny;
import static Project.services.detailServices.GameSkillDetailService.getAddP;
import static Project.services.detailServices.GameSkillDetailService.nearest;
import static io.github.kloping.mirai0.commons.resouce_and_tool.ResourceSet.FinalString.NEWLINE;

/**
 * @author github.kloping
 */
public class Skill1515 extends SkillTemplate {

    public Skill1515() {
        super(1515);
    }

    @Override
    public String getIntro() {
        return String.format("头部魂骨技能;对指定1~3个人造成*%s%%秒眩晕但命中率在80~40之间选择的目标越多概率越低", getAddP(getJid(), getId()));
    }

    @Override
    public Skill create(SkillInfo info, Number who, Number... nums) {
        return new Skill(info, who, new CopyOnWriteArrayList<>(nums), "魂骨技能") {
            @Override
            public void before() {
                Number[] qs = nearest(3, nums);
                int r0 = 80;
                if (qs.length == 2) {
                    r0 = 60;
                } else if (qs.length == 3) {
                    r0 = 40;
                }
                for (Number q : qs) {
                    BaseInfo b1 = getBaseInfoFromAny(who, who);
                    BaseInfo b2 = null;
                    b2 = getBaseInfoFromAny(who, q);
                    if (Tool.tool.RANDOM.nextInt(100) <= r0) {
                        b2.letVertigo(info.getAddPercent() * 1000L);
                        b2.apply();
                        setTips("眩晕于" + b2.getName());
                    } else {
                        setTips("未命中目标!");
                    }
                }
            }
        };
    }
}
