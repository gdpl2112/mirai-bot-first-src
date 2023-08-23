package Project.skills.normal;

import Project.aSpring.dao.SkillInfo;
import Project.services.detailServices.GameBoneDetailService;
import Project.skills.SkillTemplate;
import io.github.kloping.mirai0.commons.Skill;
import Project.utils.Tools.Tool;

import java.util.concurrent.CopyOnWriteArrayList;

import static Project.services.detailServices.GameSkillDetailService.oneNearest;

/**
 * @author github.kloping
 */
public class Skill12 extends SkillTemplate {


    public Skill12() {
        super(12);
    }


    @Override
    public Skill create(SkillInfo info, Number who, Number... nums) {
        return new Skill(info, who, new CopyOnWriteArrayList<>(nums), "增速闪避") {

            private Long q, v;

            @Override
            public void before() {
                q = oneNearest(who, nums);
                v = Long.valueOf(info.getAddPercent());
                GameBoneDetailService.addForAttr(q, v, GameBoneDetailService.Type.HIDE_PRO, 120000);
                setTips("作用于" + Tool.INSTANCE.at(q));
            }

            @Override
            public void run() {
                super.run();
            }
        };
    }
}
