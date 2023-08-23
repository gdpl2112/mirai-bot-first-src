package Project.skills.normal;

import Project.aSpring.dao.SkillInfo;
import Project.skills.SkillTemplate;
import io.github.kloping.mirai0.commons.Skill;
import Project.utils.Tools.Tool;

import java.util.concurrent.CopyOnWriteArrayList;

import static Project.dataBases.GameDataBase.getInfo;
import static Project.dataBases.skill.SkillDataBase.TAG_XX;
import static Project.services.detailServices.GameSkillDetailService.getDuration;

/**
 * @author github.kloping
 */
public class Skill6 extends SkillTemplate {


    public Skill6() {
        super(6);
    }


    @Override
    public Skill create(SkillInfo info, Number who, Number... nums) {
        return new Skill(info, who, new CopyOnWriteArrayList<>(nums), "吸血") {
            @Override
            public void before() {
                (getInfo(who).addTag(TAG_XX, info.getAddPercent(), getDuration(getJid()))).apply();
                setTips("作用于 " + Tool.INSTANCE.at(who.longValue()));
            }

            @Override
            public void run() {
                super.run();
            }
        };
    }
}
