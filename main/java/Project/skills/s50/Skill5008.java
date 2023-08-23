package Project.skills.s50;

import Project.aSpring.dao.SkillInfo;
import Project.skills.SkillTemplate;
import io.github.kloping.mirai0.commons.Skill;
import io.github.kloping.mirai0.commons.game.NormalTagPack;

import java.util.concurrent.CopyOnWriteArrayList;

import static Project.dataBases.skill.SkillDataBase.TAG_MY;
import static Project.services.detailServices.GameSkillDetailService.addTagPack;
import static Project.services.detailServices.GameSkillDetailService.oneNearest;

/**
 * @author github.kloping
 */
public class Skill5008 extends SkillTemplate {
    public Skill5008() {
        super(5008);
    }

    @Override
    public Skill create(SkillInfo info, Number who, Number... nums) {
        return new Skill(info, who, new CopyOnWriteArrayList<>(nums), "第八") {
            @Override
            public void before() {
                long q = oneNearest(who, nums);
                NormalTagPack pack = new NormalTagPack(TAG_MY, 60000);
                pack.setQ(q).setValue(info.getAddPercent().longValue());
                addTagPack(pack);
            }
        };
    }
}
