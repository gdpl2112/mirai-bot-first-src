package Project.skill.normal;

import Project.skill.SkillTemplate;
import io.github.kloping.mirai0.commons.PersonInfo;
import io.github.kloping.mirai0.commons.Skill;
import io.github.kloping.mirai0.commons.SkillIntro;
import io.github.kloping.mirai0.commons.gameEntitys.SkillInfo;
import io.github.kloping.mirai0.unitls.Tools.Tool;

import java.util.concurrent.CopyOnWriteArrayList;

import static Project.dataBases.GameDataBase.exist;
import static Project.dataBases.GameDataBase.getInfo;
import static Project.dataBases.skill.SkillDataBase.*;
import static Project.services.detailServices.GameSkillDetailService.*;

/**
 * @author github.kloping
 */
public class Skill4 extends SkillTemplate {


    public Skill4() {
        super(4);
    }

    @Override
    public SkillIntro.Type[] getTypes() {
        return WhTypes.T4;
    }

    @Override
    public String getIntro() {
        return String.format("对指定一个人增加%s%%的攻击", getAddP(getJid(), getId()));
    }

    @Override
    public Skill create(SkillInfo info, Number who, Number... nums) {
        return new Skill(info, who, new CopyOnWriteArrayList<>(nums), "单体加攻击技能") {
            @Override
            public void before() {
                Long q = oneNearest(who, nums);
                if (!exist(q)) {
                    return;
                }
                PersonInfo pInfo = getInfo(q);
                Long lon = pInfo.att();
                long v = percentTo(info.getAddPercent(), lon);
                v = v > pInfo.att() ? pInfo.att() : v;
                addAttHasTime(who.longValue(), new HasTimeAdder(System.currentTimeMillis() + getDuration(getJid()), who.longValue(), v));
                setTips("作用于 " + Tool.at(q));
            }
        };
    }
}
