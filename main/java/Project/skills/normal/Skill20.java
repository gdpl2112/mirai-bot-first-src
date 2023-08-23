package Project.skills.normal;

import Project.aSpring.dao.SkillInfo;
import Project.commons.gameEntitys.base.BaseInfo;
import Project.skills.SkillTemplate;
import io.github.kloping.mirai0.commons.Skill;

import java.util.concurrent.CopyOnWriteArrayList;

import static Project.services.detailServices.GameDetailServiceUtils.getBaseInfoFromAny;

/**
 * @author github.kloping
 */
public class Skill20 extends SkillTemplate {


    public Skill20() {
        super(20);
    }


    @Override
    public Skill create(SkillInfo info, Number who, Number... nums) {
        return new Skill(info, who, new CopyOnWriteArrayList<>(nums), "眩晕技") {
            BaseInfo b2 = null;

            float t = 0L;

            @Override
            public void before() {
                if (nums.length > 0) {
                    long q = nums[0].longValue();
                    BaseInfo b1 = getBaseInfoFromAny(who, who);
                    BaseInfo b2 = null;
                    b2 = getBaseInfoFromAny(who, q);
                    float i = info.getAddPercent();
                    if (b1.getHj() > b2.getHj() && b1.getHjL() > b2.getHjL()) {
                        i *= 2;
                    } else if (b2.getHj() > b1.getHj()) {
                        i /= 2;
                    }
                    t = i > 0 ? (long) i : 1L;
                    b2.letVertigo((long) (t * info.getAddPercent() * 1000L));
                    b2.apply();
                    setTips("作用于 " + b2.getName());
                } else {
                    setTips("未选择任何");
                }
            }

            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                super.run();
            }
        };
    }
}
