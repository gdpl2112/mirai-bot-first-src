package Project.skill.s8;

import Project.skill.SkillTemplate;
import io.github.kloping.mirai0.commons.Skill;
import io.github.kloping.mirai0.commons.gameEntitys.SkillInfo;

import java.util.concurrent.CopyOnWriteArrayList;

import static Project.dataBases.skill.SkillDataBase.AVAILABLE_IN_CONTROL;
import static Project.dataBases.skill.SkillDataBase.NEGATIVE_TAGS;
import static Project.services.detailServices.GameDetailServiceUtils.attGhostOrMan;
import static Project.services.detailServices.GameSkillDetailService.getAddP;
import static Project.services.detailServices.GameSkillDetailService.nearest;
import static io.github.kloping.mirai0.commons.resouce_and_tool.CommonSource.percentTo;

/**
 * @author github.kloping
 */
public class Skill8260 extends SkillTemplate {

    static {
        AVAILABLE_IN_CONTROL.add(8260);
    }

    public Skill8260() {
        super(8260);
    }

    @Override
    public String getIntro() {
        return String.format("海神第八魂技,解除自身所有负面控制,并对指定敌人造成%s%%的伤害",
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
                for (String negativeTag : NEGATIVE_TAGS) {
                    if (getPersonInfo().containsTag(negativeTag)) {
                        getPersonInfo().eddTag(negativeTag);
                    }
                }
                long v = percentTo(info.getAddPercent(), getPersonInfo().att());
                StringBuilder sb = new StringBuilder();
                attGhostOrMan(sb, who, qid, v);
                setTips(sb.toString());
            }
        };
    }
}
