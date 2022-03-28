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
import static Project.services.detailServices.GameSkillDetailService.addShield;
import static Project.services.detailServices.GameSkillDetailService.getAddP;

/**
 * @author github.kloping
 */
public class Skill731 extends SkillTemplate {


    public Skill731() {
        super(731);
    }

    @Override
    public SkillIntro.Type[] getTypes() {
        return new SkillIntro.Type[]{SkillIntro.Type.WHZs, SkillIntro.Type.HasTime, SkillIntro.Type.Special, SkillIntro.Type.Add, SkillIntro.Type.Mark, SkillIntro.Type.Shd};
    }

    @Override
    public String getIntro() {
        return String.format("暗金恐爪熊,发挥恐怖的威力增加%s%%的攻击和%s%%的临时护盾", getAddP(getJid(), getId()), getAddP(getJid(), getId()));
    }

    @Override
    public Skill create(SkillInfo info, Number who, Number... nums) {
        return new Skill(info, who, new CopyOnWriteArrayList<>(nums), "暗金恐爪熊") {
            private long v;

            @Override
            public void before() {
                PersonInfo pInfo = getInfo(who);
                v = percentTo(info.getAddPercent(), pInfo.getAtt());
                long v = percentTo(info.getAddPercent(), pInfo.getHpL());
                addAttHasTime(who.longValue(), new HasTimeAdder(System.currentTimeMillis() + t731, who.longValue(), v));
                addShield(who.longValue(), v, (long) t731);
                putPerson(pInfo);
            }

            @Override
            public void run() {
                super.run();
                try {
                    Thread.sleep(t731);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                setTips("武魂真身失效");
            }
        };
    }
}
