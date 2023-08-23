package Project.skills.s50;

import Project.aSpring.dao.SkillInfo;
import Project.skills.SkillTemplate;
import io.github.kloping.mirai0.commons.Skill;
import io.github.kloping.mirai0.commons.game.NormalTagPack;

import java.util.concurrent.CopyOnWriteArrayList;

import static Project.dataBases.GameDataBase.getInfo;
import static Project.dataBases.skill.SkillDataBase.TAG_ADD_ATT;
import static Project.services.detailServices.GameSkillDetailService.*;

/**
 * @author github.kloping
 */
public class Skill5005 extends SkillTemplate {
    public Skill5005() {
        super(5005);
    }

    @Override
    public Skill create(SkillInfo info, Number who, Number... nums) {
        return new Skill(info, who, new CopyOnWriteArrayList<>(nums), "五曰攻") {

            Long[] qs;

            @Override
            public void before() {
                qs = nearest(2, who.longValue(), nums);
                for (Long q : qs) {
                    NormalTagPack tagPack = new NormalTagPack(TAG_ADD_ATT, getDuration(getJid()));
                    tagPack.setQ(q).setValue(Long.valueOf(info.getAddPercent()));
                    addTagPack(tagPack);
                }
            }
        };
    }
}
