package Project.skills.s7;

import Project.commons.gameEntitys.SkillInfo;
import Project.commons.rt.CommonSource;
import Project.skills.SkillTemplate;
import Project.utils.VelocityUtils;
import io.github.kloping.mirai0.commons.PersonInfo;
import io.github.kloping.mirai0.commons.Skill;

import java.util.concurrent.CopyOnWriteArrayList;

import static Project.dataBases.GameDataBase.getInfo;
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

    public String getIntro() {
        return VelocityUtils.getTemplateToString(String.format("skill/%s.intro", getJid()),
                getAddP(getJid(), getId()), getAddP(getJid(), getId()) / 2, getAddP(getJid(), getId()) / 5);
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
                (pInfo).apply();
                addAttHasTime(who.longValue(), new HasTimeAdder(System.currentTimeMillis() + getDuration(getJid()), who.longValue(), v1, getJid()));
            }
        };
    }
}
