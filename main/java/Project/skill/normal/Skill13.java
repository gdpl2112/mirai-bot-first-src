package Project.skill.normal;

import Project.skill.SkillTemplate;
import io.github.kloping.mirai0.commons.PersonInfo;
import io.github.kloping.mirai0.commons.Skill;
import io.github.kloping.mirai0.commons.SkillIntro;
import io.github.kloping.mirai0.commons.gameEntitys.SkillInfo;
import io.github.kloping.mirai0.unitls.Tools.Tool;

import java.util.concurrent.CopyOnWriteArrayList;

import static Project.dataBases.GameDataBase.getInfo;
import static Project.dataBases.GameDataBase.putPerson;
import static Project.dataBases.skill.SkillDataBase.percentTo;
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
        return String.format("令指定一个人魂力减少%s%%", getAddP(getJid(), getId()));
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
                PersonInfo pi = getInfo(nums[0]);
                long m = pi.getHll();
                long v = percentTo(info.getAddPercent(), m);
                v = v > pi.getHll() ? pi.getHll() : v;
                pi.addHl(-v);
                putPerson(pi);
                setTips("令" + Tool.At(nums[0].longValue()) + "魂力减少");
            }
        };
    }
}
