package Project.skill.ghost;

import Project.services.detailServices.ac.GameJoinDetailService;
import Project.services.detailServices.roles.v1.TagManagers;
import Project.skill.SkillTemplate;
import io.github.kloping.mirai0.commons.GhostObj;
import io.github.kloping.mirai0.commons.Skill;
import io.github.kloping.mirai0.commons.game.NormalTagPack;
import io.github.kloping.mirai0.commons.gameEntitys.SkillInfo;
import io.github.kloping.mirai0.commons.gameEntitys.TagPack;

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
    public String getIntro() {
        return String.format("魂兽普通技能,强击魂技,强化下次被动攻击伤害,造成%s%%的伤害,当存在支援者时效果翻倍", getAddP(getJid(), getId()));
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
