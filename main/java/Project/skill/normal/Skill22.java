package Project.skill.normal;

import Project.services.detailServices.GameSkillDetailService;
import Project.skill.SkillTemplate;
import io.github.kloping.mirai0.commons.Skill;
import io.github.kloping.mirai0.commons.SkillIntro;
import io.github.kloping.mirai0.commons.gameEntitys.SkillInfo;

import java.util.concurrent.CopyOnWriteArrayList;

import static Project.services.detailServices.GameSkillDetailService.getAddP;
import static Project.services.detailServices.GameSkillDetailService.getArgFromSkillArgs;
import static io.github.kloping.mirai0.unitls.Tools.Tool.percentTo;
import static io.github.kloping.mirai0.unitls.Tools.Tool.toPercent;

/**
 * @author github.kloping
 */
public class Skill22 extends SkillTemplate {
    public Skill22() {
        super(22);
    }

    @Override
    public SkillIntro.Type[] getTypes() {
        return new SkillIntro.Type[]{SkillIntro.Type.Special, SkillIntro.Type.Add};
    }

    private static int LOWEST = 30;

    @Override
    public String getIntro() {
        return String.format("血量低于%s%%时恢复%s%%的生命值,血量高于30%%时增加当前生命值得%s%%点护盾", LOWEST, getAddP(getJid(), getId()));
    }

    @Override
    public Skill create(SkillInfo info, Number who, Number... nums) {
        return new Skill(info, who, new CopyOnWriteArrayList<>(nums), "位移") {
            @Override
            public void before() {

            }

            @Override
            public void run() {
                int ad = info.getAddPercent();
                int b = toPercent(getPersonInfo().getHp(), getPersonInfo().getHpL());
                if (b <= LOWEST) {
                    GameSkillDetailService.addHp(who, who.longValue(), ad);
                } else {
                    GameSkillDetailService.addShield(who.longValue(), percentTo(ad, getPersonInfo().getHp()));
                }
            }
        };
    }
}
