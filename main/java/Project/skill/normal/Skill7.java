package Project.skill.normal;

import Project.skill.SkillTemplate;
import io.github.kloping.mirai0.commons.Skill;
import io.github.kloping.mirai0.commons.SkillIntro;
import io.github.kloping.mirai0.commons.gameEntitys.SkillInfo;
import io.github.kloping.mirai0.unitls.Tools.Tool;

import java.util.concurrent.CopyOnWriteArrayList;

import static Project.dataBases.GameDataBase.getInfo;
import static Project.dataBases.GameDataBase.putPerson;
import static Project.dataBases.skill.SkillDataBase.TAG_FJ;
import static Project.dataBases.skill.SkillDataBase.t6;
import static Project.services.detailServices.GameSkillDetailService.WhTypes;
import static Project.services.detailServices.GameSkillDetailService.getAddP;

/**
 * @author github.kloping
 */
public class Skill7 extends SkillTemplate {


    public Skill7() {
        super(7);
    }

    @Override
    public SkillIntro.Type[] getTypes() {
        return WhTypes.T6;
    }

    @Override
    public String getIntro() {
        return String.format("给予指定一个人反甲,在接下来的一段时间内,任何攻击者,将额外受到攻击的%s%%的伤害(不可叠加))", getAddP(getJid(), getId()));
    }

    @Override
    public Skill create(SkillInfo info, Number who, Number... nums) {
        return new Skill(info, who, new CopyOnWriteArrayList<>(nums), "反甲") {
            @Override
            public void before() {
                putPerson(getInfo(who).addTag(TAG_FJ, info.getAddPercent()));
                setTips("作用于 " + Tool.At(who.longValue()));
            }

            @Override
            public void run() {
                super.run();
                try {
                    Thread.sleep(t6);
                    putPerson(getInfo(who).eddTag(TAG_FJ, info.getAddPercent()));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
    }
}
