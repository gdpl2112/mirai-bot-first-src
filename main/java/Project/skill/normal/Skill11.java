package Project.skill.normal;

import Project.skill.SkillTemplate;
import io.github.kloping.mirai0.commons.Skill;
import io.github.kloping.mirai0.commons.SkillIntro;
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
    public SkillIntro.Type[] getTypes() {
        return new SkillIntro.Type[]{SkillIntro.Type.Special, SkillIntro.Type.Add, SkillIntro.Type.Edd, SkillIntro.Type.HasTime};
    }

    @Override
    public String getIntro() {
        return String.format("狂热,让指定一人伤害变为真实伤害持续%s%%秒", getAddP(getJid(), getId()));
    }

    @Override
    public Skill create(SkillInfo info, Number who, Number... nums) {
        return new Skill(info, who, new CopyOnWriteArrayList<>(nums), "狂热") {
            private Long q;

            @Override
            public void before() {
                q = oneNearest(who, nums);
                putPerson(getInfo(q).addTag(TAG_TRUE, 1));
                setTips("作用于 " + Tool.at(q));
            }

            @Override
            public void run() {
                super.run();
                try {
                    Thread.sleep(getDuration(getJid()));
                    putPerson(getInfo(q).eddTag(TAG_TRUE, 1));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }
}
