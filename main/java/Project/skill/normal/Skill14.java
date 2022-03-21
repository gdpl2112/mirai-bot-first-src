package Project.skill.normal;

import Project.skill.SkillTemplate;
import io.github.kloping.mirai0.commons.Skill;
import io.github.kloping.mirai0.commons.SkillIntro;
import io.github.kloping.mirai0.commons.gameEntitys.*;
import io.github.kloping.mirai0.unitls.Tools.Tool;

import java.util.concurrent.CopyOnWriteArrayList;

import static Project.dataBases.GameDataBase.*;
import static Project.dataBases.skill.SkillDataBase.*;

/**
 * @author github.kloping
 */
public class Skill14 extends SkillTemplate {
    

    public Skill14() {
        super(14);
    }

    @Override
    public SkillIntro.Type[] getTypes() {
        return  new SkillIntro.Type[]{SkillIntro.Type.Special, SkillIntro.Type.Mark};
    }

    @Override
    public String getIntro() {
        return  String.format("令指定一个人无法躲避下次的攻击");
    }

    @Override
    public Skill create(SkillInfo info, Number who, Number... nums) {
        return  new Skill(info, who, new CopyOnWriteArrayList<>(nums), "无法躲避") {
            private Long q;

            @Override
            public void before() {
                if (nums.length < 1) {
                    return;
                }
                q = nums[0].longValue();
                putPerson(getInfo(q).addTag(TAG_CANT_HIDE, 0));
                setTips("作用于 " + Tool.At(q));
            }
        };
    }
}
