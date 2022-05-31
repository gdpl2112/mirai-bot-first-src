package Project.skill.s7;

import Project.skill.SkillTemplate;
import io.github.kloping.mirai0.commons.PersonInfo;
import io.github.kloping.mirai0.commons.Skill;
import io.github.kloping.mirai0.commons.SkillIntro;
import io.github.kloping.mirai0.commons.gameEntitys.SkillInfo;
import io.github.kloping.mirai0.commons.resouce_and_tool.CommonSource;

import java.util.concurrent.CopyOnWriteArrayList;

import static Project.dataBases.GameDataBase.getInfo;
import static Project.dataBases.GameDataBase.putPerson;
import static Project.dataBases.skill.SkillDataBase.HasTimeAdder;
import static Project.dataBases.skill.SkillDataBase.addAttHasTime;
import static Project.services.detailServices.GameSkillDetailService.*;

/**
 * @author github.kloping
 */
public class Skill705 extends SkillTemplate {


    public Skill705() {
        super(705);
    }

    @Override
    public SkillIntro.Type[] getTypes() {
        return WhTypes.T72;
    }

    @Override
    public String getIntro() {
        return String.format("强大的蓝银皇,增加%s%%的攻击力,拥有强大的生命力,每10秒恢复%s%%的生命值", getAddP(getJid(), getId()) * 4, getAddP(getJid(), getId()));
    }

    @Override
    public Skill create(SkillInfo info, Number who, Number... nums) {
        return new Skill(info, who, new CopyOnWriteArrayList<>(nums), "蓝银皇真身") {

            private int c = 1;

            @Override
            public void before() {
                Long q = who.longValue();
                long v = CommonSource.percentTo(info.getAddPercent() * 4, getInfo(q).att());
                addAttHasTime(who.longValue(), new HasTimeAdder(System.currentTimeMillis() + getDuration(getJid()),  who.longValue(), v, getJid()));
                eve();
            }

            @Override
            public void run() {
                super.run();
                try {
                    if (c++ >= 12) {
                        setTips("武魂真身失效");
                        return;
                    }
                    Thread.sleep(10000L);
                    eve();
                    run();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            public void eve() {
                PersonInfo pInfo = getInfo(who);
                long v = CommonSource.percentTo(info.getAddPercent(), pInfo.getHp());
                putPerson(getInfo(who).addHp(v));
            }
        };
    }
}
