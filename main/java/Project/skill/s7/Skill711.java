package Project.skill.s7;

import Project.skill.SkillTemplate;
import io.github.kloping.mirai0.Entitys.gameEntitys.*;

import java.util.concurrent.CopyOnWriteArrayList;

import static Project.dataBases.GameDataBase.*;
import static Project.dataBases.skill.SkillDataBase.*;
import static Project.services.detailServices.GameSkillDetailService.*;

/**
 * @author github.kloping
 */
public class Skill711 extends SkillTemplate {
    @Override
    public void before() {
    }

    public Skill711() {
        super(711);
    }

    @Override
    public SkillIntro.Type[] getTypes() {
        return  WhTypes.T72;
    }

    @Override
    public String getIntro() {
        return  String.format("释放破魂枪真身,增加%s%%的攻击力", getAddP(getJid(), getId()));
    }

    @Override
    public Skill create(SkillInfo info, Number who, Number... nums) {
        return  new Skill(info, who, new CopyOnWriteArrayList<>(nums), "破魂枪真身") {

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
