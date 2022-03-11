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
public class Skill712 extends SkillTemplate {
    @Override
    public void before() {
    }

    public Skill712() {
        super(712);
    }

    @Override
    public SkillIntro.Type[] getTypes() {
        return  WhTypes.T72;
    }

    @Override
    public String getIntro() {
        return  String.format("释放大力金刚熊真身,增加%s%%的攻击力", getAddP(getJid(), getId()));
    }

    @Override
    public Skill create(SkillInfo info, Number who, Number... nums) {
        return  new Skill(info, who, new CopyOnWriteArrayList<>(nums), "大力金刚熊真身") {
            @Override
            public void before() {
                Long q = who.longValue();
                if (!exist(q)) {
                    return;
                }
                long v = percentTo(info.getAddPercent(), getInfo(q).getAtt());
                addAttHasTime(who.longValue(), new HasTimeAdder(System.currentTimeMillis() + t4, who.longValue(), v));
            }
        };
    }
}
