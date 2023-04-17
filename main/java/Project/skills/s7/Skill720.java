package Project.skills.s7;

import Project.skills.SkillTemplate;
import io.github.kloping.mirai0.commons.PersonInfo;
import io.github.kloping.mirai0.commons.Skill;
import Project.commons.gameEntitys.SkillInfo;
import Project.commons.resouce_and_tool.CommonSource;

import java.util.concurrent.CopyOnWriteArrayList;

import static Project.dataBases.GameDataBase.getInfo;
import static Project.services.detailServices.GameSkillDetailService.addShield;

/**
 * @author github.kloping
 */
public class Skill720 extends SkillTemplate {


    public Skill720() {
        super(720);
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
