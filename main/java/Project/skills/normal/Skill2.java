package Project.skills.normal;

import Project.commons.gameEntitys.SkillInfo;
import Project.skills.SkillTemplate;
import io.github.kloping.mirai0.commons.Skill;
import io.github.kloping.mirai0.unitls.Tools.Tool;

import java.util.concurrent.CopyOnWriteArrayList;

import static Project.dataBases.GameDataBase.exist;
import static Project.services.detailServices.GameSkillDetailService.addHl;
import static Project.services.detailServices.GameSkillDetailService.oneNearest;

/**
 * @author github.kloping
 */
public class Skill2 extends SkillTemplate {


    public Skill2() {
        super(2);
    }

    @Override
    public Skill create(SkillInfo info, Number who, Number... nums) {
        return new Skill(info, who, new CopyOnWriteArrayList<>(nums), "单体加魂力技能") {
            @Override
            public void before() {
                Long q = oneNearest(who.longValue(), nums);
                if (!exist(q)) {
                    return;
                }
                addHl(who, q, info.getAddPercent());
                setTips("作用于 " + Tool.INSTANCE.at(q));
            }
        };
    }
}
