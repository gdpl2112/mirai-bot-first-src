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
public class Skill722 extends SkillTemplate {
    

    public Skill722() {
        super(722);
    }

    @Override
    public SkillIntro.Type[] getTypes() {
        return  new SkillIntro.Type[]{SkillIntro.Type.WHZs, SkillIntro.Type.HasTime, SkillIntro.Type.Special, SkillIntro.Type.Add};
    }

    @Override
    public String getIntro() {
        return  String.format("光明圣龙,增加%s%%的攻击,和恢复%s%%的 血量,魂力,精神力", getAddP(getJid(), getId()), getAddP(getJid(), getId()) / 2);
    }

    @Override
    public Skill create(SkillInfo info, Number who, Number... nums) {
        return  new Skill(info, who, new CopyOnWriteArrayList<>(nums), "光明圣龙真身") {
            private Long v1;

            @Override
            public void before() {
                Long q = who.longValue();
                PersonInfo info_ = getInfo(q);
                Long lon = info_.getAtt();
                int b = info.getAddPercent();
                v1 = percentTo(b, lon);
                long v2 = percentTo(b / 2, info_.getHpL());
                long v3 = percentTo(b / 2, info_.getHll());
                long v4 = percentTo(b / 2, info_.getHjL());
                info_.addHp(v2);
                info_.addHl(v3);
                info_.addHj(v4);
                putPerson(info_);
                addAttHasTime(who.longValue(), new HasTimeAdder(System.currentTimeMillis() + t722, who.longValue(), v1));
            }
        };
    }
}
