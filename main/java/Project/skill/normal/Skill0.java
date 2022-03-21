package Project.skill.normal;

import Project.skill.SkillTemplate;
import io.github.kloping.mirai0.commons.Skill;
import io.github.kloping.mirai0.commons.gameEntitys.SkillInfo;
import io.github.kloping.mirai0.commons.SkillIntro;
import io.github.kloping.mirai0.unitls.Tools.Tool;

import java.util.concurrent.CopyOnWriteArrayList;

import static Project.dataBases.GameDataBase.exist;
import static Project.services.detailServices.GameSkillDetailService.*;

/**
 * @author github.kloping
 */
public class Skill0 extends SkillTemplate {

    public Skill0() {
        super(0);
    }

    @Override
    public SkillIntro.Type[] getTypes() {
        return WhTypes.T0;
    }

    @Override
    public String getIntro() {
        return String.format("对指定一个人恢复%s%%的血量", getAddP(getJid(), getId()));
    }

    @Override
    public Skill create(SkillInfo info, Number who, Number... nums) {
        return new Skill(info, who, new CopyOnWriteArrayList<>(nums), "单体加血技能") {
            @Override
            public void before() {
                Long q = oneNearest(who.longValue(), nums);
                if (!exist(q)) {
                    return;
                }
                addHp(who.longValue(), q, info.getAddPercent());
                setTips("作用于 " + Tool.At(q));
            }
        };
    }
}
