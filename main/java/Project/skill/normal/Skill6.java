package Project.skill.normal;

import Project.skill.SkillTemplate;
import io.github.kloping.mirai0.commons.Skill;
import io.github.kloping.mirai0.commons.SkillIntro;
import io.github.kloping.mirai0.commons.gameEntitys.SkillInfo;
import io.github.kloping.mirai0.unitls.Tools.Tool;

import java.util.concurrent.CopyOnWriteArrayList;

import static Project.dataBases.GameDataBase.getInfo;
import static Project.dataBases.GameDataBase.putPerson;
import static Project.dataBases.skill.SkillDataBase.TAG_XX;
import static Project.dataBases.skill.SkillDataBase.t6;
import static Project.services.detailServices.GameSkillDetailService.WhTypes;
import static Project.services.detailServices.GameSkillDetailService.getAddP;

/**
 * @author github.kloping
 */
public class Skill6 extends SkillTemplate {


    public Skill6() {
        super(6);
    }

    @Override
    public SkillIntro.Type[] getTypes() {
        return WhTypes.T6;
    }

    @Override
    public String getIntro() {
        return String.format("在接下来的一段时间内,攻击任何,将额外恢复攻击的%s%%的生命(不可叠加)", getAddP(getJid(), getId()));
    }

    @Override
    public Skill create(SkillInfo info, Number who, Number... nums) {
        return new Skill(info, who, new CopyOnWriteArrayList<>(nums), "吸血") {
            @Override
            public void before() {
                putPerson(getInfo(who).addTag(TAG_XX, info.getAddPercent()));
                setTips("作用于 " + Tool.At(who.longValue()));
            }

            @Override
            public void run() {
                super.run();
                try {
                    Thread.sleep(t6);
                    putPerson(getInfo(who).eddTag(TAG_XX, info.getAddPercent()));
                    setTips("吸血失效");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
    }
}
