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
public class Skill716 extends SkillTemplate {
    @Override
    public void before() {
    }

    public Skill716() {
        super(716);
    }

    @Override
    public SkillIntro.Type[] getTypes() {
        return  new SkillIntro.Type[]{SkillIntro.Type.WHZs, SkillIntro.Type.HasTime, SkillIntro.Type.Add, SkillIntro.Type.Mark};
    }

    @Override
    public String getIntro() {
        return  String.format("释放蛇矛真身,为自己增加%s%%的攻击,同时增加%s的吸血", getAddP(getJid(), getId()), getAddP(getJid(), getId()) / 8);
    }

    @Override
    public Skill create(SkillInfo info, Number who, Number... nums) {
        return  new Skill(info, who, new CopyOnWriteArrayList<>(nums), "蛇矛真身") {
            @Override
            public void before() {
                Long q = who.longValue();
                if (!exist(q)) {
                    return;
                }
                PersonInfo pInfo = getInfo(q);
                Long lon = pInfo.getAtt();
                int p = info.getAddPercent();
                long v = percentTo(p, lon);
                long o = percentTo(p, v);
                addShield(q, o);
                addAttHasTime(who.longValue(), new HasTimeAdder(System.currentTimeMillis() + t716, who.longValue(), v));
                putPerson(pInfo.addTag(TAG_XX, info.getAddPercent() / 8));
            }
        };
    }
}
