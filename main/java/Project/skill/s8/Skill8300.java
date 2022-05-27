package Project.skill.s8;

import Project.skill.SkillTemplate;
import io.github.kloping.mirai0.commons.Skill;
import io.github.kloping.mirai0.commons.SkillIntro;
import io.github.kloping.mirai0.commons.gameEntitys.SkillInfo;

import java.util.concurrent.CopyOnWriteArrayList;

import static Project.services.detailServices.GameDetailServiceUtils.attGhostOrMan;
import static Project.services.detailServices.GameSkillDetailService.getAddP;
import static Project.services.detailServices.GameSkillDetailService.nearest;
import static io.github.kloping.mirai0.commons.resouce_and_tool.CommonSource.percentTo;
import static io.github.kloping.mirai0.commons.resouce_and_tool.CommonSource.toPercent;

/**
 * @author github.kloping
 */
public class Skill8300 extends SkillTemplate {

    public Skill8300() {
        super(8300);
    }
    @Override
    public SkillIntro.Type[] getTypes() {
        return new SkillIntro.Type[]{SkillIntro.Type.Att, SkillIntro.Type.ToOne};
    }

    @Override
    public String getIntro() {
        return String.format("魔神剑第八魂技,以自身当前30%%生命值为引,引血越多伤害越高,对指定敌人造成%s%%+的伤害",
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
                long v0 = getPersonInfo().getHp();
                long v1 = percentTo(30, v0);
                getPersonInfo().addHp(-v1).apply();
                int b = toPercent(v1, getPersonInfo().getHpL());
                b += info.getAddPercent();
                setTips("将造成" + b + "%");
                long v = percentTo(b, getPersonInfo().att());
                StringBuilder sb = new StringBuilder();
                attGhostOrMan(sb, who, qid, v);
                setTips(sb.toString());
            }
        };
    }
}
