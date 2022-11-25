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

import static Project.dataBases.skill.SkillDataBase.TAG_CANT_HIDE;
import static Project.services.detailServices.GameSkillDetailService.getAddP;

/**
 * @author github.kloping
 */
public class Skill1003 extends SkillTemplate {

    public Skill1003() {
        super(1003);
        setName("锁定魂技");
    }

    @Override
    public String getIntro() {
        return String.format("魂兽普通技能,锁定魂技,下一次攻击无法躲避,锁定时间%s", getAddP(getJid(), getId()));
    }

    @Override
    public Skill create(SkillInfo info, Number who, Number... nums) {
        return new Skill(info, who, new CopyOnWriteArrayList<>(nums), "锁定魂技") {
            @Override
            public void before() {
                GhostObj ghostObj = GameJoinDetailService.getGhostObjFrom(-who.longValue());
                TagPack pack = new NormalTagPack(TAG_CANT_HIDE, 1000 * getAddP(getJid(), getId()));
                pack.setQ(-who.longValue()).setValue(1L);
                TagManagers.getTagManager(-who.longValue()).addTag(pack);
            }
        };
    }
}
