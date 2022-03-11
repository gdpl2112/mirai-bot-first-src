package Project.skill.s7;

import Project.dataBases.skill.SkillDataBase;
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
public class Skill713 extends SkillTemplate {
    @Override
    public void before() {
    }

    public Skill713() {
        super(713);
    }

    @Override
    public SkillIntro.Type[] getTypes() {
        return  new SkillIntro.Type[]{SkillIntro.Type.WHZs, SkillIntro.Type.OneTime, SkillIntro.Type.Add, SkillIntro.Type.Control};
    }

    @Override
    public String getIntro() {
        return  String.format("释放奇茸通天菊真身,增加%s%%的攻击力,并令一个人,无法躲避下次攻击", getAddP(getJid(), getId()));
    }

    @Override
    public Skill create(SkillInfo info, Number who, Number... nums) {
        return  new Skill(info, who, new CopyOnWriteArrayList<>(nums), "奇茸通天菊真身") {
            @Override
            public void before() {
                Long q = who.longValue();
                PersonInfo info_ = getInfo(q);
                Long lon = info_.getAtt();
                long v = percentTo(info.getAddPercent(), lon);
                addAttHasTime(who.longValue(), new HasTimeAdder(System.currentTimeMillis() + t4, who.longValue(), v));
                try {
                    if (nums.length != 0) {
                        putPerson(getInfo(nums[0]).addTag(SkillDataBase.TAG_CANT_HIDE, 0));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }
}
