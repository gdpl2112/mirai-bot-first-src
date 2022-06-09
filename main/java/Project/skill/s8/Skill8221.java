package Project.skill.s8;

import Project.skill.SkillTemplate;
import io.github.kloping.mirai0.commons.Skill;
import io.github.kloping.mirai0.commons.gameEntitys.SkillInfo;

import java.util.concurrent.CopyOnWriteArrayList;

import static Project.dataBases.skill.SkillDataBase.NEGATIVE_TAGS;
import static Project.dataBases.skill.SkillDataBase.TAG2NAME;
import static Project.services.detailServices.GameDetailServiceUtils.attGhostOrMan;
import static Project.services.detailServices.GameSkillDetailService.getAddP;
import static Project.services.detailServices.GameSkillDetailService.nearest;
import static io.github.kloping.mirai0.commons.resouce_and_tool.CommonSource.percentTo;

/**
 * @author github.kloping
 */
public class Skill8221 extends SkillTemplate {

    public Skill8221() {
        super(8221);
    }

    @Override
    public String getIntro() {
        return String.format("光明圣龙第八魂技,清除指定敌人的所有增益效果,并对其造成%s%%的伤害",
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
                TAG2NAME.forEach((k, v) -> {
                    if (NEGATIVE_TAGS.contains(k)) return;
                    if (getPersonInfo().containsTag(k)) {
                        getPersonInfo().eddTag(k);
                    }
                });
                long v = percentTo(info.getAddPercent(), getPersonInfo().att());
                StringBuilder sb = new StringBuilder();
                attGhostOrMan(sb, who, qid, v);
                setTips(sb.toString());
            }
        };
    }
}
