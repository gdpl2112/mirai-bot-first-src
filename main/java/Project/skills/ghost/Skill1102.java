package Project.skills.ghost;

import Project.aSpring.dao.SkillInfo;
import Project.commons.gameEntitys.TagPack;
import Project.services.detailServices.ac.GameJoinDetailService;
import Project.services.detailServices.roles.v1.TagManagers;
import Project.skills.SkillTemplate;
import io.github.kloping.mirai0.commons.GhostObj;
import io.github.kloping.mirai0.commons.Skill;
import io.github.kloping.mirai0.commons.game.NormalTagPack;

import java.util.concurrent.CopyOnWriteArrayList;

import static Project.dataBases.skill.SkillDataBase.TAG_CANT_USE;
import static Project.services.detailServices.GameSkillDetailService.getAddP;

/**
 * @author github.kloping
 */
public class Skill1102 extends SkillTemplate {

    public Skill1102() {
        super(1102);
        setName("极致冰冻");
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
                long t = getAddP(getJid(), getId());
                GhostObj ghostObj = GameJoinDetailService.getGhostObjFrom(-who.longValue());
                TagPack pack = new NormalTagPack(TAG_CANT_USE, 1000 * getAddP(getJid(), getId()));
                pack.setQ(-who.longValue()).setValue(1L);
                TagManagers.getTagManager(-who.longValue()).addTag(pack);
            }
        };
    }
}
