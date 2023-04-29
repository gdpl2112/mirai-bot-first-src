package Project.skills.normal;

import Project.commons.gameEntitys.SkillInfo;
import Project.commons.rt.CommonSource;
import Project.services.detailServices.GameSkillDetailService;
import Project.skills.SkillTemplate;
import Project.utils.VelocityUtils;
import io.github.kloping.mirai0.commons.Skill;

import java.util.concurrent.CopyOnWriteArrayList;

import static Project.commons.rt.CommonSource.toPercent;
import static Project.services.detailServices.GameSkillDetailService.getAddP;

/**
 * @author github.kloping
 */
public class Skill22 extends SkillTemplate {
    private static int LOWEST = 30;

    public Skill22() {
        super(22);
    }


    @Override
    public String getIntro() {
        return VelocityUtils.getTemplateToString(String.format("skill/%s.intro", getJid()), LOWEST, getAddP(getJid(), getId()), getAddP(getJid(), getId()));
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
                    GameSkillDetailService.addShield(who.longValue(), CommonSource.percentTo(ad, getPersonInfo().getHp()));
                }
            }
        };
    }
}
