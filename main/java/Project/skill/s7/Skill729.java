package Project.skill.s7;

import Project.skill.SkillTemplate;
import io.github.kloping.mirai0.Entitys.gameEntitys.PersonInfo;
import io.github.kloping.mirai0.Entitys.gameEntitys.Skill;
import io.github.kloping.mirai0.Entitys.gameEntitys.SkillInfo;
import io.github.kloping.mirai0.Entitys.gameEntitys.SkillIntro;

import java.util.concurrent.CopyOnWriteArrayList;

import static Project.dataBases.GameDataBase.getInfo;
import static Project.dataBases.skill.SkillDataBase.*;
import static Project.services.detailServices.GameSkillDetailService.getAddP;

/**
 * @author github.kloping
 */
public class Skill729 extends SkillTemplate {


    public Skill729() {
        super(729);
    }

    @Override
    public SkillIntro.Type[] getTypes() {
        return new SkillIntro.Type[]{SkillIntro.Type.WHZs, SkillIntro.Type.HasTime, SkillIntro.Type.Special, SkillIntro.Type.Add, SkillIntro.Type.Mark, SkillIntro.Type.Edd};
    }

    @Override
    public String getIntro() {
        return String.format("杀神昊天锤,奥义炸环第一魂环,第一魂技进入冷却,增加%s%%^^的攻击(随第一魂环品质提升而提升)", getAddP(getJid(), getId()));
    }

    @Override
    public Skill create(SkillInfo info, Number who, Number... nums) {
        return new Skill(info, who, new CopyOnWriteArrayList<>(nums), "杀神昊天锤") {
            @Override
            public void before() {
                Long q = who.longValue();
                PersonInfo pInfo = getInfo(q);
                Long lon = pInfo.getAtt();
                int b = info.getAddPercent();
                int id = getSkillInfo(who.longValue()).get(1).getId();
                SkillInfo i1 = getSkillInfo(who.longValue()).get(0);
                i1.setTime(System.currentTimeMillis() + 1000 * 60 * 30);
                updateSkillInfo(i1);
                id -= 200;
                b += id;
                long v = percentTo(b, lon);
                addAttHasTime(who.longValue(), new HasTimeAdder(System.currentTimeMillis() + t729, who.longValue(), v));
                setTips(String.format("增加%s%%(%s)攻击", b, percentTo(b, getInfo(who).getAtt())));
            }
        };
    }
}
