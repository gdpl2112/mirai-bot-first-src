package Project.skill.ghost;

import Project.services.detailServices.GameJoinDetailService;
import Project.services.detailServices.roles.v1.TagManagers;
import Project.skill.SkillTemplate;
import io.github.kloping.mirai0.commons.GhostObj;
import io.github.kloping.mirai0.commons.Skill;
import io.github.kloping.mirai0.commons.game.NormalTagPack;
import io.github.kloping.mirai0.commons.gameEntitys.SkillInfo;

import java.util.concurrent.CopyOnWriteArrayList;

import static Project.dataBases.skill.SkillDataBase.TAG_DAMAGE_REDUCTION;
import static Project.services.detailServices.GameSkillDetailService.getAddP;
import static io.github.kloping.mirai0.commons.resouce_and_tool.CommonSource.toPercent;

/**
 * @author github.kloping
 */
public class Skill1005 extends SkillTemplate {

    public Skill1005() {
        super(1005);
        setName("弱化技能");
    }

    @Override
    public String getIntro() {
        return String.format("魂兽普通技能,弱化技能,生命值每损失2%%额外增加1%%的免伤,基础免伤%s%%", getAddP(getJid(), getId()));
    }

    @Override
    public Skill create(SkillInfo info, Number who, Number... nums) {
        return new Skill(info, who, new CopyOnWriteArrayList<>(nums), "") {
            @Override
            public void before() {
                ghostObj = GameJoinDetailService.getGhostObjFrom(-who.longValue());

            }

            GhostObj ghostObj;

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
