package Project.skills.normal;

import Project.aSpring.dao.SkillInfo;
import Project.skills.SkillTemplate;
import io.github.kloping.mirai0.commons.Skill;
import io.github.kloping.mirai0.commons.game.NormalWithWhoTagPack;

import java.util.concurrent.CopyOnWriteArrayList;

import static Project.dataBases.skill.SkillDataBase.TAG_EDD_ATT;
import static Project.services.detailServices.GameSkillDetailService.addTagPack;

/**
 * @author github.kloping
 */
public class Skill13 extends SkillTemplate {

    public Skill13() {
        super(13);
    }


    @Override
    public Skill create(SkillInfo info, Number who, Number... nums) {
        return new Skill(info, who, new CopyOnWriteArrayList<>(nums), "减魂力") {
            @Override
            public void before() {
                if (nums == null || nums.length == 0) {
                    setTips("该玩家未注册");
                    return;
                }
                long qid = nums[0].longValue();
                long v2 = info.getAddPercent();
                NormalWithWhoTagPack tagPack = new NormalWithWhoTagPack(TAG_EDD_ATT, 60 * 1000);
                tagPack.setWho(who.longValue()).setQ(qid).setValue(v2);
                addTagPack(tagPack);
            }
        };
    }
}
