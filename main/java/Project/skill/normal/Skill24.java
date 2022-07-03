package Project.skill.normal;

import Project.skill.SkillTemplate;
import io.github.kloping.mirai0.commons.Skill;
import io.github.kloping.mirai0.commons.gameEntitys.SkillInfo;
import io.github.kloping.mirai0.unitls.Tools.Tool;

import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import static Project.dataBases.skill.SkillDataBase.getSkillInfo;
import static Project.dataBases.skill.SkillDataBase.updateSkillInfo;
import static Project.services.detailServices.GameSkillDetailService.getAddP;
import static Project.services.detailServices.GameSkillDetailService.nearest;

/**
 * @author github.kloping
 */
public class Skill24 extends SkillTemplate {

    public Skill24() {
        super(24);
    }


    @Override
    public String getIntro() {
        return String.format("减少指定人对应该魂技的位置的魂技%s秒的冷却", Tool.tool.device(getAddP(getJid(), getId()), 1000, 1));
    }

    @Override
    public Skill create(SkillInfo info, Number who, Number... nums) {
        return new Skill(info, who, new CopyOnWriteArrayList<>(nums), "减cd") {
            @Override
            public void before() {
                long q = nearest(1, who.longValue(), nums)[0];
                Map<Integer, SkillInfo> infos = getSkillInfo(q);
                if (infos.containsKey(getSt())) {
                    SkillInfo skillInfo = infos.get(getSt());
                    skillInfo.setTime(skillInfo.getTime() - info.getAddPercent());
                    infos.put(getSt(), skillInfo);
                    updateSkillInfo(info);
                    setTips("作用于:" + q);
                } else {
                    setTips("其魂技未解锁");
                }
            }
        };
    }
}
