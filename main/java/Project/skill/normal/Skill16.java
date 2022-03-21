package Project.skill.normal;

import Project.skill.SkillTemplate;
import io.github.kloping.mirai0.commons.Skill;
import io.github.kloping.mirai0.commons.SkillIntro;
import io.github.kloping.mirai0.commons.gameEntitys.*;
import io.github.kloping.mirai0.unitls.Tools.Tool;

import java.util.concurrent.CopyOnWriteArrayList;

import static Project.dataBases.GameDataBase.*;
import static Project.dataBases.skill.SkillDataBase.*;
import static Project.services.detailServices.GameSkillDetailService.*;

/**
 * @author github.kloping
 */
public class Skill16 extends SkillTemplate {
    

    public Skill16() {
        super(16);
    }

    @Override
    public SkillIntro.Type[] getTypes() {
        return  new SkillIntro.Type[]{SkillIntro.Type.Mark, SkillIntro.Type.Shd};
    }

    @Override
    public String getIntro() {
        return  String.format("为自己增加一个最大生命值的%s%%的临时护盾持续时间%s秒,##永久护盾和临时护盾不能叠加", getAddP(getJid(), getId()), getAddP(getJid(), getId()) / 5);
    }

    @Override
    public Skill create(SkillInfo info, Number who, Number... nums) {
        return  new Skill(info, who, new CopyOnWriteArrayList<>(nums), "临时护盾") {
            @Override
            public void before() {
                int b = info.getAddPercent();
                t = b / 5;
                long v2 = percentTo(b, getInfo(who).getHpL());
                addShield(who.longValue(), v2, t * 1000);
                setTips("作用于 " + Tool.At(who.longValue()));
            }

            long t;

            @Override
            public void run() {
                super.run();
            }
        };
    }
}
