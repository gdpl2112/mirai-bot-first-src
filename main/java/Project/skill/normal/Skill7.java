package Project.skill.normal;

import Project.skill.SkillTemplate;
import io.github.kloping.mirai0.commons.Skill;
import io.github.kloping.mirai0.commons.gameEntitys.SkillInfo;
import io.github.kloping.mirai0.unitls.Tools.Tool;

import java.util.concurrent.CopyOnWriteArrayList;

import static Project.dataBases.GameDataBase.*;
import static Project.dataBases.skill.SkillDataBase.TAG_FJ;
import static Project.services.detailServices.GameSkillDetailService.*;

/**
 * @author github.kloping
 */
public class Skill7 extends SkillTemplate {


    public Skill7() {
        super(7);
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
                Long q = oneNearest(who.longValue(), nums);
                if (q < 0 || !exist(q)) {
                    return;
                }
                putPerson(getInfo(q).addTag(TAG_FJ, info.getAddPercent(), getDuration(getJid())));
                setTips("作用于 " + Tool.tool.at(who.longValue()));
            }

            @Override
            public void run() {
                super.run();
            }
        };
    }
}
