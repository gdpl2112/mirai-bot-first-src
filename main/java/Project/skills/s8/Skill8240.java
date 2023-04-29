package Project.skills.s8;

import Project.commons.gameEntitys.SkillInfo;
import Project.commons.gameEntitys.base.BaseInfo;
import Project.commons.rt.CommonSource;
import Project.skills.SkillTemplate;
import io.github.kloping.mirai0.commons.Skill;

import java.util.concurrent.CopyOnWriteArrayList;

import static Project.commons.rt.CommonSource.toPercent;
import static Project.services.detailServices.GameDetailServiceUtils.attGhostOrMan;
import static Project.services.detailServices.GameDetailServiceUtils.getBaseInfoFromAny;

/**
 * @author github.kloping
 */
public class Skill8240 extends SkillTemplate {

    public static final int V0 = 3;


    public Skill8240() {
        super(8240);
    }


    @Override
    public Skill create(SkillInfo info, Number who, Number... nums) {
        return new Skill(info, who, new CopyOnWriteArrayList<>(nums), "") {

            @Override
            public void before() {

            }

            @Override
            public void run() {
                super.run();
                if (nums.length <= 0) return;
                long qid = nums[0].longValue();
                StringBuilder sb = new StringBuilder();
                long v = CommonSource.percentTo(info.getAddPercent(), getPersonInfo().att());
                BaseInfo baseInfo = getBaseInfoFromAny(who, qid);
                int b = toPercent(baseInfo.getHp(), baseInfo.getHpL());
                if (b <= 50) {
                    v = CommonSource.percentTo(150, v);
                }
                sb.append("将造成").append(v).append("伤害");
                attGhostOrMan(sb, who, nums[0].longValue(), v);
                setTips(sb.toString());

            }
        };
    }
}
