package Project.skill.s7;

import Project.skill.SkillTemplate;
import io.github.kloping.mirai0.commons.PersonInfo;
import io.github.kloping.mirai0.commons.Skill;
import io.github.kloping.mirai0.commons.gameEntitys.SkillInfo;
import io.github.kloping.mirai0.commons.resouce_and_tool.CommonSource;

import java.util.concurrent.CopyOnWriteArrayList;

import static Project.dataBases.GameDataBase.getInfo;
import static Project.dataBases.GameDataBase.putPerson;
import static Project.dataBases.skill.SkillDataBase.*;
import static Project.services.detailServices.GameSkillDetailService.getAddP;
import static Project.services.detailServices.GameSkillDetailService.getDuration;

/**
 * @author github.kloping
 */
public class Skill723 extends SkillTemplate {


    public Skill723() {
        super(723);
    }


    @Override
    public String getIntro() {
        return String.format("黑暗圣龙,增加%s%%的攻击,和恢复%s%%的 精神力,并增加 %s%%的吸血", getAddP(getJid(), getId()), getAddP(getJid(), getId()) / 2, getAddP(getJid(), getId()) / 5);
    }

    @Override
    public Skill create(SkillInfo info, Number who, Number... nums) {
        return new Skill(info, who, new CopyOnWriteArrayList<>(nums), "黑暗圣龙真身") {
            private Long v1;

            @Override
            public void before() {
                Long q = who.longValue();
                PersonInfo pInfo = getInfo(q);
                Long lon = pInfo.att();
                int b = info.getAddPercent();
                v1 = CommonSource.percentTo(b, lon);
                long v4 = CommonSource.percentTo(b / 2, pInfo.getHjL());
                pInfo.addHj(v4);
                pInfo.addTag(TAG_XX, b / 5, getDuration(getJid()));
                putPerson(pInfo);
                addAttHasTime(who.longValue(), new HasTimeAdder(System.currentTimeMillis() + getDuration(getJid()), who.longValue(), v1, getJid()));
            }
        };
    }
}
