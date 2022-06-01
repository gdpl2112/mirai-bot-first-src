package Project.skill.s7;

import Project.skill.SkillTemplate;
import io.github.kloping.mirai0.commons.PersonInfo;
import io.github.kloping.mirai0.commons.Skill;
import io.github.kloping.mirai0.commons.gameEntitys.SkillInfo;
import io.github.kloping.mirai0.commons.resouce_and_tool.CommonSource;

import java.util.concurrent.CopyOnWriteArrayList;

import static Project.dataBases.GameDataBase.getInfo;
import static Project.services.detailServices.GameSkillDetailService.addShield;
import static Project.services.detailServices.GameSkillDetailService.getAddP;

/**
 * @author github.kloping
 */
public class Skill720 extends SkillTemplate {


    public Skill720() {
        super(720);
    }


    @Override
    public String getIntro() {
        return String.format("释放玄龟真身,为自己增加一个最大生命值的%s%%的护盾,长久护盾", getAddP(getJid(), getId()));
    }

    @Override
    public Skill create(SkillInfo info, Number who, Number... nums) {
        return new Skill(info, who, new CopyOnWriteArrayList<>(nums), "玄龟真身") {
            @Override
            public void before() {
                PersonInfo pInfo = getInfo(who);
                long v = pInfo.getHpL();
                int p = info.getAddPercent();
                long o = CommonSource.percentTo(p, v);
                addShield(who.longValue(), o);
            }
        };
    }
}
