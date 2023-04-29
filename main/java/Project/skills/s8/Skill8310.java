package Project.skills.s8;

import Project.commons.gameEntitys.SkillInfo;
import Project.skills.SkillTemplate;
import io.github.kloping.mirai0.commons.Skill;

import java.util.concurrent.CopyOnWriteArrayList;

import static Project.commons.rt.CommonSource.RANDOM;
import static Project.commons.rt.CommonSource.percentTo;
import static Project.services.detailServices.GameDetailServiceUtils.attGhostOrMan;
import static Project.services.detailServices.GameSkillDetailService.nearest;

/**
 * @author github.kloping
 */
public class Skill8310 extends SkillTemplate {

    public Skill8310() {
        super(8310);
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
