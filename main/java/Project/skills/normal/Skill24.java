package Project.skills.normal;

import Project.commons.gameEntitys.SkillInfo;
import Project.skills.SkillTemplate;
import Project.utils.VelocityUtils;
import io.github.kloping.mirai0.commons.Skill;
import Project.utils.Tools.Tool;

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

    public String getIntro() {
        return VelocityUtils.getTemplateToString(String.format("skill/%s.intro", getJid()), Tool.INSTANCE.device(getAddP(getJid(), getId()), 1000, 1));
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
