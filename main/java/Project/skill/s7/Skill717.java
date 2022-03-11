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
public class Skill717 extends SkillTemplate {
    @Override
    public void before() {
    }

    public Skill717() {
        super(717);
    }

    @Override
    public SkillIntro.Type[] getTypes() {
        return new SkillIntro.Type[]{SkillIntro.Type.WHZs, SkillIntro.Type.HasTime, SkillIntro.Type.Add, SkillIntro.Type.Shd, SkillIntro.Type.Mark};
    }

    @Override
    public String getIntro() {
        return String.format("骨龙真身,为自己增加%s%%的攻击,增加一个最大生命值的%s%%的护盾", getAddP(getJid(), getId()), getAddP(getJid(), getId()));
    }

    @Override
    public Skill create(SkillInfo info, Number who, Number... nums) {
        return new Skill(info, who, new CopyOnWriteArrayList<>(nums), "骨龙") {
            @Override
            public void before() {
                Long q = who.longValue();
                PersonInfo pInfo = getInfo(q);
                Long lon = pInfo.getAtt();
                long v = percentTo(info.getAddPercent(), lon);
                addShield(q, percentTo(info.getAddPercent(), pInfo.getHpL()));
                addAttHasTime(who.longValue(), new HasTimeAdder(System.currentTimeMillis() + t717, who.longValue(), v));
            }
        };
    }
}
