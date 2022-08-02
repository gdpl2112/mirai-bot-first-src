package Project.skill.s8;

import Project.skill.SkillTemplate;
import io.github.kloping.mirai0.commons.Skill;
import io.github.kloping.mirai0.commons.gameEntitys.SkillInfo;

import java.util.concurrent.CopyOnWriteArrayList;

import static Project.services.detailServices.GameDetailServiceUtils.attGhostOrMan;
import static Project.services.detailServices.GameSkillDetailService.getAddP;
import static Project.services.detailServices.GameSkillDetailService.nearest;
import static io.github.kloping.mirai0.commons.resouce_and_tool.CommonSource.RANDOM;
import static io.github.kloping.mirai0.commons.resouce_and_tool.CommonSource.percentTo;

/**
 * @author github.kloping
 */
public class Skill8310 extends SkillTemplate {

    public Skill8310() {
        super(8310);
    }

    @Override
    public String getIntro() {
        return String.format("暗金恐爪熊第八魂技,连续辉出两爪,每爪对指定敌人造成%s%%的伤害,但其弹道之窄每爪只有40%%的命中率",
                getAddP(getJid(), getId())
        );
    }

    @Override
    public Skill create(SkillInfo info, Number who, Number... nums) {
        return new Skill(info, who, new CopyOnWriteArrayList<>(nums), "") {

            @Override
            public void before() {

            }

            @Override
            public void run() {
                super.run();
                Long[] ls = nearest(1, nums);
                if (ls.length < 0) return;
                long qid = ls[0];
                long v = percentTo(info.getAddPercent(), getPersonInfo().att());
                StringBuilder sb = new StringBuilder();
                if (RANDOM.nextInt(100) < 40) {
                    attGhostOrMan(sb, who, qid, v);
                } else {
                    sb.append("没有命中");
                }
                if (RANDOM.nextInt(100) < 40) {
                    attGhostOrMan(sb, who, qid, v);
                } else {
                    sb.append("没有命中");
                }
                setTips(sb.toString());

            }
        };
    }
}
