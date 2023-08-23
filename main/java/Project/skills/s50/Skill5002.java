package Project.skills.s50;

import Project.aSpring.dao.SkillInfo;
import Project.services.detailServices.GameBoneDetailService;
import Project.skills.SkillTemplate;
import io.github.kloping.mirai0.commons.Skill;
import Project.utils.Tools.Tool;

import java.util.concurrent.CopyOnWriteArrayList;

import static Project.services.detailServices.GameSkillDetailService.nearest;

/**
 * @author github.kloping
 */
public class Skill5002 extends SkillTemplate {
    public Skill5002() {
        super(5002);
    }

    @Override
    public Skill create(SkillInfo info, Number who, Number... nums) {
        return new Skill(info, who, new CopyOnWriteArrayList<>(nums), "二曰速") {
            @Override
            public void before() {
                for (Long qid : nearest(2, who.longValue(), nums)) {
                    GameBoneDetailService.addForAttr(qid, info.getAddPercent(), GameBoneDetailService.Type.HIDE_PRO, 120000);
                    setTips("作用于" + Tool.INSTANCE.at(qid));
                }
            }
        };
    }
}
