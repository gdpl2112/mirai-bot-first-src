package Project.skills.ghost;

import Project.aSpring.dao.SkillInfo;
import Project.services.detailServices.ac.GameJoinDetailService;
import Project.services.detailServices.roles.v1.TagManagers;
import Project.skills.SkillTemplate;
import io.github.kloping.mirai0.commons.GhostObj;
import io.github.kloping.mirai0.commons.Skill;
import io.github.kloping.mirai0.commons.game.NormalTagPack;

import java.util.concurrent.CopyOnWriteArrayList;

import static Project.commons.rt.CommonSource.toPercent;
import static Project.dataBases.skill.SkillDataBase.TAG_DAMAGE_REDUCTION;
import static Project.services.detailServices.GameSkillDetailService.getAddP;

/**
 * @author github.kloping
 */
public class Skill1005 extends SkillTemplate {

    public Skill1005() {
        super(1005);
        setName("弱化技能");
    }


    @Override
    public Skill create(SkillInfo info, Number who, Number... nums) {
        return new Skill(info, who, new CopyOnWriteArrayList<>(nums), "") {
            GhostObj ghostObj;

            @Override
            public void before() {
                ghostObj = GameJoinDetailService.getGhostObjFrom(-who.longValue());

            }

            @Override
            public void run() {
                super.run();
                long v = getAddP(getJid(), getId());
                int b = toPercent(ghostObj.getHp(), ghostObj.getMaxHp());
                b = 100 - b;
                v += b / 2;
                NormalTagPack pack = new NormalTagPack(TAG_DAMAGE_REDUCTION, 15000);
                pack.setQ(who.longValue()).setValue(v);
                pack.setTime(System.currentTimeMillis() + 15000);
                TagManagers.getTagManager(who.longValue()).addTag(pack);
            }
        };
    }
}
