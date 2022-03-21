package Project.skill.s7;

import Project.skill.SkillTemplate;
import io.github.kloping.mirai0.commons.Skill;
import io.github.kloping.mirai0.commons.SkillIntro;
import io.github.kloping.mirai0.commons.gameEntitys.*;

import java.util.concurrent.CopyOnWriteArrayList;

import static Project.dataBases.skill.SkillDataBase.*;
import static Project.services.detailServices.GameSkillDetailService.*;

/**
 * @author github.kloping
 */
public class Skill719 extends SkillTemplate {
    

    public Skill719() {
        super(719);
    }

    @Override
    public SkillIntro.Type[] getTypes() {
        return  WhTypes.T72;
    }

    @Override
    public String getIntro() {
        return  String.format("蓝银草,释放蓝银草,每%s秒恢复%s%%的生命值", (t719 / 1000), getAddP(getJid(), getId()));
    }

    @Override
    public Skill create(SkillInfo info, Number who, Number... nums) {
        return  new Skill(info, who, new CopyOnWriteArrayList<>(nums), "蓝银草真身") {

            @Override
            public void before() {
                eve();
            }

            private int c = 1;

            @Override
            public void run() {
                super.run();
                try {
                    if (c++ > t79C) {
                        setTips("武魂真身失效");
                        return;
                    }
                    Thread.sleep(t79);
                    eve();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            public void eve() {
                addHp(who, who.longValue(), info.getAddPercent());
            }
        };
    }
}
