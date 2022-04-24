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
import static Project.services.detailServices.GameSkillDetailService.getDuration;

/**
 * @author github.kloping
 */
public class Skill722 extends SkillTemplate {


    public Skill722() {
        super(722);
    }

    @Override
    public SkillIntro.Type[] getTypes() {
        return new SkillIntro.Type[]{SkillIntro.Type.WHZs, SkillIntro.Type.HasTime, SkillIntro.Type.Special, SkillIntro.Type.Add};
    }

    @Override
    public String getIntro() {
        return String.format("光明圣龙,增加%s%%的攻击,和恢复%s%%的 血量,魂力,精神力", getAddP(getJid(), getId()), getAddP(getJid(), getId()) / 2);
    }

    @Override
    public Skill create(SkillInfo info, Number who, Number... nums) {
        return new Skill(info, who, new CopyOnWriteArrayList<>(nums), "光明圣龙真身") {
            private Long v1;

            @Override
            public void before() {
                Long q = who.longValue();
                PersonInfo pInfo = getInfo(q);
                Long lon = pInfo.att();
                int b = info.getAddPercent();
                v1 = percentTo(b, lon);
                long v2 = percentTo(b / 2, pInfo.getHpL());
                long v3 = percentTo(b / 2, pInfo.getHll());
                long v4 = percentTo(b / 2, pInfo.getHjL());
                pInfo.addHp(v2);
                pInfo.addHl(v3);
                pInfo.addHj(v4);
                putPerson(pInfo);
                addAttHasTime(who.longValue(), new HasTimeAdder(System.currentTimeMillis() + getDuration(getJid()), who.longValue(), v1));
            }
        };
    }
}
