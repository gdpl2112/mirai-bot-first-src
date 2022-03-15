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
public class Skill15 extends SkillTemplate {
    

    public Skill15() {
        super(15);
    }

    @Override
    public SkillIntro.Type[] getTypes() {
        return  new SkillIntro.Type[]{SkillIntro.Type.Mark, SkillIntro.Type.Shd};
    }

    @Override
    public String getIntro() {
        return  String.format("为自己增加一个最大生命值的%s%%的永久护盾", getAddP(getJid(), getId()));
    }

    @Override
    public Skill create(SkillInfo info, Number who, Number... nums) {
        return  new Skill(info, who, new CopyOnWriteArrayList<>(nums), "永久护盾") {
            @Override
            public void before() {
                long v = getInfo(who).getHp();
                int b = info.getAddPercent();
                long v2 = percentTo(b, v);
                addShield(who.longValue(),v2);
                setTips("作用于 " + Tool.At(who.longValue()));
            }
        };
    }
}
