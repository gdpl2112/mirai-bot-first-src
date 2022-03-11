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
public class Skill723 extends SkillTemplate {
    @Override
    public void before() {
    }

    public Skill723() {
        super(723);
    }

    @Override
    public SkillIntro.Type[] getTypes() {
        return  new SkillIntro.Type[]{SkillIntro.Type.WHZs, SkillIntro.Type.HasTime, SkillIntro.Type.Special, SkillIntro.Type.Add};
    }

    @Override
    public String getIntro() {
        return  String.format("黑暗圣龙,增加%s%%的攻击,和恢复%s%%的 精神力,并增加 %s%%的吸血", getAddP(getJid(), getId()), getAddP(getJid(), getId()) / 2, getAddP(getJid(), getId()) / 5);
    }

    @Override
    public Skill create(SkillInfo info, Number who, Number... nums) {
        return  new Skill(info, who, new CopyOnWriteArrayList<>(nums), "黑暗圣龙真身") {
            private Long v1;

            @Override
            public void before() {
                Long q = who.longValue();
                PersonInfo pInfo = getInfo(q);
                Long lon = pInfo.getAtt();
                int b = info.getAddPercent();
                v1 = percentTo(b, lon);
                long v4 = percentTo(b / 2, pInfo.getHjL());
                pInfo.addHj(v4);
                pInfo.addTag(TAG_XX, b / 5);
                putPerson(pInfo);
                addAttHasTime(who.longValue(), new HasTimeAdder(System.currentTimeMillis() + t723, who.longValue(), v1));
            }
        };
    }
}
