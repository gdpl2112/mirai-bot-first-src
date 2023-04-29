package Project.skills.s50;

import Project.commons.gameEntitys.SkillInfo;
import Project.skills.SkillTemplate;
import io.github.kloping.mirai0.commons.Skill;
import io.github.kloping.number.NumberUtils;

import java.util.concurrent.CopyOnWriteArrayList;

import static Project.dataBases.GameDataBase.getInfo;
import static Project.services.detailServices.GameSkillDetailService.nearest;

/**
 * @author github.kloping
 */
public class Skill5003 extends SkillTemplate {
    public Skill5003() {
        super(5003);
    }

    @Override
    public Skill create(SkillInfo info, Number who, Number... nums) {
        return new Skill(info, who, new CopyOnWriteArrayList<>(nums), "三曰魂") {

            Long[] qs;

            long v0;

            @Override
            public void before() {
                qs = nearest(2, who.longValue(), nums);
                v0 = NumberUtils.percentTo(info.getAddPercent(), getPersonInfo().getHll());
            }

            @Override
            public void run() {
                super.run();
                int r = 0;
                while (true) {
                    try {
                        Thread.sleep(3000);
                        for (Long q : qs) {
                            long v1 = getInfo(q).getHll() / 2;
                            v1 = v0 > v1 ? v0 : v1;
                            getInfo(q).addHl(v1).apply();
                        }
                    } catch (InterruptedException e) {
                        break;
                    }
                    r++;
                    if (r >= 40) break;
                }
            }
        };
    }
}
