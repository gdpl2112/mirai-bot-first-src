package Project.skill.s7;

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
public class Skill731 extends SkillTemplate {
    

    public Skill731() {
        super(731);
    }

    @Override
    public SkillIntro.Type[] getTypes() {
        return  new SkillIntro.Type[]{SkillIntro.Type.WHZs, SkillIntro.Type.HasTime, SkillIntro.Type.Special, SkillIntro.Type.Add, SkillIntro.Type.Mark, SkillIntro.Type.Shd};
    }

    @Override
    public String getIntro() {
        return  String.format("暗金恐爪熊,发挥恐怖的威力增加%s%%的攻击和%s%%的临时护盾", getAddP(getJid(), getId()), getAddP(getJid(), getId()));
    }

    @Override
    public Skill create(SkillInfo info, Number who, Number... nums) {
        return  new Skill(info, who, new CopyOnWriteArrayList<>(nums), "暗金恐爪熊") {
            @Override
            public void before() {
                PersonInfo pInfo = getInfo(who);
                v_ = percentTo(info.getAddPercent(), pInfo.getAtt());
                long v = percentTo(info.getAddPercent(), pInfo.getHpL());
                addAttHasTime(who.longValue(), new HasTimeAdder(System.currentTimeMillis() + t731, who.longValue(), v));
                addShield(who.longValue(), v, (long) t731);
                putPerson(pInfo);
            }

            private long v_;

            @Override
            public void run() {
                super.run();
                try {
                    Thread.sleep(t731);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                setTips("武魂真身失效");
            }
        };
    }
}
