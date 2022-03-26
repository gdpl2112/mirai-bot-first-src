package Project.skill.s7;

import Project.skill.SkillTemplate;
import io.github.kloping.mirai0.commons.PersonInfo;
import io.github.kloping.mirai0.commons.Skill;
import io.github.kloping.mirai0.commons.SkillIntro;
import io.github.kloping.mirai0.commons.gameEntitys.SkillInfo;

import java.util.concurrent.CopyOnWriteArrayList;

import static Project.dataBases.GameDataBase.exist;
import static Project.dataBases.GameDataBase.getInfo;
import static Project.dataBases.skill.SkillDataBase.*;
import static Project.services.detailServices.GameSkillDetailService.getAddP;

/**
 * @author github.kloping
 */
public class Skill78 extends SkillTemplate {


    public Skill78() {
        super(78);
    }

    @Override
    public SkillIntro.Type[] getTypes() {
        return new SkillIntro.Type[]{SkillIntro.Type.WHZs, SkillIntro.Type.Special, SkillIntro.Type.Add, SkillIntro.Type.HasTime, SkillIntro.Type.Edd};
    }

    @Override
    public String getIntro() {
        return String.format("释放邪火凤凰真身,增加(%s + 魂力剩余百分比的一半)%% 的攻击力", getAddP(getJid(), getId()));
    }

    @Override
    public Skill create(SkillInfo info, Number who, Number... nums) {
        return new Skill(info, who, new CopyOnWriteArrayList<>(nums), "邪火凤凰真身") {
            @Override
            public void before() {
                Long q = who.longValue();
                if (!exist(q)) {
                    return;
                }
                PersonInfo info1 = getInfo(who);
                int n = toPercent(info1.getHl(), info1.getHll());
                int n2 = n / 2;
                int a = info.getAddPercent() + n2;
                long v = percentTo(a, info1.getHll());
                addAttHasTime(who.longValue(), new HasTimeAdder(System.currentTimeMillis() + t78, who.longValue(), v));
                setTips("剩余" + n + "% 的魂力,增加" + a + "%的攻击力");
            }
        };
    }
}
