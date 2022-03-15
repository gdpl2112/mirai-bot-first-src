package Project.skill.normal;

import Project.skill.SkillTemplate;
import io.github.kloping.mirai0.Entitys.gameEntitys.*;
import io.github.kloping.mirai0.unitls.Tools.Tool;

import java.util.concurrent.CopyOnWriteArrayList;

import static Project.dataBases.GameDataBase.*;
import static Project.dataBases.skill.SkillDataBase.*;
import static Project.services.detailServices.GameDetailServiceUtils.*;
import static Project.services.detailServices.GameJoinDetailService.*;
import static Project.services.detailServices.GameSkillDetailService.*;
import static io.github.kloping.mirai0.unitls.Tools.Tool.RANDOM;
import io.github.kloping.mirai0.Entitys.gameEntitys.base.BaseInfo;
import Project.services.detailServices.GameBoneDetailService;
/**
 * @author github.kloping
 */
public class Skill20 extends SkillTemplate {
    

    public Skill20() {
        super(20);
    }

    @Override
    public SkillIntro.Type[] getTypes() {
        return  new SkillIntro.Type[]{SkillIntro.Type.Control};
    }

    @Override
    public String getIntro() {
        return  String.format("控制型技能,指定敌人,2秒后 对其 眩晕 %s 秒 其值受攻击者和被攻击者的精神力影响", getAddP(getJid(), getId()));
    }

    @Override
    public Skill create(SkillInfo info, Number who, Number... nums) {
        return  new Skill(info, who, new CopyOnWriteArrayList<>(nums), "眩晕技") {
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
                super.run();
            }
        };
    }
}
