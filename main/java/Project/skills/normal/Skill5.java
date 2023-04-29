package Project.skills.normal;

import Project.commons.gameEntitys.SkillInfo;
import Project.skills.SkillTemplate;
import io.github.kloping.mirai0.commons.PersonInfo;
import io.github.kloping.mirai0.commons.Skill;
import io.github.kloping.mirai0.unitls.Tools.Tool;

import java.util.concurrent.CopyOnWriteArrayList;

import static Project.commons.rt.CommonSource.percentTo;
import static Project.dataBases.GameDataBase.getInfo;
import static Project.dataBases.skill.SkillDataBase.HasTimeAdder;
import static Project.dataBases.skill.SkillDataBase.addAttHasTime;
import static Project.services.detailServices.GameSkillDetailService.getDuration;
import static Project.services.detailServices.GameSkillDetailService.nearest;

/**
 * @author github.kloping
 */
public class Skill5 extends SkillTemplate {


    public Skill5() {
        super(5);
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
                    setTips("作用于 " + Tool.INSTANCE.at(q));
                }
            }
        };
    }
}
