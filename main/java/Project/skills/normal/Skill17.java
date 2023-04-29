package Project.skills.normal;

import Project.commons.gameEntitys.SkillInfo;
import Project.skills.SkillTemplate;
import io.github.kloping.mirai0.commons.Skill;
import io.github.kloping.mirai0.commons.game.NormalTagPack;
import io.github.kloping.mirai0.unitls.Tools.Tool;

import java.util.concurrent.CopyOnWriteArrayList;

import static Project.dataBases.skill.SkillDataBase.TAG_MY;
import static Project.services.detailServices.GameSkillDetailService.addTagPack;

/**
 * @author github.kloping
 */
public class Skill17 extends SkillTemplate {


    public Skill17() {
        super(17);
    }


    @Override
    public Skill create(SkillInfo info, Number who, Number... nums) {
        return new Skill(info, who, new CopyOnWriteArrayList<>(nums), "玄玉手") {
            @Override
            public void before() {
                NormalTagPack pack = new NormalTagPack(TAG_MY, info.getAddPercent() * 1000);
                pack.setQ(who.longValue()).setValue(1L);
                addTagPack(pack);
                setTips("作用于 " + Tool.INSTANCE.at(who.longValue()));
            }

            @Override
            public void run() {
                super.run();
            }
        };
    }
}
