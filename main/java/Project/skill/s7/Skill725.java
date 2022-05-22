package Project.skill.s7;

import Project.skill.SkillTemplate;
import io.github.kloping.mirai0.commons.PersonInfo;
import io.github.kloping.mirai0.commons.Skill;
import io.github.kloping.mirai0.commons.SkillIntro;
import io.github.kloping.mirai0.commons.game.NormalTagPack;
import io.github.kloping.mirai0.commons.gameEntitys.SkillInfo;
import io.github.kloping.mirai0.commons.resouce_and_tool.CommonSource;

import java.util.concurrent.CopyOnWriteArrayList;

import static Project.dataBases.GameDataBase.getInfo;
import static Project.dataBases.GameDataBase.putPerson;
import static Project.dataBases.skill.SkillDataBase.*;
import static Project.services.detailServices.GameSkillDetailService.*;
import static io.github.kloping.mirai0.commons.resouce_and_tool.CommonSource.*;

/**
 * @author github.kloping
 */
public class Skill725 extends SkillTemplate {


    public Skill725() {
        super(725);
    }

    @Override
    public SkillIntro.Type[] getTypes() {
        return new SkillIntro.Type[]{SkillIntro.Type.WHZs, SkillIntro.Type.HasTime, SkillIntro.Type.Special, SkillIntro.Type.Add, SkillIntro.Type.Mark};
    }

    @Override
    public String getIntro() {
        return String.format("青龙真身,增加%s%%的攻击,并为自己增加%s%%的反甲效果", getAddP(getJid(), getId()), getAddP(getJid(), getId()));
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
                NormalTagPack tagPack = new NormalTagPack(TAG_FJ,getDuration(getJid()));
                tagPack.setQ(who.longValue()).setValue((long) b).setEffected(false);
                addTagPack(tagPack);
                addAttHasTime(who.longValue(), new HasTimeAdder(System.currentTimeMillis() + getDuration(getJid()), who.longValue(), v1));
            }

            @Override
            public void run() {
                super.run();
            }
        };
    }
}
