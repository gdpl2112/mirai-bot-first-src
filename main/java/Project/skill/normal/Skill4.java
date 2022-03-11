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
public class Skill4 extends SkillTemplate {
    @Override
    public void before() {
    }

    public Skill4() {
        super(4);
    }

    @Override
    public SkillIntro.Type[] getTypes() {
        return  WhTypes.T4;
    }

    @Override
    public String getIntro() {
        return  String.format("对指定一个人增加%s%%的攻击", getAddP(getJid(), getId()));
    }

    @Override
    public Skill create(SkillInfo info, Number who, Number... nums) {
        return  new Skill(info, who, new CopyOnWriteArrayList<>(nums), "单体加攻击技能") {
            @Override
            public void before() {
                Long q = oneNearest(who, nums);
                if (!exist(q)) {
                    return;
                }
                PersonInfo info_ = getInfo(q);
                Long lon = info_.getAtt();
                long v = percentTo(info.getAddPercent(), lon);
                v = v > info_.getAtt() ? info_.getAtt() : v;
                addAttHasTime(who.longValue(), new HasTimeAdder(System.currentTimeMillis() + t4, who.longValue(), v));
                setTips("作用于 " + Tool.At(q));
            }
        };
    }
}
