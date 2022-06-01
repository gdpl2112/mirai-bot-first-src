package Project.skill.s8;

import Project.skill.SkillTemplate;
import io.github.kloping.mirai0.commons.Skill;
import io.github.kloping.mirai0.commons.game.NormalTagPack;
import io.github.kloping.mirai0.commons.gameEntitys.SkillInfo;

import java.util.concurrent.CopyOnWriteArrayList;

import static Project.dataBases.skill.SkillDataBase.TAG_FJ;
import static Project.services.detailServices.GameSkillDetailService.*;
import static io.github.kloping.mirai0.commons.resouce_and_tool.CommonSource.toPercent;

/**
 * @author github.kloping
 */
public class Skill8250 extends SkillTemplate {

    public static final int V0 = 3;


    public Skill8250() {
        super(8250);
    }

    @Override
    public String getIntro() {
        return String.format("青龙第八魂技,每损失1%%的生命值增加1%%的反甲效果最多%s%%,最少%s%%",
                getAddP(getJid(), getId()) * V0,
                getAddP(getJid(), getId())
        );
    }

    @Override
    public Skill create(SkillInfo info, Number who, Number... nums) {
        return new Skill(info, who, new CopyOnWriteArrayList<>(nums), "") {

            @Override
            public void before() {

            }

            @Override
            public void run() {
                super.run();
                long hpl = getPersonInfo().getHpL();
                long hp = getPersonInfo().getHp();
                int b0 = toPercent(hp, hpl);
                int b = 100 - b0;
                int max = info.getAddPercent() * V0;
                b = b < info.getAddPercent() ? info.getAddPercent() : b > max ? max : b;
                setTips("增加" + b + "%");
                NormalTagPack tagPack = new NormalTagPack(TAG_FJ, getDuration(getJid()));
                tagPack.setQ(who.longValue()).setValue((long) b).setEffected(false);
                addTagPack(tagPack);
            }
        };
    }
}
