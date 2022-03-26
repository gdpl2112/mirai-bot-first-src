package Project.skill.s7;

import Project.skill.SkillTemplate;
import io.github.kloping.mirai0.commons.PersonInfo;
import io.github.kloping.mirai0.commons.Skill;
import io.github.kloping.mirai0.commons.SkillIntro;
import io.github.kloping.mirai0.commons.gameEntitys.SkillInfo;

import java.util.concurrent.CopyOnWriteArrayList;

import static Project.dataBases.GameDataBase.*;
import static Project.dataBases.skill.SkillDataBase.*;
import static Project.services.detailServices.GameSkillDetailService.WhTypes;
import static Project.services.detailServices.GameSkillDetailService.getAddP;

/**
 * @author github.kloping
 */
public class Skill75 extends SkillTemplate {


    public Skill75() {
        super(75);
    }

    @Override
    public SkillIntro.Type[] getTypes() {
        return WhTypes.T72;
    }

    @Override
    public String getIntro() {
        return String.format("强大的蓝银皇,增加%s的攻击力,拥有强大的生命力,每%s秒恢复%s%%的生命值", getAddP(getJid(), getId()) * 4, (t75 / 1000), getAddP(getJid(), getId()));
    }

    @Override
    public Skill create(SkillInfo info, Number who, Number... nums) {
        return new Skill(info, who, new CopyOnWriteArrayList<>(nums), "蓝银皇真身") {

            private int c = 1;

            @Override
            public void before() {
                Long q = who.longValue();
                if (!exist(q)) {
                    return;
                }
                PersonInfo pInfo = getInfo(q);
                Long lon = pInfo.getAtt();
                long v = percentTo(info.getAddPercent() * 4, lon);
                addAttHasTime(who.longValue(), new HasTimeAdder(System.currentTimeMillis() + t75, who.longValue(), v));
                putPerson(pInfo);
                eve();
            }

            @Override
            public void run() {
                super.run();
                try {
                    if (c++ > t75C) {
                        setTips("武魂真身失效");
                        return;
                    }
                    Thread.sleep(t75);
                    eve();
                    run();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            public void eve() {
                PersonInfo info_ = getInfo(who);
                long v = percentTo(info.getAddPercent(), info_.getHp());
                putPerson(getInfo(who).addHp(v));
            }
        };
    }
}
