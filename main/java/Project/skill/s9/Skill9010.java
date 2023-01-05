package Project.skill.s9;

import Project.services.detailServices.GameSkillDetailService;
import Project.skill.SkillTemplate;
import io.github.kloping.mirai0.commons.Skill;
import io.github.kloping.mirai0.commons.game.NormalTagPack;
import io.github.kloping.mirai0.commons.gameEntitys.SkillInfo;

import java.util.concurrent.CopyOnWriteArrayList;

import static Project.dataBases.skill.SkillDataBase.TAG_LIGHT_ATT;
import static Project.services.detailServices.GameSkillDetailService.getAddP;
import static Project.services.detailServices.GameSkillDetailService.getDuration;

/**
 * @author github.kloping
 */
public class Skill9010 extends SkillTemplate {

    public Skill9010() {
        super(9010);
    }

    @Override
    public String getIntro() {
        return String.format("使用后,对指定敌人造成[雷]元素,造成%s的攻击加成", getAddP(getJid(), getId()));
    }

    @Override
    public Skill create(SkillInfo info, Number who, Number... nums) {
        return new Skill(info, who, new CopyOnWriteArrayList<>(nums), "蓝电霸王龙第九魂技") {
            @Override
            public void before() {
                GameSkillDetailService.addTagPack(new NormalTagPack(TAG_LIGHT_ATT, getDuration(getJid()))
                        .setQ(who.longValue()).setValue(info.getAddPercent().longValue()));
            }
        };
    }
}
