package Project.skill.normal;

import Project.skill.SkillTemplate;
import io.github.kloping.mirai0.commons.PersonInfo;
import io.github.kloping.mirai0.commons.Skill;
import io.github.kloping.mirai0.commons.gameEntitys.SkillInfo;
import io.github.kloping.mirai0.unitls.Tools.Tool;

import java.util.concurrent.CopyOnWriteArrayList;

import static Project.dataBases.GameDataBase.getInfo;
import static Project.services.detailServices.GameSkillDetailService.getAddP;
import static Project.services.detailServices.GameSkillDetailService.nearest;

/**
 * @author github.kloping
 */
public class Skill26 extends SkillTemplate {

    public Skill26() {
        super(26);
    }


    @Override
    public String getIntro() {
        return String.format("使指定人的攻击冷却立刻刷新", Tool.tool.device(getAddP(getJid(), getId()), 1000, 1));
    }

    @Override
    public Skill create(SkillInfo info, Number who, Number... nums) {
        return new Skill(info, who, new CopyOnWriteArrayList<>(nums), "减cd") {
            @Override
            public void before() {
                long t0 = info.getAddPercent();
                for (Long q : nearest(2, who.longValue(), nums)) {
                    PersonInfo pInfo = getInfo(q);
                    pInfo.setJak1(1L)
                            .setAk1(1L).apply();
                    setTips("作用于:" + q);
                }
            }
        };
    }
}
