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
import static io.github.kloping.mirai0.commons.resouce_and_tool.CommonSource.percentTo;

/**
 * @author github.kloping
 */
public class Skill726 extends SkillTemplate {


    public Skill726() {
        super(726);
    }

    @Override
    public SkillIntro.Type[] getTypes() {
        return new SkillIntro.Type[]{SkillIntro.Type.WHZs, SkillIntro.Type.HasTime, SkillIntro.Type.Special, SkillIntro.Type.Add, SkillIntro.Type.Mark};
    }

    @Override
    public String getIntro() {
        return String.format("海神,千载空悠,武魂真身时间内,增加%s%%的攻击,对有护盾的敌人造成额外的%s%%伤害,", getAddP(getJid(), getId()), getAddP(getJid(), getId()) / 8);
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
                NormalTagPack tagPack = new NormalTagPack(TAG_SHE,getDuration(getJid()));
                tagPack.setQ(who.longValue()).setValue((long) b/8).setEffected(false);
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
