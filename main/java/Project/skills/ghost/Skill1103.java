package Project.skills.ghost;

import Project.commons.gameEntitys.SkillInfo;
import Project.services.detailServices.ac.GameJoinDetailService;
import Project.skills.SkillTemplate;
import io.github.kloping.mirai0.commons.GhostObj;
import io.github.kloping.mirai0.commons.Skill;
import io.github.kloping.mirai0.commons.game.NormalWithWhoTagPack;
import io.github.kloping.number.NumberUtils;

import java.util.concurrent.CopyOnWriteArrayList;

import static Project.dataBases.skill.SkillDataBase.TAG_SHIELD;
import static Project.services.detailServices.GameSkillDetailService.addTagPack;
import static Project.services.detailServices.GameSkillDetailService.getAddP;

/**
 * @author github.kloping
 */
public class Skill1103 extends SkillTemplate {

    public Skill1103() {
        super(1103);
        setName("极冰•护盾");
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
                Long t = getAddP(getJid(), getId());
                GhostObj ghostObj = GameJoinDetailService.getGhostObjFrom(-who.longValue());
                NormalWithWhoTagPack tagPack = new NormalWithWhoTagPack(TAG_SHIELD, 15000);
                tagPack.setWho(who.longValue()).setQ(who.longValue())
                        .setValue(NumberUtils.percentTo(t.intValue(), ghostObj.getMaxHp()));
                addTagPack(tagPack);
            }
        };
    }
}
