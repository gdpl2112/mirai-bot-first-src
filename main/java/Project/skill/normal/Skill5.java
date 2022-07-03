package Project.skill.normal;

import Project.skill.SkillTemplate;
import io.github.kloping.mirai0.commons.PersonInfo;
import io.github.kloping.mirai0.commons.Skill;
import io.github.kloping.mirai0.commons.gameEntitys.SkillInfo;
import io.github.kloping.mirai0.unitls.Tools.Tool;

import java.util.concurrent.CopyOnWriteArrayList;

import static Project.dataBases.GameDataBase.exist;
import static Project.dataBases.GameDataBase.getInfo;
import static Project.dataBases.skill.SkillDataBase.HasTimeAdder;
import static Project.dataBases.skill.SkillDataBase.addAttHasTime;
import static Project.services.detailServices.GameSkillDetailService.*;
import static io.github.kloping.mirai0.commons.resouce_and_tool.CommonSource.percentTo;

/**
 * @author github.kloping
 */
public class Skill5 extends SkillTemplate {


    public Skill5() {
        super(5);
    }


    @Override
    public String getIntro() {
        return String.format("对指定几个人增加%s%%的攻击", getAddP(getJid(), getId()));
    }

    @Override
    public Skill create(SkillInfo info, Number who, Number... nums) {
        return new Skill(info, who, new CopyOnWriteArrayList<>(nums), "群体加攻击") {

            @Override
            public void before() {
                long v = percentTo(info.getAddPercent(), getPersonInfo().att());
                for (Long q : nearest(3, who.longValue(), nums)) {
                    PersonInfo pInfo = getInfo(q);
                    v = v > pInfo.att() ? pInfo.att() : v;
                    addAttHasTime(q, new HasTimeAdder(System.currentTimeMillis() + getDuration(getJid()), q.longValue(), v, getJid()));
                    setTips("作用于 " +  Tool.tool.at(q));
                }
            }
        };
    }
}
