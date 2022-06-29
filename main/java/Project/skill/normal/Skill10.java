package Project.skill.normal;

import Project.skill.SkillTemplate;
import io.github.kloping.mirai0.commons.Skill;
import io.github.kloping.mirai0.commons.game.NormalTagPack;
import io.github.kloping.mirai0.commons.gameEntitys.SkillInfo;
import io.github.kloping.mirai0.unitls.Tools.Tool;

import java.util.concurrent.CopyOnWriteArrayList;

import static Project.dataBases.skill.SkillDataBase.TAG_MS;
import static Project.services.detailServices.GameSkillDetailService.addTagPack;
import static Project.services.detailServices.GameSkillDetailService.getAddP;

/**
 * @author github.kloping
 */
public class Skill10 extends SkillTemplate {

    public Skill10() {
        super(10);
    }


    @Override
    public String getIntro() {
        return String.format("在接下来的来%s秒内,免疫一次死亡", getAddP(getJid(), getId()));
    }

    @Override
    public Skill create(SkillInfo info, Number who, Number... nums) {
        return new Skill(info, who, new CopyOnWriteArrayList<>(nums), "免死") {
            @Override
            public void before() {
                setTips("作用于 " + Tool.at(who.longValue()));
            }

            @Override
            public void run() {
                super.run();
                addTagPack(new NormalTagPack(TAG_MS, info.getAddPercent() * 1000L).setQ(who.longValue()).setValue(1L));
            }
        };
    }
}
