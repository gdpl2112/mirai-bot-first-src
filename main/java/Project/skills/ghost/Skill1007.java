package Project.skills.ghost;

import Project.aSpring.dao.SkillInfo;
import Project.services.detailServices.ac.GameJoinDetailService;
import Project.skills.SkillTemplate;
import io.github.kloping.mirai0.commons.GhostObj;
import io.github.kloping.mirai0.commons.Skill;

import java.util.concurrent.CopyOnWriteArrayList;

import static Project.commons.rt.CommonSource.percentTo;
import static Project.commons.rt.CommonSource.toPercent;
import static Project.services.detailServices.GameSkillDetailService.getAddP;

/**
 * @author github.kloping
 */
public class Skill1007 extends SkillTemplate {

    public Skill1007() {
        super(1007);
        setName("恢复技能");
    }

    @Override
    public Skill create(SkillInfo info, Number who, Number... nums) {
        return new Skill(info, who, new CopyOnWriteArrayList<>(nums), "") {
            GhostObj ghostObj;

            @Override
            public void before() {
                ghostObj = GameJoinDetailService.getGhostObjFrom(-who.longValue());
            }

            @Override
            public void run() {
                super.run();
                long v = getAddP(getJid(), getId());
                int b = toPercent(ghostObj.getHp(), ghostObj.getMaxHp());
                b = 100 - b;
                v += b / 2;
                ghostObj.setHp(ghostObj.getHp() + percentTo((int) v, ghostObj.getAtt()));
                ghostObj.apply();
                setTips("加血" + v + "%");
            }
        };
    }
}
