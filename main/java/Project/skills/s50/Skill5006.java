package Project.skills.s50;

import Project.commons.gameEntitys.SkillInfo;
import Project.skills.SkillTemplate;
import io.github.kloping.mirai0.commons.PersonInfo;
import io.github.kloping.mirai0.commons.Skill;
import io.github.kloping.number.NumberUtils;

import java.util.concurrent.CopyOnWriteArrayList;

import static Project.dataBases.GameDataBase.getInfo;
import static Project.services.detailServices.GameSkillDetailService.*;

/**
 * @author github.kloping
 */
public class Skill5006 extends SkillTemplate {
    public Skill5006() {
        super(5006);
    }

    @Override
    public Skill create(SkillInfo info, Number who, Number... nums) {
        return new Skill(info, who, new CopyOnWriteArrayList<>(nums), "六曰增") {

            Long[] qs;

            @Override
            public void before() {
                long v = NumberUtils.percentTo(info.getAddPercent(), getPersonInfo().getHpL());
                qs = nearest(2, who.longValue(), nums);
                for (Long q : qs) {
                    PersonInfo pinfo = getInfo(q);
                    if (NumberUtils.toPercent(pinfo.getHp(), pinfo.getHpL()) > 50) {
                        addShield(q, v * 3);
                    } else {
                        addHp(who.longValue(), q, info.getAddPercent());
                        addShield(q, v);
                    }
                }
            }
        };
    }
}
