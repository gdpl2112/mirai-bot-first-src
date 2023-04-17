package Project.skills.bone;

import Project.skills.SkillTemplate;
import io.github.kloping.mirai0.commons.Skill;
import Project.commons.gameEntitys.SkillInfo;
import Project.commons.resouce_and_tool.CommonSource;
import io.github.kloping.mirai0.unitls.Tools.Tool;

import java.util.concurrent.CopyOnWriteArrayList;

import static Project.dataBases.GameDataBase.getInfo;
import static Project.services.detailServices.GameDetailServiceUtils.attGhostOrMan;
import static Project.services.detailServices.GameSkillDetailService.*;
import static Project.commons.resouce_and_tool.ResourceSet.FinalString.NEWLINE;

/**
 * @author github.kloping
 */
public class Skill1514 extends SkillTemplate {

    public Skill1514() {
        super(1514);
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
                StringBuilder sb = new StringBuilder();
                for (Number q : qs) {
                    if (Tool.INSTANCE.RANDOM.nextInt(100) <= r0) {
                        long v = CommonSource.percentTo(info.getAddPercent(), getInfo(who).att());
                        attGhostOrMan(sb, who, nums[0], v);
                    } else {
                        sb.append(NEWLINE).append("未命中目标!");
                    }
                }
                setTips(sb.toString());
            }
        };
    }
}
