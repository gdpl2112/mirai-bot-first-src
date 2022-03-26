package Project.skill.s7;

import Project.skill.SkillTemplate;
import io.github.kloping.mirai0.commons.PersonInfo;
import io.github.kloping.mirai0.commons.Skill;
import io.github.kloping.mirai0.commons.SkillIntro;
import io.github.kloping.mirai0.commons.gameEntitys.SkillInfo;

import java.util.concurrent.CopyOnWriteArrayList;

import static Project.dataBases.GameDataBase.getInfo;
import static Project.dataBases.GameDataBase.putPerson;
import static Project.dataBases.skill.SkillDataBase.*;
import static Project.services.detailServices.GameSkillDetailService.getAddP;

/**
 * @author github.kloping
 */
public class Skill724 extends SkillTemplate {


    public Skill724() {
        super(724);
    }

    @Override
    public SkillIntro.Type[] getTypes() {
        return new SkillIntro.Type[]{SkillIntro.Type.WHZs, SkillIntro.Type.HasTime, SkillIntro.Type.Special, SkillIntro.Type.Add};
    }

    @Override
    public String getIntro() {
        return String.format("修罗神剑,令自身变真实伤害一分钟,增加%s%%的攻击,并恢复%s%%的魂力", getAddP(getJid(), getId()), getAddP(getJid(), getId()) / 3);
    }

    @Override
    public Skill create(SkillInfo info, Number who, Number... nums) {
        return new Skill(info, who, new CopyOnWriteArrayList<>(nums), "修罗神剑") {
            @Override
            public void before() {
                Long q = who.longValue();
                PersonInfo info_ = getInfo(q);
                Long lon = info_.getAtt();
                long v = percentTo(info.getAddPercent(), lon);
                info_.addHl(percentTo(info.getAddPercent() / 3, info_.getHll()));
                putPerson(info_.addTag(TAG_TRUE, 1));
                addAttHasTime(who.longValue(), new HasTimeAdder(System.currentTimeMillis() + t724, who.longValue(), v));
            }

            @Override
            public void run() {
                super.run();
                try {
                    Thread.sleep(t724);
                    putPerson(getInfo(who).eddTag(TAG_TRUE, 1));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }
}
