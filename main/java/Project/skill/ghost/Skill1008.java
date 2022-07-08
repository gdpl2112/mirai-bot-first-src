package Project.skill.ghost;

import Project.services.detailServices.GameJoinDetailService;
import Project.services.detailServices.roles.v1.TagManagers;
import Project.skill.SkillTemplate;
import io.github.kloping.mirai0.commons.GhostObj;
import io.github.kloping.mirai0.commons.Skill;
import io.github.kloping.mirai0.commons.game.NormalTagPack;
import io.github.kloping.mirai0.commons.gameEntitys.SkillInfo;
import io.github.kloping.mirai0.commons.gameEntitys.TagPack;

import java.util.concurrent.CopyOnWriteArrayList;

import static Project.dataBases.skill.SkillDataBase.TAG_HJ_IMMUNITY;
import static Project.services.detailServices.GameSkillDetailService.getAddP;

/**
 * @author github.kloping
 */
public class Skill1008 extends SkillTemplate {

    public Skill1008() {
        super(1008);
        setName("精神护罩");
    }

    @Override
    public String getIntro() {
        return String.format("魂兽普通技能,精神护罩,%s秒内免疫精神攻击", getAddP(getJid(), getId()));
    }

    @Override
    public Skill create(SkillInfo info, Number who, Number... nums) {
        return new Skill(info, who, new CopyOnWriteArrayList<>(nums), "") {
            @Override
            public void before() {
                GhostObj ghostObj = GameJoinDetailService.getGhostObjFrom(-who.longValue());
                TagPack pack = new NormalTagPack(TAG_HJ_IMMUNITY, 1000 * getAddP(getJid(), getId()));
                pack.setQ(who.longValue()).setValue(1L);
                TagManagers.getTagManager(who.longValue()).addTag(pack);
            }
        };
    }
}
