package Project.skills.s7;

import Project.utils.VelocityUtils;
import Project.skills.SkillTemplate;
import io.github.kloping.mirai0.commons.Skill;
import io.github.kloping.mirai0.commons.game.NormalTagPack;
import Project.commons.gameEntitys.SkillInfo;

import java.util.concurrent.CopyOnWriteArrayList;

import static Project.dataBases.skill.SkillDataBase.*;
import static Project.services.detailServices.GameSkillDetailService.*;
import static Project.commons.resouce_and_tool.CommonSource.percentTo;

/**
 * @author github.kloping
 */
public class Skill725 extends SkillTemplate {


    public static final int F0 = 3;

    public Skill725() {
        super(725);
    }

    public String getIntro() {
        return VelocityUtils.getTemplateToString(String.format("skill/%s.intro", getJid()),
                getAddP(getJid(), getId()), getAddP(getJid(), getId()) / F0);
    }

    @Override
    public Skill create(SkillInfo info, Number who, Number... nums) {
        return new Skill(info, who, new CopyOnWriteArrayList<>(nums), "青龙真身") {
            long v1;

            @Override
            public void before() {
                Long q = who.longValue();
                Long lon = getPersonInfo().att();
                int b = info.getAddPercent();
                v1 = percentTo(b, lon);
                NormalTagPack tagPack = new NormalTagPack(TAG_FJ, getDuration(getJid()));
                tagPack.setQ(who.longValue()).setValue((long) b / F0);
                addTagPack(tagPack);
                addAttHasTime(who.longValue(), new HasTimeAdder(System.currentTimeMillis() + getDuration(getJid()), who.longValue(), v1, getJid()));
            }

            @Override
            public void run() {
                super.run();
            }
        };
    }
}
