package Project.skills.s50;

import Project.commons.gameEntitys.SkillInfo;
import Project.skills.SkillTemplate;
import Project.utils.VelocityUtils;
import io.github.kloping.mirai0.commons.PersonInfo;
import io.github.kloping.mirai0.commons.Skill;
import io.github.kloping.number.NumberUtils;

import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import static Project.dataBases.skill.SkillDataBase.getSkillInfo;
import static Project.dataBases.skill.SkillDataBase.updateSkillInfo;
import static Project.services.detailServices.GameSkillDetailService.getAddP;

/**
 * @author github.kloping
 */
public class Skill5007 extends SkillTemplate {
    public Skill5007() {
        super(5007);
    }

    @Override
    public String getIntro() {
        return VelocityUtils.getTemplateToString(String.format("skill/%s.intro", getJid()), getAddP(getJid(), getId()), getAddP(getJid(), getId()));
    }

    @Override
    public Skill create(SkillInfo info, Number who, Number... nums) {
        return new Skill(info, who, new CopyOnWriteArrayList<>(nums), "真身") {
            @Override
            public void before() {
                PersonInfo pinfo = getPersonInfo();
                long v = NumberUtils.percentTo(info.getAddPercent(), pinfo.getHll() - pinfo.getHl());
                pinfo.addHl(v).apply();
                Map<Integer, SkillInfo> infos = getSkillInfo(who.longValue());
                for (SkillInfo value : infos.values()) {
                    long t = NumberUtils.percentTo(info.getAddPercent(), value.getTimeL());
                    value.setTime(value.getTime() - t);
                    updateSkillInfo(value);
                }
            }
        };
    }
}
