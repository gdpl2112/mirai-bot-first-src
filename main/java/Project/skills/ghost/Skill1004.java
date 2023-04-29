package Project.skills.ghost;

import Project.commons.gameEntitys.SkillInfo;
import Project.commons.gameEntitys.TagPack;
import Project.services.detailServices.ac.GameJoinDetailService;
import Project.services.detailServices.roles.v1.TagManagers;
import Project.skills.SkillTemplate;
import io.github.kloping.mirai0.commons.GhostObj;
import io.github.kloping.mirai0.commons.Skill;
import io.github.kloping.mirai0.commons.game.NormalTagPack;

import java.util.concurrent.CopyOnWriteArrayList;

import static Project.dataBases.skill.SkillDataBase.TAG_STRENGTHEN_ATT;
import static Project.services.detailServices.GameSkillDetailService.getAddP;

/**
 * @author github.kloping
 */
public class Skill1004 extends SkillTemplate {

    public Skill1004() {
        super(1004);
        setName("强击魂技");
    }

    @Override
    public Skill create(SkillInfo info, Number who, Number... nums) {
        return new Skill(info, who, new CopyOnWriteArrayList<>(nums), "") {
            @Override
            public void before() {
                GhostObj ghostObj = GameJoinDetailService.getGhostObjFrom(-who.longValue());
                TagPack pack = new NormalTagPack(TAG_STRENGTHEN_ATT, 20000);
                long v = getAddP(getJid(), getId());
                if (ghostObj.getWiths().size() > 0) {
                    v *= 2;
                }
                pack.setQ(who.longValue()).setValue(v);
                TagManagers.getTagManager(who.longValue()).addTag(pack);
            }
        };
    }
}
