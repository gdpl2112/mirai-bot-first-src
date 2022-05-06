package Project.skill.normal;

import Project.skill.SkillTemplate;
import io.github.kloping.mirai0.commons.Skill;
import io.github.kloping.mirai0.commons.SkillIntro;
import io.github.kloping.mirai0.commons.gameEntitys.SkillInfo;
import io.github.kloping.mirai0.commons.resouce_and_tool.CommonSource;
import io.github.kloping.mirai0.unitls.Tools.Tool;

import java.util.concurrent.CopyOnWriteArrayList;

import static Project.dataBases.GameDataBase.getInfo;
import static Project.services.detailServices.GameSkillDetailService.addShield;
import static Project.services.detailServices.GameSkillDetailService.getAddP;

/**
 * @author github.kloping
 */
public class Skill16 extends SkillTemplate {


    public Skill16() {
        super(16);
    }

    @Override
    public SkillIntro.Type[] getTypes() {
        return new SkillIntro.Type[]{SkillIntro.Type.Mark, SkillIntro.Type.Shd};
    }

    @Override
    public String getIntro() {
        return String.format("为自己增加一个最大生命值的%s%%的临时护盾持续时间%s秒", getAddP(getJid(), getId()), getAddP(getJid(), getId()) / 5);
    }

    @Override
    public Skill create(SkillInfo info, Number who, Number... nums) {
        return new Skill(info, who, new CopyOnWriteArrayList<>(nums), "临时护盾") {
            @Override
            public void before() {
                int b = info.getAddPercent();
                long t = b / 5;
                long v2 = CommonSource.percentTo(b, getInfo(who).getHpL());
                addShield(who.longValue(), v2, t * 1000);
                setTips("作用于 " + Tool.at(who.longValue()));
            }

            @Override
            public void run() {
                super.run();
            }
        };
    }
}
