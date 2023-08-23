package Project.skills.s7;

import Project.aSpring.dao.SkillInfo;
import Project.commons.rt.CommonSource;
import Project.skills.SkillTemplate;
import Project.aSpring.dao.PersonInfo;
import io.github.kloping.mirai0.commons.Skill;

import java.util.concurrent.CopyOnWriteArrayList;

import static Project.dataBases.GameDataBase.getInfo;
import static Project.dataBases.skill.SkillDataBase.*;
import static Project.services.detailServices.GameSkillDetailService.getDuration;

/**
 * @author github.kloping
 */
public class Skill729 extends SkillTemplate {


    public Skill729() {
        super(729);
    }


    @Override
    public Skill create(SkillInfo info, Number who, Number... nums) {
        return new Skill(info, who, new CopyOnWriteArrayList<>(nums), "杀神昊天锤") {
            @Override
            public void before() {
                Long q = who.longValue();
                PersonInfo pInfo = getInfo(q);
                Long lon = pInfo.att();
                int b = info.getAddPercent();
                SkillInfo i1 = getSkillInfo(who.longValue()).get(1);
                int id = i1.getId();
                i1.setTime(System.currentTimeMillis() + 1000 * 60 * 30);
                updateSkillInfo(i1);
                id -= 200;
                b += id;
                long v = CommonSource.percentTo(b, lon);
                addAttHasTime(who.longValue(), new HasTimeAdder(System.currentTimeMillis() + getDuration(getJid()), who.longValue(), v, getJid()));
                setTips(String.format("增加%s%%(%s)攻击", b, CommonSource.percentTo(b, getInfo(who).att())));
            }
        };
    }
}
