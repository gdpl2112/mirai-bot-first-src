package Project.skills.normal;

import Project.aSpring.dao.SkillInfo;
import Project.skills.SkillTemplate;
import Project.utils.VelocityUtils;
import io.github.kloping.mirai0.commons.Skill;
import io.github.kloping.mirai0.commons.game.NormalWithWhoTagPack;

import java.util.concurrent.CopyOnWriteArrayList;

import static Project.commons.rt.CommonSource.percentTo;
import static Project.dataBases.GameDataBase.getInfo;
import static Project.dataBases.skill.SkillDataBase.TAG_SHIELD;
import static Project.services.detailServices.GameSkillDetailService.*;

/**
 * @author github.kloping
 */
public class Skill16 extends SkillTemplate {

    public Skill16() {
        super(16);
    }

    public String getIntro() {
        return VelocityUtils.getTemplateToString(String.format("skill/%s.intro", getJid()), getAddP(getJid(), getId()), getAddP(getJid(), getId()) / 4);
    }

    @Override
    public Skill create(SkillInfo info, Number who, Number... nums) {
        return new Skill(info, who, new CopyOnWriteArrayList<>(nums), "临时护盾") {
            @Override
            public void before() {
                long qid = nearest(1, who.longValue(), nums)[0];
                int b = info.getAddPercent();
                long t = b / 4;
                long v2 = percentTo(b, getInfo(who).getHpL());
                NormalWithWhoTagPack tagPack = new NormalWithWhoTagPack(TAG_SHIELD, t * 1000);
                tagPack.setWho(who.longValue()).setQ(qid).setValue(v2);
                addTagPack(tagPack);
            }

            @Override
            public void run() {
                super.run();
            }
        };
    }
}
