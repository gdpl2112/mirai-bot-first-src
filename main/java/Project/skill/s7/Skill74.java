package Project.skill.s7;

import Project.skill.SkillTemplate;
import io.github.kloping.mirai0.commons.Skill;
import io.github.kloping.mirai0.commons.SkillIntro;
import io.github.kloping.mirai0.commons.gameEntitys.*;

import java.util.concurrent.CopyOnWriteArrayList;

import static Project.dataBases.GameDataBase.*;
import static Project.dataBases.skill.SkillDataBase.*;
import static Project.services.detailServices.GameDetailServiceUtils.*;
import static Project.services.detailServices.GameSkillDetailService.*;

/**
 * @author github.kloping
 */
public class Skill74 extends SkillTemplate {
    

    public Skill74() {
        super(74);
    }

    @Override
    public SkillIntro.Type[] getTypes() {
        return  new SkillIntro.Type[]{SkillIntro.Type.WHZs, SkillIntro.Type.Add, SkillIntro.Type.Att, SkillIntro.Type.OneTime};
    }

    @Override
    public String getIntro() {
        return  String.format("释放噬魂真身,吸取敌人%s%%的攻击力,恢复 吸取值得一半 的生命值", getAddP(getJid(), getId()));
    }

    @Override
    public Skill create(SkillInfo info, Number who, Number... nums) {
        return  new Skill(info, who, new CopyOnWriteArrayList<>(nums), "噬魂蛛皇真身") {
            @Override
            public void before() {
                if (nums.length == 0) {
                    return;
                }
                long v1 = getAttFromAny(who, nums[0]);
                if (v1 == 0) {
                    setTips("该玩家未注册");
                    return;
                }
                v = percentTo(info.getAddPercent(), v1);
                eddAttAny(who, nums[0], v);
                putPerson(getInfo(who).addHp(v / 2));
            }

            long v;

            @Override
            public void run() {
                super.run();
                try {
                    Thread.sleep(t74);
                    eddAttAny(who, nums[0], -v);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                setTips("武魂真身失效");
            }
        };
    }
}
