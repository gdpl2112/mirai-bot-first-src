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
import static Project.services.detailServices.GameSkillDetailService.*;

/**
 * @author github.kloping
 */
public class Skill73 extends SkillTemplate {


    public Skill73() {
        super(73);
    }

    @Override
    public SkillIntro.Type[] getTypes() {
        return WhTypes.T72;
    }

    @Override
    public String getIntro() {
        return String.format("释放天使真身,每10秒恢复5%%的魂力,增加%s%%的攻击力", getAddP(getJid(), getId()));
    }

    @Override
    public Skill create(SkillInfo info, Number who, Number... nums) {
        return new Skill(info, who, new CopyOnWriteArrayList<>(nums), "天使真身") {

            private Integer c = 1;

            @Override
            public void before() {
                PersonInfo info1 = getInfo(who);
                long v = percentTo(info.getAddPercent(), info1.getAtt());
                addAttHasTime(who.longValue(), new HasTimeAdder(System.currentTimeMillis() + getDuration(getJid()), who.longValue(), v));
                eve();
            }

            @Override
            public void run() {
                super.run();
                try {
                    Thread.sleep(100000);
                    if (c++ > 12) {
                        eve();
                        run();
                    } else {
                        over();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            private void over() {
                setTips("武魂真身失效");
            }

            private void eve() {
                PersonInfo info1 = getInfo(who);
                Long v1 = percentTo(5, info1.getHll());
                putPerson(getInfo(who).addHl(v1));
            }
        };
    }
}
