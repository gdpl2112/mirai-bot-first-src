package Project.skill.normal;

import Project.skill.SkillTemplate;
import io.github.kloping.mirai0.commons.PersonInfo;
import io.github.kloping.mirai0.commons.Skill;
import io.github.kloping.mirai0.commons.SkillIntro;
import io.github.kloping.mirai0.commons.gameEntitys.SkillInfo;
import io.github.kloping.mirai0.commons.resouce_and_tool.CommonSource;
import io.github.kloping.mirai0.unitls.Tools.Tool;

import java.util.concurrent.CopyOnWriteArrayList;

import static Project.dataBases.GameDataBase.getInfo;
import static Project.dataBases.GameDataBase.putPerson;
import static Project.services.detailServices.GameSkillDetailService.getAddP;

/**
 * @author github.kloping
 */
public class Skill13 extends SkillTemplate {


    public Skill13() {
        super(13);
    }

    @Override
    public SkillIntro.Type[] getTypes() {
        return new SkillIntro.Type[]{SkillIntro.Type.Edd, SkillIntro.Type.OneTime, SkillIntro.Type.ToOne};
    }

    @Override
    public String getIntro() {
        return String.format("令指定一个人魂力减少,自身当前魂力的%s%%的魂力的值", getAddP(getJid(), getId()));
    }

    @Override
    public Skill create(SkillInfo info, Number who, Number... nums) {
        return new Skill(info, who, new CopyOnWriteArrayList<>(nums), "减魂力") {
            @Override
            public void before() {
                if (nums == null || nums.length == 0) {
                    setTips("该玩家未注册");
                    return;
                }
                PersonInfo p2 = getInfo(nums[0]);
                PersonInfo p1 = getPersonInfo();
                long m = p1.getHl();
                long v = CommonSource.percentTo(info.getAddPercent(), m);
                p2.addHl(-v);
                putPerson(p2);
                setTips("令" + Tool.at(nums[0].longValue()) + "魂力减少");
            }
        };
    }
}
