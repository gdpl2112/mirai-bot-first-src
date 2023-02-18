package Project.skills.s8;

import Project.skills.SkillTemplate;
import io.github.kloping.mirai0.commons.Skill;
import io.github.kloping.mirai0.commons.gameEntitys.SkillInfo;
import io.github.kloping.mirai0.unitls.Tools.Tool;

import java.util.concurrent.CopyOnWriteArrayList;

import static Project.services.detailServices.GameSkillDetailService.*;
import static io.github.kloping.mirai0.commons.resouce_and_tool.CommonSource.percentTo;

/**
 * @author github.kloping
 */
public class Skill8270 extends SkillTemplate {

    public Skill8270() {
        super(8270);
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
                long v = percentTo(info.getAddPercent(), getPersonInfo().getHpL());
                for (long qid : nearest(5, who.longValue(), nums)) {
                    addHp(who, qid, info.getAddPercent());
                    addShield(qid, v);
                    setTips("作用于:" + Tool.INSTANCE.at(who.longValue()));
                }
            }
        };
    }
}
