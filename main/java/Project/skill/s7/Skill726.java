package Project.skill.s7;

import Project.e0.VelocityUtils;
import Project.skill.SkillTemplate;
import io.github.kloping.mirai0.commons.Skill;
import io.github.kloping.mirai0.commons.game.NormalTagPack;
import io.github.kloping.mirai0.commons.gameEntitys.SkillInfo;

import java.util.concurrent.CopyOnWriteArrayList;

import static Project.dataBases.skill.SkillDataBase.*;
import static Project.services.detailServices.GameSkillDetailService.*;
import static io.github.kloping.mirai0.commons.resouce_and_tool.CommonSource.percentTo;

/**
 * @author github.kloping
 */
public class Skill726 extends SkillTemplate {

    public Skill726() {
        super(726);
    }

    @Override
    public String getIntro() {
        return VelocityUtils.getTemplateToString(String.format("skill/%s.intro", getJid()), getAddP(getJid(), getId()),getAddP(getJid(), getId())/8);
    }

    @Override
    public Skill create(SkillInfo info, Number who, Number... nums) {
        return new Skill(info, who, new CopyOnWriteArrayList<>(nums), "海神") {
            private long v1;

            @Override
            public void before() {
                Long q = who.longValue();
                Long lon = getPersonInfo().att();
                int b = info.getAddPercent();
                v1 = percentTo(b, lon);
                NormalTagPack tagPack = new NormalTagPack(TAG_SHE, getDuration(getJid()));
                tagPack.setQ(who.longValue()).setValue((long) b / 8);
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
