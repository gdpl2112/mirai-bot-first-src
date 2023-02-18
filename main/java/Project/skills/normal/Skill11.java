package Project.skills.normal;

import Project.skills.SkillTemplate;
import io.github.kloping.mirai0.commons.Skill;
import io.github.kloping.mirai0.commons.gameEntitys.SkillInfo;
import io.github.kloping.mirai0.unitls.Tools.Tool;

import java.util.concurrent.CopyOnWriteArrayList;

import static Project.dataBases.GameDataBase.getInfo;
import static Project.dataBases.GameDataBase.putPerson;
import static Project.dataBases.skill.SkillDataBase.TAG_TRUE;
import static Project.services.detailServices.GameSkillDetailService.*;

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
                putPerson(getInfo(q).addTag(TAG_TRUE, 1, getDuration(getJid())));
                setTips("作用于 " +  Tool.INSTANCE.at(q));
            }

            @Override
            public void run() {
                super.run();
            }
        };
    }
}
