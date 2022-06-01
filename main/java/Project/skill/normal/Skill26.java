package Project.skill.normal;

import Project.skill.SkillTemplate;
import io.github.kloping.mirai0.commons.PersonInfo;
import io.github.kloping.mirai0.commons.Skill;
import io.github.kloping.mirai0.commons.gameEntitys.SkillInfo;

import java.util.concurrent.CopyOnWriteArrayList;

import static Project.dataBases.GameDataBase.getInfo;
import static Project.services.detailServices.GameSkillDetailService.getAddP;
import static Project.services.detailServices.GameSkillDetailService.nearest;
import static io.github.kloping.mirai0.unitls.Tools.Tool.device;

/**
 * @author github.kloping
 */
public class Skill26 extends SkillTemplate {

    public Skill26() {
        super(26);
    }


    @Override
    public String getIntro() {
        return String.format("使指定人的后摇,减少%s秒冷却", device(getAddP(getJid(), getId()), 1000, 1));
    }

    @Override
    public Skill create(SkillInfo info, Number who, Number... nums) {
        return new Skill(info, who, new CopyOnWriteArrayList<>(nums), "减cd") {
            @Override
            public void before() {
                long t0 = info.getAddPercent();
                for (Long q : nearest(2, who.longValue(), nums)) {
                    PersonInfo pInfo = getInfo(q);
                    pInfo.setJak1(pInfo.getJak1() - t0)
                            .setAk1(pInfo.getAk1() - t0).apply();
                    setTips("作用于:" + q);
                }
            }
        };
    }
}
