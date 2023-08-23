package Project.skills.normal;

import Project.aSpring.dao.SkillInfo;
import Project.skills.SkillTemplate;
import io.github.kloping.mirai0.commons.Skill;
import Project.utils.Tools.Tool;

import java.util.concurrent.CopyOnWriteArrayList;

import static Project.dataBases.GameDataBase.getInfo;
import static Project.dataBases.skill.SkillDataBase.TAG_TRUE;
import static Project.services.detailServices.GameSkillDetailService.getDuration;
import static Project.services.detailServices.GameSkillDetailService.oneNearest;

/**
 * @author github.kloping
 */
public class Skill11 extends SkillTemplate {


    public Skill11() {
        super(11);
    }


    @Override
    public Skill create(SkillInfo info, Number who, Number... nums) {
        return new Skill(info, who, new CopyOnWriteArrayList<>(nums), "狂热") {
            private Long q;

            @Override
            public void before() {
                q = oneNearest(who, nums);
                (getInfo(q).addTag(TAG_TRUE, 1, getDuration(getJid()))).apply();
                setTips("作用于 " + Tool.INSTANCE.at(q));
            }

            @Override
            public void run() {
                super.run();
            }
        };
    }
}
